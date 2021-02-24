package com.sdc2ch.service.admin.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdc2ch.service.admin.IAnalsLdngTimeService;
import com.sdc2ch.web.admin.repo.dao.V_AnalsGradeScopeHistRepository;
import com.sdc2ch.web.admin.repo.dto.IAnalsDstrbChrtColumnDto;
import com.sdc2ch.web.admin.repo.dto.IAnalsDstrbChrtPieDto;
import com.sdc2ch.web.admin.repo.dto.IAnalsDstrbDto;
import com.sdc2ch.web.admin.repo.dto.IAnalsDstrbHistDto;
import com.sdc2ch.web.admin.repo.dto.IAnalsLdngDtlsDto;
import com.sdc2ch.web.admin.repo.dto.IAnalsRankDto;

@Service
public class AnalsLdngTimeServiceImpl implements IAnalsLdngTimeService {

	@Autowired
	private V_AnalsGradeScopeHistRepository vAnalsRepo;

	@Override
	public List<IAnalsRankDto> searchAnalsLdngTimeRank(String fctryCd, String fromDe, String toDe, String caralcTy,
			String vhcleTy, String routeNo, String wkdayYn, String satYn, String sunYn) {
		return vAnalsRepo.searchAnalsLdngTimeRank(fctryCd, fromDe, toDe, caralcTy, vhcleTy, routeNo, wkdayYn, satYn, sunYn);
	}

	@Override
	public List<IAnalsDstrbDto> searchAnalsLdngTimeDstrbList(String fctryCd, String fromDe, String toDe,
			String caralcTy, String vhcleTy, String routeNo, String wkdayYn, String satYn, String sunYn, Long stdTime) {
		return vAnalsRepo.searchAnalsLdngTimeDstrbList(fctryCd, fromDe, toDe, caralcTy, vhcleTy, routeNo, wkdayYn, satYn, sunYn, stdTime);
	}

	@Override
	public List<IAnalsDstrbHistDto> searchAnalsLdngTimeDstrbSetupHist(String fctryCd, String fromDe, String toDe,
			String caralcTy, String vhcleTy, String routeNo, String wkdayYn, String satYn, String sunYn, Long stdTime) {
		return vAnalsRepo.searchAnalsLdngTimeDstrbSetupHist(fctryCd, fromDe, toDe, caralcTy, vhcleTy, routeNo, wkdayYn, satYn, sunYn, stdTime);
	}

	@Override
	public void saveAnalsLdngTimeDstrbSetup(String fctryCd, String fromDe, String toDe, String caralcTy, String vhcleTy,
			String routeNo, String wkdayYn, String satYn, String sunYn, Long stdTime) {
		vAnalsRepo.saveAnalsLdngTimeDstrbSetup(fctryCd, fromDe, toDe, caralcTy, vhcleTy, routeNo, wkdayYn, satYn, sunYn, stdTime);
	}

	@Override
	public List<IAnalsLdngDtlsDto> searchAnalsLdngTimeDetailList(String fctryCd, String fromDe, String toDe,
			String caralcTy, String vhcleTy, String routeNo, String wkdayYn, String satYn, String sunYn) {
		return vAnalsRepo.searchAnalsLdngTimeDetailList(fctryCd, fromDe, toDe, caralcTy, vhcleTy, routeNo, wkdayYn, satYn, sunYn);
	}

}
