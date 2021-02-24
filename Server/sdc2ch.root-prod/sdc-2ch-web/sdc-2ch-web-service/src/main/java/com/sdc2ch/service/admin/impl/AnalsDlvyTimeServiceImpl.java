package com.sdc2ch.service.admin.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdc2ch.service.admin.IAnalsDlvyTimeService;
import com.sdc2ch.web.admin.repo.dao.V_AnalsGradeScopeHistRepository;
import com.sdc2ch.web.admin.repo.dto.IAnalsDlvyDtlsDto;
import com.sdc2ch.web.admin.repo.dto.IAnalsDstrbChrtColumnDto;
import com.sdc2ch.web.admin.repo.dto.IAnalsDstrbChrtPieDto;
import com.sdc2ch.web.admin.repo.dto.IAnalsDstrbDto;
import com.sdc2ch.web.admin.repo.dto.IAnalsDstrbHistDto;
import com.sdc2ch.web.admin.repo.dto.IAnalsRankDto;

@Service
public class AnalsDlvyTimeServiceImpl implements IAnalsDlvyTimeService{

	@Autowired
	private V_AnalsGradeScopeHistRepository vAnalsRepo;

	@Override
	public List<IAnalsRankDto> searchAnalsDlvyTimeRank(String fctryCd, String fromDe, String toDe, String routeNo,
			String caralcTy, String vhcleTy, String wkdayYn, String satYn, String sunYn){
		return vAnalsRepo.searchAnalsDlvyTimeRank(fctryCd, fromDe, toDe, routeNo, caralcTy, vhcleTy, wkdayYn, satYn, sunYn);
	}

	@Override
	public List<IAnalsDstrbDto> searchAnalsDlvyTimeDstrbList(String fctryCd, String fromDe, String toDe,
			String routeNo, String caralcTy, String vhcleTy, String wkdayYn, String satYn, String sunYn, Long stdTime) {
		return vAnalsRepo.searchAnalsDlvyTimeDstrbList(fctryCd, fromDe, toDe, routeNo, caralcTy, vhcleTy, wkdayYn, satYn, sunYn, stdTime);
	}

	@Override
	public List<IAnalsDstrbHistDto> searchAnalsDlvyTimeDstrbSetupHist(String fctryCd, String fromDe, String toDe,
			String routeNo, String caralcTy, String vhcleTy, String wkdayYn, String satYn, String sunYn, Long stdTime) {
		return vAnalsRepo.searchAnalsDlvyTimeDstrbSetupHist(fctryCd, fromDe, toDe, routeNo, caralcTy, vhcleTy, wkdayYn, satYn, sunYn, stdTime);
	}

	@Override
	public void saveAnalsDlvyTimeDstrbSetup(String fctryCd, String fromDe, String toDe, String routeNo, String caralcTy,
			String vhcleTy, String wkdayYn, String satYn, String sunYn, Long stdTime) {
		vAnalsRepo.saveAnalsDlvyTimeDstrbSetup(fctryCd, fromDe, toDe, routeNo, caralcTy, vhcleTy, wkdayYn, satYn, sunYn, stdTime);
	}

	@Override
	public List<IAnalsDlvyDtlsDto> searchAnalsDlvyTimeDetailList(String fctryCd, String fromDe, String toDe,
			String routeNo, String caralcTy, String vhcleTy, String wkdayYn, String satYn, String sunYn) {
		return vAnalsRepo.searchAnalsDlvyTimeDetailList(fctryCd, fromDe, toDe, routeNo, caralcTy, vhcleTy, wkdayYn, satYn, sunYn);
	}

}
