package com.sdc2ch.service.admin.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microsoft.sqlserver.jdbc.StringUtils;
import com.querydsl.core.BooleanBuilder;
import com.sdc2ch.service.admin.IBconMappingService;
import com.sdc2ch.web.admin.repo.dao.T_BconMappingRepository;
import com.sdc2ch.web.admin.repo.domain.op.QT_BCON_MAPPING;
import com.sdc2ch.web.admin.repo.domain.op.T_BCON_MAPPING;

@Service

public class BconMappingService implements IBconMappingService{

	@Autowired
	private T_BconMappingRepository bconMappingReop;

	@Override
	public List<T_BCON_MAPPING> searchBconMapping(String fctryCd){
		QT_BCON_MAPPING bconMapping = QT_BCON_MAPPING.t_BCON_MAPPING;

		BooleanBuilder where = new BooleanBuilder();

		if(!StringUtils.isEmpty(fctryCd)) {
			where.and(bconMapping.fctryCd.eq(fctryCd));
		}

		return (List<T_BCON_MAPPING>)bconMappingReop.findAll(where);
	}

	@Override
	@Transactional
	public T_BCON_MAPPING saveBconMapping(T_BCON_MAPPING entity) {
		return bconMappingReop.save(entity);
	}

	@Override
	@Transactional
	public List<T_BCON_MAPPING> saveBconMapping(List<T_BCON_MAPPING> entities) {
		if(entities == null || entities.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		return bconMappingReop.saveAll(entities);
	}


}
