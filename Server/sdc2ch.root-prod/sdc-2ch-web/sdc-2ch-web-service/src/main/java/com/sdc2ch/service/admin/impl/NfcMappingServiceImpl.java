package com.sdc2ch.service.admin.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.sdc2ch.service.admin.INfcMappingService;
import com.sdc2ch.web.admin.repo.dao.T_NfcMappingRepository;
import com.sdc2ch.web.admin.repo.domain.op.T_NFC_MAPPING;

@Service
public class NfcMappingServiceImpl implements INfcMappingService{

	@Autowired
	private T_NfcMappingRepository nfcMappingRepo;


	@Override
	public List<T_NFC_MAPPING> searchNfcMappingByFctryCd(String fctryCd){
		return  StringUtils.isEmpty(fctryCd) ?
				nfcMappingRepo.findAll():
				nfcMappingRepo.findByFctryCd(fctryCd);
	}


	@Override
	public T_NFC_MAPPING saveNfcMapping(T_NFC_MAPPING entity) {
		return nfcMappingRepo.save(entity);
	}


	@Override
	public List<T_NFC_MAPPING> saveNfcMapping(List<T_NFC_MAPPING> entityList) {
		return nfcMappingRepo.saveAll(entityList);
	}


}
