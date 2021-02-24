package com.sdc2ch.service.common.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sdc2ch.repo.builder.IAdmQueryBuilder;
import com.sdc2ch.repo.io.TmsDriverIO;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.pubsub.I2ChEventConsumer;
import com.sdc2ch.require.pubsub.I2ChEventManager;
import com.sdc2ch.tms.event.ISmsEvent;
import com.sdc2ch.tms.event.ISmsEvent.SmsType;

@Component
public class SendSmsComponemt {
	
	private static final String SP_NAME = "[TMS].[DBO].[SP_EXE_SMS_ACTION]";
	@Autowired IAdmQueryBuilder builder;
	
	I2ChEventConsumer<ISmsEvent> subscriber;
	
	@Autowired
	public void onLoad(I2ChEventManager manager) {
		subscriber = manager.subscribe(ISmsEvent.class);
		subscriber.filter(e -> {
			if(e instanceof ISmsEvent) {
				try {
					ISmsEvent event = (ISmsEvent) e;
					
					if(SmsType.OTP != event.getSmsTy()) {
						IUser user = e.user();
						String factryCd = "";
						if(user != null) {
							if(user.getUserDetails() instanceof TmsDriverIO) {
								factryCd = ((TmsDriverIO)user.getUserDetails()).getFctryCd();
							}
						}

						Object[] params = {"", "", "", "", "1833-7713", "", "", event.getMobileNo(), event.getContents(), event.getSender(), ""};

						builder.storedProcedureCall(SP_NAME, params);
					}

				}catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	}
}
