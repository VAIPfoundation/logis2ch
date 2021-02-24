package com.sdc2ch.tms.service;

import java.util.List;

import com.sdc2ch.tms.io.TmsDayOffExeIO;

public interface ITmsDayOffExeService {

	
	public List<TmsDayOffExeIO> findAllByFctryCdAndDlvyMon(String fctryCd, String month);
	
}


