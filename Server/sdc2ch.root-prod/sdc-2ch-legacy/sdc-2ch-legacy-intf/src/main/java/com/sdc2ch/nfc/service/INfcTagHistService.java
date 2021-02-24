package com.sdc2ch.nfc.service;

import java.util.Date;
import java.util.List;

import com.sdc2ch.nfc.enums.NfcEventType;
import com.sdc2ch.nfc.io.NfcEventIO;

public interface INfcTagHistService {

	
	List<NfcEventIO> findAll(Integer yyyMMdd, Date fromDt, Date toDt);

	List<NfcEventIO> findAll(Integer yyyMMdd, Date fromDt, Date toDt, NfcEventType ... events);

}
