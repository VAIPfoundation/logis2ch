package com.sdc2ch.service.admin;

import java.util.List;

import com.sdc2ch.web.admin.repo.dto.IAnalsDlvyLcRankListDto;
import com.sdc2ch.web.admin.repo.dto.IAnalsDstrbDto;

public interface IAnalsDlvyLcTimeService {

	
	List<IAnalsDlvyLcRankListDto> searchAnalsDlvyLcTimeRankList(String fctryCd, String fromDe, String toDe,
			String wkdayYn, String satYn, String sunYn);


	
	List<IAnalsDstrbDto> searchAnalsDlvyLcTimeDstrbList(String fctryCd, String fromDe, String toDe, String dlvyLcCd, String dlvyLcTime, String wkdayYn, String satYn, String sunYn);


	
	List<IAnalsDstrbDto> searchAnalsDlvyLcTimeDetailsList(String fctryCd, String fromDe, String toDe, String dlvyLcCd,
			String wkdayYn, String satYn, String sunYn);

}
