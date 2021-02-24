package com.sdc2ch.service.admin;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sdc2ch.web.admin.repo.domain.op.T_LOCATION_INFO_HIST;

public interface ILocationHistService {

	List<T_LOCATION_INFO_HIST> searchLocationHistByDate(Date fromDe, Date toDe, String fctryCd, String vrn, Boolean gpsYn);

	Page<T_LOCATION_INFO_HIST> searchPageLocationHistByDate(Date fromDt, Date toDt, String fctryCd, String vrn,
			Boolean gpsYn, Pageable pageable);
}
