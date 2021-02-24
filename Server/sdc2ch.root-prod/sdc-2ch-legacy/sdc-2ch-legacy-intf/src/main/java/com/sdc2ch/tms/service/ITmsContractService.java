package com.sdc2ch.tms.service;

import java.util.List;

import com.sdc2ch.tms.io.TmsContractIO;

public interface ITmsContractService {
	public List<TmsContractIO> findAll();
}
