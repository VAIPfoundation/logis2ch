package com.sdc2ch.service;

import java.util.Date;
import java.util.List;

import com.sdc2ch.web.admin.repo.domain.lgist.T_LGIST_MODEL;


public interface ILgistModelService {
	
	
	List<T_LGIST_MODEL> searchModel();
	List<T_LGIST_MODEL> searchModel(Date start, Date end);
	List<T_LGIST_MODEL> searchModel(Date start, Date end, String modelNm);
	List<T_LGIST_MODEL> searchModel(Date start, Date end, String modelNm, String logistTy);
	
	
	T_LGIST_MODEL save(T_LGIST_MODEL model);
	T_LGIST_MODEL findById(Long id);

}
