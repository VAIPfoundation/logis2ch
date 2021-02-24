package com.sdc2ch.service.common;

import com.sdc2ch.service.common.model.alarm.ChkNoCnfirmAlarmDto;
import com.sdc2ch.service.common.model.alarm.ChkNoEtyBoxDto;
import com.sdc2ch.service.common.model.alarm.ChkNoLoadingDto;
import com.sdc2ch.service.common.model.alarm.ChkNoPassageDto;
import com.sdc2ch.service.common.model.alarm.ChkOffGpsDto;

public interface IAlarmService {

	
	void chkTotalAlarmAndSendMsg(String dlvyDe);

	
	ChkNoCnfirmAlarmDto chkCnfirmCaralcDtls(String fctryCd, String dlvyDe);

	
	ChkNoLoadingDto chkNoLoading(String fctryCd, String dlvyDe);

	
	ChkNoEtyBoxDto chkNoEtyBox(String fctryCd, String dlvyDe);

	
	ChkNoPassageDto chkNoPassage(String fctryCd, String dlvyDe);

	
	ChkOffGpsDto chkOffGps(String fctryCd, String dlvyDe);

	
	void sendMsngrMsg(String trnmisUserId, String trnsmisUserNm, String rcverUserId, String title, String body);



	
}
