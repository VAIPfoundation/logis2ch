package com.sdc2ch.service.mobile;

import java.util.List;

import com.sdc2ch.service.mobile.model.MobileCtrlInfoVo;
import com.sdc2ch.web.admin.repo.domain.v.V_CARALC_PLAN;

 
public interface IMobileCntrlInfoService {
	
	
	V_CARALC_PLAN searchDlvyLc(String dlvyDe, String dlvyLcCd);
	
	
	List<Object[]> searchRoute(String dlvyDe, String dlvyLcCd);
	
	
	MobileCtrlInfoVo searchVhcle(String dlvyDe, String dlvyLcCd, String routeNo, String vrn);
	
}
