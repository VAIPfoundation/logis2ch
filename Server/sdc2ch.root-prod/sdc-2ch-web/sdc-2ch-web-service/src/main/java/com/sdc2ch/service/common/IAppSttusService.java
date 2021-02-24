package com.sdc2ch.service.common;

import java.util.Date;

import com.sdc2ch.web.admin.repo.domain.sttus.T_APP_STTUS;

public interface IAppSttusService {

	
	T_APP_STTUS save(String appName, Date cnfrimDt, boolean sttus, String message);
	T_APP_STTUS save(T_APP_STTUS appSttus);

	
	T_APP_STTUS searchAppSttusById(String appName);
}
