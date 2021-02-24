package com.sdc2ch.service.admin;

import java.util.List;

import com.sdc2ch.web.admin.repo.domain.v.V_MOBILE_APP_USE_INFO;
import com.sdc2ch.web.admin.repo.domain.v.V_TOS_HIST;


public interface IManageMobileAppService {
	
	
	List<V_MOBILE_APP_USE_INFO> searchMobileApp(String fctryCd);
	List<V_MOBILE_APP_USE_INFO> searchMobileApp(String fctryCd, String vhcleTy, String vrn, String mobileNo, String driverNm);
	
	
	List<V_TOS_HIST> searchMobileAppHist(String vrn);
	

}
