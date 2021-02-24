package com.sdc2ch.service.admin.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.microsoft.sqlserver.jdbc.StringUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.sdc2ch.require.enums.SetupLcType;
import com.sdc2ch.service.admin.IBconHistService;
import com.sdc2ch.web.admin.repo.AdmQueryBuilder;
import com.sdc2ch.web.admin.repo.dao.T_BconHistoryRepository;
import com.sdc2ch.web.admin.repo.domain.op.QT_BCON_HIST;
import com.sdc2ch.web.admin.repo.domain.op.QT_BCON_MAPPING;
import com.sdc2ch.web.admin.repo.domain.op.T_BCON_HIST;
import com.sdc2ch.web.admin.repo.domain.v.QV_VHCLE;

@Service
public class BconHistService implements IBconHistService{

	@Autowired
	private T_BconHistoryRepository bconHistRepo;

	@Autowired
	private AdmQueryBuilder builder;

	@Override
	public Page<T_BCON_HIST> searchPageBconHist(Date fromDt, Date toDt, String fctryCd, String vrn, SetupLcType setupLc, Pageable pageable) {
		QT_BCON_HIST from = QT_BCON_HIST.t_BCON_HIST;
		
		QV_VHCLE vhcle = QV_VHCLE.v_VHCLE;
		BooleanBuilder vhcleOn = new BooleanBuilder();
		vhcleOn.and(from.mdn.eq(vhcle.mobileNo));

		
		QT_BCON_MAPPING mapping = QT_BCON_MAPPING.t_BCON_MAPPING;
		BooleanBuilder mappingOn = new BooleanBuilder();
		mappingOn.and(from.bconId.eq(mapping.bconId));

		
		BooleanBuilder where = new BooleanBuilder();
		if(fromDt != null) {
			where.and(from.dataDt.goe(fromDt));
		}

		if(toDt != null) {
			where.and(from.dataDt.loe(toDt));
		}

		if (!StringUtils.isEmpty(fctryCd)) {
			where.and(vhcle.fctryCd.eq(fctryCd));
		}

		if (!StringUtils.isEmpty(vrn)) {
			where.and(vhcle.vrn.like("%" + vrn + "%"));
		}

		if (setupLc != null) {
			where.and(mapping.setupLc.eq(setupLc));
		}

		JPAQuery<T_BCON_HIST> query = builder.create().select(Projections.fields(
				T_BCON_HIST.class,
				from.id.as("id"),
				from.createDt.as("createDt"),
				from.updateDt.as("updateDt"),
				from.bconId.as("bconId"),
				from.dataDt.as("dataDt"),
				from.inoutTy.as("inoutTy"),
				from.mdn.as("mdn"),
				vhcle.vrn.as("vrn"),
				vhcle.driverNm.as("driverNm"),
				vhcle.driverCd.as("driverCd"),
				vhcle.fctryCd.as("fctryCd"),
				mapping.setupLc.as("setupLc")
				)).from(from)
				.leftJoin(vhcle).on(vhcleOn)
				.leftJoin(mapping).on(mappingOn)
				.where(where);

		if(pageable != null && pageable.getSort() != null) { 
			pageable.getSort().stream().forEach(o -> {
				query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, Expressions.stringPath(o.getProperty())));
			});
		}else {
			query.orderBy(from.dataDt.asc());
		}

		if(pageable != null) { 
			query.offset(pageable.getOffset());
			query.limit(pageable.getPageSize());
		}

		return new PageImpl<>(query.fetch(), pageable, query.fetchCount());
	}

}
