package com.sdc2ch.service.admin;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sdc2ch.web.admin.repo.domain.T_MOBILE_HEALTH_CHK;
import com.sdc2ch.web.admin.repo.domain.T_MOBILE_HEALTH_CHK_HIST;

public interface IMobileHealthCheckService {

	
	T_MOBILE_HEALTH_CHK_HIST saveHist(T_MOBILE_HEALTH_CHK_HIST hist);
	T_MOBILE_HEALTH_CHK saveLast(T_MOBILE_HEALTH_CHK cur);
	
	List<T_MOBILE_HEALTH_CHK_HIST> searchMobileHealthCheckHistByDate(Date fromDt, Date toDt, String fctryCd, String vrn, String mdn);
	
	Page<T_MOBILE_HEALTH_CHK_HIST> searchPageMobileHealthCheckHistByDate(Date fromDt, Date toDt, String fctryCd, String vrn, String mdn, Pageable pageable);

}
