package com.sdc2ch.service.admin;

import java.util.List;

import com.sdc2ch.web.admin.repo.domain.op.T_BCON_MAPPING;

public interface IBconMappingService {

	
	List<T_BCON_MAPPING> searchBconMapping(String fctryCd);

	
	T_BCON_MAPPING saveBconMapping(T_BCON_MAPPING entity);
	List<T_BCON_MAPPING> saveBconMapping(List<T_BCON_MAPPING> entities);


}
