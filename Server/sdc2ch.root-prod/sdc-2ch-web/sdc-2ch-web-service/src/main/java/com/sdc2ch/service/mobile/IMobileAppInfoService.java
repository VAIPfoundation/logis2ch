package com.sdc2ch.service.mobile;

import java.util.List;
import java.util.Optional;

import com.sdc2ch.web.admin.repo.domain.T_MOBILE_APP_USE_INFO;

 
public interface IMobileAppInfoService {

	
	Optional<T_MOBILE_APP_USE_INFO> findDriverAppInfoById(String id);
	
	
	List<T_MOBILE_APP_USE_INFO> findAllByIds(String ... ids);
	List<T_MOBILE_APP_USE_INFO> findAllByIds(List<String> ids);
	
	
	
	T_MOBILE_APP_USE_INFO save(T_MOBILE_APP_USE_INFO appInfo); 

}
