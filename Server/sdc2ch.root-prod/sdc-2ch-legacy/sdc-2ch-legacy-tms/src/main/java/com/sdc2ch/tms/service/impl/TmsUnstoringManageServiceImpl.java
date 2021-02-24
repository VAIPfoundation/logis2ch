package com.sdc2ch.tms.service.impl;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sdc2ch.tms.config.TmsQueryBuilder;
import com.sdc2ch.tms.service.ITmsUnstoringManageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class TmsUnstoringManageServiceImpl implements ITmsUnstoringManageService {

	
	@Autowired TmsQueryBuilder builder;

	@Override
	@Transactional
	public int UpdateDelayResnById(Long rowId, String delayResn) {
		Object[] param = {rowId, delayResn};
		Object[] result = builder.storedProcedureSingleResultCall("[DBO].[SP_2CH_ORDER_SIL_DELAY_RESN_UPDATE]", param);
		return 1;
	}

}
