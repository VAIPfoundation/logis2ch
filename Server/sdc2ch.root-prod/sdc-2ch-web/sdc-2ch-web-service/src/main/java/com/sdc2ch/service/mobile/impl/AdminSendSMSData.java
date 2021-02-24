package com.sdc2ch.service.mobile.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.sdc2ch.core.ISetupData;
import com.sdc2ch.require.pubsub.I2ChEventManager;
import com.sdc2ch.require.pubsub.I2ChEventPublisher;
import com.sdc2ch.tms.event.ISmsEvent;
import com.sdc2ch.web.admin.repo.dao.T_NfcMappingRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AdminSendSMSData implements ISetupData {

	@Autowired T_NfcMappingRepository nfcRepo;
	
	private I2ChEventPublisher<ISmsEvent> smsPub;
	
	@Autowired
	private void init(I2ChEventManager manager) {
		
		smsPub = manager.regist(ISmsEvent.class);
		
	}
	
	@Override
	public void install() {

		try {
			ClassPathResource classPathResource = new ClassPathResource("data/sendSms");
			BufferedReader reader = new BufferedReader(new InputStreamReader(classPathResource.getInputStream()));
			String s;
			List<String> list = new ArrayList<>();
			while((s = reader.readLine()) != null) {
				String prefix = s.length() == 8 ? "010" : "011";
				list.add(prefix + s);
			}
			

			int i = 0;
			for(String sms : list) {
				













			}

			
			
		}catch (IOException e) {
			log.error("{}", e);
		}
	}

	@Override
	public int order() {
		return 0;
	}
}
