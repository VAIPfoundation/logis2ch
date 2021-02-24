package com.sdc2ch.service.admin;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sdc2ch.require.enums.SetupLcType;
import com.sdc2ch.web.admin.repo.domain.op.T_BCON_HIST;

public interface IBconHistService {

	Page<T_BCON_HIST> searchPageBconHist(Date fromDt, Date toDt, String fctryCd, String vrn, SetupLcType setupLc,Pageable pageable);

}
