package com.sdc2ch.service.admin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sdc2ch.require.enums.SetupLcType;
import com.sdc2ch.web.admin.repo.domain.op.T_BCON_HIST;
import com.sdc2ch.web.admin.repo.domain.v.V_UNSTORING_MANAGE;


public interface IUnstoringManageService {

	
	
	List<HashMap<String,Object>> listTaglist(String dlvyDe, String month);
	
			
	List<HashMap<String,Object>> search(String fctryCd, String fromDe, String toDe);
	List<HashMap<String,Object>> search(String fctryCd, String fromDe, String toDe, String fromTime, String toTime, String vhcleTy, String vrn, String caralcTy);

	
	
	


	@Deprecated
	Page<V_UNSTORING_MANAGE> search(String fctryCd, String fromDe, String toDe, Pageable pageable);
	@Deprecated
	Page<V_UNSTORING_MANAGE> search(String fctryCd, String fromDe, String toDe, String fromTime, String toTime, String vhcleTy, String vrn, String caralcTy, Pageable pageable);

	
	V_UNSTORING_MANAGE searchOne(String dlvyDe, String routeNo);

	int update(List<V_UNSTORING_MANAGE> unstoringVos);



}
