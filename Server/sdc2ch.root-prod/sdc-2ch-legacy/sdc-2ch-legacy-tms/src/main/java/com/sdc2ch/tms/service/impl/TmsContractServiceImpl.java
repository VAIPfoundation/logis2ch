package com.sdc2ch.tms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdc2ch.tms.dao.TmsContractRepositiry;
import com.sdc2ch.tms.io.TmsContractIO;
import com.sdc2ch.tms.service.ITmsContractService;

@Service
public class TmsContractServiceImpl implements ITmsContractService {
	
	@Autowired TmsContractRepositiry repo;

	@SuppressWarnings("unchecked")
	@Override
	public List<TmsContractIO> findAll() {
		return (List<TmsContractIO>)(Object)repo.findAll();
	}

}
