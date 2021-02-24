package com.sdc2ch.tms.service;

import java.util.List;

public interface ITmsSmsService {

	
	String PROC_SEND_SMS = "[DBO].[SP_EXE_SMS_ACTION]";

	
	boolean sendSms(String userName, String msg, String targetMdn);
	
	
	boolean sendSms(boolean enable, String userName, String msg, List<String> mdnList);

}
