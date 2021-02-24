package com.sdc2ch.service.admin.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microsoft.sqlserver.jdbc.StringUtils;
import com.querydsl.core.BooleanBuilder;
import com.sdc2ch.service.admin.IAlarmSetupService;
import com.sdc2ch.web.admin.repo.dao.T_AlarmSetupRepository;
import com.sdc2ch.web.admin.repo.domain.alloc.QT_ALARM_SETUP;
import com.sdc2ch.web.admin.repo.domain.alloc.T_ALARM_SETUP;
import com.sdc2ch.web.admin.repo.domain.alloc.type.AlarmSetupType;

@Service
public class AlarmSetupServiceImpl implements IAlarmSetupService{

	@Autowired
	private T_AlarmSetupRepository alarmSetupRepo;

	@Override
	@Transactional
	public List<T_ALARM_SETUP> saveAlarmSetup(List<T_ALARM_SETUP> setup) {
		return alarmSetupRepo.saveAll(setup);
	}
	@Override
	public T_ALARM_SETUP saveAlarmSetup(T_ALARM_SETUP setup) {
		return alarmSetupRepo.save(setup);
	}
	@Override
	public T_ALARM_SETUP saveAlarmSetup(Long id, String fctryCd, AlarmSetupType setupTy, String value) {
		T_ALARM_SETUP setup = new T_ALARM_SETUP();
		setup.setId(id);
		setup.setFctryCd(fctryCd);
		setup.setSetupTy(setupTy);
		setup.setValue(value);
		return alarmSetupRepo.save(setup);
	}

	@Override
	public List<T_ALARM_SETUP> searchAlarmSetupByAll(String fctryCd, String type, String value){
		QT_ALARM_SETUP from = QT_ALARM_SETUP.t_ALARM_SETUP;

		BooleanBuilder where = new BooleanBuilder();

		if (!StringUtils.isEmpty(fctryCd)) {
			where.and(from.fctryCd.eq(fctryCd));
		}

		if (!StringUtils.isEmpty(type)) {
			where.and(from.setupTy.eq(AlarmSetupType.valueOf(type)));
		}
		if (!StringUtils.isEmpty(value)) {
			where.and(from.value.eq(value));
		}

		return (List<T_ALARM_SETUP>)alarmSetupRepo.findAll(where);
	}




}
