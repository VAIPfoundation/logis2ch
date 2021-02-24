package com.sdc2ch.agent.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.sdc2ch.agent.service.vo.HealthCheckResVo;


public interface IHealthCheckWatch2chWebService {

	
	long restart2chWebProcess() throws InterruptedException, UnsupportedEncodingException, IOException;
	
	
	int checkProcess(int pid);

	
	Process runProcess(String execPath);
	
	
	int killProcess(int pid);
	
	
	int report(int errorCount,int tryCount);

	int report(String msg);
}
