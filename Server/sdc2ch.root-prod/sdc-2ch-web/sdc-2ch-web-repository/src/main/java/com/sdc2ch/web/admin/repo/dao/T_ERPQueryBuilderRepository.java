package com.sdc2ch.web.admin.repo.dao;

public interface T_ERPQueryBuilderRepository {
	
	
	public static String FRONT_EXIT_QUERY = ""
			+ "select * from OPENQUERY(MILK, 'select 노선번호 as route_No,  출고창고 as fctry_Cd, 차량번호 as vrn, 기사번호 as driver_Cd, "
			+ "통문시간 as front_exit  from XXA_TMS_통문정보 where 요청일자 = ''%s'' and 출고창고 = ''%s''')";
}
