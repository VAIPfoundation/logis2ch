package com.sdc2ch.tms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdc2ch.tms.dao.TmsTurnRateRepositiry;
import com.sdc2ch.tms.io.TmsTurnRateIO;
import com.sdc2ch.tms.service.ITmsTurnRateService;

@Service
public class TmsTurnRateServiceImpl implements ITmsTurnRateService {

	@Autowired TmsTurnRateRepositiry turnReqp;
	@SuppressWarnings("unchecked")
	@Override
	public List<TmsTurnRateIO> findAll() {
		return (List<TmsTurnRateIO>)(Object)turnReqp.findAll();
	}

}
