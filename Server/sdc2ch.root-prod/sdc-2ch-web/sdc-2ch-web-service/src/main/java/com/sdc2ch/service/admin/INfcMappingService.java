package com.sdc2ch.service.admin;

import java.util.List;

import com.sdc2ch.web.admin.repo.domain.op.T_NFC_MAPPING;

public interface INfcMappingService {

	
	List<T_NFC_MAPPING> searchNfcMappingByFctryCd(String fctryCd);

	
	T_NFC_MAPPING saveNfcMapping(T_NFC_MAPPING entity);
	List<T_NFC_MAPPING> saveNfcMapping(List<T_NFC_MAPPING> entityList);

}
