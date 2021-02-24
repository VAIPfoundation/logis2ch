package com.sdc2ch.service.admin.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdc2ch.service.admin.IDailyDlvyErorSttusService;
import com.sdc2ch.service.admin.model.DailyDlvyErorSttusVo;
import com.sdc2ch.web.admin.repo.AdmQueryBuilder;
import com.sdc2ch.web.admin.repo.dao.V_AnalsGradePointHistRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DailyDlvyErorSttusServiceImpl implements IDailyDlvyErorSttusService  {

	@Autowired
	V_AnalsGradePointHistRepository vAnalsGradePointHistRepo;
	@Autowired
	private AdmQueryBuilder builder;

	@Override
	public DailyDlvyErorSttusVo listDailyDlvyErorSttus(String fctryCd, String fromDe, String toDe,
			String caralcTy, String vhcleTy, String routeNo, String dlvyLcCd, String wkdayYn, String satYn,
			String sunYn) {

		DailyDlvyErorSttusVo vo = new DailyDlvyErorSttusVo();

		vo.setDatasA(vAnalsGradePointHistRepo.listDailyDlvyErorSttus( "A", fctryCd, fromDe, toDe, caralcTy, vhcleTy, routeNo, dlvyLcCd, wkdayYn, satYn, sunYn));
		vo.setDatasB(vAnalsGradePointHistRepo.listDailyDlvyErorSttus( "B", fctryCd, fromDe, toDe, caralcTy, vhcleTy, routeNo, dlvyLcCd, wkdayYn, satYn, sunYn));
		vo.setDatasC(vAnalsGradePointHistRepo.listDailyDlvyErorSttus( "C", fctryCd, fromDe, toDe, caralcTy, vhcleTy, routeNo, dlvyLcCd, wkdayYn, satYn, sunYn));

		return vo;
	}

}
