package com.sdc2ch.service.admin;

import java.util.List;

import com.sdc2ch.web.admin.repo.domain.alloc.T_ALARM_STTUS;
import com.sdc2ch.web.admin.repo.domain.v.V_ALARM_STTUS;

public interface IAlarmSttusService {

	
	T_ALARM_STTUS save(T_ALARM_STTUS entity);

	
	List<T_ALARM_STTUS> save(List<T_ALARM_STTUS> entitys);

	
	List<V_ALARM_STTUS> searchViewAlarmSttus(String dlvyDe, String fctryCd, String caralcTy, String vrn);

	
	List<T_ALARM_STTUS> searchAlarmSttus(String dlvyDe, String fctryCd, String caralcTy, String routeNo, String vrn);


}
