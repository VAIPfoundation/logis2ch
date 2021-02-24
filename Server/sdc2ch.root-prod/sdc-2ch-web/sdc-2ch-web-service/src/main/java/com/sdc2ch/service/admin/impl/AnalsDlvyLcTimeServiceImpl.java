package com.sdc2ch.service.admin.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdc2ch.service.admin.IAnalsDlvyLcTimeService;
import com.sdc2ch.web.admin.repo.dao.V_AnalsGradeScopeHistRepository;
import com.sdc2ch.web.admin.repo.dto.IAnalsDlvyLcRankListDto;
import com.sdc2ch.web.admin.repo.dto.IAnalsDstrbDto;

@Service
public class AnalsDlvyLcTimeServiceImpl
 implements IAnalsDlvyLcTimeService
{

	@Autowired
	private V_AnalsGradeScopeHistRepository vAnalsRepo;

	@Override
	public List<IAnalsDlvyLcRankListDto> searchAnalsDlvyLcTimeRankList(String fctryCd, String fromDe, String toDe,
			String wkdayYn, String satYn, String sunYn) {
		return vAnalsRepo.searchAnalsDlvyLcTimeRankList(fctryCd, fromDe, toDe, wkdayYn, satYn, sunYn);
	}

	@Override
	public List<IAnalsDstrbDto> searchAnalsDlvyLcTimeDstrbList(String fctryCd, String fromDe, String toDe,
			String dlvyLcCd, String dlvyLcTime, String wkdayYn, String satYn, String sunYn) {
		return vAnalsRepo.searchAnalsDlvyLcTimeDstrbList(fctryCd, fromDe, toDe,  dlvyLcCd, dlvyLcTime, wkdayYn, satYn, sunYn);
	}

	@Override
	public List<IAnalsDstrbDto> searchAnalsDlvyLcTimeDetailsList(String fctryCd, String fromDe, String toDe,
			String dlvyLcCd, String wkdayYn, String satYn, String sunYn) {

		return vAnalsRepo.searchAnalsDlvyLcTimeDetailsList(fctryCd, fromDe, toDe, dlvyLcCd, wkdayYn, satYn, sunYn);
	}



}
