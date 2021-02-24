package com.sdc2ch.service.admin.impl;

import java.util.Collections;
import java.util.List;

import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.QSort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.ResultTransformer;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.sdc2ch.service.admin.IAllocationConfirmService;
import com.sdc2ch.web.admin.repo.AdmQueryBuilder;
import com.sdc2ch.web.admin.repo.dao.V_CaralcDtlsRepository;
import com.sdc2ch.web.admin.repo.dao.V_CaralcMstrRepository;
import com.sdc2ch.web.admin.repo.dao.V_CaralcPlanRepository;
import com.sdc2ch.web.admin.repo.domain.v.QV_CARALC_DTLS;
import com.sdc2ch.web.admin.repo.domain.v.QV_CARALC_MSTR;
import com.sdc2ch.web.admin.repo.domain.v.QV_CARALC_PLAN;
import com.sdc2ch.web.admin.repo.domain.v.V_CARALC_DTLS;
import com.sdc2ch.web.admin.repo.domain.v.V_CARALC_MSTR;
import com.sdc2ch.web.admin.repo.domain.v.V_CARALC_PLAN;

@Service
class AllocationConfirmServiceImpl implements IAllocationConfirmService {

	@Autowired
	AdmQueryBuilder builder;






	@Autowired V_CaralcMstrRepository caralcMstrRepo;
	@Autowired V_CaralcDtlsRepository caralcDtlsRepo;
	@Autowired V_CaralcPlanRepository caralcPlanRepo;






































	@Override
	public List<V_CARALC_MSTR> findCaralcMstrByDlvyDeAndVrn(String dlvyDe, String vrn){
		QV_CARALC_MSTR from = QV_CARALC_MSTR.v_CARALC_MSTR;
		return (List<V_CARALC_MSTR>)caralcMstrRepo.findAllByDlvyDeAndVrn(dlvyDe, vrn, new QSort(from.id.asc())).orElse(Collections.emptyList());
	}

	@Override
	public List<V_CARALC_DTLS> findCaralcDtlsByDeliveryDateAndRouteNo(String dlvyDe, String routeNo){
		QV_CARALC_DTLS from = QV_CARALC_DTLS.v_CARALC_DTLS;
		return (List<V_CARALC_DTLS>)caralcDtlsRepo.findAllByDeliveryDateAndRouteNo(dlvyDe, routeNo, new QSort(from.id.asc())).orElse( Collections.emptyList());
	}
	@Override
	public List<V_CARALC_PLAN> findCaralcPlanByDlvyDeAndVrn(String dlvyDe, String vrn){
		QV_CARALC_PLAN from = QV_CARALC_PLAN.v_CARALC_PLAN;

		return (List<V_CARALC_PLAN>)caralcPlanRepo.findAllByDlvyDeAndVrnAndIdIsNotNull(dlvyDe, vrn,new QSort(from.scheStartDate.asc(), from.scheStartTime.asc(), from.stopSeq.asc()))
				.orElse(Collections.emptyList());

	}

	@Override
	public List<V_CARALC_MSTR> search2(String dlvyDe, String fctryCd, String caralcTy, String routeNo, String vrn,
			String dcsnYn) {
		return Lists.newArrayList(caralcMstrRepo.findAll(predicate2(dlvyDe, fctryCd, caralcTy, routeNo, vrn, dcsnYn)));
	}





























	private Predicate predicate2(String dlvyDe, String fctryCd, String caralcTy, String routeNo,
			String vrn, String dcsnYn) {

		BooleanBuilder where = new BooleanBuilder();
		QV_CARALC_MSTR mstr = QV_CARALC_MSTR.v_CARALC_MSTR;
		where.and(mstr.fctryCd.eq(fctryCd)).and(mstr.dlvyDe.eq(dlvyDe)).and(mstr.vrn.substring(0, 2).notEqualsIgnoreCase("물류"));

		if(!StringUtils.isEmpty(caralcTy)) {
			where.and(mstr.batchNo.eq(caralcTy));
		}
		if(!StringUtils.isEmpty(routeNo)) {
			where.and(mstr.routeNo.like("%" + routeNo + "%"));
		}
		if(!StringUtils.isEmpty(vrn)) {
			where.and(mstr.vrn.like("%" + vrn + "%"));
		}









		return where;
	}

	@Override
	public List<V_CARALC_MSTR> findCaralcMstrByDlvyDe(String dlvyDe) {
		BooleanBuilder where = new BooleanBuilder();
		QV_CARALC_MSTR mstr = QV_CARALC_MSTR.v_CARALC_MSTR;
		where.and(mstr.dlvyDe.eq(dlvyDe)).and(mstr.vrn.substring(0, 2).notEqualsIgnoreCase("물류"));
		return (List<V_CARALC_MSTR>)caralcMstrRepo.findAll(where, new QSort(mstr.id.asc()));
	}

}
