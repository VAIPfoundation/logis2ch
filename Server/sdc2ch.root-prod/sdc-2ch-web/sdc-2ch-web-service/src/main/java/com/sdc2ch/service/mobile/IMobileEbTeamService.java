package com.sdc2ch.service.mobile;

import java.util.List;

import com.sdc2ch.prcss.eb.vo.DriverSquareboxErpVo;
import com.sdc2ch.prcss.eb.vo.EmptyboxErpVo;
import com.sdc2ch.prcss.eb.vo.EmptyboxVo;
import com.sdc2ch.prcss.eb.vo.SummaryEmptyboxVo;
import com.sdc2ch.web.admin.repo.domain.v.V_CARALC_PLAN;

 
public interface IMobileEbTeamService {
	SummaryEmptyboxVo summaryEmptyboxByRouteNoAndDlvyDe(String routeNo, String dlvyDe);
	SummaryEmptyboxVo summaryEmptyboxByRouteNoAndDlvyDe(String routeNo, String dlvyDe, boolean erp);

	List<EmptyboxVo> listEmptyboxByRouteNoAndDlvyDe(String routeNo, String dlvyDe);
	List<EmptyboxVo> listEmptyboxByRouteNoAndDlvyDe(String routeNo, String dlvyDe, boolean erp);

	List<DriverSquareboxErpVo> listEmptyBoxDriverMonthly(String dlvyDe, String vrn);


	List<V_CARALC_PLAN> listCaralcPlanMonthly(String dlvyDe, String vrn);
	List<EmptyboxErpVo> listEmptyBoxPalletMonthly(String dlvyDe, List<V_CARALC_PLAN> planList);




}
