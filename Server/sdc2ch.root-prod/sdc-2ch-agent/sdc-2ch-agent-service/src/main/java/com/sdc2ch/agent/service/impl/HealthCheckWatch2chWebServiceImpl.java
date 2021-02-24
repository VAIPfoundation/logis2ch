package com.sdc2ch.agent.service.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sdc2ch.agent.service.I2chWebProcessStatService;
import com.sdc2ch.agent.service.IHealthCheckWatch2chWebService;
import com.sdc2ch.agent.service.ProcessStat;
import com.sdc2ch.core.utils.Smilk2ChProcess;
import com.sdc2ch.prcss.msngr.IMessengerService;
import com.sdc2ch.tms.service.ITmsSmsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HealthCheckWatch2chWebServiceImpl implements IHealthCheckWatch2chWebService  {

	
	@Value(value = "${api.healthCheck.2chweb.runcommand.path:./run.2chweb.bat}")
	String exec2ChWebPath="";

	
	@Value(value = "${application.pid.2chweb:/etc/smilk2ch/2chweb.pid}")
	String pidFilePath2ChWeb="";
	
	
	@Value(value = "${api.healthCheck.sendsms.enable:false}")
	protected boolean isSendSms;

	
	@Value(value = "${api.healthCheck.sendMessenger.enable:false}")
	protected boolean isSendMessenger;

	
	@Value(value = "${api.healthCheck.restart.enable:true}")
	protected boolean enableRestartProcess;
	
	
	@Autowired
	protected ITmsSmsService smsSvc;
	
	
	@Autowired
	IMessengerService msngrSvc;
	
	@Autowired
	protected I2chWebProcessStatService processStatSvc;

	
	public long restart2chWebProcess() throws InterruptedException, IOException
	{
		int pId2chWeb = get2chWebPid();
		Process pr = null;
		
		if( enableRestartProcess == false )
			return 0L;
		
		int pstat = checkProcess(pId2chWeb); 
		log.info("checkProcess pid={},stat={}",pId2chWeb,pstat);

		if(pstat < 0) {
			processStatSvc.setProcessStat(ProcessStat.NO_PROCESS);	
		}
		while (pstat > 0) {	
			try {
				log.info("killProcess pid={}",pId2chWeb);
				killProcess(pId2chWeb);	
				processStatSvc.setProcessStat(ProcessStat.KILLED);	
				Thread.sleep(10000);	
				pstat = checkProcess(pId2chWeb); 
				log.info("checkProcess after killing. pid={},stat={}",pId2chWeb,pstat);
			} catch (Exception e) {
				log.error("[Exception] restart2chWebProcess:{}",e.toString());
			}
		}


		pr = runProcess(exec2ChWebPath); 
		if(pr!=null) {
			processStatSvc.setProcessStat(ProcessStat.STARTED);	
	        InputStreamReader isReader = new InputStreamReader(pr.getInputStream(),"MS949");
	        BufferedReader bReader = new BufferedReader(isReader);
	        String strLine = null;
	        for(int i = 0 ; i < 1 ; i ++) {	
	        	strLine= bReader.readLine();
	        	if(strLine == null) 
	        		break;
	        	log.info("RUNNING >>>"+strLine);;
	        }
			
			Thread.sleep(5000); 
			pId2chWeb = get2chWebPid(); 
			
			pstat = checkProcess(pId2chWeb); 
			log.info("checkProcess again. pid={},stat={}",pId2chWeb,pstat);
			return pId2chWeb;
		} else {
			processStatSvc.setProcessStat(ProcessStat.NO_PROCESS);	
		}
		return -1L;
	}
	
	
	int get2chWebPid() {
		
		int pid = -1; 

		try (FileReader reader = new FileReader(pidFilePath2ChWeb); BufferedReader br = new BufferedReader(reader)) {

			
			String line;
			if ((line = br.readLine()) != null) {
				pid = Integer.parseInt(line);
			} else {
				pid = -1;
				log.error("get2chWebPid:{} CANNOT read pid;{}", line);
			}

		} catch (IOException e) {
			log.error("[IOException] get2chWebPid:{}", e.toString());
		} catch (NumberFormatException e) {
			log.error("[NumberFormatException] get2chWebPid:{}", e.toString());
		}
		
		return pid;	
	}
	
	
	public int checkProcess(int pid)
	{
		boolean alive = Smilk2ChProcess.isStillAllive(String.valueOf(pid));
		if(alive)
			return 1;
		return -1;
	}
	
	public int checkProcess(Process pr)
	{
		if(pr.isAlive()) {
			log.info("process is alive.");
			return 1;
		}
		log.info("process is down.");
		return -1;
	}

	
	public Process runProcess(String execPath) {
		
		Process pr=null;
		String command = execPath;
		try {
			pr = Smilk2ChProcess.execProcess(command);
			log.info("runProcess: {}",command);

		} catch (IOException e) {
			
			log.error("runProcess:"+e.toString());
		}
		
		return pr;
	}

	
	public int killProcess(int pid)
	{
		return Smilk2ChProcess.killProcess(pid);
	}

	
	public String getExec2ChWebPath() {
		return exec2ChWebPath;
	}

	
	public void setExec2ChWebPath(String exec2ChWebPath) {
		this.exec2ChWebPath = exec2ChWebPath;
	}

	
	@Value(value = "${api.healthCheck.login.username:pweb}")
	protected String userName;

	
	@Value(value = "#{'${api.healthCheck.sms.phone:01032239770,01049157459}'.split(',')}")
	protected List<String> targetMdn;

	
	@Value(value = "#{'${api.healthCheck.messenger.sendto.ids:aivision12,aivision14}'.split(',')}")
	List<String> targetMsgnrIds;
	
	
	@Value(value = "${api.healthCheck.sendsms.sendSmsCycleMin:30}")
	protected int sendSmsCycleMin;

	
	@Value(value = "${api.healthCheck.watch.alert1.msg:[2CH 시스템] 시스템 장애로 인해 2ch web process를 자동으로 재기동 합니다.}")
	protected String alertMsg;
	
	
	@Value(value = "${api.healthCheck.sendMessenger.senderid:aivision12}")
	protected String msngrSenderId;
	
	
	@Value(value = "${api.healthCheck.sendMessenger.sendername:2CH 시스템}")
	protected String msngrSenderName;
	
	Date lastSendSmsDt;
	Date lastSendMsngrDt;
	
	@Override
	public int report(int errorCount,int tryCount) {
		String msg = alertMsg;
		report(msg);












		return 0;
	}
	@Override
	public int report(String msg) {
		if(isSendMessenger && msngrSvc!=null ) {
			msngrSvc.sendMessengerMsg(msngrSenderId, msngrSenderName, targetMsgnrIds, "2CH SYSTEM ALARM", msg);
		}
		if(isSendSms) {
			smsSvc.sendSms(isSendSms, userName, msg, targetMdn);
		}
		return 0;
	}
	
	protected boolean chkSendReportTime(Date old, Date now, int cycleMin) {
		if (old == null) {
			return true;
		}
		return (now.getTime() - old.getTime()) > (cycleMin * 60 * 1000);
	}	
	
}
