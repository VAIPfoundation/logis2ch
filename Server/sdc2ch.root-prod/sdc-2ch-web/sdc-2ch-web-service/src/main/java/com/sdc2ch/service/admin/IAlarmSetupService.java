package com.sdc2ch.service.admin;

import java.util.List;

import com.sdc2ch.web.admin.repo.domain.alloc.T_ALARM_SETUP;
import com.sdc2ch.web.admin.repo.domain.alloc.type.AlarmSetupType;

public interface IAlarmSetupService {

	
	T_ALARM_SETUP saveAlarmSetup(T_ALARM_SETUP setup);
	T_ALARM_SETUP saveAlarmSetup(Long id, String fctryCd, AlarmSetupType setupTy, String value);
	List<T_ALARM_SETUP> saveAlarmSetup(List<T_ALARM_SETUP> setup);
	
	List<T_ALARM_SETUP> searchAlarmSetupByAll(String fctryCd, String type, String value);


}
