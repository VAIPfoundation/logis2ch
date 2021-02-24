package com.sdc2ch.service.admin.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationPid;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.sdc2ch.service.admin.ISysActInfoService;
import com.sdc2ch.web.admin.repo.dao.T_SysActInfoHistRepository;
import com.sdc2ch.web.admin.repo.domain.sys.QT_SYS_ACT_INFO_HIST;
import com.sdc2ch.web.admin.repo.domain.sys.T_SYS_ACT_INFO_HIST;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SysActInfoServiceImpl implements ISysActInfoService{

	@Value("${app.id:}")
	private String appId;

	@Value("${app.name:}")
	private String appName;

	@Value("${app.version:}")
	private String appVersion;


	@Autowired
	private T_SysActInfoHistRepository sysActInfoHistRepo;

	@Override
	public String getAppId() {
		return appId;
	}

	@Override
	public String getAppName() {
		return appName;
	}

	@Override
	public String getAppVersion() {
		return appVersion;
	}

	@Override
	public T_SYS_ACT_INFO_HIST save(T_SYS_ACT_INFO_HIST hist) {
		return sysActInfoHistRepo.save(hist);
	}

	@Override
	public List<T_SYS_ACT_INFO_HIST> searchSysActInfoHistByAll(Date fromDt, Date toDt, String appId, String appName, String appVersion, String ip, String host, Boolean sttus, String path, Long size){
		QT_SYS_ACT_INFO_HIST from = QT_SYS_ACT_INFO_HIST.t_SYS_ACT_INFO_HIST;
		BooleanBuilder where = new BooleanBuilder();

		if (fromDt != null) {
			where.and(from.dataDt.goe(fromDt));
		}

		if (toDt != null) {
			where.and(from.dataDt.loe(toDt));
		}

		if (!StringUtils.isEmpty(appId)) {
			where.and(from.appName.like("%"+appId+"%"));
		}

		if (!StringUtils.isEmpty(appName)) {
			where.and(from.appName.like("%"+appName+"%"));
		}

		if (!StringUtils.isEmpty(appVersion)) {
			where.and(from.appVersion.like("%"+appVersion+"%"));
		}

		if (!StringUtils.isEmpty(ip)) {
			where.and(from.ip.like("%"+ip+"%"));
		}

		if (!StringUtils.isEmpty(host)) {
			where.and(from.host.like("%"+host+"%"));
		}

		if(sttus != null) {
			where.and(from.sttus.eq(sttus));
		}

		if(size != null) {
			where.and(from.size.eq(size));
		}

		return (List<T_SYS_ACT_INFO_HIST>) sysActInfoHistRepo.findAll(where);
	}


    private Long getFileSize(String appName, String appVersion) {
		String fileName = "."+File.separator+appName+"-"+appVersion+".jar";
		File file = new File(fileName);
		if(!file.exists()) {
			return null;
		}
		return file.length();
	}

	@Override
	public  T_SYS_ACT_INFO_HIST makeAppActInfo(boolean sttus) throws UnknownHostException {
		T_SYS_ACT_INFO_HIST info = new T_SYS_ACT_INFO_HIST() ;
		info.setAppPid(new ApplicationPid().toString());
		info.setAppId(appId);
		info.setAppName(appName);
		info.setDataDt(new Date());
		info.setHost(InetAddress.getLocalHost().getHostName());
		info.setIp(InetAddress.getLocalHost().getHostAddress());
		info.setPath(System.getProperty("user.dir").replace("\\", "/"));
		info.setSttus(sttus);
		info.setAppVersion(appVersion);
		info.setSize(getFileSize(appName, appVersion));
		return info;
	}

	@Value(value = "${application.pid.2chweb:/etc/smilk2ch/2chweb.main.pid}")
	String pidFilePath2ChWeb="";
	
	public void savePId() {
	    File file = new File(pidFilePath2ChWeb);
	    
	    try {
	    	log.info("write my PID into '{}'.",file.getCanonicalPath());
			new ApplicationPid().write(file);
		} catch (IOException e2) {
			
			log.error("{}",e2);
		}
	    
	}


}
