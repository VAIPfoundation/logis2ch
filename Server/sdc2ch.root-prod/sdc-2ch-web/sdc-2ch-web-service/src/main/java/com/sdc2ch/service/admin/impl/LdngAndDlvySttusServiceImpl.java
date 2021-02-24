package com.sdc2ch.service.admin.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.sdc2ch.cvo.io.TraceIO;
import com.sdc2ch.cvo.service.ICvoControlService;
import com.sdc2ch.service.admin.IAnalsVhcleService;
import com.sdc2ch.service.admin.ILdngAndDlvySttusService;
import com.sdc2ch.web.admin.repo.AdmQueryBuilder;
import com.sdc2ch.web.admin.repo.dao.V_CarRepository;
import com.sdc2ch.web.admin.repo.domain.sta.QV_ANALS_GRADE_SCOPE_HIST;
import com.sdc2ch.web.admin.repo.domain.sta.V_ANALS_GRADE_SCOPE_HIST;
import com.sdc2ch.web.admin.repo.domain.v.QV_REALTIME_INFO;
import com.sdc2ch.web.admin.repo.domain.v.V_REALTIME_INFO;
import com.sdc2ch.web.admin.repo.domain.v.V_VHCLE;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
class LdngAndDlvySttusServiceImpl implements ILdngAndDlvySttusService {

	
	@Autowired AdmQueryBuilder builder;

	@Autowired V_CarRepository vehicleRepo;

	@Autowired(required = false) ICvoControlService cvoSvc;



	@Override
	public List<V_ANALS_GRADE_SCOPE_HIST> listLdngAndDlvySttusDaily(String dlvyDe, String fctryCd) {
		return listLdngAndDlvySttusDaily(dlvyDe, fctryCd, null, null);
	}
	@Override
	public List<V_ANALS_GRADE_SCOPE_HIST> listLdngAndDlvySttusDaily(String dlvyDe, String fctryCd, String routeNo, String vrn) {
		log.info("dlvyDe={}, fctryCd={}, routeNo={}, vrn={}", dlvyDe, fctryCd, routeNo, vrn);

		QV_ANALS_GRADE_SCOPE_HIST from = QV_ANALS_GRADE_SCOPE_HIST.v_ANALS_GRADE_SCOPE_HIST;

		BooleanBuilder where = new BooleanBuilder();
		where.and(from.fctryCd.eq(fctryCd));
		where.and(from.dlvyDe.eq(dlvyDe));

		if(!StringUtils.isEmpty(routeNo)) {
			where.and(from.routeNo.like("%" + routeNo + "%"));
		}
		if(!StringUtils.isEmpty(vrn)) {
			where.and(from.vrn.like("%" + vrn + "%"));
		}

		return builder
				.create()
					.select(
						Projections.fields(
							V_ANALS_GRADE_SCOPE_HIST.class,
							from.id, from.createDt, from.updateDt
							, from.fctryCd
							, from.aclGroupId, from.dlvyDe, from.vrn, from.vhcleTy, from.driverCd, from.driverNm
							, from.routeNo, from.caralcTy, from.stdTime, from.adjustTime
							, from.statusCd, from.statusNm, from.grad
							, from.fromDlvyLcCd
							, from.fromPlanDe, from.fromPlanTime, from.fromPlanDt
							, from.fromRealDe, from.fromRealTime, from.fromRealDt
							, from.toDlvyLcCd
							, from.toPlanDe, from.toPlanTime, from.toPlanDt
							, from.toRealDe, from.toRealTime, from.toRealDt
						)
					)
					.from(from)
					.where(where)
					.orderBy(from.vrn.asc())
				.fetch()
				.stream()
					.collect(Collectors.toList());
	}


	@Override
	public List<V_ANALS_GRADE_SCOPE_HIST> listLdngAndDlvySttusVhcle(String dlvyDe, String fctryCd, String vrn) {
		return listLdngAndDlvySttusVhcle(dlvyDe, fctryCd, null, null, null);
	}
	@Override
	public List<V_ANALS_GRADE_SCOPE_HIST> listLdngAndDlvySttusVhcle(String dlvyDe, String fctryCd, String vrn, String ldngGrad, String dlvyGrad) {
		log.info("dlvyDe={}, fctryCd={}, vrn={}, ldngGrad={}, dlvyGrad={}", dlvyDe, fctryCd, vrn, ldngGrad, dlvyGrad);

		QV_ANALS_GRADE_SCOPE_HIST from = QV_ANALS_GRADE_SCOPE_HIST.v_ANALS_GRADE_SCOPE_HIST;

		BooleanBuilder where = new BooleanBuilder();
		where.and(from.fctryCd.eq(fctryCd));
		where.and(from.dlvyDe.like(dlvyDe + "%"));	

		if(!StringUtils.isEmpty(vrn)) {
			where.and(from.vrn.like("%" + vrn + "%"));
		}
		if(!StringUtils.isEmpty(ldngGrad)) {
			where.and(from.statusCd.eq("LDNG").and(from.grad.eq(ldngGrad)));
		}
		if(!StringUtils.isEmpty(dlvyGrad)) {
			where.and(from.statusCd.eq("DELIVERY").and(from.grad.eq(dlvyGrad)));
		}

		return builder
				.create()
					.select(
						Projections.fields(
								V_ANALS_GRADE_SCOPE_HIST.class,
								from.id, from.createDt, from.updateDt
								, from.fctryCd
								, from.aclGroupId, from.dlvyDe, from.vrn, from.vhcleTy, from.driverCd, from.driverNm
								, from.routeNo, from.caralcTy, from.stdTime, from.adjustTime
								, from.statusCd, from.statusNm, from.grad
								, from.fromDlvyLcCd
								, from.fromPlanDe, from.fromPlanTime, from.fromPlanDt
								, from.fromRealDe, from.fromRealTime, from.fromRealDt
								, from.toDlvyLcCd
								, from.toPlanDe, from.toPlanTime, from.toPlanDt
								, from.toRealDe, from.toRealTime, from.toRealDt
						)
					)
					.from(from)
					.where(where)
					.orderBy(from.dlvyDe.asc())
				.fetch()
				.stream()
					.collect(Collectors.toList());
	}



}
