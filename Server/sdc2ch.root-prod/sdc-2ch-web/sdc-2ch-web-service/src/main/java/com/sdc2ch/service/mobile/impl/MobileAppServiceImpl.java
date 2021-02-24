package com.sdc2ch.service.mobile.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.sdc2ch.core.utils.HashUtils;
import com.sdc2ch.repo.io.MobileApkInfoIO;
import com.sdc2ch.repo.io.MobileAppInfoIO;
import com.sdc2ch.repo.io.ProdVerIO;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.service.I2ChUserDetailsService;
import com.sdc2ch.service.admin.IAllocationConfirmGroupService;
import com.sdc2ch.service.mobile.IMobileApkInfoService;
import com.sdc2ch.service.mobile.IMobileAppInfoService;
import com.sdc2ch.web.admin.repo.domain.T_MOBILE_APP_USE_INFO;
import com.sdc2ch.web.admin.repo.domain.T_PROD_VER_INFO;
import com.sdc2ch.web.service.IMobileAppService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
class MobileAppServiceImpl implements IMobileAppService {


	@Autowired I2ChUserDetailsService driverSvc;
	@Autowired IMobileAppInfoService appInfoScv;
	@Autowired IMobileApkInfoService apkSvc;
	@Autowired IAllocationConfirmGroupService alloGroupSvc;

	@Override
	public boolean isAppVer(IUser user) {

		try {
			return findByAppInfo(user).getApk().isCurrent();
		} catch (Exception e) {
			log.warn("{}", e);
		}
		return false;
	}
	@Override
	public String getCurAppVersion(IUser user) {

		try {
			return findByAppInfo(user).getApk().getVersion();
		} catch (Exception e) {
			log.warn("{}", e);
		}
		return null;
	}

	@Override
	public boolean isTosVer(IUser user) {
		try {
			return !findByAppInfo(user).getTosses().stream().anyMatch(t -> t.isCurrent() == false);
		} catch (Exception e) {
			log.warn("{}", e);
		}
		return false;
	}

	@Override
	public boolean isAppTkn(IUser user) {
		try {
			return findByAppInfo(user).isValidTkn();
		} catch (Exception e) {
			log.warn("{}", e);
		}
		return false;
	}

	@Override
	public Optional<MobileApkInfoIO> findApkByVersion(int major, int minor) {
		String sha256 = major + "." + minor;
		return Optional.ofNullable(apkSvc.findByCrc(HashUtils.sha256(sha256)).orElse(null));
	}

	@Override
	public Optional<ProdVerIO> findWebByVersion(int major, int minor) {
		String prodTy = "MOBILE_WEB";
		return Optional.ofNullable(apkSvc.findByWebVersion(prodTy, major, minor).orElse(null));
	}

	@Override
	public Optional<MobileAppInfoIO> findAppInfoByUser(IUser user) {
		Assert.notNull(user, "IUser can not be null");
		return Optional.ofNullable(appInfoScv.findDriverAppInfoById(user.getUsername()).orElse(null));
	}

	private MobileAppInfoIO findByAppInfo(IUser user) {
		return findAppInfoByUser(user)
				.orElseThrow(() -> new RuntimeException("user app info not found"));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MobileAppInfoIO> findAllByIds(List<String> ids) {
		return (List<MobileAppInfoIO>)(Object)appInfoScv.findAllByIds(ids);
	}

	@Override
	public List<String> findAllAppKeyByIds(List<String> ids) {
		return findAllByIds(ids).stream().map(i -> i.getAppTkn()).collect(Collectors.toList());
	}

	@Override
	public MobileAppInfoIO save(MobileAppInfoIO appInfo) {
		return appInfoScv.save((T_MOBILE_APP_USE_INFO)appInfo);
	}
	@Override
	public boolean isCurrentApkVersion(String version) {
		return apkSvc.isCurrentApkVersion(version);
	}

}
