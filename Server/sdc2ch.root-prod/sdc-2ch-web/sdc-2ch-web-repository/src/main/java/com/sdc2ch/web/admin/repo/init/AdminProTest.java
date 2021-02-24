package com.sdc2ch.web.admin.repo.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sdc2ch.core.ISetupData;
import com.sdc2ch.web.admin.repo.AdmQueryBuilder;
import com.sdc2ch.web.admin.repo.init.impl.SetupDataImpl.INIT_ORDER;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AdminProTest implements ISetupData {

	@Autowired AdmQueryBuilder builder; 
	
	
	@Override
	public void install() {
		












		
	}

	@Override
	public int order() {
		return INIT_ORDER.TWO.ordinal();
	}

}
