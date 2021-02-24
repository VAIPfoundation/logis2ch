package com.sdc2ch.tms.service;

import java.util.List;

import com.sdc2ch.tms.io.TmsOilPriceIO;

public interface ITmsOilPriceService {
	public List<TmsOilPriceIO> findAll();
}
