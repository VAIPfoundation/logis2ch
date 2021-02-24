package com.sdc2ch.nfc.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdc2ch.nfc.domain.entity.T_lg;
import com.sdc2ch.nfc.enums.NfcEventType;
import com.sdc2ch.nfc.io.NfcEventIO;
import com.sdc2ch.nfc.service.INfcEventService;
import com.sdc2ch.nfc.service.INfcTagHistService;

@Service
public class NfcTagHistServiceImpl implements INfcTagHistService{

	@Autowired
	private INfcEventService nfcEventSvc;

	@Override
	public List<NfcEventIO> findAll(Integer yyyMMdd, Date fromDt, Date toDt){


		return findAll(yyyMMdd, fromDt, toDt, NfcEventType.values());
	}

	@Override
	public List<NfcEventIO> findAll(Integer yyyMMdd, Date fromDt, Date toDt, NfcEventType ... events){

		return nfcEventSvc.findAll(yyyMMdd, fromDt, toDt, events).stream().map(this::convertToNfcEventIO).collect(Collectors.toList());
	}


	private NfcEventIO convertToNfcEventIO(T_lg lg) {
	    return (NfcEventIO)lg;
	}
}
