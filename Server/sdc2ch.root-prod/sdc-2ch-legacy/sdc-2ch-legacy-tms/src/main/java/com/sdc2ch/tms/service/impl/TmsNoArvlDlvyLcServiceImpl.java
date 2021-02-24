package com.sdc2ch.tms.service.impl;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sdc2ch.tms.config.TmsQueryBuilder;
import com.sdc2ch.tms.service.ITmsNoArvlDlvyLcService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class TmsNoArvlDlvyLcServiceImpl implements ITmsNoArvlDlvyLcService {

	
	@Autowired TmsQueryBuilder builder;

	@Override
	@Transactional
	public int UpdateStopLocation(String dlvyLcCd, String adres, String lat, String lng) {


		System.out.println("dlvyLcCd " + dlvyLcCd);
		System.out.println("adres " + adres);
		System.out.println("lat " + lat);
		System.out.println("lng " + lng);
		Object[] param = {dlvyLcCd, adres, lat, lng};
		Object[] result = builder.storedProcedureSingleResultCall("[DBO].[SP_2CH_ANALS_NO_ARVL_DLVY_LC_UPDATE]", param);
		return 1;
	}

}
