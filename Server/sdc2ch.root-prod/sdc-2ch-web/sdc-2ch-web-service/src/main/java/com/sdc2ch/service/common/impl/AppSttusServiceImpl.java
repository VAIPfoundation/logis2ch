package com.sdc2ch.service.common.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sdc2ch.service.common.IAppSttusService;
import com.sdc2ch.web.admin.repo.dao.T_AppSttusHistRepository;
import com.sdc2ch.web.admin.repo.dao.T_AppSttusRepository;
import com.sdc2ch.web.admin.repo.domain.sttus.T_APP_STTUS;
import com.sdc2ch.web.admin.repo.domain.sttus.T_APP_STTUS_HIST;

import lombok.extern.slf4j.Slf4j;



@Slf4j
@Service
public class AppSttusServiceImpl implements IAppSttusService {

	@Autowired
	private T_AppSttusRepository appSttusRepo;
	@Autowired
	private T_AppSttusHistRepository appSttusHistRepo;


	@Override
	public T_APP_STTUS save(String appName, Date cnfrimDt, boolean sttus, String message) {

		T_APP_STTUS appSttus = new T_APP_STTUS();
		appSttus.setAppName(appName);
		appSttus.setCnfirmDt(cnfrimDt);
		appSttus.setMssage(message);
		appSttus.setSttus(sttus);
		return save(appSttus);




	}

	@Transactional
	@Override
	public T_APP_STTUS save(T_APP_STTUS appSttus) {
		
		T_APP_STTUS_HIST hist = new T_APP_STTUS_HIST();
		hist.setAppName(appSttus.getAppName());
		hist.setCnfirmDt(appSttus.getCnfirmDt());
		hist.setMssage(appSttus.getMssage());
		hist.setSttus(appSttus.isSttus());
		
		
		appSttusHistRepo.save(hist);

		return appSttusRepo.save(appSttus);
	}

	@Override
	public T_APP_STTUS searchAppSttusById(String appName){
		return appSttusRepo.findById(appName).orElse(null);
	}


}
