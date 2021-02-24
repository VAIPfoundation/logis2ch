package com.sdc2ch.web.admin.repo.init;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdc2ch.core.ISetupData;
import com.sdc2ch.core.utils.HashUtils;
import com.sdc2ch.web.admin.repo.dao.T_MobileApkRepository;
import com.sdc2ch.web.admin.repo.domain.T_MOBILE_APK_INFO;
import com.sdc2ch.web.admin.repo.init.impl.SetupDataImpl.INIT_ORDER;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AdminApkData implements ISetupData {

	private ObjectMapper mapper;
	private T_MobileApkRepository repo;
	
	@Autowired
	public void setAutowire(ObjectMapper mapper, T_MobileApkRepository repo) {
		this.mapper = mapper;
		this.repo = repo;
	}
	
	@Override
	public void install() {
		ClassPathResource classPathResource = new ClassPathResource("data/ApkData.json");
		
		try {
			
			
			@SuppressWarnings("unchecked")
			List<Object> apks = mapper.readValue(classPathResource.getURL(), List.class);
			
			for(Object mapped : apks) {
				T_MOBILE_APK_INFO apk = mapper.convertValue(mapped, T_MOBILE_APK_INFO.class);
				apk.setBin(new byte[(int) (10 + apk.getId())]);
				apk.setExtension("apk");
				String version = apk.getMajorVer() + "." + apk.getMinorVer();
				String sha256 = HashUtils.sha256(version);
				T_MOBILE_APK_INFO db = repo.findBySha256(sha256).orElse(null);
				if(db != null) {
					db.setCurrent(apk.isCurrent());
					apk = db;
				}
				apk.setSha256(sha256);
				apk.setMd5(HashUtils.md5(version));
				repo.save(apk);
			}  

		} catch (IOException e) {
			log.warn("{}", e.getMessage());
		}
	}

	@Override
	public int order() {
		return INIT_ORDER.SEVEN.ordinal();
	}

}
