package com.sdc2ch.tms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdc2ch.tms.config.TmsQueryBuilder;
import com.sdc2ch.tms.service.ITmsSmsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TmsSmsService implements ITmsSmsService{

	@Autowired
	TmsQueryBuilder builder;

	@Override
	public boolean sendSms(String userName, String msg, String targetMdn) {
		try {
			Object[] params = { "", "", "", "", "1833-7713", "", "", targetMdn, msg, userName, "" };
			builder.storedProcedureCall(PROC_SEND_SMS, params);
			
			if(log.isDebugEnabled())
				log.debug("sendSms: Fr:" +userName+", To:"+ targetMdn +", " + msg );

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("sendSms2: {}", e);
			return false;
		}
	}
	
	@Override
	public boolean sendSms(boolean enable, String userName, String msg, List<String> mdnList) {
		try {
			if (enable) {
				mdnList.stream().forEach(mdn -> {
					if (mdn != null && mdn.length() > 3 && mdn.charAt(0) != '#') 
						sendSms(userName, msg, mdn);
				});
				return true;
			}
			return false;
		} catch (Exception e) {
			log.info("sendSms1: {}", e);
			return false;
		}
	}

}
