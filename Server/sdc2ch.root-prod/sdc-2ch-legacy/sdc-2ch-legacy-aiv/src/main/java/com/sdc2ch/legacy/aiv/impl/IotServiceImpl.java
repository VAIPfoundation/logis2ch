package com.sdc2ch.legacy.aiv.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sdc2ch.aiv.event.IBeaconFireEvent;
import com.sdc2ch.aiv.event.IBeaconFireEvent.InoutType;
import com.sdc2ch.aiv.event.IFirebaseNotificationEvent;
import com.sdc2ch.aiv.event.IGpsEvent;
import com.sdc2ch.legacy.aiv.event.BeaconFireEventImpl;
import com.sdc2ch.legacy.aiv.service.IOTSensorService;
import com.sdc2ch.legacy.endpoint.request.BeaconData;
import com.sdc2ch.legacy.endpoint.request.GpsData;
import com.sdc2ch.legacy.endpoint.request.LocationRequest;
import com.sdc2ch.repo.io.BeaconMappingIO;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.pubsub.I2ChEventManager;
import com.sdc2ch.require.pubsub.I2ChEventPublisher;
import com.sdc2ch.require.service.I2ChUserService;
import com.sdc2ch.service.io.GpsDataIO;
import com.sdc2ch.service.io.MobileHealthCheckIO;
import com.sdc2ch.tms.enums.FactoryType;
import com.sdc2ch.web.service.IMobileAppService;
import com.sdc2ch.web.service.IOTEventService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IotServiceImpl implements IOTSensorService {

	@Autowired I2ChUserService userSvc;
	@Autowired IMobileAppService appSvc;          
	@Autowired IOTEventService iotSvc;


	private I2ChEventPublisher<IGpsEvent> gpsPub;
	private I2ChEventPublisher<IBeaconFireEvent> bconPub;
	private I2ChEventPublisher<IFirebaseNotificationEvent> eventPush;

	@Autowired
	private void publish(I2ChEventManager manager) {
		gpsPub = manager.regist(IGpsEvent.class);
		bconPub = manager.regist(IBeaconFireEvent.class);
		this.eventPush = manager.regist(IFirebaseNotificationEvent.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void saveLocation(String mdn, List<GpsData> datas) {
		IUser user = userSvc.findByMobileNo(mdn).orElse(null);
		if(user != null) {
			datas.stream().forEach(d -> {
				try {
					d.setUser(user);
					this.fireGpsEvent(d);
					iotSvc.saveLocation(mdn, (List<GpsDataIO>)(Object)datas);
				}catch (Exception e) {
					log.error("{}", e);
				}
			});
		}else {
			log.info("can not find user -> ", mdn);
		}
	}
	private void fireGpsEvent(GpsData d) {
		gpsPub.fireEvent(d);
	}
	private void fireBconEvent(IBeaconFireEvent d) {
		bconPub.fireEvent(d);
	}

	@Override
	public void saveBeacon(String mdn, BeaconData beacon) {

		IUser user = userSvc.findByMobileNo(mdn).orElse(null);

		if(user != null) {
			try {
				BeaconMappingIO io = iotSvc.findByBconId(beacon.getDeviceId());
				if(io != null) {
					BeaconFireEventImpl beaconEvent = new BeaconFireEventImpl();
					beaconEvent.setDeviceId(io.getBconId());
					beaconEvent.setFctryTy(find(io.getFctryCd()));
					beaconEvent.setSetupLcTy(io.getSetupLc());
					beaconEvent.setInoutTy(InoutType.valueOf(beacon.getInoutType()));
					beaconEvent.setTimestamp(beacon.getTimestamp());
					beaconEvent.setUser(user);
					fireBconEvent(beaconEvent);
				}

			}catch (Exception e) {
				log.error("{}", e);
			}finally {
				iotSvc.saveBconHist(beacon);
			}
		}
	}

	private FactoryType find(String factoryType) {
		return Stream.of(FactoryType.values()).filter(f -> f.getCode().equals(factoryType)).findFirst().orElse(FactoryType.FFFF);
	}

	@Override
	public void saveMobielHealthCheck(MobileHealthCheckIO mobileHealthCheck) {
		iotSvc.saveMobielHealthCheck(mobileHealthCheck);
	}

	@Override
	public void saveLocationV2(String mdn, List<GpsData> datas) {
		IUser user = userSvc.findByMobileNo(mdn).orElse(null);
		if(user != null) {
			datas.stream().forEach(d -> {
				try {
					d.setUser(user);
					iotSvc.saveLocation(mdn, (List<GpsDataIO>)(Object)datas);
				}catch (Exception e) {
					log.error("{}", e);
				}
			});
		}else {
			log.info("can not find user -> ", mdn);
		}

	}

	@Override
	public void saveBeaconV2(String mdn, BeaconData beacon) {
		iotSvc.saveBconHist(beacon);
	}

	@Override
	public void fireEventGps(String mdn, List<GpsData> datas) {
		IUser user = userSvc.findByMobileNo(mdn).orElse(null);
		if(user != null) {
			datas.stream().forEach(d -> {
				try {
					d.setUser(user);
					this.fireGpsEvent(d);
				}catch (Exception e) {
					log.error("{}", e);
				}
			});
		}else {
			log.info("can not find user -> ", mdn);
		}
	}

	@Override
	public void fireEventBeacon(String mdn, BeaconData beacon) {
		IUser user = userSvc.findByMobileNo(mdn).orElse(null);

		if (user != null) {
			try {
				BeaconMappingIO io = iotSvc.findByBconId(beacon.getDeviceId());
				if (io != null) {
					BeaconFireEventImpl beaconEvent = new BeaconFireEventImpl();
					beaconEvent.setDeviceId(io.getBconId());
					beaconEvent.setFctryTy(find(io.getFctryCd()));
					beaconEvent.setSetupLcTy(io.getSetupLc());
					beaconEvent.setInoutTy(InoutType.valueOf(beacon.getInoutType()));
					beaconEvent.setTimestamp(beacon.getTimestamp());
					beaconEvent.setUser(user);
					fireBconEvent(beaconEvent);
				}

			} catch (Exception e) {
				log.error("{}", e);
			}
		}

	}

	@Override
	public void restCallGpsEvent(String mdn, LocationRequest req) {
		RestTemplate restTemplate = new RestTemplate();
		String url = "http:
		restTemplate.postForObject(url, req, String.class);
	}

	@Override
	public void restCallBconEvent(String mdn, BeaconData req) {
		RestTemplate restTemplate = new RestTemplate();
		String url = "http:
		restTemplate.postForObject(url, req, String.class);
	}
}


