package com.sdc2ch.service.admin.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microsoft.sqlserver.jdbc.StringUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.sdc2ch.service.admin.IAnalsDlvyStdTimeSerivce;
import com.sdc2ch.service.grade.IAnalsStdTimeSerivce;
import com.sdc2ch.service.grade.io.AnalsStdGradeIO;
import com.sdc2ch.web.admin.repo.AdmQueryBuilder;
import com.sdc2ch.web.admin.repo.dao.T_AnalsDlvyStdTimeHistRepository;
import com.sdc2ch.web.admin.repo.dao.T_AnalsDlvyStdTimeRepository;
import com.sdc2ch.web.admin.repo.domain.anals.QT_ANALS_DLVY_STD_TIME;
import com.sdc2ch.web.admin.repo.domain.anals.QT_ANALS_DLVY_STD_TIME_HIST;
import com.sdc2ch.web.admin.repo.domain.anals.QT_ANALS_LDNG_STD_TIME;
import com.sdc2ch.web.admin.repo.domain.anals.QT_ANALS_LDNG_STD_TIME_HIST;
import com.sdc2ch.web.admin.repo.domain.anals.T_ANALS_DLVY_STD_TIME;
import com.sdc2ch.web.admin.repo.domain.anals.T_ANALS_DLVY_STD_TIME_HIST;
import com.sdc2ch.web.admin.repo.domain.anals.T_ANALS_LDNG_STD_TIME;
import com.sdc2ch.web.admin.repo.domain.anals.T_ANALS_LDNG_STD_TIME_HIST;
import com.sdc2ch.web.admin.repo.domain.v.QV_ANALS_DLVY_STD_TIME_MSTR;
import com.sdc2ch.web.admin.repo.domain.v.QV_CMMN_CODE;
import com.sdc2ch.web.admin.repo.domain.v.V_ANALS_DLVY_STD_TIME_MSTR;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AnalsDlvyStdTimeSerivceImpl implements IAnalsDlvyStdTimeSerivce, IAnalsStdTimeSerivce {

	@Autowired
	private T_AnalsDlvyStdTimeRepository analsDlvyStdTimeRepo;
	@Autowired
	private T_AnalsDlvyStdTimeHistRepository analsDlvyStdTimeHistRepo;
	@Autowired
	private AdmQueryBuilder builder;

	private final String CARALC_CD = "023";

	@Override
	public T_ANALS_DLVY_STD_TIME save(T_ANALS_DLVY_STD_TIME analsDlvyStdTime) {





		
		T_ANALS_DLVY_STD_TIME saved = analsDlvyStdTimeRepo.save(analsDlvyStdTime);
		saveHist(saved);
		return saved;
	}

	private T_ANALS_DLVY_STD_TIME_HIST saveHist(T_ANALS_DLVY_STD_TIME analsDlvyStdTime) {
		if(analsDlvyStdTime != null) {
			T_ANALS_DLVY_STD_TIME_HIST hist = new T_ANALS_DLVY_STD_TIME_HIST();
			hist.setAdjustTime(analsDlvyStdTime.getAdjustTime());
			hist.setDlvyStdTimeId(analsDlvyStdTime.getId());
			hist.setCaralcTy(analsDlvyStdTime.getCaralcTy());
			hist.setFctryCd(analsDlvyStdTime.getFctryCd());
			hist.setRegDt(analsDlvyStdTime.getRegDt());
			hist.setRegUserId(analsDlvyStdTime.getRegUserId());
			hist.setRouteNo(analsDlvyStdTime.getRouteNo());
			hist.setStdTime(analsDlvyStdTime.getStdTime());
			hist.setUseYn(analsDlvyStdTime.isUseYn());
			hist.setVhcleTy(new BigDecimal(analsDlvyStdTime.getVhcleTy()).toString());
			return analsDlvyStdTimeHistRepo.save(hist);
		}
		return null;
	}

	@Override
	public List<T_ANALS_DLVY_STD_TIME> save(List<T_ANALS_DLVY_STD_TIME> analsDlvyStdTimeList) {
		analsDlvyStdTimeList.stream().forEach(o -> {
			save(o);
		});
		return analsDlvyStdTimeList;
	}


	@Override
	public List<V_ANALS_DLVY_STD_TIME_MSTR> searchAnalsDlvyStdTimeMaster(String fctryCd) {
		return searchAnalsDlvyStdTimeMaster(fctryCd, null);
	}
	@Override
	public List<V_ANALS_DLVY_STD_TIME_MSTR> searchAnalsDlvyStdTimeMaster(String fctryCd, String routeNo) {
		QV_ANALS_DLVY_STD_TIME_MSTR from = QV_ANALS_DLVY_STD_TIME_MSTR.v_ANALS_DLVY_STD_TIME_MSTR;

		
		BooleanBuilder where = new BooleanBuilder();
		where.and(from.caralcTy.isNull()).and(from.vhcleTy.isNull());

		if(!StringUtils.isEmpty(fctryCd)) {
			where.and(from.fctryCd.eq(fctryCd));
		}

		if(!StringUtils.isEmpty(routeNo)) {
			where.and(from.routeNo.like("%"+routeNo+"%"));
		}

		return builder.create().select(Projections.fields(V_ANALS_DLVY_STD_TIME_MSTR.class,
				from.id,
				from.createDt,
				from.updateDt,
				from.fctryCd,
				from.vhcleTy,
				from.caralcTy,
				from.routeNo,
				from.stdTime,
				from.adjustTime,
				from.useYn,
				from.regUserId,
				from.regDt,
				from.caralcTyNm
			)).from(from)
			.where(where)
			.orderBy(from.routeNo.asc())
			.fetch();


	}

	@Override
	public List<T_ANALS_DLVY_STD_TIME> searchAnalsDlvyStdTimeDetail(String fctryCd, String routeNo) {
		QT_ANALS_DLVY_STD_TIME from = QT_ANALS_DLVY_STD_TIME.t_ANALS_DLVY_STD_TIME;
		
		QV_CMMN_CODE commCd = QV_CMMN_CODE.v_CMMN_CODE;
		BooleanBuilder commCdOn = new BooleanBuilder();
		commCdOn.and(from.caralcTy.eq(commCd.cd)).and(commCd.groupCd.eq(CARALC_CD));
		
		BooleanBuilder where = new BooleanBuilder();
		where.and(from.useYn.eq(true));
		where.and(from.fctryCd.eq(fctryCd));
		where.and(from.routeNo.eq(routeNo));

		List<T_ANALS_DLVY_STD_TIME> dlvyStdTimes = builder.create().select(Projections.fields(T_ANALS_DLVY_STD_TIME.class,
				from.id,
				from.createDt,
				from.updateDt,
				from.fctryCd,
				from.vhcleTy,
				from.caralcTy,
				from.routeNo,
				from.stdTime,
				from.adjustTime,
				from.useYn,
				from.base,
				from.regUserId,
				from.regDt,
				commCd.cdNm.as("caralcTyNm")
				)).from(from)
				.leftJoin(commCd).on(commCdOn)
				.where(where)
				.fetch();
		if(dlvyStdTimes == null || dlvyStdTimes.isEmpty()) {
			Condition condition = Condition.builder().fctryCd(fctryCd).routeNo(routeNo).build();
			dlvyStdTimes = makeStdGradeBase(condition);
		}
		return dlvyStdTimes;
	}
	
	@Override
	public T_ANALS_DLVY_STD_TIME findAnalsDlvyStdTimeDetail(String fctryCd, String routeNo, String caralcTy, String vhcleTy) {
		log.info("fctryCd : {}, routeNo : {}, caralcTy : {}, vhcleTy : {}", fctryCd, routeNo, caralcTy, vhcleTy);
		QT_ANALS_DLVY_STD_TIME from = QT_ANALS_DLVY_STD_TIME.t_ANALS_DLVY_STD_TIME;

		
		QV_CMMN_CODE commCd = QV_CMMN_CODE.v_CMMN_CODE;
		BooleanBuilder commCdOn = new BooleanBuilder();
		commCdOn.and(from.caralcTy.eq(commCd.cd)).and(commCd.groupCd.eq(CARALC_CD));

		
		BooleanBuilder where = new BooleanBuilder();
		where.and(from.useYn.eq(true));
		where.and(from.fctryCd.eq(fctryCd))
			.and(from.caralcTy.eq(caralcTy))
			.and(from.vhcleTy.eq(new BigDecimal(vhcleTy).floatValue()))
			.and(from.routeNo.eq(routeNo));


		return builder.create().select(Projections.fields(T_ANALS_DLVY_STD_TIME.class,
				from.id,
				from.createDt,
				from.updateDt,
				from.fctryCd,
				from.vhcleTy,
				from.caralcTy,
				from.routeNo,
				from.stdTime,
				from.adjustTime,
				from.useYn,
				from.base,
				from.regUserId,
				from.regDt,
				commCd.cdNm.as("caralcTyNm")
				)).from(from)
				.leftJoin(commCd).on(commCdOn)
				.where(where)
				.fetchOne();

		
	}

	private List<T_ANALS_DLVY_STD_TIME> makeStdGradeBase(Condition param){
		T_ANALS_DLVY_STD_TIME ldng = new T_ANALS_DLVY_STD_TIME();
		ldng.setBase(true);
		ldng.setAdjustTime(20L);
		ldng.setCaralcTy(param.getCaralcTy());
		ldng.setCaralcTyNm(param.getCaralcTy());
		ldng.setFctryCd(param.getFctryCd());
		ldng.setRouteNo(param.getRouteNo());
		ldng.setStdTime(120L);
		ldng.setUseYn(true);
		ldng.setVhcleTy(param.getVehicleTy());
		return Arrays.asList(ldng);
	}

	@Override
	public List<T_ANALS_DLVY_STD_TIME_HIST> searchHistByDlvyStdTimeId(Long dlvyStdTimeId){
		QT_ANALS_DLVY_STD_TIME_HIST from = QT_ANALS_DLVY_STD_TIME_HIST.t_ANALS_DLVY_STD_TIME_HIST;
		
		QV_CMMN_CODE commCd = QV_CMMN_CODE.v_CMMN_CODE;
		BooleanBuilder commCdOn = new BooleanBuilder();
		commCdOn.and(from.caralcTy.eq(commCd.cd)).and(commCd.groupCd.eq(CARALC_CD));
		
		BooleanBuilder where = new BooleanBuilder();
		where.and(from.dlvyStdTimeId.eq(dlvyStdTimeId));

		return builder.create().select(Projections.fields(T_ANALS_DLVY_STD_TIME_HIST.class,
				from.id,
				from.dlvyStdTimeId,
				from.createDt,
				from.updateDt,
				from.fctryCd,
				from.vhcleTy,
				from.caralcTy,
				from.routeNo,
				from.stdTime,
				from.adjustTime,
				from.useYn,
				from.regUserId,
				from.regDt,
				commCd.cdNm.as("caralcTyNm")
				)).from(from)
				.leftJoin(commCd).on(commCdOn)
				.where(where)
				.fetch();
		
	}

	@Override
	public List<T_ANALS_DLVY_STD_TIME_HIST> searchHistByDlvyStdTimeParams(String fctryCd, String caralcTy, String vhcleTy, String routeNo){
		QT_ANALS_DLVY_STD_TIME_HIST from = QT_ANALS_DLVY_STD_TIME_HIST.t_ANALS_DLVY_STD_TIME_HIST;

		
		QV_CMMN_CODE commCd = QV_CMMN_CODE.v_CMMN_CODE;
		BooleanBuilder commCdOn = new BooleanBuilder();
		commCdOn.and(from.caralcTy.eq(commCd.cd)).and(commCd.groupCd.eq(CARALC_CD));

		
		BooleanBuilder where = new BooleanBuilder();
		where.and(from.fctryCd.eq(fctryCd));
		where.and(from.caralcTy.eq(caralcTy));
		where.and(from.vhcleTy.eq(vhcleTy));
		where.and(from.routeNo.eq(routeNo));

		return builder.create().select(Projections.fields(T_ANALS_DLVY_STD_TIME_HIST.class,
				from.id,
				from.dlvyStdTimeId,
				from.createDt,
				from.updateDt,
				from.fctryCd,
				from.vhcleTy,
				from.caralcTy,
				from.routeNo,
				from.stdTime,
				from.adjustTime,
				from.useYn,
				from.regUserId,
				from.regDt,
				commCd.cdNm.as("caralcTyNm")
			)).from(from)
			.leftJoin(commCd).on(commCdOn)
			.where(where)
			.orderBy(from.createDt.desc())
				.fetch();


	}

	@Override
	public boolean supported(StdtimeScopeTy ty) {
		return StdtimeScopeTy.DLVY == ty;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AnalsStdGradeIO> searchAnalsStdTimeDetail(Condition param) {
		return (List<AnalsStdGradeIO>)(Object)searchAnalsDlvyStdTimeDetail(param.getFctryCd(), param.getRouteNo());
	}

}
