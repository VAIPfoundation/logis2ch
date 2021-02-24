package com.sdc2ch.service.admin;

import java.util.List;

import com.sdc2ch.web.admin.repo.domain.sta.V_ANALS_GRADE_SCOPE_HIST;


public interface ILdngAndDlvySttusService {

	
	List<V_ANALS_GRADE_SCOPE_HIST> listLdngAndDlvySttusDaily(String dlvyDe, String fctryCd);
	List<V_ANALS_GRADE_SCOPE_HIST> listLdngAndDlvySttusDaily(String dlvyDe, String fctryCd, String routeNo, String vrn);


	
	List<V_ANALS_GRADE_SCOPE_HIST> listLdngAndDlvySttusVhcle(String dlvyDe, String fctryCd, String vrn);
	List<V_ANALS_GRADE_SCOPE_HIST> listLdngAndDlvySttusVhcle(String dlvyDe, String fctryCd, String vrn, String ldngGrad, String dlvyGrad);

}
