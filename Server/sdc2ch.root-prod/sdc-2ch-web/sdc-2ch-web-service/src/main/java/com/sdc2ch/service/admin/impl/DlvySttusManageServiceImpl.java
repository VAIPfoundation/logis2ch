package com.sdc2ch.service.admin.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.sdc2ch.service.admin.IDlvySttusManageService;
import com.sdc2ch.service.admin.IUnstoringManageService;
import com.sdc2ch.web.admin.repo.AdmQueryBuilder;
import com.sdc2ch.web.admin.repo.domain.op.T_BCON_HIST;
import com.sdc2ch.web.admin.repo.domain.v.QV_CVO_MONITOR;
import com.sdc2ch.web.admin.repo.domain.v.QV_UNSTORING_MANAGE;
import com.sdc2ch.web.admin.repo.domain.v.V_CVO_MONITOR;
import com.sdc2ch.web.admin.repo.domain.v.V_UNSTORING_MANAGE;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
class DlvySttusManageServiceImpl implements IDlvySttusManageService {

	
	@Autowired AdmQueryBuilder builder;

	private QV_CVO_MONITOR QGROUP = QV_CVO_MONITOR.v_CVO_MONITOR;

	@Override
	public List<V_CVO_MONITOR> search(String fctryCd) {
		return search(fctryCd, null, null);
	}

	@Override
	public List<V_CVO_MONITOR> search(String fctryCd, String vhcleTy, String vrn) {
		log.info("fctryCd={}, vhcleTy={}, vrn={}", fctryCd, vhcleTy, vrn);
		QV_CVO_MONITOR from = QV_CVO_MONITOR.v_CVO_MONITOR;

		BooleanBuilder where = new BooleanBuilder();
		where.and(from.fctryCd.eq(fctryCd));
		if ( !StringUtils.isEmpty(vhcleTy) ) {
			where.and(from.vhcleTy.eq(vhcleTy));
		}
		if ( !StringUtils.isEmpty(vrn) ) {
			where.and(from.vrn.like("%" + vrn + "%"));
		}

		return builder
				.create()
					.select(
						Projections.fields(
							V_CVO_MONITOR.class,
							from.fctryCd, from.fctryNm, from.trnsprtCmpnyCd, from.trnsprtCmpny, from.vhcleTy,
							from.ldngTy, from.driverCd, from.driverNm, from.vrn, from.driveSttus,
							from.tempt1, from.tempt2, from.lastReportDt, from.mileg, from.wtSm,
							from.driveDstnc, from.emptVhcleMoveDstnc, from.ldngVhcleMoveDstnc, from.tonkm, from.fuelUsgqty,
							from.co2Dscamt, from.energyEfcIdx, from.unitTonkm
						)
					)
					.from(from)
					.where(where)
					.orderBy(from.vrn.asc())
				.fetch();
	}

}