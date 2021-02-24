package com.sdc2ch.agent.service;

import java.util.List;

import com.sdc2ch.agent.service.vo.DetailTreeResVo;
import com.sdc2ch.agent.service.vo.HealthCheckResVo;
import com.sdc2ch.prcss.ds.repo.domain.T_ROUTE_STATE;

public interface IHealthCheckWatchService {

	String getToken(String url, String userName, String passWord);
	DetailTreeResVo chkHealthCheck(String url, String token);
	
	
	T_ROUTE_STATE selectLastDriverCd(String DLVY_DE);
	
	boolean checkErp();
}
