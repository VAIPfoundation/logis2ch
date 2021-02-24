package com.sdc2ch.legacy.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sdc2ch.aiv.event.IFirebaseNotificationEvent;
import com.sdc2ch.legacy.aiv.service.IOTSensorService;
import com.sdc2ch.legacy.endpoint.request.BeaconData;
import com.sdc2ch.legacy.endpoint.request.HealthCheckReq;
import com.sdc2ch.legacy.endpoint.request.LocationRequest;
import com.sdc2ch.legacy.endpoint.request.RefreshTokenRequest;
import com.sdc2ch.legacy.endpoint.response.LocationResponse;
import com.sdc2ch.legacy.endpoint.response.RefreshTokenResponse;
import com.sdc2ch.require.pubsub.I2ChEventManager;
import com.sdc2ch.require.pubsub.I2ChEventPublisher;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/m")
public class MobileLocationEndpoint {


	@Autowired IOTSensorService locSvc;

	private I2ChEventPublisher<IFirebaseNotificationEvent> eventPush;


	private static final Logger logger = LoggerFactory.getLogger(MobileLocationEndpoint.class);

	@Autowired
	public void init(I2ChEventManager manager) {
		this.eventPush = manager.regist(IFirebaseNotificationEvent.class);
	}



	@RequestMapping("/api/loc/{mdn}")
	public LocationResponse saveLocation(@RequestBody LocationRequest body, @PathVariable String mdn){

		LocationResponse response = new LocationResponse();

		if(body != null) {

			

			locSvc.saveLocationV2(mdn, body.getDatas());

			try {
				locSvc.restCallGpsEvent(mdn, body);
			} catch (Exception e) {
				e.printStackTrace();
				log.error("{}", e);
			}

		}
		return response;
	}


	@RequestMapping("/api/loc/event/{mdn}")
	public LocationResponse callLocationEvent(@RequestBody LocationRequest body, @PathVariable String mdn){

		LocationResponse response = new LocationResponse();
		if(body != null) {

			locSvc.fireEventGps(mdn, body.getDatas());
		}
		return response;
	}

	@RequestMapping("/api/loc/hasdelivery/{mdn}")
	public String has(@PathVariable String mdn){


























		return "{\"hasDelivery\":" + true + "}";
	}

	@RequestMapping("/api/app/version")
	public String version(){


		String version = "1.0.1";


		return "{\"version\":\"" + version + "\"}";
	}

	@RequestMapping("/api/app/refreshToken")
	public RefreshTokenResponse refreshToken(@RequestBody RefreshTokenRequest req){

		RefreshTokenResponse res = new RefreshTokenResponse();
		res.setSuccess(true);
		res.setCode(1);

		return res;
	}
	@RequestMapping("/api/app/refreshToken/{mdn}")
	public RefreshTokenResponse refreshToken2(@PathVariable String mdn, @RequestBody RefreshTokenRequest req){
		return refreshToken(req);
	}

	



	@RequestMapping("/api/app/healthCheck")
	public void healthCheck(@RequestBody HealthCheckReq req){
		try {
			locSvc.saveMobielHealthCheck(req);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/api/app/healthCheck/{mdn}")
	public void healthCheck2(@PathVariable String mdn, @RequestBody HealthCheckReq req){
		req.setMdn(mdn);

		try {
			locSvc.saveMobielHealthCheck(req);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	@RequestMapping("/api/beacon/{mdn}")
	public void saveBeacon(@RequestBody BeaconData body, @PathVariable String mdn){

		if(body != null) {


			

			locSvc.saveBeaconV2(mdn, body);
			try {
				locSvc.restCallBconEvent(mdn, body);
			} catch (Exception e) {
				e.printStackTrace();
				log.error("{}", e);
			}
		}
	}

	@RequestMapping("/api/beacon/event/{mdn}")
	public void callBeaconEvent(@RequestBody BeaconData body, @PathVariable String mdn){
		if(body != null) {

			locSvc.fireEventBeacon(mdn, body);
		}
	}

}
