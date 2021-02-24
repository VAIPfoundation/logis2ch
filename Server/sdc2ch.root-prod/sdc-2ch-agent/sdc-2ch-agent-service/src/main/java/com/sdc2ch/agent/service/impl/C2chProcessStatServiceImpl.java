package com.sdc2ch.agent.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.sdc2ch.agent.service.I2chWebProcessStatService;
import com.sdc2ch.agent.service.ProcessStat;

@Service
public class C2chProcessStatServiceImpl implements I2chWebProcessStatService {

	Date lastModifiedDatetime = new Date();
	
	ProcessStat pstat = ProcessStat.NONE;
	
	@Override
	public ProcessStat getProcessStat() {
		return pstat;
	}

	@Override
	public void setProcessStat(ProcessStat ps) {
		if(pstat != ps) {
			lastModifiedDatetime = new Date();	
			pstat = ps;
		}
	}

	@Override
	public Date getLastStatTime() {
		return lastModifiedDatetime;
	}

}
