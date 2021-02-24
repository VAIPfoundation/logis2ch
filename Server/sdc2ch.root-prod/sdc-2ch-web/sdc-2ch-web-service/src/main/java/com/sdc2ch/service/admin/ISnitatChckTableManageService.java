package com.sdc2ch.service.admin;

import java.util.List;

import com.sdc2ch.service.common.exception.ServiceException;
import com.sdc2ch.web.admin.repo.domain.alloc.T_SNITAT_CHCK_IF_SUMRY;
import com.sdc2ch.web.admin.repo.domain.alloc.T_SNITAT_CHCK_TABLE;
import com.sdc2ch.web.admin.repo.domain.v.V_SNITAT_CHCK_TABLE_MONTHLY;


public interface ISnitatChckTableManageService {

	
	List<V_SNITAT_CHCK_TABLE_MONTHLY> searchMonthlyList(String year, String fctryCd);
	List<V_SNITAT_CHCK_TABLE_MONTHLY> searchMonthlyList(String year, String fctryCd, String vhcleTy, String vrn, String driverNm);

	
	List<T_SNITAT_CHCK_TABLE> searchMonthlyDetailList(String year, String month, String fctryCd, String vrn);

	
	List<T_SNITAT_CHCK_TABLE> searchDailyList(String fromDe, String toDe, String fctryCd);
	List<T_SNITAT_CHCK_TABLE> searchDailyList(String fromDe, String toDe, String fctryCd, String vhcleTy, String vrn, String driverNm, String keyword);

	
	T_SNITAT_CHCK_TABLE searchOne(String dlvyDe, String routeNo);

	List<T_SNITAT_CHCK_TABLE> update(List<T_SNITAT_CHCK_TABLE> unstoringVos);



	
	public List<T_SNITAT_CHCK_IF_SUMRY> searchSnitatcheckSumry(String factryCd, String year, String month);

	
	public T_SNITAT_CHCK_IF_SUMRY interfaceSnitatCheckSumry(String userId, String factryCd, String year, String month);
	
	public T_SNITAT_CHCK_IF_SUMRY interfaceSnitatCheckSumry2(String userId, String factryCd, String year, String month, Boolean retry) throws ServiceException;

	
	public int insertAllExcludeDayoffExe(String fctryCd, String month);

	
	public long updateAllState(String fromDe, String toDe, String fctryCd, String vhcleTy, String vrn, String driverNm, String keyword);



}
