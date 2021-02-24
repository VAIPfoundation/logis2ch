package com.sdc2ch.service.admin;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

import com.sdc2ch.web.admin.repo.domain.sys.T_SYS_ACT_INFO_HIST;

public interface ISysActInfoService {

	
	T_SYS_ACT_INFO_HIST save(T_SYS_ACT_INFO_HIST hist);

	
	List<T_SYS_ACT_INFO_HIST> searchSysActInfoHistByAll(Date fromDt, Date toDt, String appId, String appName, String appVersion, String ip, String host, Boolean sttus, String path, Long size);


	
	T_SYS_ACT_INFO_HIST makeAppActInfo(boolean sttus) throws UnknownHostException;

	
	String getAppId();

	
	String getAppName();

	
	String getAppVersion();

	
	void savePId();


}
