package com.sdc2ch.cvo.service;

import java.util.List;

import com.sdc2ch.cvo.io.TraceIO;

public interface ICvoControlService {
	
	List<TraceIO> getVehicleTrace(String dlvyDe, String setId);
}
