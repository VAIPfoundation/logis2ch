package com.sdc2ch.nfc.service;

import java.util.Date;
import java.util.List;

import com.sdc2ch.nfc.domain.entity.T_lg;
import com.sdc2ch.nfc.enums.NfcEventType;

public interface INfcEventService {
	List<T_lg> findAll();
	List<T_lg> gt(int tlogId);


	List<T_lg> findAll(Integer yyyyMM, Date fromDt, Date toDt);
	List<T_lg> findAll(Integer yyyyMM, Date fromDt, Date toDt, NfcEventType ... events);

}
