package com.sdc2ch.service.admin;

import java.util.Optional;

import com.sdc2ch.web.admin.repo.domain.alloc.T_SNITAT_CHCK_TABLE;

public interface ISanitationService {
		
	Optional<T_SNITAT_CHCK_TABLE> findById(Long id);
	
}
