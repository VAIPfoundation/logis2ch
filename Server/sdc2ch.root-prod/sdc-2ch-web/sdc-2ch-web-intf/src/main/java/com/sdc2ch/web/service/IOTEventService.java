package com.sdc2ch.web.service;

import java.util.Date;
import java.util.List;

import com.sdc2ch.repo.io.BeaconMappingIO;
import com.sdc2ch.repo.io.NfcMappingIO;
import com.sdc2ch.service.io.BeaconDataIO;
import com.sdc2ch.service.io.GpsDataIO;
import com.sdc2ch.service.io.MobileHealthCheckIO;
import com.sdc2ch.service.io.NfcSeqIO;


public interface IOTEventService {
	
	BeaconMappingIO findByBconId(String bconId);

	
	NfcMappingIO findByNfcId(int id);

	
	void saveBconHist(BeaconDataIO data);
	
	void saveLocation(String mdn, List<GpsDataIO> datas);

	void writeNfcLogSeqId(int id);
	void writeNfcLog(int id, String lastmonth);

	int readNfcLogSeqId();
	public Date readNfcSeqDate();
	public NfcSeqIO findNfcSeqById(Long Id);


	
	public void saveMobielHealthCheck(MobileHealthCheckIO io);


}
