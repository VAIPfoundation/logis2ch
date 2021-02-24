package com.sdc2ch.service.admin.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdc2ch.service.admin.INoArvlDlvyLcService;
import com.sdc2ch.tms.io.TmsLocationIO;
import com.sdc2ch.tms.service.ITmsNoArvlDlvyLcService;
import com.sdc2ch.tms.service.ITmsStopService;
import com.sdc2ch.web.admin.repo.AdmQueryBuilder;
import com.sdc2ch.web.admin.repo.dao.V_AnalsGradePointHistRepository;
import com.sdc2ch.web.admin.repo.dto.INoArvlDlvyLcDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NoArvlDlvyLcServiceImpl implements INoArvlDlvyLcService  {

	@Autowired
	private V_AnalsGradePointHistRepository vAnalsGradePointHistRepo;
	@Autowired
	private ITmsNoArvlDlvyLcService iTmsNoArvlDlvyLcService;
	@Autowired
	private AdmQueryBuilder builder;
	@Autowired
	private ITmsStopService tmsStopSvc; 

	@Override
	public List<INoArvlDlvyLcDto> listNoArvlDlvyLc(String fctryCd, String fromDe, String toDe, String caralcTy, String vhcleTy, String routeNo, String trnsprtCmpny, String vrn) {

		return vAnalsGradePointHistRepo.listNoArvlDlvyLc(fctryCd, fromDe, toDe, caralcTy, vhcleTy, routeNo, trnsprtCmpny, vrn);
	}

	@Override
	public int updateNoArvlDlvyLc(String dlvyLcCd, String adres, String lat, String lng) {
		return iTmsNoArvlDlvyLcService.UpdateStopLocation(dlvyLcCd, adres, lat, lng);
	}

	@Override
	public TmsLocationIO findStopLocation(String stopCd) {
		return tmsStopSvc.findStopLocation(stopCd);
	}

}
