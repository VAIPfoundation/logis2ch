package com.sdc2ch.agent.batch;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import com.sdc2ch.agent.service.I2chWebProcessStatService;
import com.sdc2ch.agent.service.IHealthCheckService;
import com.sdc2ch.agent.service.ILoginService;
import com.sdc2ch.agent.service.ProcessStat;
import com.sdc2ch.agent.service.vo.HealthCheckResVo;
import com.sdc2ch.service.common.IAppSttusService;
import com.sdc2ch.tms.service.ITmsSmsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ApiServerHealthCheckExcutor {

	@Autowired
	protected ILoginService loginSvc;
	@Autowired
	protected IHealthCheckService healthSvc;
	@Autowired
	protected IAppSttusService appSttusSvc;
	@Autowired
	protected ITmsSmsService smsSvc;


	
	@Value(value = "${api.healthCheck.try:3}")
	protected int reTry;
	
	@Value(value = "${api.healthCheck.app.name:2CH-API-SERVER}")
	protected String appName;
	
	@Value(value = "#{'${api.healthCheck.sms.phone:01041041207}'.split(',')}")
	protected List<String> targetMdn;
	
	@Value(value = "${api.healthCheck.sms.msg:서버에 장애가 발생하였습니다.}")
	protected String smsMsg;
	
	@Value(value = "${api.healthCheck.url.login:http:
	protected String urlLogin;
	
	@Value(value = "${api.healthCheck.url.healthcheck:http:
	protected String urlHealthChk;

	
	@Value(value = "${api.healthCheck.watch.url.healthcheck:http:
	protected String urlHealthChkUserCurrent;

	
	@Value(value = "${api.healthCheck.watch.try:10}")
	protected int reTryWatching;

	
	@Value(value = "${api.healthCheck.login.username:pweb}")
	protected String userName;

	
	@Value(value = "${api.healthCheck.login.password:pweb}")
	protected String passWord;

	
	@Value(value = "${api.healthCheck.sendsms.enable:false}")
	protected boolean isSendSms;

	
	@Value(value = "${api.healthCheck.sendsms.sendSmsCycleMin:30}")
	protected int sendSmsCycleMin;

	
	@Value(value = "${api.healthCheck.try.retry.interval.msec:90000}")
	protected int retryInterval;

	
	protected Date listSendSmsDt = null;

	@Scheduled(fixedDelayString = "${api.healthCheck.fixedDelay:60000}")
	public void excute() throws Exception {

		boolean success = true;
		String message = null;
		for (int i = 1; i <= reTry; i++) {
			try {
				String token = loginSvc.getToken(urlLogin, userName, passWord);
				
				HealthCheckResVo res = healthSvc.chkHealthCheck(urlHealthChk, token);
				log.info("{}", res);
				if (res.isSuccess()) {
					success = res.isSuccess();
					break;
				}
				success = false;
				message = res.getMsg();
			} catch (ResourceAccessException e) { 
				log.error("ResourceAccessException : {}", e);
				if (e.getRootCause() instanceof SocketTimeoutException 	|| e.getRootCause() instanceof ConnectException) {

					message = e.toString();
				}
			} catch (HttpClientErrorException e) { 
				log.error("HttpClientErrorException : {}", e);
				message = e.toString();
			}
			Thread.sleep(retryInterval); 
		}
		appSttusSvc.save(appName, new Date(), success, message); 

		
		if (!success && chkSendSmsTime(listSendSmsDt, new Date(), sendSmsCycleMin)) { 
			boolean smsSuccess = sendSms(isSendSms, userName, smsMsg, targetMdn);
			if (smsSuccess) { 
				listSendSmsDt = new Date(); 
			}
		}
	}

	
	protected boolean sendSms(boolean enable, String userName, String msg, List<String> mdnList) {
		try {
			if (enable) {
				mdnList.stream().forEach(mdn -> {
					smsSvc.sendSms(userName, msg, mdn);
				});
				return true;
			}
			return false;
		} catch (Exception e) {
			log.info("sendSms: {}", e);
			return false;
		}
	}

	
	protected boolean chkSendSmsTime(Date old, Date now, int cycleMin) {
		if (old == null) {
			return true;
		}
		return (now.getTime() - old.getTime()) > (cycleMin * 60 * 1000);
	}
	

}
