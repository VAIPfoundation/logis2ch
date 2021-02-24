package com.sdc2ch.nfc.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.sdc2ch.nfc.component.JPAQueryBuilder;
import com.sdc2ch.nfc.domain.NfcFireEvent;
import com.sdc2ch.nfc.domain.entity.T_dev;
import com.sdc2ch.nfc.domain.entity.T_lg;
import com.sdc2ch.nfc.enums.NfcEventType;
import com.sdc2ch.nfc.event.INfcFireEvent;
import com.sdc2ch.nfc.service.INfcDeviceService;
import com.sdc2ch.nfc.service.INfcEventService;
import com.sdc2ch.nfc.service.INfcFireEventService;
import com.sdc2ch.nfc.service.INfcUserService;
import com.sdc2ch.repo.io.NfcMappingIO;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.pubsub.I2ChEventManager;
import com.sdc2ch.require.pubsub.I2ChEventPublisher;
import com.sdc2ch.require.service.I2ChUserDetailsService;
import com.sdc2ch.require.service.I2ChUserService;
import com.sdc2ch.service.io.NfcSeqIO;
import com.sdc2ch.tms.enums.FactoryType;
import com.sdc2ch.web.service.IOTEventService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
class NfcFireEventServiceImpl implements INfcFireEventService {




	@Autowired INfcEventService     eventSvc;
	@Autowired INfcUserService      userSvc;
	@Autowired INfcDeviceService    deviceSvc;
	@Autowired INfcFireEventService publisher;
	@Autowired I2ChUserDetailsService userDtlsSvc;
	@Autowired I2ChUserService tmsUser;
	@Autowired IOTEventService iotSvc;

	@Autowired JPAQueryBuilder builder;

	private I2ChEventPublisher<INfcFireEvent> pub;


	@Autowired
	protected void init(I2ChEventManager manager) {
		this.pub = manager.regist(INfcFireEvent.class);
	}

	@Override
	public void fireEvent(NfcFireEvent event) {
		log.info("onFireEvent");
		pub.fireEvent(event);
	}

	@Value(value="${debug.nfc.fire.enable:true}")
	boolean nfcFireEnable;

	@Scheduled(fixedRate = 1000 * 5)
	public void checkSession() {

		if(nfcFireEnable==false)
			return;

		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMM");
		Calendar cal = Calendar.getInstance();
		Date currentTime = cal.getTime();
		String currentMonth = fmt.format(currentTime);
		StringBuffer sqlSb = new StringBuffer();


		NfcSeqIO nfcSeq = iotSvc.findNfcSeqById(1L);


		String lastMonth = nfcSeq.getLastmonth();
		int id = nfcSeq.getSequence();

		
		if ( lastMonth == null ) {
			id = 0;
			lastMonth = currentMonth;
		}
		sqlSb.append("SELECT EVTLGUID, DEVDT, DEVUID, USRID, EVT, SRVDT")
			.append(" FROM t_lg").append(lastMonth)
			.append(" WHERE EVTLGUID > ").append(id);
		String sql = sqlSb.toString();
		List<?> results = builder.createSelectNativeQuery(sql);

		if ( results.size() == 0 ) {
			log.info("[NFC] results.size={}, lastMonth={}, seq={}, currentMonth={}, Maria SQL={}", results.size(), lastMonth, id, currentMonth, sql);
		}
		
		if ( results.size() == 0 && !lastMonth.equals(currentMonth)) {
			sqlSb.setLength(0);
			sqlSb.append("SELECT EVTLGUID, DEVDT, DEVUID, USRID, EVT, SRVDT")
				.append(" FROM t_lg").append(currentMonth)
				.append(" WHERE EVTLGUID > ").append("0");
			String sql2 = sqlSb.toString();
			results = builder.createSelectNativeQuery(sql2);

			
			if ( results.size() > 0 ) {

				lastMonth = currentMonth;
			}
		}

		for( Object o : results) {
			T_lg lg = new T_lg();
			Object[] colums = (Object[]) o;
			int i = 0;
			lg.setId((Integer)colums[i++]);	
			lg.setDevdt((Integer)colums[i++]);	
			lg.setDevuid((Integer)colums[i++]);	
			lg.setUsrid((String)colums[i++]);
			lg.setEvt((Integer)colums[i++]);
			lg.setSrvdt((Date)colums[i++]);

			fireEvent(lg, lastMonth);

		}



		log.info("NFC -> t_lg table queryed sequnce id {}", id);
	}
	@PostConstruct
	public void init() {
		log.info("NFC -> @PostConstruct Call");



























		

	}

	private void fireEvent(T_lg lg, String lastMonth) {


		if(isManagedEvent(lg.getEvt())) {

			try {


				T_dev tdev = deviceSvc.findById(lg.getDevuid());

				NfcFireEvent event = new NfcFireEvent();
				event.setEvent(NfcEventType.valueOf(lg.getEvt()));


				long eventTime = lg.getSrvdt() != null ? lg.getSrvdt().getTime() : System.currentTimeMillis();
				event.setEventTime(eventTime);	
				event.setNfcDeviceId(tdev.getId());
				event.setNfcDeviceName(tdev.getName());

				NfcMappingIO mapping = iotSvc.findByNfcId(tdev.getId());

				if(mapping != null) {

					event.setFactoryType(FactoryType.convert(mapping.getFctryCd()));
					event.setSetupLcTy(mapping.getSetupLc());

				}

				if (!StringUtils.isEmpty(lg.getUsrid())) {
					IUser user = tmsUser.findByMobileNo("010" + lg.getUsrid()).orElseGet(() -> {
						return tmsUser.findByMobileNo("011" + lg.getUsrid()).orElse(null);
					});

					if (user != null) {
						event.setUser(user);
						
						publisher.fireEvent(event);
					}
				}

			}catch(Exception e) {
				log.error("{}" , e);
			}finally {

				writeSeq(lg.getId(), lastMonth);
			}
		}
	}

	private void writeSeq(int id) {

		try {
			NfcSeqIO nfcSeq = iotSvc.findNfcSeqById(1L);
			int _id = isSameMonth(nfcSeq.getLastUpdated()) ? id : 0;

			iotSvc.writeNfcLogSeqId(_id);
		} catch (Exception e) {
			log.error("{}", e);
		}
	}

	
	private void writeSeq(int id, String lastMonth) {

		try {
			NfcSeqIO nfcSeq = iotSvc.findNfcSeqById(1L);
			int _id = id;
			
			
			if ( lastMonth != null && !lastMonth.equals(nfcSeq.getLastmonth())) {
				_id = 0;
			}

			iotSvc.writeNfcLog(_id, lastMonth);
		} catch (Exception e) {
			log.error("{}", e);
		}
	}

	private boolean isManagedEvent(int nfcEvt) {
		return NfcEventType.DEFAULT_EVENT_CODE.contains(nfcEvt);
	}

	private boolean isSameMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int before = cal.get(Calendar.MONTH);
		Calendar cal2 = Calendar.getInstance();
		int cur = cal2.get(Calendar.MONTH);
		return before == cur;
	}

	
	private boolean isTrueFirstDay(int sequence, Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int today = cal.get(Calendar.DAY_OF_MONTH);
		return !(today == 1 && sequence >= 10000);
	}

	public static void main(String[] args) {





		
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMM");
		Calendar cal = Calendar.getInstance();
		Date currentTime = cal.getTime();
		String currentMonth = fmt.format(currentTime);
		
		System.out.println(currentMonth);
	}
}
