package com.sdc2ch.tms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdc2ch.tms.dao.TmsOilPriveRepositiry;
import com.sdc2ch.tms.io.TmsOilPriceIO;
import com.sdc2ch.tms.service.ITmsOilPriceService;

@Service
public class TmsOilPriceServiceImpl implements ITmsOilPriceService {
	
	@Autowired TmsOilPriveRepositiry repo;

	@SuppressWarnings("unchecked")
	@Override
	public List<TmsOilPriceIO> findAll() {
		return (List<TmsOilPriceIO>)(Object)repo.findAll();
	}

}
