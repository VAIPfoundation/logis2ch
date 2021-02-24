package com.sdc2ch.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.sdc2ch.service.ILgistModelService;
import com.sdc2ch.web.admin.repo.dao.lgist.T_LgistModelRepository;
import com.sdc2ch.web.admin.repo.domain.lgist.QT_LGIST_MODEL;
import com.sdc2ch.web.admin.repo.domain.lgist.T_LGIST_MODEL;
import com.sdc2ch.web.admin.repo.domain.lgist.T_LGIST_MODEL.TableType;

@Service
public class LgistModelServiceImpl implements ILgistModelService {

	@Autowired T_LgistModelRepository modelRepo;
	
	
	@Override
	public List<T_LGIST_MODEL> searchModel() {
		return searchModel(null, null);
	}

	@Override
	public List<T_LGIST_MODEL> searchModel(Date start, Date end) {
		return searchModel(start, end, null);
	}

	@Override
	public List<T_LGIST_MODEL> searchModel(Date start, Date end, String modelNm) {
		return searchModel(start, end, null, TableType.DELIVERY.name());
	}
	@Override
	public List<T_LGIST_MODEL> searchModel(Date start, Date end, String modelNm, String logistTy) {
		return Lists.newArrayList(modelRepo.findAll(predicate(start, end, modelNm, logistTy)));
	}

	@Override
	public T_LGIST_MODEL save(T_LGIST_MODEL model) {
		return modelRepo.save(model);
	}
	
	
	private Predicate predicate(Date start, Date end, String modelNm, String logistTy) {
		QT_LGIST_MODEL qModel = QT_LGIST_MODEL.t_LGIST_MODEL;
		BooleanBuilder where = new BooleanBuilder();
		where.and(qModel.useYn.eq(true));
		
		if(start != null) {
			where.and(qModel.createDt.between(start, end));
		}
		if(!StringUtils.isEmpty(logistTy)) {
			where.and(qModel.lgistTy.eq(TableType.valueOf(logistTy)));
		}
		if(!StringUtils.isEmpty(modelNm)) {
			where.and(qModel.modelNm.like("%" + modelNm + "%"));
		}
		return where;
	}

	@Override
	public T_LGIST_MODEL findById(Long id) {
		return modelRepo.findById(id).orElse(new T_LGIST_MODEL());
	}

}
