package com.sdc2ch.agent.service;

import java.util.Date;


public interface I2chWebProcessStatService {

	
	ProcessStat getProcessStat();
	
	void setProcessStat(ProcessStat ps);
	
	Date getLastStatTime();
}
