package com.sdc2ch.service.admin.impl;

import java.util.Date;
import java.util.List;

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
import com.sdc2ch.service.admin.IMobileHealthCheckService;
import com.sdc2ch.web.admin.repo.AdmQueryBuilder;
import com.sdc2ch.web.admin.repo.dao.T_MobileHealthChkHistRepository;
import com.sdc2ch.web.admin.repo.dao.T_MobileHealthChkRepository;
import com.sdc2ch.web.admin.repo.domain.QT_MOBILE_HEALTH_CHK_HIST;
import com.sdc2ch.web.admin.repo.domain.T_MOBILE_HEALTH_CHK;
import com.sdc2ch.web.admin.repo.domain.T_MOBILE_HEALTH_CHK_HIST;
import com.sdc2ch.web.admin.repo.domain.v.QV_MOBILE_APP_USE_INFO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MobileHealthCheckServiceImpl implements IMobileHealthCheckService {


	@Autowired
	private T_MobileHealthChkHistRepository mobileChkRepo;
	@Autowired
	private T_MobileHealthChkRepository curMobileChkRepo;

	@Autowired AdmQueryBuilder builder;

	@Override
	public T_MOBILE_HEALTH_CHK_HIST saveHist(T_MOBILE_HEALTH_CHK_HIST hist) {
		return mobileChkRepo.save(hist);
	}

	@Override
	public T_MOBILE_HEALTH_CHK saveLast(T_MOBILE_HEALTH_CHK cur) {
		return curMobileChkRepo.save(cur);
	}


	@Override
	public List<T_MOBILE_HEALTH_CHK_HIST> searchMobileHealthCheckHistByDate(Date fromDt, Date toDt, String fctryCd,	String vrn, String mdn) {
		QT_MOBILE_HEALTH_CHK_HIST from = QT_MOBILE_HEALTH_CHK_HIST.t_MOBILE_HEALTH_CHK_HIST;

		QV_MOBILE_APP_USE_INFO mobileAppUseInfo = QV_MOBILE_APP_USE_INFO.v_MOBILE_APP_USE_INFO;
		BooleanBuilder onMobileAppUseInfo = new BooleanBuilder();
		onMobileAppUseInfo.and(from.vrn.eq(mobileAppUseInfo.vrn)).and(from.fctryCd.eq(mobileAppUseInfo.fctryCd));

		BooleanBuilder where = new BooleanBuilder();
		if (fromDt != null) {
			where.and(from.dataDt.goe(fromDt));
		}

		if (toDt != null) {
			where.and(from.dataDt.loe(toDt));
		}

		if (!StringUtils.isEmpty(fctryCd)) {
			where.and(from.fctryCd.eq(fctryCd));
		}

		if (!StringUtils.isEmpty(vrn)) {
			where.and(from.vrn.like("%" + vrn + "%"));
		}

		if (!StringUtils.isEmpty(mdn)) {
			where.and(from.mdn.like("%" + mdn.replaceAll("-", "") + "%"));
		}
		return builder.create()
				.select(
						Projections.fields(
								T_MOBILE_HEALTH_CHK_HIST.class,
								from.batteryUsage, from.callRecvEnabled, from.createDt, from.dataDt, from.dozeMode,
								from.driverCd, from.fctryCd, from.forgroundService, from.id,
								from.locEnabled, from.mdn, from.network, from.permissions, from.runningService,
								from.strDataDt, from.updateDt, from.vrn, mobileAppUseInfo.driverNm.as("driverNm"),
								mobileAppUseInfo.fctryNm.as("fctryNm"), mobileAppUseInfo.vhcleTy.as("vhcleTy"))
						)
				.from(from)
				.leftJoin(mobileAppUseInfo)
				.on(onMobileAppUseInfo)
				.where(where)
				.fetch();
	}


	@Override
	public Page<T_MOBILE_HEALTH_CHK_HIST> searchPageMobileHealthCheckHistByDate(Date fromDt, Date toDt, String fctryCd,	String vrn, String mdn, Pageable pageable) {
		QT_MOBILE_HEALTH_CHK_HIST from = QT_MOBILE_HEALTH_CHK_HIST.t_MOBILE_HEALTH_CHK_HIST;

		QV_MOBILE_APP_USE_INFO mobileAppUseInfo = QV_MOBILE_APP_USE_INFO.v_MOBILE_APP_USE_INFO;
		BooleanBuilder onMobileAppUseInfo = new BooleanBuilder();
		onMobileAppUseInfo.and(from.vrn.eq(mobileAppUseInfo.vrn)).and(from.fctryCd.eq(mobileAppUseInfo.fctryCd));

		BooleanBuilder where = new BooleanBuilder();
		if (fromDt != null) {
			where.and(from.dataDt.goe(fromDt));
		}

		if (toDt != null) {
			where.and(from.dataDt.loe(toDt));
		}

		if (!StringUtils.isEmpty(fctryCd)) {
			where.and(from.fctryCd.eq(fctryCd));
		}

		if (!StringUtils.isEmpty(vrn)) {
			where.and(from.vrn.like("%" + vrn + "%"));
		}

		if (!StringUtils.isEmpty(mdn)) {
			where.and(from.mdn.like("%" + mdn.replaceAll("-", "") + "%"));
		}

		JPAQuery query =  builder.create()
		.select(Projections.fields(
				T_MOBILE_HEALTH_CHK_HIST.class,
				from.batteryUsage.as("batteryUsage"),
				from.callRecvEnabled.as("callRecvEnabled"),
				from.createDt.as("createDt"),
				from.dataDt.as("dataDt"),
				from.dozeMode.as("dozeMode"),
				from.driverCd.as("driverCd"),
				from.fctryCd.as("fctryCd"),
				from.forgroundService.as("forgroundService"),
				from.id.as("id"),
				from.locEnabled.as("locEnabled"),
				from.mdn.as("mdn"),
				from.network.as("network"),
				from.permissions.as("permissions"),
				from.runningService.as("runningService"),
				from.strDataDt.as("strDataDt"),
				from.updateDt.as("updateDt"),
				from.vrn.as("vrn"),
				mobileAppUseInfo.driverNm.as("driverNm"),
				mobileAppUseInfo.fctryNm.as("fctryNm"),
				mobileAppUseInfo.vhcleTy.as("vhcleTy"))
				)
		.from(from)
		.leftJoin(mobileAppUseInfo)
		.on(onMobileAppUseInfo)
		.where(where);

		if (pageable != null && pageable.getSort() != null) { 
			pageable.getSort().stream().forEach(o -> {
				query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, Expressions.stringPath(o.getProperty())));
			});
		} else {
			query.orderBy(from.dataDt.asc());
		}

		if (pageable != null) { 
			query.offset(pageable.getOffset());
			query.limit(pageable.getPageSize());
		}

		return new PageImpl(query.fetch(), pageable, query.fetchCount());
	}
}
