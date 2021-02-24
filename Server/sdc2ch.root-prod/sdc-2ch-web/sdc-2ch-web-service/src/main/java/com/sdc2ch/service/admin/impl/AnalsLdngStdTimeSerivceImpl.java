package com.sdc2ch.service.admin.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.sdc2ch.service.admin.IAnalsLdngStdTimeSerivce;
import com.sdc2ch.service.grade.IAnalsStdTimeSerivce;
import com.sdc2ch.service.grade.io.AnalsStdGradeIO;
import com.sdc2ch.web.admin.repo.AdmQueryBuilder;
import com.sdc2ch.web.admin.repo.dao.T_AnalsLdngStdTimeHistRepository;
import com.sdc2ch.web.admin.repo.dao.T_AnalsLdngStdTimeRepository;
import com.sdc2ch.web.admin.repo.domain.anals.QT_ANALS_LDNG_STD_TIME;
import com.sdc2ch.web.admin.repo.domain.anals.QT_ANALS_LDNG_STD_TIME_HIST;
import com.sdc2ch.web.admin.repo.domain.anals.T_ANALS_LDNG_STD_TIME;
import com.sdc2ch.web.admin.repo.domain.anals.T_ANALS_LDNG_STD_TIME_HIST;
import com.sdc2ch.web.admin.repo.domain.v.QV_ANALS_LDNG_STD_TIME_MSTR;
import com.sdc2ch.web.admin.repo.domain.v.QV_CMMN_CODE;
import com.sdc2ch.web.admin.repo.domain.v.V_ANALS_LDNG_STD_TIME_MSTR;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AnalsLdngStdTimeSerivceImpl implements IAnalsLdngStdTimeSerivce, IAnalsStdTimeSerivce  {

	@Autowired
	private T_AnalsLdngStdTimeRepository analsLdngStdTimeRepo;
	@Autowired
	private T_AnalsLdngStdTimeHistRepository analsLdngStdTimeHistRepo;

	@Autowired
	private AdmQueryBuilder builder;

	private final String CARALC_CD = "023";

	@Override
	public List<V_ANALS_LDNG_STD_TIME_MSTR> searchAnalsLdngStdTimeMaster(String fctryCd) {
		return searchAnalsLdngStdTimeMaster(fctryCd, null, null);
	}
	@Override
	public List<V_ANALS_LDNG_STD_TIME_MSTR> searchAnalsLdngStdTimeMaster(String fctryCd, String caralcTy, String vhcleTy) {
		log.info("fctryCd : {}, caralcTy : {}, vhcleTy : {}", fctryCd, caralcTy, vhcleTy);
		QV_ANALS_LDNG_STD_TIME_MSTR from = QV_ANALS_LDNG_STD_TIME_MSTR.v_ANALS_LDNG_STD_TIME_MSTR;

		
		BooleanBuilder where = new BooleanBuilder();

		if(!StringUtils.isEmpty(fctryCd)) {
			where.and(from.fctryCd.eq(fctryCd));
		}

		if(!StringUtils.isEmpty(caralcTy)) {
			where.and(from.caralcTy.eq(caralcTy));
		}

		if(!StringUtils.isEmpty(vhcleTy)) {
			where.and(from.vhcleTy.eq(vhcleTy));
		}

		return builder.create().select(Projections.fields(V_ANALS_LDNG_STD_TIME_MSTR.class,
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
			.orderBy(from.caralcTy.asc(), from.vhcleTy.asc())
			.fetch();


	}

	@Override
	public List<T_ANALS_LDNG_STD_TIME> searchAnalsLdngStdTimeDetail(String fctryCd, String caralcTy, String vhcleTy) {
		log.info("fctryCd : {}, caralcTy : {}, vhcleTy : {}", fctryCd, caralcTy, vhcleTy);
		QT_ANALS_LDNG_STD_TIME from = QT_ANALS_LDNG_STD_TIME.t_ANALS_LDNG_STD_TIME;

		
		QV_CMMN_CODE commCd = QV_CMMN_CODE.v_CMMN_CODE;
		BooleanBuilder commCdOn = new BooleanBuilder();
		commCdOn.and(from.caralcTy.eq(commCd.cd)).and(commCd.groupCd.eq(CARALC_CD));

		
		BooleanBuilder where = new BooleanBuilder();
		where.and(from.useYn.eq(true));
		where.and(from.fctryCd.eq(fctryCd))
			.and(from.caralcTy.eq(caralcTy))
			.and(from.vhcleTy.eq(new BigDecimal(vhcleTy).floatValue()));

		return builder.create().select(Projections.fields(T_ANALS_LDNG_STD_TIME.class,
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

		
	}


	@Override
	public T_ANALS_LDNG_STD_TIME findAnalsLdngStdTimeDetail(String fctryCd, String caralcTy, String vhcleTy, String routeNo) {
		log.info("fctryCd : {}, caralcTy : {}, vhcleTy : {}, routeNo : {}", fctryCd, caralcTy, vhcleTy, routeNo);
		QT_ANALS_LDNG_STD_TIME from = QT_ANALS_LDNG_STD_TIME.t_ANALS_LDNG_STD_TIME;

		
		QV_CMMN_CODE commCd = QV_CMMN_CODE.v_CMMN_CODE;
		BooleanBuilder commCdOn = new BooleanBuilder();
		commCdOn.and(from.caralcTy.eq(commCd.cd)).and(commCd.groupCd.eq(CARALC_CD));

		
		BooleanBuilder where = new BooleanBuilder();
		where.and(from.useYn.eq(true));
		where.and(from.fctryCd.eq(fctryCd))
			.and(from.caralcTy.eq(caralcTy))
			.and(from.vhcleTy.eq(new BigDecimal(vhcleTy).floatValue()))
			.and(from.routeNo.eq(routeNo));


		return builder.create().select(Projections.fields(T_ANALS_LDNG_STD_TIME.class,
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


	@Override
	public T_ANALS_LDNG_STD_TIME save(T_ANALS_LDNG_STD_TIME analsLdngStdTime) {




		T_ANALS_LDNG_STD_TIME saved = analsLdngStdTimeRepo.save(analsLdngStdTime);
		saveHist(saved);
		return saved;
	}

	private T_ANALS_LDNG_STD_TIME_HIST saveHist(T_ANALS_LDNG_STD_TIME analsLdngStdTime){
		if (analsLdngStdTime != null) {
			T_ANALS_LDNG_STD_TIME_HIST hist = new T_ANALS_LDNG_STD_TIME_HIST();
			hist.setAdjustTime(analsLdngStdTime.getAdjustTime());
			hist.setCaralcTy(analsLdngStdTime.getCaralcTy());
			hist.setFctryCd(analsLdngStdTime.getFctryCd());
			hist.setLdngStdTimeId(analsLdngStdTime.getId());
			hist.setRegDt(analsLdngStdTime.getRegDt());
			hist.setRegUserId(analsLdngStdTime.getRegUserId());
			hist.setRouteNo(analsLdngStdTime.getRouteNo());
			hist.setStdTime(analsLdngStdTime.getStdTime());
			hist.setUseYn(analsLdngStdTime.isUseYn());
			hist.setVhcleTy(new BigDecimal(analsLdngStdTime.getVhcleTy()).toString());
			return analsLdngStdTimeHistRepo.save(hist);
		}
		return null;
	}

	@Override
	public List<T_ANALS_LDNG_STD_TIME> save(List<T_ANALS_LDNG_STD_TIME> analsLdngStdTimeList) {
		analsLdngStdTimeList.stream().forEach(o -> {
			save(o);
		});
		return analsLdngStdTimeList;
	}


	@Override
	public List<T_ANALS_LDNG_STD_TIME_HIST> searchHistByLdngStdTimeId(Long ldngStdTimeId){
		QT_ANALS_LDNG_STD_TIME_HIST from = QT_ANALS_LDNG_STD_TIME_HIST.t_ANALS_LDNG_STD_TIME_HIST;

		
		QV_CMMN_CODE commCd = QV_CMMN_CODE.v_CMMN_CODE;
		BooleanBuilder commCdOn = new BooleanBuilder();
		commCdOn.and(from.caralcTy.eq(commCd.cd)).and(commCd.groupCd.eq(CARALC_CD));

		
		BooleanBuilder where = new BooleanBuilder();
		where.and(from.ldngStdTimeId.eq(ldngStdTimeId));

		return builder.create().select(Projections.fields(T_ANALS_LDNG_STD_TIME_HIST.class,
				from.id,
				from.ldngStdTimeId,
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
	public List<T_ANALS_LDNG_STD_TIME_HIST> searchHistByLdngStdTimeParams(String fctryCd, String caralcTy, String vhcleTy, String routeNo){
		QT_ANALS_LDNG_STD_TIME_HIST from = QT_ANALS_LDNG_STD_TIME_HIST.t_ANALS_LDNG_STD_TIME_HIST;

		
		QV_CMMN_CODE commCd = QV_CMMN_CODE.v_CMMN_CODE;
		BooleanBuilder commCdOn = new BooleanBuilder();
		commCdOn.and(from.caralcTy.eq(commCd.cd)).and(commCd.groupCd.eq(CARALC_CD));

		
		BooleanBuilder where = new BooleanBuilder();
		where.and(from.fctryCd.eq(fctryCd));
		where.and(from.caralcTy.eq(caralcTy));
		where.and(from.vhcleTy.eq(vhcleTy));
		where.and(from.routeNo.eq(routeNo));

		return builder.create().select(Projections.fields(T_ANALS_LDNG_STD_TIME_HIST.class,
				from.id,
				from.ldngStdTimeId,
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
		return StdtimeScopeTy.LDNG == ty;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AnalsStdGradeIO> searchAnalsStdTimeDetail(Condition param) {

		QT_ANALS_LDNG_STD_TIME from = QT_ANALS_LDNG_STD_TIME.t_ANALS_LDNG_STD_TIME;

		
		QV_CMMN_CODE commCd = QV_CMMN_CODE.v_CMMN_CODE;
		BooleanBuilder commCdOn = new BooleanBuilder();
		commCdOn.and(from.caralcTy.eq(commCd.cd)).and(commCd.groupCd.eq(CARALC_CD));

		
		BooleanBuilder where = new BooleanBuilder();
		where.and(from.useYn.eq(true));
		where.and(from.fctryCd.eq(param.getFctryCd())).and(commCd.cdNm.eq(param.getCaralcTy())).and(from.vhcleTy.eq(param.getVehicleTy()));

		List<T_ANALS_LDNG_STD_TIME> stdTimes = builder.create().select(Projections.fields(T_ANALS_LDNG_STD_TIME.class,
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

		if(stdTimes == null || stdTimes.isEmpty()) {
			stdTimes = makeStdGradeBase(param);
		}
		return  (List<AnalsStdGradeIO>)(Object)stdTimes;
	}

	private List<T_ANALS_LDNG_STD_TIME> makeStdGradeBase(Condition param){
		T_ANALS_LDNG_STD_TIME ldng = new T_ANALS_LDNG_STD_TIME();
		ldng.setBase(true);
		ldng.setAdjustTime(20L);
		ldng.setCaralcTy(param.getCaralcTy());
		ldng.setCaralcTyNm(param.getCaralcTy());
		ldng.setFctryCd(param.getFctryCd());
		ldng.setStdTime(120L);
		ldng.setUseYn(true);
		ldng.setVhcleTy(param.getVehicleTy());
		return Arrays.asList(ldng);
	}
}
