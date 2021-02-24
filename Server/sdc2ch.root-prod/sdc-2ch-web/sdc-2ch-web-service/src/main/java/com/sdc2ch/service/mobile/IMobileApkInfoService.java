package com.sdc2ch.service.mobile;

import java.util.Optional;

import com.sdc2ch.web.admin.repo.domain.T_MOBILE_APK_INFO;
import com.sdc2ch.web.admin.repo.domain.T_PROD_VER_INFO;

public interface IMobileApkInfoService {

	
	Optional<T_MOBILE_APK_INFO> findByCrc(String crc);
	boolean isCurrentApkVersion(String version);

	Optional<T_PROD_VER_INFO> findByWebVersion(String prodTy, int major, int minor);

}
