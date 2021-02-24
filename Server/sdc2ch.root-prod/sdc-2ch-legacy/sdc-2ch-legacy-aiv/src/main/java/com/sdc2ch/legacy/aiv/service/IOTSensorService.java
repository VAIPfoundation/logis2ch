package com.sdc2ch.legacy.aiv.service;

import java.util.List;

import com.sdc2ch.legacy.endpoint.request.BeaconData;
import com.sdc2ch.legacy.endpoint.request.GpsData;
import com.sdc2ch.legacy.endpoint.request.LocationRequest;
import com.sdc2ch.service.io.MobileHealthCheckIO;

public interface IOTSensorService {
	void saveLocation(String mdn, List<GpsData> datas);
	void saveBeacon(String mdn, BeaconData beacon);
	void saveMobielHealthCheck(MobileHealthCheckIO mobileHealthCheck);

	void saveLocationV2(String mdn, List<GpsData> datas);
	void saveBeaconV2(String mdn, BeaconData beacon);

	void fireEventGps(String mdn, List<GpsData> datas);
	void fireEventBeacon(String mdn, BeaconData beacon);


	void restCallGpsEvent(String mdn, LocationRequest req);
	void restCallBconEvent(String mdn, BeaconData req);
}
