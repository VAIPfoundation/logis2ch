package com.sdc2ch.service.admin.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.sdc2ch.service.admin.IAlarmSttusService;
import com.sdc2ch.service.admin.model.StatsCommonVo;
import com.sdc2ch.web.admin.repo.AdmQueryBuilder;
import com.sdc2ch.web.admin.repo.dao.T_AlarmSttusRepository;
import com.sdc2ch.web.admin.repo.dao.V_AlarmSttusRepository;
import com.sdc2ch.web.admin.repo.domain.alloc.QT_ALARM_STTUS;
import com.sdc2ch.web.admin.repo.domain.alloc.T_ALARM_STTUS;
import com.sdc2ch.web.admin.repo.domain.alloc.type.AlarmType;
import com.sdc2ch.web.admin.repo.domain.v.QV_ALARM_STTUS;
import com.sdc2ch.web.admin.repo.domain.v.V_ALARM_STTUS;

@Service
public class AlarmSttusServiceImpl implements IAlarmSttusService {

	@Autowired AdmQueryBuilder builder;

	@Autowired T_AlarmSttusRepository alarmSttusRepo;

	@Autowired V_AlarmSttusRepository viewAlarmSttusRepo;


	@Override
	public T_ALARM_STTUS save(T_ALARM_STTUS entity) {
		return alarmSttusRepo.save(entity);
	}

	@Override
	public List<T_ALARM_STTUS> save(List<T_ALARM_STTUS> entitys) {
		return alarmSttusRepo.saveAll(entitys);
	}

	@Override
	public List<V_ALARM_STTUS> searchViewAlarmSttus(String dlvyDe, String fctryCd, String caralcTy, String vrn) {
		QV_ALARM_STTUS vAlarmSttus = QV_ALARM_STTUS.v_ALARM_STTUS;
		Object[] param = {fctryCd, dlvyDe, vrn};
		
		List<Object[]> resultList = builder.storedProcedureCallRet("[dbo].[SP_2CH_ALARM_SEL]", param);

		List<V_ALARM_STTUS> voList = new ArrayList<V_ALARM_STTUS>();

		for ( Object[] res : resultList ) {
			V_ALARM_STTUS asVo = new V_ALARM_STTUS();
			int i=0;

			asVo.setDlvyDe((String)(res[i++]));
			asVo.setFctryCd((String)(res[i++]));
			asVo.setVrn((String)(res[i++]));
			asVo.setNoCnfrmCaralcDtlsAlarmCnt((Integer)(res[i++]));
			asVo.setNoStartWorkAlarmCnt((Integer)(res[i++]));
			asVo.setNoPassageAlarmCnt((Integer)(res[i++]));
			asVo.setNoLdngAlarmCnt((Integer)(res[i++]));
			asVo.setFctryStartAlarmCnt((Integer)(res[i++]));
			asVo.setEtyBoxDcsnNoOprtnAlarmCnt((Integer)(res[i++]));
			asVo.setNoEndWorkAlarmCnt((Integer)(res[i++]));
			asVo.setRtngudWrhousngAlarmCnt((Integer)(res[i++]));
			asVo.setGpsOffAlarmCnt((Integer)(res[i++]));
			voList.add(asVo);
		}
		return voList;
	}

	@Override
	public List<T_ALARM_STTUS> searchAlarmSttus(String dlvyDe, String fctryCd, String caralcTy, String routeNo, String vrn){
		QT_ALARM_STTUS alarmSttus = QT_ALARM_STTUS.t_ALARM_STTUS;
		BooleanBuilder where = new BooleanBuilder();
		if(!StringUtils.isEmpty(dlvyDe)) {
			where.and(alarmSttus.dlvyDe.eq(dlvyDe));
		}

		if(!StringUtils.isEmpty(fctryCd)) {
			where.and(alarmSttus.fctryCd.eq(fctryCd));
		}

		if(!StringUtils.isEmpty(caralcTy)) {
			where.and(alarmSttus.caralcTy.eq(caralcTy));
		}

		if(!StringUtils.isEmpty(routeNo)) {
			where.and(alarmSttus.routeNo.eq(routeNo).or(alarmSttus.routeNo.isNull()));
		}

		if(!StringUtils.isEmpty(vrn)) {
			where.and(alarmSttus.vrn.eq(vrn));
		}




		
		return (List<T_ALARM_STTUS>) alarmSttusRepo.findAll(where, alarmSttus.trnsmisDt.desc());
	}

}

