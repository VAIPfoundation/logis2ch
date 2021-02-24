package com.sdc2ch.service.admin;

import java.util.List;

import com.sdc2ch.service.admin.model.DailyDlvyErorSttusVo;


public interface IDailyDlvyErorSttusService {

	
	DailyDlvyErorSttusVo listDailyDlvyErorSttus(String fctryCd, String fromDe, String toDe, String caralcTy,
			String vhcleTy, String routeNo, String dlvyLcCd, String wkdayYn, String satYn, String sunYn);


}
