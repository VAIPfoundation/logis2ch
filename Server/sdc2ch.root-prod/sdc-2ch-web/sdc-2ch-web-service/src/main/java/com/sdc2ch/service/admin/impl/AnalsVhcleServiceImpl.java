package com.sdc2ch.service.admin.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.sdc2ch.cvo.io.TraceIO;
import com.sdc2ch.cvo.service.ICvoControlService;
import com.sdc2ch.service.admin.IAnalsVhcleService;
import com.sdc2ch.service.admin.model.VhcleCntrlVo;
import com.sdc2ch.web.admin.repo.AdmQueryBuilder;
import com.sdc2ch.web.admin.repo.dao.V_CarRepository;
import com.sdc2ch.web.admin.repo.domain.op.T_LOCATION_INFO_HIST;
import com.sdc2ch.web.admin.repo.domain.v.QV_REALTIME_INFO;
import com.sdc2ch.web.admin.repo.domain.v.V_REALTIME_INFO;
import com.sdc2ch.web.admin.repo.domain.v.V_VHCLE;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
class AnalsVhcleServiceImpl implements IAnalsVhcleService {

	
	@Autowired AdmQueryBuilder builder;

	@Autowired V_CarRepository vehicleRepo;

	@Autowired(required = false) ICvoControlService cvoSvc;

	@Value("${trace.temp.yn}")
	private boolean temporary;


	
	@Override
	public V_REALTIME_INFO searchRtVhcleCntrlByVrn(String vrn) {
		QV_REALTIME_INFO code = QV_REALTIME_INFO.v_REALTIME_INFO;
		return builder
				.create()
					.select(
						Projections.fields(
							V_REALTIME_INFO.class,
							code.dlvyDe, code.trnsprtCmpny, code.vrn, code.vhcleTy, code.ldngTy,	
							code.driverNm, code.mobileNo, code.lat, code.lng, code.adres, code.state, code.dataDt	
						)
					)
					.from(code)
					.where(
						code.vrn.eq(vrn)
					)
				.fetchOne();
	}



	@Override
	public List<V_REALTIME_INFO> searchRtVhcleCntrl(String fctryCd) {
		return searchRtVhcleCntrl(fctryCd, null, null, null, null, null, null, null, null, null, null, null);
	}
	@Override
	public List<V_REALTIME_INFO> searchRtVhcleCntrl(String fctryCd, String vrn, String indvdlVhcleYn, String readyYn, String ldngYn, String dlvyYn,
			String rtnDriveYn, String comptYn, String hvofYn, String arvlDelayRiskYn, String arvlDelayYn, String overCtnuDriveYn) {
		log.info("fctryCd={}, vrn={}, indvdlVhcleYn={}, readyYn={}, ldngYn={}, dlvyYn={}, rtnDriveYn={}", fctryCd, vrn, indvdlVhcleYn, readyYn, ldngYn, dlvyYn, rtnDriveYn);
		log.info("comptYn={}, hvofYn={}, arvlDelayRiskYn={}, arvlDelayYn={}, overCtnuDriveYn={}", comptYn, hvofYn, arvlDelayRiskYn, arvlDelayYn, overCtnuDriveYn);



		QV_REALTIME_INFO code = QV_REALTIME_INFO.v_REALTIME_INFO;

		BooleanBuilder where = new BooleanBuilder();
		where.and(code.fctryCd.eq(fctryCd));


		if(!StringUtils.isEmpty(vrn)) {
			where.and(code.vrn.like("%" + vrn + "%"));
		}










		return builder
				.create()
					.select(
						Projections.fields(
							V_REALTIME_INFO.class,
							code.dlvyDe, code.trnsprtCmpny, code.vrn, code.vhcleTy, code.ldngTy,	
							code.driverNm, code.mobileNo, code.lat, code.lng, code.adres, code.state, code.dataDt	
						)
					)
					.from(code)
					.where(where)
					.orderBy(code.vrn.asc())
				.fetch()
				.stream()
					.collect(Collectors.toList());
	}



	@Override
	public List<T_LOCATION_INFO_HIST> searchHistVhcleCntrl(String dlvyDe, String vrn) {
		
		if(temporary) {
			log.info("하누리티엔 관제를 사용하여 궤적을 구하고 있습니다. 변경을 원하시면 spring.properties -> trace.temp.yn = 0 으로 변경바랍니다.");
			List<T_LOCATION_INFO_HIST> res = searchHistVhcleCntrl_new(dlvyDe, vrn);
			return (!res.isEmpty()) ? res : searchHistVhcleCntrl_old(dlvyDe, vrn);
		}
		else {
			return searchHistVhcleCntrl_old(dlvyDe, vrn);
		}
		
	}


	private List<T_LOCATION_INFO_HIST> searchHistVhcleCntrl_old(String dlvyDe, String vrn) {
		log.info("dlvyDe={}, vrn={}", dlvyDe, vrn);
		if( dlvyDe != null && dlvyDe.length() == 8) {
			dlvyDe = dlvyDe.substring(0, 4) + "-" + dlvyDe.substring(4, 6) + "-" + dlvyDe.substring(6, 8);
		}
		Object[] param = {dlvyDe, vrn};
		List<Object[]> resultList = builder.storedProcedureCallRet("[dbo].[SP_2CH_VHCLE_CNTRL_LOCATION_HIST]", param);

		List<T_LOCATION_INFO_HIST> histList = new ArrayList<T_LOCATION_INFO_HIST>();

		for ( Object[] res : resultList ) {
			try {
				T_LOCATION_INFO_HIST hist = new T_LOCATION_INFO_HIST();
				int i = 0;

				Long rowId = Long.valueOf(String.valueOf(res[i++]));
				java.util.Date createDt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse( String.valueOf(res[i++]) );
				java.util.Date updateDt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse( String.valueOf(res[i++]) );
				int accDistance = Integer.valueOf(String.valueOf(res[i++]));
				BigDecimal accuracy = BigDecimal.valueOf(Double.valueOf(String.valueOf(res[i++])));
				BigDecimal lat = BigDecimal.valueOf(Double.valueOf(String.valueOf(res[i++])));
				BigDecimal lng = BigDecimal.valueOf(Double.valueOf(String.valueOf(res[i++])));
				String adres = String.valueOf(res[i++]);
				int speed = Integer.valueOf(String.valueOf(res[i++]));
				String _vrn = String.valueOf(res[i++]);
				String driverCd = String.valueOf(res[i++]);
				int altitude = Integer.valueOf(String.valueOf(res[i++]));
				java.util.Date dataDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse( String.valueOf(res[i++]) );
				int dgree = Integer.valueOf(String.valueOf(res[i++]));
				int distance = Integer.valueOf(String.valueOf(res[i++]));


				hist.setId(rowId);
	
	
				hist.setAccDistance(accDistance);
				hist.setAccuracy(accuracy);
				hist.setLat(lat);
				hist.setLng(lng);
				hist.setAdres(adres);
				hist.setSpeed(speed);
				hist.setVrn(_vrn);
				hist.setDriverCd(driverCd);
				hist.setAltitude(altitude);
				hist.setDataDate(dataDate);
				hist.setDgree(dgree);
				hist.setDistance(distance);
				histList.add(hist);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}


		return histList;



























	}

	private List<T_LOCATION_INFO_HIST> searchHistVhcleCntrl_new(String dlvyDe, String vrn) {

		List<T_LOCATION_INFO_HIST> hists = Collections.emptyList();
		try {

			if(cvoSvc != null) {
				V_VHCLE vhcle = vehicleRepo.findByVrn(vrn).orElse(null);

				AtomicLong anc = new AtomicLong();
				if (vhcle != null) {
					List<TraceIO> traces = cvoSvc.getVehicleTrace(dlvyDe, vhcle.getSetId());
					if (traces != null) {
						hists = traces.stream().map(t -> {
							T_LOCATION_INFO_HIST hist = new T_LOCATION_INFO_HIST();
							BeanUtils.copyProperties(t, hist);
							hist.setId(anc.incrementAndGet());
							return hist;
						}).collect(Collectors.toList());
					}
				}
			}
		}
		catch(Exception e) {
			log.error("{}", e);
		}
		return hists;
	}



	@Override
	public List<VhcleCntrlVo> searchEventHistVhcleCntrl(String dlvyDe, String vrn) {

		Object[] param = {dlvyDe, vrn};
		List<Object[]> resultList = builder.storedProcedureCallRet("[dbo].[SP_2CH_VHCLE_CNTRL_EVENT_HIST]", param);

		List<VhcleCntrlVo> voList = new ArrayList<VhcleCntrlVo>();

		for ( Object[] res : resultList ) {
			VhcleCntrlVo vcVo = new VhcleCntrlVo();

			String eventStr = String.valueOf(res[0]);
			String tmsPlanStr = String.valueOf(res[2]);

			java.util.Date eventDt = StringUtils.isEmpty(eventStr) ? null : new java.util.Date(Timestamp.valueOf( eventStr ).getTime());
			String eventNm = (String)res[1];
			java.util.Date tmsPlanDt = StringUtils.isEmpty(tmsPlanStr) ? null : new java.util.Date(Timestamp.valueOf( tmsPlanStr ).getTime());



			vcVo.setEventDt(eventDt);
			vcVo.setEventNm(eventNm);
			vcVo.setTmsPlanDt(tmsPlanDt);
			voList.add(vcVo);
		}

		return voList;
	}



	@Override
	public List<String> searchHistVhcleCntrlOnlyMongo(String vrn, String month) {

		V_VHCLE vhcle = vehicleRepo.findByVrn(vrn).orElse(null);
		List<String> hists = new ArrayList<>();

		if (vhcle != null) {
			int year = Integer.valueOf(month.substring(0, 4));
			int mon = Integer.valueOf(month.substring(4))-1;
			Calendar cal = Calendar.getInstance();
			cal.set(year, mon, 1);

			String head = null;
			while(cal.get(Calendar.MONTH) == mon) {
				try {
					String dlvyDe = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
					List<TraceIO> traces = cvoSvc.getVehicleTrace(dlvyDe, vhcle.getSetId());
					if (traces != null && !traces.isEmpty()) {
						if(head == null) {
							head = traces.get(0).toHCsv();
							hists.add(head);
						}
						hists.addAll(traces.stream().map(t -> {
							t.setVrn(vrn);
							return t.toCsv();
						}).collect(Collectors.toList()));
					}
				}catch (Exception e) {
					log.error("{}", e);
				}finally {
					cal.add(Calendar.DAY_OF_YEAR, 1);
				}
			}
		}
		return hists;
	}

}
