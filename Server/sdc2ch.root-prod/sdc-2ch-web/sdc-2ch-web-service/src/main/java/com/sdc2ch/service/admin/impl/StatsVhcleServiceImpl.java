package com.sdc2ch.service.admin.impl;

import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_20;
import static com.sdc2ch.require.repo.schema.GTConfig.USER_NAME_LNG_10;
import static com.sdc2ch.require.repo.schema.GTConfig.VRN_LNG_10;
import static com.sdc2ch.require.repo.schema.GTConfig.YYYYMMDD_08;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.sdc2ch.require.service.I2ChUserService;
import com.sdc2ch.service.admin.IStatsVhcleService;
import com.sdc2ch.service.admin.model.StatsCommonVo;
import com.sdc2ch.service.admin.model.VhcleCntrlVo;
import com.sdc2ch.web.admin.repo.AdmQueryBuilder;
import com.sdc2ch.web.admin.repo.domain.sta.QT_DRIVE_DIARY;
import com.sdc2ch.web.admin.repo.domain.sta.QV_ANALS_DRIVING_HIST;
import com.sdc2ch.web.admin.repo.domain.sta.QV_DRIVE_DIARY_DAILY;
import com.sdc2ch.web.admin.repo.domain.sta.QV_DRIVE_DIARY_DLVY_LC;
import com.sdc2ch.web.admin.repo.domain.sta.QV_DRIVE_DIARY_DTLS;
import com.sdc2ch.web.admin.repo.domain.sta.T_DRIVE_DIARY;
import com.sdc2ch.web.admin.repo.domain.sta.V_ANALS_DRIVING_HIST;
import com.sdc2ch.web.admin.repo.domain.sta.V_DRIVE_DIARY_DAILY;
import com.sdc2ch.web.admin.repo.domain.sta.V_DRIVE_DIARY_DLVY_LC;
import com.sdc2ch.web.admin.repo.domain.sta.V_DRIVE_DIARY_DTLS;
import com.sdc2ch.web.admin.repo.domain.v.QV_STATS_SUMRY;
import com.sdc2ch.web.admin.repo.domain.v.V_STATS_SUMRY;
import com.sdc2ch.web.service.IMobileAppService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
class StatsVhcleServiceImpl implements IStatsVhcleService {



	
	@Autowired AdmQueryBuilder builder;


	@Override
	public List<StatsCommonVo> searchPeriodVhcle(String fromDe, String toDe, String fctryCd) {
		log.info("fromDe={}, toDe={}, fctryCd={}", fromDe, toDe, fctryCd);

		Object[] param = {fctryCd, fromDe, toDe};

		
		List<Object[]> resultList = builder.storedProcedureCallRet("[dbo].[SP_2CH_STATS_PERIOD_VHCLE_SEL]", param);

		List<StatsCommonVo> voList = new ArrayList<StatsCommonVo>();

		for ( Object[] res : resultList ) {
			StatsCommonVo scVo = new StatsCommonVo();
			int i=0;
			scVo.setFctryCd(scVo.convertString(res[i++]));
			scVo.setVhcleTy(scVo.convertString(res[i++]));
			scVo.setTrnsprtCmpny(scVo.convertString(res[i++]));
			scVo.setVrn(scVo.convertString(res[i++]));
			scVo.setDriverNm(scVo.convertString(res[i++]));
			scVo.setLdngGradCntA(scVo.convertString(res[i++]));
			scVo.setLdngGradCntB(scVo.convertString(res[i++]));
			scVo.setLdngGradCntC(scVo.convertString(res[i++]));
			scVo.setDlvyGradCntA(scVo.convertString(res[i++]));
			scVo.setDlvyGradCntB(scVo.convertString(res[i++]));
			scVo.setDlvyGradCntC(scVo.convertString(res[i++]));
			scVo.setAlarmCnt(scVo.convertString(res[i++]));
			voList.add(scVo);
		}
		return voList;

	}

	@Override
	public List<StatsCommonVo> searchPeriodDlvyLc(String fromDe, String toDe, String fctryCd, String arvlGrad) {
		log.info("fromDe={}, toDe={}, fctryCd={}, arvlGrad={}", fromDe, toDe, fctryCd, arvlGrad);
		Object[] param = {fctryCd, fromDe, toDe, arvlGrad};

		
		List<Object[]> resultList = builder.storedProcedureCallRet("[dbo].[SP_2CH_STATS_PERIOD_DLVY_LC_SEL]", param);

		List<StatsCommonVo> voList = new ArrayList<StatsCommonVo>();

		for ( Object[] res : resultList ) {
			StatsCommonVo scVo = new StatsCommonVo();
			int i=0;

			scVo.setFctryCd(scVo.convertString(res[i++]));
			scVo.setFctryNm(scVo.convertString(res[i++]));
			scVo.setCaralcTy(scVo.convertString(res[i++]));
			scVo.setDlvyLcTy(scVo.convertString(res[i++]));
			scVo.setDlvyLcCd(scVo.convertString(res[i++]));
			scVo.setDlvyLcNm(scVo.convertString(res[i++]));
			scVo.setArvlGradCntA(scVo.convertString(res[i++]));
			scVo.setArvlGradCntB(scVo.convertString(res[i++]));
			scVo.setArvlGradCntC(scVo.convertString(res[i++]));
			scVo.setAvgUnldngTime(scVo.convertString(res[i++]));

			voList.add(scVo);
		}
		return voList;

	}


	@Override
	public List<StatsCommonVo> searchVhcle(String fromDe, String toDe, String fctryCd) {
		return searchVhcle(fromDe, toDe, fctryCd, null, null);
	}
	@Override
	public List<StatsCommonVo> searchVhcle(String fromDe, String toDe, String fctryCd, String ldngGrad, String arvlGrad) {
		log.info("fromDe={}, toDe={}, fctryCd={}, ldngGrad={}, arvlGrad={}", fromDe, toDe, fctryCd, ldngGrad, arvlGrad);
		Object[] param = {fctryCd, fromDe, toDe, ldngGrad, arvlGrad};
		
		List<Object[]> resultList = builder.storedProcedureCallRet("[dbo].[SP_2CH_STATS_DRIVE_VHCLE_SEL]", param);

		List<StatsCommonVo> voList = new ArrayList<StatsCommonVo>();

		for ( Object[] res : resultList ) {
			StatsCommonVo scVo = new StatsCommonVo();
			int i=0;
			scVo.setFctryCd( scVo.convertString(res[i++]) );
			scVo.setDlvyDe(scVo.convertString(res[i++]));
			scVo.setTrnsprtCmpny(scVo.convertString(res[i++]));
			scVo.setVrn(scVo.convertString(res[i++]));
			scVo.setVhcleTy(scVo.convertString(res[i++]));
			scVo.setLdngTy(scVo.convertString(res[i++]));
			scVo.setDriverCd(scVo.convertString(res[i++]));
			scVo.setDriverNm(scVo.convertString(res[i++]));
			scVo.setMobileNo(scVo.convertString(res[i++]));
			scVo.setRtateRate(scVo.convertString(res[i++]));
			scVo.setRouteCnt(scVo.convertString(res[i++]));
			scVo.setEndReportTime((Date)(res[i++]));
			voList.add(scVo);
		}
		return voList;
	}



	@Override
	public List<StatsCommonVo> searchRoute(String dlvyDe, String vrn) {
		log.info("dlvyDe={}, vrn={}", dlvyDe, vrn);
		Object[] param = {dlvyDe, vrn};
		
		List<Object[]> resultList = builder.storedProcedureCallRet("[dbo].[SP_2CH_STATS_DRIVE_ROUTE_SEL]", param);

		List<StatsCommonVo> voList = new ArrayList<StatsCommonVo>();

		for ( Object[] res : resultList ) {
			StatsCommonVo scVo = new StatsCommonVo();
			int i=0;
			scVo.setDlvyDe(scVo.convertString(res[i++]));
			scVo.setRouteNo(scVo.convertString(res[i++]));
			scVo.setCaralcTy(scVo.convertString(res[i++]));
			scVo.setFtEnter((Date)(res[i++]));
			scVo.setLdngWaitTime(scVo.convertString(res[i++]));
			scVo.setLdngSt((Date)(res[i++]));
			scVo.setLdngEd((Date)(res[i++]));
			scVo.setLdngReqreTime(scVo.convertString(res[i++]));
			scVo.setLdngGrad((Date)(res[i++]));
			scVo.setFtExit((Date)(res[i++]));
			scVo.setFtTurn((Date)(res[i++]));
			voList.add(scVo);
		}
		return voList;

	}


	@Override
	public List<StatsCommonVo> searchDlvyLc(String dlvyDe, String vrn, String routeNo) {
		log.info("dlvyDe={}, vrn={}, routeNo={}", dlvyDe, vrn, routeNo);
		Object[] param = {dlvyDe, vrn, routeNo};
		
		List<Object[]> resultList = builder.storedProcedureCallRet("[dbo].[SP_2CH_STATS_DRIVE_DLVY_LC_SEL]", param);

		List<StatsCommonVo> voList = new ArrayList<StatsCommonVo>();

		for ( Object[] res : resultList ) {
			StatsCommonVo scVo = new StatsCommonVo();
			int i=0;
			scVo.setDlvyDe(scVo.convertString(res[i++]));
			scVo.setDlvyLcCd(scVo.convertString(res[i++]));
			scVo.setDlvyLcNm(scVo.convertString(res[i++]));
			scVo.setCcEnter((Date)(res[i++]));
			scVo.setCcArrive((Date)(res[i++]));
			scVo.setCcTakeover((Date)(res[i++]));
			scVo.setCcDepart((Date)(res[i++]));
			scVo.setCcExpcTime((Date)(res[i++]));
			scVo.setCcExpcTimeDiff(scVo.convertString(res[i++]));
			scVo.setArvlGrad(scVo.convertString(res[i++]));
			voList.add(scVo);
		}
		return voList;
	}


	@Override
	public V_ANALS_DRIVING_HIST searchDriveDiarySmry(String dlvyDe, String vrn) {
		log.info("dlvyDe={}, vrn={}", dlvyDe, vrn);
		QV_ANALS_DRIVING_HIST from = QV_ANALS_DRIVING_HIST.v_ANALS_DRIVING_HIST;
		BooleanBuilder where = new BooleanBuilder();
		where.and(from.dlvyDe.eq(dlvyDe));
		where.and(from.vrn.eq(vrn));



 		return builder
				.create()
					.select(
						Projections.fields(
							V_ANALS_DRIVING_HIST.class,
							from.id, from.createDt, from.updateDt,
							from.aclGroupId, from.dlvyDe, from.vrn, from.vhcleTy, from.driverCd, from.driverNm
							, from.drvDistance, from.drvTimeSec, from.maxDrvTimeSec
							, from.beFinDt, from.restTimeSec
							, from.fctryCd, from.fctryNm, from.mobileNo
						)
					)
					.from( from )
					.where( where )
				.fetchOne();


	}


	@Override
	public List<T_DRIVE_DIARY> searchDriveDiaryList(String dlvyDe, String vrn) {
		log.info("dlvyDe={}, vrn={}", dlvyDe, vrn);
		QT_DRIVE_DIARY from = QT_DRIVE_DIARY.t_DRIVE_DIARY;
		BooleanBuilder where = new BooleanBuilder();
		where.and(from.dlvyDe.eq(dlvyDe));
		where.and(from.vrn.eq(vrn));

 		return builder
				.create()
					.select(
						Projections.fields(
							T_DRIVE_DIARY.class,
							from.aclGroupId, from.fctryCd, from.dlvyDe, from.vrn, from.trnsprtCmpny,
							from.ldngTy, from.vhcleTy, from.driverCd, from.driverNm, from.mobileNo,
							from.routeNo, from.caralcTy, from.rtateRate, from.dlvyLcCd, from.dlvyLcNm,
							from.statusCd, from.statusNm, from.eventTy, from.eventDt, from.eventNm,
							from.eventCd, from.remark, from.orderNo, from.grade, 
							from.stdTime, from.adjustTime, from.expcTime
						)
					)
					.from( from )
					.where( where )
					.orderBy(from.dlvyDe.asc(), from.orderNo.asc())
				.fetch()
				.stream()
					.collect(Collectors.toList());
	}

}
