package com.sdc2ch.service.admin.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.sdc2ch.service.admin.ISanitationService;
import com.sdc2ch.web.admin.repo.dao.T_SnitatChckTableRepository;
import com.sdc2ch.web.admin.repo.domain.alloc.T_SNITAT_CHCK_TABLE;

public class SanitationServiceImpl implements ISanitationService  {
	
	@Autowired T_SnitatChckTableRepository repo; 
	
	
	@Override
	public Optional<T_SNITAT_CHCK_TABLE> findById(Long id) {
		return repo.findById(id);
	}
}
