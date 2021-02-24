package com.sdc2ch.agent.batch;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import com.sdc2ch.agent.service.I2chWebProcessStatService;
import com.sdc2ch.agent.service.IHealthCheckWatch2chWebService;
import com.sdc2ch.agent.service.IHealthCheckWatchService;
import com.sdc2ch.agent.service.ProcessStat;
import com.sdc2ch.agent.service.vo.DetailTreeResVo;
import com.sdc2ch.prcss.ds.repo.domain.T_ROUTE_STATE;
import com.sdc2ch.prcss.msngr.IMessengerService;
import com.sdc2ch.tms.service.ITmsSmsService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class ApiServerWatchExecutor extends ApiServerHealthCheckExcutor {

	@Autowired
	IHealthCheckWatch2chWebService healthCheckWatch2chWebService;
	@Autowired
	protected IHealthCheckWatchService healthWatchSvc;
	@Autowired
	protected I2chWebProcessStatService processStatSvc;

	
	@Value(value = "${api.healthCheck.watch.readTimeout.maxErrorCount:3}")
	int maxErrorCount;

	
	@Value(value = "${api.healthCheck.watch.max.connectfail.min:5}")
	int maxConnectFailTimeMin;

	Date lastSendReportDt;
	Date lastSendReportDt_RestartFail;
	
	int errorCount = 0;

	
	@Scheduled(fixedDelayString = "${api.healthCheck.watch.fixedDelay:180000}")
	public void excute() throws Exception {

		boolean sms = true; 
		boolean success = true; 
		String message = null; 














		LocalDate today = LocalDate.now().plusDays(1); 
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String stoday = today.format(formatter);

		errorCount = 0;

		
		T_ROUTE_STATE rs = null;
		boolean chkErp = false;
		try {
			rs = healthWatchSvc.selectLastDriverCd(stoday);
		}catch(Exception e) {
			success = false;
			message = "ms-sql query 실패. query 실패 시 health-check procedure는 중단됩니다. ";
			log.error(message);
			return;
		}
		
		try {
			chkErp = healthWatchSvc.checkErp();
			log.debug("healthWatchSvc.checkErp: " + chkErp);
			if(chkErp==false) {
				success = false;
				message = "Oracle check query 실패. query 실패 시 health-check procedure는 중단됩니다. ";
				log.error(message);
				appSttusSvc.save(appName, new Date(), success, message); 
				return;
			}
		}
		catch(Exception e) {
			success = false;
			message = "[Exception] Oracle check query 실패. query 실패 시 health-check procedure는 중단됩니다. " + e.toString();
			log.error(message);
			appSttusSvc.save(appName, new Date(), success, message); 
			return;
		}

		
		for (int j = 0; j < 10 && rs == null; j++) {
			
			today = today.plusDays(-1);
			stoday = today.format(formatter);
			rs = healthWatchSvc.selectLastDriverCd(stoday);
		}

		
		if (rs == null) {
			success = false;
			message = "excute: No Data - healthWatchSvc.selectLastDriverCd[{" + stoday + "}]";
			appSttusSvc.save(appName, new Date(), success, message); 
			return;
		}

		userName = rs.getDriverCd(); 
		passWord = "TEST"; 
		int loopCount = 0;
		String url="url";
		ProcessStat ps = processStatSvc.getProcessStat();
		for (loopCount = 0; loopCount < reTryWatching; loopCount++) {

			long startmsec = System.currentTimeMillis();
			long endmsecLogin = 0, endmsecHealthCheck = 0;
			try {
				
				url = urlLogin;
				String token = healthWatchSvc.getToken(urlLogin, userName, passWord);

				
				
				if (ps != ProcessStat.RUNNING) {
					ps = ProcessStat.RUNNING;
					processStatSvc.setProcessStat(ps);
				}
				
				endmsecLogin = System.currentTimeMillis();
				
				url = urlHealthChkUserCurrent;
				DetailTreeResVo res = healthWatchSvc.chkHealthCheck(urlHealthChkUserCurrent, token);
				
				endmsecHealthCheck = System.currentTimeMillis();

				endmsecHealthCheck -= endmsecLogin;
				endmsecLogin -= startmsec;



				if (res != null) {
					
					success = true;
					message = "ok - " + url + ", Login=" + endmsecLogin + " ms, HealthCheck="
							+ endmsecHealthCheck + " ms";
					errorCount = 0;
					log.info("Health Check: " + message);
					
					continue;
				}

				
				success = false;
				message = "fail - " + url + ", Login=" + endmsecLogin + " ms, HealthCheck="
						+ endmsecHealthCheck + " ms";
				log.warn("Health Check: " + message);

			} catch (ResourceAccessException e) { 
				success = false;
				
				if (e.getRootCause() instanceof SocketTimeoutException) {

					if (endmsecLogin == 0) {
						endmsecLogin = System.currentTimeMillis();
					}
					if (endmsecHealthCheck == 0) {
						endmsecHealthCheck = System.currentTimeMillis();
					}
					endmsecHealthCheck -= endmsecLogin;
					endmsecLogin -= startmsec;
					message = "fail - " + e.toString() + " - " + url + ", Login=" + endmsecLogin
							+ " ms, HealthCheck=" + endmsecHealthCheck + " ms";
					log.warn("Health Check: {}", message);
					errorCount++;

				} else 
				if (e.getRootCause() instanceof ConnectException) {
					if (endmsecLogin == 0) {
						endmsecLogin = System.currentTimeMillis();
					}
					if (endmsecHealthCheck == 0) {
						endmsecHealthCheck = System.currentTimeMillis();
					}
					endmsecHealthCheck -= endmsecLogin;
					endmsecLogin -= startmsec;
					message = "fail - " + e.toString() + " - " + url + ", Login=" + endmsecLogin
							+ " ms, HealthCheck=" + endmsecHealthCheck + " ms";
					log.warn("Health Check: {}", message);
	
					
					if (ps == ProcessStat.STARTED
							&& chkTimeGap(processStatSvc.getLastStatTime(), new Date(), maxConnectFailTimeMin)) {

						long pid = healthCheckWatch2chWebService.restart2chWebProcess();

						if(pid>0) {
							message = "서버 자동 재기동 "+maxConnectFailTimeMin+" 분 내에 접속이 안되어 다시 재기동-성공.";
							log.info("RESTART again 2ch web process : PID="+pid+", "+message);
						}else {
							message = "서버 자동 재기동 "+maxConnectFailTimeMin+" 분 내에 접속이 안되어 다시 재기동-실패";
							log.info("RESTART FAIL!!! 2ch web process : PID="+pid+", "+message);
						}
						log.info(message);
						healthCheckWatch2chWebService.report(message);
						
						break;

					}
					
					if( ps == ProcessStat.RUNNING) {
						log.debug("정상이었는데 접속이 안됨.");
						errorCount++;
					}
				}
			} catch (HttpClientErrorException e) { 
				success = false;
				if (endmsecLogin == 0) {
					endmsecLogin = System.currentTimeMillis();
				}
				if (endmsecHealthCheck == 0) {
					endmsecHealthCheck = System.currentTimeMillis();
				}
				endmsecHealthCheck -= endmsecLogin;
				endmsecLogin -= startmsec;
				
				switch(e.getStatusCode()) {
					case NOT_FOUND:
						message = "ok (but exception) - " + e.toString() + " - " + url + ", Login=" + endmsecLogin + " ms, HealthCheck=" + endmsecHealthCheck + " ms";
						log.info("Health Check: {}", message);
						break;
					default:
						message = "fail - " + e.toString() + " - " + url + ", Login=" + endmsecLogin + " ms, HealthCheck=" + endmsecHealthCheck + " ms";
						log.warn("Health Check: {}", message);
						errorCount++;
						break;
				}
				
			} catch (Exception e) {

				log.error("Exception : {}", e);
				success = false; 
				message = e.toString();
				sms = false; 
				break;

			}
			Thread.sleep(1000); 
		}

		log.info("healthCheckWatch: errCnt={}, loopCnt={}", errorCount, loopCount);

		if (errorCount > maxErrorCount) {
			try {
				
				healthCheckWatch2chWebService.report(errorCount, loopCount);
				
				long pid = healthCheckWatch2chWebService.restart2chWebProcess();
				
				message = "TOO MANY ERRORS("+errorCount+"/"+loopCount+"). RESTART 2ch web process";
				log.info(message);
			} catch (Exception e3) {
				log.error("{}", e3.toString());
			}
			success = false; 
		} else {
			success = true;
		}

		appSttusSvc.save(appName, new Date(), success, message); 

	}
	
	
	protected boolean chkTimeGap(Date old, Date now, int gapMin) {
		if (old == null) {
			return true;
		}
		return (now.getTime() - old.getTime()) > (gapMin * 60 * 1000);
	}
	
	
	protected boolean chkSendReportTime(Date old, Date now, int cycleMin) {
		return chkTimeGap(old,now,cycleMin);
	}	
}
