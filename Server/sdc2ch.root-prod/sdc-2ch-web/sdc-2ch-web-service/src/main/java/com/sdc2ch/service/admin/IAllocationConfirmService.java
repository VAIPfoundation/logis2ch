package com.sdc2ch.service.admin;

import java.util.List;

import com.sdc2ch.web.admin.repo.domain.v.V_CARALC_DTLS;
import com.sdc2ch.web.admin.repo.domain.v.V_CARALC_MSTR;
import com.sdc2ch.web.admin.repo.domain.v.V_CARALC_PLAN;


public interface IAllocationConfirmService {








	


	List<V_CARALC_MSTR> search2(String date, String fctryCd, String carAlcType, String routeNo, String vrn, String dcsnYn);

	List<V_CARALC_MSTR> findCaralcMstrByDlvyDe(String dlvyDe);

	List<V_CARALC_MSTR> findCaralcMstrByDlvyDeAndVrn(String dlvyDe, String vrn);

	List<V_CARALC_DTLS> findCaralcDtlsByDeliveryDateAndRouteNo(String dlvyDe, String routeNo);

	List<V_CARALC_PLAN> findCaralcPlanByDlvyDeAndVrn(String dlvyDe, String vrn);
}
