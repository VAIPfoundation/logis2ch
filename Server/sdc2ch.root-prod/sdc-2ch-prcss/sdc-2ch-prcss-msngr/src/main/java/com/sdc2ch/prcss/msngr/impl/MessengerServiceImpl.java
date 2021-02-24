package com.sdc2ch.prcss.msngr.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.sdc2ch.prcss.msngr.IMessengerService;
import com.sdc2ch.repo.builder.IAdmQueryBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MessengerServiceImpl implements IMessengerService {

	@Autowired IAdmQueryBuilder builder;

	
	
	@Override
	public void sendMessengerMsg(String trnmisUserId, String trnsmisUserNm, List<String> rcverUserIds, String title, String body) {
		if(rcverUserIds != null && !rcverUserIds.isEmpty()) {
			for(String id : rcverUserIds) {
				sendMessengerMsg(trnmisUserId, trnsmisUserNm, id, title, body);
			}
		}
	}


	@Override
	public void sendMessengerMsg(String trnmisUserId, String trnsmisUserNm, String rcverUserId, String title, String body) {
		Assert.notNull(trnmisUserId, "trnmisUserId is must not be null");
		Assert.notNull(trnsmisUserNm,  "trnsmisUserNm is must not be null");
		Assert.notNull(rcverUserId,  "rcverUserId is must not be null");
		Assert.notNull(title,  "title is must not be null");
		Assert.notNull(body,  "body is must not be null");
		Object[] params = { trnmisUserId, trnsmisUserNm, rcverUserId, title, body};
		try {
			builder.storedProcedureCall(REG_MSNGR_MSG, params);
			if(log.isDebugEnabled())
				log.debug("sendMessengerMsg:" + rcverUserId +", " + title + ", " + body );
		}catch(Exception e) {
			log.error("{}", e);
		}
	}
	
}
