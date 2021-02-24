package com.sdc2ch.agent.service;

import com.sdc2ch.agent.service.vo.HealthCheckResVo;

public interface IHealthCheckService {

	HealthCheckResVo chkHealthCheck(String url, String token);

}
