package com.sdc2ch.service.admin;

import java.util.List;

import com.sdc2ch.web.admin.repo.dto.IAnalsDstrbChrtColumnDto;
import com.sdc2ch.web.admin.repo.dto.IAnalsDstrbChrtPieDto;
import com.sdc2ch.web.admin.repo.dto.IAnalsDstrbDto;
import com.sdc2ch.web.admin.repo.dto.IAnalsDstrbHistDto;
import com.sdc2ch.web.admin.repo.dto.IAnalsLdngDtlsDto;
import com.sdc2ch.web.admin.repo.dto.IAnalsRankDto;

public interface IAnalsLdngTimeService {

	
	List<IAnalsRankDto> searchAnalsLdngTimeRank(String fctryCd, String fromDt, String toDe, String caralcTy,
			String vhcleTy, String routeNo, String wkdayYn, String satYn, String sunYn);

	
	List<IAnalsDstrbDto> searchAnalsLdngTimeDstrbList(String fctryCd, String fromDe, String toDe, String caralcTy,
			String vhcleTy, String routeNo, String wkdayYn, String satYn, String sunYn, Long stdTime);

	
	List<IAnalsDstrbHistDto> searchAnalsLdngTimeDstrbSetupHist(String fctryCd, String fromDe, String toDe,
			String caralcTy, String vhcleTy, String routeNo, String wkdayYn, String satYn, String sunYn, Long stdTime);

	
	void saveAnalsLdngTimeDstrbSetup(String fctryCd, String fromDe, String toDe, String caralcTy, String vhcleTy,
			String routeNo, String wkdayYn, String satYn, String sunYn, Long stdTime);

	
	List<IAnalsLdngDtlsDto> searchAnalsLdngTimeDetailList(String fctryCd, String fromDe, String toDe, String caralcTy,
			String vhcleTy, String routeNo, String wkdayYn, String satYn, String sunYn);

}
