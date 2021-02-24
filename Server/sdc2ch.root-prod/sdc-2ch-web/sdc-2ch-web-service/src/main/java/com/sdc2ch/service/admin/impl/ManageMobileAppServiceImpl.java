package com.sdc2ch.service.admin.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.sdc2ch.service.admin.IManageMobileAppService;
import com.sdc2ch.web.admin.repo.AdmQueryBuilder;
import com.sdc2ch.web.admin.repo.domain.v.QV_MOBILE_APP_USE_INFO;
import com.sdc2ch.web.admin.repo.domain.v.QV_TOS_HIST;
import com.sdc2ch.web.admin.repo.domain.v.V_MOBILE_APP_USE_INFO;
import com.sdc2ch.web.admin.repo.domain.v.V_TOS_HIST;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
class ManageMobileAppServiceImpl implements IManageMobileAppService {

	
	@Autowired AdmQueryBuilder builder;

	@Override
	public List<V_MOBILE_APP_USE_INFO> searchMobileApp(String fctryCd) {
		return searchMobileApp(fctryCd, null, null, null, null);
	}

	
	@Override
	public List<V_MOBILE_APP_USE_INFO> searchMobileApp(String fctryCd, String vhcleTy, String vrn, String mobileNo, String driverNm) {
		log.info("fctryCd={}, vhcleTy={}, vrn={}, mobileNo={}, driverNm={}", fctryCd, vhcleTy, vrn, mobileNo);
		QV_MOBILE_APP_USE_INFO code = QV_MOBILE_APP_USE_INFO.v_MOBILE_APP_USE_INFO;

		BooleanBuilder where = new BooleanBuilder();
		where.and(code.fctryCd.eq(fctryCd));
		where.and(code.vrn.notLike("물류%"));

		if(!StringUtils.isEmpty(vhcleTy)) {
			where.and(code.vhcleTy.eq(vhcleTy));
		}
		if(!StringUtils.isEmpty(vrn)) {
			where.and(code.vrn.like("%" + vrn + "%"));
		}
		if(!StringUtils.isEmpty(driverNm)) {
			where.and(code.driverNm.like("%" + driverNm + "%"));
		}
		if(!StringUtils.isEmpty(mobileNo)) {
			where.and(code.mobileNo.like("%" + mobileNo + "%"));
		}

		return builder
				.create()
					.select(
						Projections.fields(
							V_MOBILE_APP_USE_INFO.class,
							code.id, code.fctryCd, code.fctryNm, code.vhcleTy, code.vrn,
							code.driverCd, code.driverNm, code.mobileNo, code.mobileModel, code.mobileOsName,
							code.mobileOsVer, code.mobileTelco, code.apkVer, code.stplatAgreDt
						)
					)
					.from(code)
					.where(where)
					.orderBy(code.vrn.asc())
				.fetch();
	}

	
	@Override
	public List<V_TOS_HIST> searchMobileAppHist(String vrn) {
		log.info("vrn={}", vrn);
		QV_TOS_HIST code = QV_TOS_HIST.v_TOS_HIST;

		return builder
				.create()
					.select(
						Projections.fields(
							V_TOS_HIST.class,
							code.createDt, code.tosVerMajor, code.tosVerMinor, code.tosRegType
						)
					)
					.from(code)
					.where(code.vrn.eq(vrn))
					.orderBy(code.createDt.desc())
				.fetch();
	}

}
