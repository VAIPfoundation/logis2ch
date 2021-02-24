package com.sdc2ch.service.mobile.impl;

import org.springframework.stereotype.Service;

import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.service.mobile.IMobilePushService;

@Service
class MobilePushServiceImpl implements IMobilePushService {

	@Override
	public boolean isPushTkn(IUser user) {
		
		return false;
	}

}
