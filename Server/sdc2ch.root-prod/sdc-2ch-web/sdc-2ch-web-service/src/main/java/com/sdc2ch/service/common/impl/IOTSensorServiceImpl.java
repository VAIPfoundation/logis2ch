package com.sdc2ch.service.common.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.sdc2ch.repo.io.BeaconMappingIO;
import com.sdc2ch.repo.io.NfcMappingIO;
import com.sdc2ch.repo.io.ShippingID;
import com.sdc2ch.repo.io.TmsDriverIO;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.service.I2ChUserService;
import com.sdc2ch.service.admin.IMobileHealthCheckService;
import com.sdc2ch.service.io.BeaconDataIO;
import com.sdc2ch.service.io.GpsDataIO;
import com.sdc2ch.service.io.MobileHealthCheckIO;
import com.sdc2ch.service.io.NfcSeqIO;
import com.sdc2ch.web.admin.repo.dao.T_BconHistoryRepository;
import com.sdc2ch.web.admin.repo.dao.T_BconMappingRepository;
import com.sdc2ch.web.admin.repo.dao.T_LastLocationInfoRepo;
import com.sdc2ch.web.admin.repo.dao.T_LocationHistoryRepo;
import com.sdc2ch.web.admin.repo.dao.T_NfcMappingRepository;
import com.sdc2ch.web.admin.repo.dao.T_NfcSeqRepository;
import com.sdc2ch.web.admin.repo.domain.T_MOBILE_HEALTH_CHK;
import com.sdc2ch.web.admin.repo.domain.T_MOBILE_HEALTH_CHK_HIST;
import com.sdc2ch.web.admin.repo.domain.op.T_BCON_HIST;
import com.sdc2ch.web.admin.repo.domain.op.T_LOCATION_INFO;
import com.sdc2ch.web.admin.repo.domain.op.T_LOCATION_INFO_HIST;
import com.sdc2ch.web.admin.repo.domain.op.T_NFC_SEQ;
import com.sdc2ch.web.service.IMobileAppService;
import com.sdc2ch.web.service.IOTEventService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class IOTSensorServiceImpl implements IOTEventService {


	@Autowired I2ChUserService userSvc;
	@Autowired IMobileAppService appSvc;          

	@Autowired T_LocationHistoryRepo repo;
	@Autowired T_LastLocationInfoRepo lstRepo;

	@Autowired T_BconMappingRepository bconMapRepo;
	@Autowired T_BconHistoryRepository bconHisRepo;
	@Autowired T_NfcMappingRepository nfcMapRepo;
	@Autowired T_NfcSeqRepository nfcseqRepo;

	@Autowired IMobileHealthCheckService mobileHealthSvc;
	@Override
	public BeaconMappingIO findByBconId(String bconId) {
		return bconMapRepo.findByBconId(bconId).orElseThrow(() -> new ServiceException("can not find becon mapping " + bconId));
	}

	@Override
	public void saveBconHist(BeaconDataIO data) {
		T_BCON_HIST HIST = new T_BCON_HIST();
		HIST.setDataDt(data.getDataDt());
		HIST.setBconId(data.getBconId());
		HIST.setMdn(data.getMdn());
		HIST.setInoutTy(data.getInoutType());
		bconHisRepo.save(HIST);
	}

	public void saveLocation(String mdn, List<GpsDataIO> datas) {

		IUser user = userSvc.findByMobileNo(mdn).orElse(null);
		long time = System.currentTimeMillis();

		if(user != null) {
			datas.stream().forEach(d -> {
				try {
					TmsDriverIO driver = (TmsDriverIO) user.getUserDetails();
					repo.save(hist(user.getUsername(), driver.getCar().getVrn(), d));

					lstRepo.save(last(user.getUsername(), driver.getCar().getVrn(), datas.stream().reduce((a, b) -> b).get()));

				}catch (Exception e) {
					log.error("{}", e);
				}
			});
		}
	}

	private int isNullSafe(String accDistance) {
		return StringUtils.isEmpty(accDistance) ? 0 : new BigDecimal(accDistance).intValue();
	}
	private BigDecimal setScale(String data, int scale) {
		if(!StringUtils.hasText(data))
			return BigDecimal.ZERO;
		return new BigDecimal(data).setScale(scale, BigDecimal.ROUND_HALF_UP);
	}
	private Date parse(String date) {
		try {
			return new SimpleDateFormat("yyyyMMddHHmmss").parse(date);
		} catch (ParseException e) {
			return new Date();
		}
	}

	private T_LOCATION_INFO_HIST hist(String driverCd, String vrn, GpsDataIO d) {
		T_LOCATION_INFO_HIST loc = new T_LOCATION_INFO_HIST();
		loc.setAccDistance(isNullSafe(d.getAccDistance()));
		loc.setAccuracy(setScale(d.getAccuracy(), 1));
		loc.setAltitude(isNullSafe(d.getAltitude()));
		loc.setDataDate(parse(d.getDataDate()));
		loc.setDgree(isNullSafe(d.getDgree()));
		loc.setDistance(isNullSafe(d.getDistance()));
		loc.setLat(setScale(d.getGpsy(), 6));
		loc.setLng(setScale(d.getGpsx(), 6));
		loc.setAdres(d.getAddress());
		loc.setVrn(vrn);
		loc.setDriverCd(driverCd);
		loc.setSpeed(isNullSafe(d.getAccDistance()));
		return loc;

	}
	private T_LOCATION_INFO last(String driverCd, String vrn, GpsDataIO gpsData) {
		T_LOCATION_INFO loc = new T_LOCATION_INFO();
		loc.setAccDistance(isNullSafe(gpsData.getAccDistance()));
		loc.setAccuracy(setScale(gpsData.getAccuracy(), 1));
		loc.setAltitude(isNullSafe(gpsData.getAltitude()));
		loc.setDataDate(parse(gpsData.getDataDate()));
		loc.setDgree(isNullSafe(gpsData.getDgree()));
		loc.setDistance(isNullSafe(gpsData.getDistance()));
		loc.setEvent(gpsData.getEvent());
		loc.setLat(setScale(gpsData.getGpsy(), 6));
		loc.setLng(setScale(gpsData.getGpsx(), 6));
		ShippingID id = new ShippingID(vrn, driverCd);
		loc.setId(id);
		loc.setSpeed(isNullSafe(gpsData.getSpeed()));
		loc.setAdres(gpsData.getAddress());
		return loc;
	}

	@Override
	public NfcMappingIO findByNfcId(int nfcId) {
		return nfcMapRepo.findByNfcId(nfcId).orElse(null);
	}

	@Override
	public void writeNfcLogSeqId(int id) {
		T_NFC_SEQ seq = new T_NFC_SEQ();
		seq.setId(1L);
		seq.setSequence(id);
		nfcseqRepo.save(seq);
	}

	@Override
	public void writeNfcLog(int id, String lastmonth) {
		T_NFC_SEQ seq = new T_NFC_SEQ();
		seq.setId(1L);
		seq.setSequence(id);
		seq.setLastmonth(lastmonth);
		nfcseqRepo.save(seq);
	}

	@Override
	public int readNfcLogSeqId() {
		return nfcseqRepo.findById(1L).orElse(new T_NFC_SEQ()).getSequence();
	}

	@Override
	public Date readNfcSeqDate() {
		return nfcseqRepo.findById(1L).orElse(new T_NFC_SEQ()).getUpdateDt();
	}

	@Override
	public NfcSeqIO findNfcSeqById(Long Id) {
		return nfcseqRepo.findById(Id).orElse(new T_NFC_SEQ());
	}

	@Override
	public void saveMobielHealthCheck(MobileHealthCheckIO io) {
		if (io != null) {
			String driverCd = null;
			String fctryCd = null;
			String vrn = null;
			IUser user = userSvc.findByMobileNo(io.getMdn()).orElse(null);
			if (user != null) {
				TmsDriverIO driver = (TmsDriverIO) user.getUserDetails();
				driverCd = user.getUsername();
				fctryCd = driver.getFctryCd();
				vrn = driver.getCar().getVrn();
			}

			try {
				
				saveLastMobielHealthCheck(makeLastHealthChk(driverCd, fctryCd, vrn, io));
			}catch(Exception e) {

			}
			try {
				
				saveHistMobielHealthCheck(makeHistHealthChk(driverCd, fctryCd, vrn, io));
			}catch(Exception e) {

			}
		}
	}

	private T_MOBILE_HEALTH_CHK_HIST makeHistHealthChk(String driverCd, String fctryCd, String vrn, MobileHealthCheckIO io) {

		T_MOBILE_HEALTH_CHK_HIST hcHist = new T_MOBILE_HEALTH_CHK_HIST();
		hcHist.setMdn(io.getMdn());
		hcHist.setVrn(vrn);
		hcHist.setDriverCd(driverCd);
		hcHist.setFctryCd(fctryCd);
		hcHist.setNetwork(io.getNetwork());
		hcHist.setPermissions(io.getPermissions());

		hcHist.setBatteryUsage(io.getBatteryUsage());
		hcHist.setCallRecvEnabled(io.getCallRecvEnabled());
		hcHist.setStrDataDt(io.getDataDate());
		if(!StringUtils.isEmpty(io.getDataDate())) {
			hcHist.setDataDt(parse(io.getDataDate()));
		}
		hcHist.setDozeMode(io.getDozeMode());
		hcHist.setForgroundService(io.getForgroundService());
		hcHist.setLocEnabled(io.getLocEnabled());
		hcHist.setRunningService(io.getRunningService());
		return hcHist;
	}

	private T_MOBILE_HEALTH_CHK makeLastHealthChk(String driverCd, String fctryCd, String vrn, MobileHealthCheckIO io) {

		T_MOBILE_HEALTH_CHK hcHist = new T_MOBILE_HEALTH_CHK();
		hcHist.setMdn(io.getMdn());
		hcHist.setVrn(vrn);
		hcHist.setDriverCd(driverCd);
		hcHist.setFctryCd(fctryCd);
		hcHist.setNetwork(io.getNetwork());
		hcHist.setPermissions(io.getPermissions());

		hcHist.setBatteryUsage(io.getBatteryUsage());
		hcHist.setCallRecvEnabled(io.getCallRecvEnabled());
		hcHist.setStrDataDt(io.getDataDate());
		if(!StringUtils.isEmpty(io.getDataDate())) {
			hcHist.setDataDt(parse(io.getDataDate()));
		}
		hcHist.setDozeMode(io.getDozeMode());
		hcHist.setForgroundService(io.getForgroundService());
		hcHist.setLocEnabled(io.getLocEnabled());
		hcHist.setRunningService(io.getRunningService());
		return hcHist;
	}

	private T_MOBILE_HEALTH_CHK_HIST saveHistMobielHealthCheck(T_MOBILE_HEALTH_CHK_HIST entity) {
		return mobileHealthSvc.saveHist(entity);
	}

	private T_MOBILE_HEALTH_CHK saveLastMobielHealthCheck(T_MOBILE_HEALTH_CHK entity) {
		return mobileHealthSvc.saveLast(entity);
	}

}
