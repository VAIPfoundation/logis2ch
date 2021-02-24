package com.sdc2ch.tms.service;

import java.util.List;

import com.sdc2ch.tms.io.TmsTurnRateIO;

public interface ITmsTurnRateService {
	public List<TmsTurnRateIO> findAll();
}
