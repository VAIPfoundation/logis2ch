package com.sdc2ch.service.admin;

import java.util.List;

import com.sdc2ch.web.admin.repo.dto.IAnalsDlvyDtlsDto;
import com.sdc2ch.web.admin.repo.dto.IAnalsDstrbChrtColumnDto;
import com.sdc2ch.web.admin.repo.dto.IAnalsDstrbChrtPieDto;
import com.sdc2ch.web.admin.repo.dto.IAnalsDstrbDto;
import com.sdc2ch.web.admin.repo.dto.IAnalsDstrbHistDto;
import com.sdc2ch.web.admin.repo.dto.IAnalsRankDto;

public interface IAnalsDlvyTimeService {

	
	List<IAnalsRankDto> searchAnalsDlvyTimeRank(String fctryCd, String fromDe, String toDe, String routeNo,
			String caralcTy, String vhcleTy, String wkdayYn, String satYn, String sunYn);

	
	List<IAnalsDstrbDto> searchAnalsDlvyTimeDstrbList(String fctryCd, String fromDe, String toDe, String routeNo,
			String caralcTy, String vhcleTy, String wkdayYn, String satYn, String sunYn, Long stdTime);

	
	List<IAnalsDstrbHistDto> searchAnalsDlvyTimeDstrbSetupHist(String fctryCd, String fromDe, String toDe,
			String routeNo, String caralcTy, String vhcleTy, String wkdayYn, String satYn, String sunYn, Long stdTime);

	
	void saveAnalsDlvyTimeDstrbSetup(String fctryCd, String fromDe, String toDe, String routeNo, String caralcTy,
			String vhcleTy, String wkdayYn, String satYn, String sunYn, Long stdTime);

	
	List<IAnalsDlvyDtlsDto> searchAnalsDlvyTimeDetailList(String fctryCd, String fromDe, String toDe, String routeNo,
			String caralcTy, String vhcleTy, String wkdayYn, String satYn, String sunYn);

}
