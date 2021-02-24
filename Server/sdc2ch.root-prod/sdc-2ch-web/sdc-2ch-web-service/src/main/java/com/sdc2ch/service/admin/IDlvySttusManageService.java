package com.sdc2ch.service.admin;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sdc2ch.require.enums.SetupLcType;
import com.sdc2ch.web.admin.repo.domain.op.T_BCON_HIST;
import com.sdc2ch.web.admin.repo.domain.v.V_CVO_MONITOR;
import com.sdc2ch.web.admin.repo.domain.v.V_UNSTORING_MANAGE;


public interface IDlvySttusManageService {

	
	List<V_CVO_MONITOR> search(String fctryCd);
	List<V_CVO_MONITOR> search(String fctryCd, String vhcleTy, String vrn);

}
