package com.sdc2ch.web.service;

import java.util.List;
import java.util.Optional;

import com.sdc2ch.repo.io.MobileApkInfoIO;
import com.sdc2ch.repo.io.MobileAppInfoIO;
import com.sdc2ch.repo.io.ProdVerIO;
import com.sdc2ch.require.domain.IUser;

 
public interface IMobileAppService {

	
	boolean isAppVer(IUser user);

	
	boolean isTosVer(IUser user);

	
	boolean isAppTkn(IUser user);

	
	Optional<MobileApkInfoIO> findApkByVersion(int major, int minor);

	
	Optional<ProdVerIO> findWebByVersion(int major, int minor);

	
	Optional<MobileAppInfoIO> findAppInfoByUser(IUser user);

	
	List<MobileAppInfoIO> findAllByIds(List<String> ids);

	
	List<String> findAllAppKeyByIds(List<String> ids);

	
	MobileAppInfoIO save(MobileAppInfoIO appInfo);

	

	public String getCurAppVersion(IUser user);

	boolean isCurrentApkVersion(String version);

}
