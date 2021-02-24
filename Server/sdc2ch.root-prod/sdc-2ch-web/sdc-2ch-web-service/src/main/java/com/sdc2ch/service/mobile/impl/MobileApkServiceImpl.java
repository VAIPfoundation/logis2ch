package com.sdc2ch.service.mobile.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdc2ch.service.mobile.IMobileApkInfoService;
import com.sdc2ch.web.admin.repo.dao.T_MobileApkRepository;
import com.sdc2ch.web.admin.repo.dao.T_ProdVerInfoRepository;
import com.sdc2ch.web.admin.repo.domain.T_MOBILE_APK_INFO;
import com.sdc2ch.web.admin.repo.domain.T_PROD_VER_INFO;

@Service
class MobileApkServiceImpl implements IMobileApkInfoService {

	@Autowired T_MobileApkRepository apkRepo;
	@Autowired T_ProdVerInfoRepository prodVerRepo;

	@Override
	public Optional<T_MOBILE_APK_INFO> findByCrc(String crc) {
		return apkRepo.findBySha256(crc);
	}

	@Override
	public boolean isCurrentApkVersion(String version) {
		T_MOBILE_APK_INFO apk = apkRepo.findAll().stream().filter(a -> a.isCurrent()).findFirst().orElse(null);
		if(apk != null) {
			return version.equals(apk.getMajorVer() + "." + apk.getMinorVer());
		}
		return false;
	}

	@Override
	public Optional<T_PROD_VER_INFO> findByWebVersion(String prodTy, int major, int minor) {
		return prodVerRepo.findByProdTyAndMajorVerAndMinorVer(prodTy, major, minor);
	}
}
