package com.sdc2ch.service.admin;

import java.util.List;

import com.sdc2ch.web.admin.repo.domain.anals.T_ANALS_DLVY_STD_TIME;
import com.sdc2ch.web.admin.repo.domain.anals.T_ANALS_DLVY_STD_TIME_HIST;
import com.sdc2ch.web.admin.repo.domain.anals.T_ANALS_LDNG_STD_TIME;
import com.sdc2ch.web.admin.repo.domain.anals.T_ANALS_LDNG_STD_TIME_HIST;
import com.sdc2ch.web.admin.repo.domain.v.V_ANALS_DLVY_STD_TIME_MSTR;

public interface IAnalsDlvyStdTimeSerivce {

	
	T_ANALS_DLVY_STD_TIME save(T_ANALS_DLVY_STD_TIME analsDlvyStdTime);
	List<T_ANALS_DLVY_STD_TIME> save(List<T_ANALS_DLVY_STD_TIME> analsDlvyStdTimeList);

	
	List<V_ANALS_DLVY_STD_TIME_MSTR> searchAnalsDlvyStdTimeMaster(String fctryCd);
	List<V_ANALS_DLVY_STD_TIME_MSTR> searchAnalsDlvyStdTimeMaster(String fctryCd, String routeNo);

	
	List<T_ANALS_DLVY_STD_TIME> searchAnalsDlvyStdTimeDetail(String fctryCd, String routeNo);

	
	T_ANALS_DLVY_STD_TIME findAnalsDlvyStdTimeDetail(String fctryCd, String routeNo, String caraclTy, String vhcleTy);
	

	
	List<T_ANALS_DLVY_STD_TIME_HIST> searchHistByDlvyStdTimeId(Long dlvyStdTimeId);

	
	List<T_ANALS_DLVY_STD_TIME_HIST> searchHistByDlvyStdTimeParams(String fctryCd, String caralcTy, String vhcleTy, String routeNo);
}
