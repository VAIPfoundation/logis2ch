package com.sdc2ch.service.admin;

import java.util.List;

import com.sdc2ch.web.admin.repo.domain.anals.T_ANALS_LDNG_STD_TIME;
import com.sdc2ch.web.admin.repo.domain.anals.T_ANALS_LDNG_STD_TIME_HIST;
import com.sdc2ch.web.admin.repo.domain.v.V_ANALS_LDNG_STD_TIME_MSTR;
import com.sdc2ch.web.admin.repo.dto.IAnalsRankDto;

public interface IAnalsLdngStdTimeSerivce {

	
	List<V_ANALS_LDNG_STD_TIME_MSTR> searchAnalsLdngStdTimeMaster(String fctryCd);
	List<V_ANALS_LDNG_STD_TIME_MSTR> searchAnalsLdngStdTimeMaster(String fctryCd, String caraclTy, String vhcleTy);

	
	List<T_ANALS_LDNG_STD_TIME> searchAnalsLdngStdTimeDetail(String fctryCd, String caraclTy, String vhcleTy);

	
	T_ANALS_LDNG_STD_TIME findAnalsLdngStdTimeDetail(String fctryCd, String caraclTy, String vhcleTy, String routeNo);

	
	T_ANALS_LDNG_STD_TIME save(T_ANALS_LDNG_STD_TIME analsLdngStdTime);
	List<T_ANALS_LDNG_STD_TIME> save(List<T_ANALS_LDNG_STD_TIME> analsLdngStdTimeList);

	
	List<T_ANALS_LDNG_STD_TIME_HIST> searchHistByLdngStdTimeId(Long ldngStdTimeId);

	
	List<T_ANALS_LDNG_STD_TIME_HIST> searchHistByLdngStdTimeParams(String fctryCd, String caralcTy, String vhcleTy, String routeNo);

}
