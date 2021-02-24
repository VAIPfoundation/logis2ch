package com.sdc2ch.agent.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sdc2ch.core.endpoint.DefaultEndpoint;
import com.sdc2ch.service.common.IAppSttusService;
import com.sdc2ch.web.admin.repo.domain.sttus.T_APP_STTUS;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/monitor/")
public class SttusEndpoint extends DefaultEndpoint {

	@Autowired
	private IAppSttusService appSttusSvc;

	@ApiOperation(value = "서버 상태 확인", response = T_APP_STTUS.class)
	@RequestMapping(value = "/getAppSttus/{appName}", produces = SUPPORTED_TYPE, method= {RequestMethod.GET, RequestMethod.POST})
	public T_APP_STTUS searchAppSttus(@PathVariable(name="appName", required=true) String appName) {
		return appSttusSvc.searchAppSttusById(appName);
	}
}
