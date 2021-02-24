package com.sdc2ch.service.mobile.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.Projections;
import com.sdc2ch.prcss.eb.IEmptyBoxService;
import com.sdc2ch.prcss.eb.vo.DriverSquareboxErpVo;
import com.sdc2ch.prcss.eb.vo.EmptyboxErpVo;
import com.sdc2ch.prcss.eb.vo.EmptyboxVo;
import com.sdc2ch.prcss.eb.vo.SummaryEmptyboxVo;
import com.sdc2ch.repo.builder.IAdmQueryBuilder;
import com.sdc2ch.service.mobile.IMobileEbTeamService;
import com.sdc2ch.web.admin.repo.domain.v.QV_CARALC_PLAN;
import com.sdc2ch.web.admin.repo.domain.v.V_CARALC_PLAN;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MobileEbTeamServiceImpl implements IMobileEbTeamService {

	@Autowired IAdmQueryBuilder builder;
	@Autowired IEmptyBoxService emptySvc;

	@Override
	public SummaryEmptyboxVo summaryEmptyboxByRouteNoAndDlvyDe(String routeNo, String dlvyDe) {
		return summaryEmptyboxByRouteNoAndDlvyDe(routeNo, dlvyDe, false);
	}
	@Override
	public SummaryEmptyboxVo summaryEmptyboxByRouteNoAndDlvyDe(String routeNo, String dlvyDe, boolean erp) {
		return emptySvc.sumEmptyboxByDlvyDeAndRouteNo(dlvyDe, routeNo, erp);
	}

	@Override
	public List<EmptyboxVo> listEmptyboxByRouteNoAndDlvyDe(String routeNo, String dlvyDe) {
		return listEmptyboxByRouteNoAndDlvyDe(routeNo, dlvyDe, false);
	}
	@Override
	public List<EmptyboxVo> listEmptyboxByRouteNoAndDlvyDe(String routeNo, String dlvyDe, boolean erp) {
		return emptySvc.listEmptyboxByDlvyDeAndRouteNo(dlvyDe, routeNo, erp);
	}
	@Override
	public List<DriverSquareboxErpVo> listEmptyBoxDriverMonthly(String dlvyDe, String vrn) {
		return emptySvc.listEmptyBoxDriverMonthly(dlvyDe, vrn);
	}
	@Override
	public List<V_CARALC_PLAN> listCaralcPlanMonthly(String dlvyDe, String vrn) {
		QV_CARALC_PLAN code = QV_CARALC_PLAN.v_CARALC_PLAN;
		dlvyDe = dlvyDe.replace("-", "");

		return builder.create()
				.select( Projections.fields(V_CARALC_PLAN.class,code.dlvyDe, code.routeNo, code.dlvyLcCd) )
				.from(code)
				.where(code.dlvyDe.like(dlvyDe+"%").and(code.vrn.eq(vrn)))
				.fetch();
	}
	@Override
	public List<EmptyboxErpVo> listEmptyBoxPalletMonthly(String dlvyDe, List<V_CARALC_PLAN> planList) {

		List<EmptyboxErpVo> ebVoList = new ArrayList<EmptyboxErpVo>();

		for ( V_CARALC_PLAN plan : planList ) {
			EmptyboxErpVo ebVo = new EmptyboxErpVo();
			ebVo.setDlvyDe(plan.getDlvyDe());
			ebVo.setRouteNo(plan.getRouteNo());
			ebVo.setStopCd(plan.getDlvyLcCd());
			ebVoList.add(ebVo);
		}

		return emptySvc.listEmptyBoxPalletMonthly(dlvyDe, ebVoList);
	}

}
