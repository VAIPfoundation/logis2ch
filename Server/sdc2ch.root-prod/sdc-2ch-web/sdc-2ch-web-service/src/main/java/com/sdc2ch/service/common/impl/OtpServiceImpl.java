package com.sdc2ch.service.common.impl;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.sdc2ch.core.security.auth.I2CHAuthorization;
import com.sdc2ch.require.cache.CacheMenager;
import com.sdc2ch.require.cache.CacheMenager.Future;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.pubsub.I2ChEventManager;
import com.sdc2ch.require.pubsub.I2ChEventPublisher;
import com.sdc2ch.require.service.I2ChUserService;
import com.sdc2ch.service.common.IOptService;
import com.sdc2ch.service.event.model.AppSmsEvent;
import com.sdc2ch.service.util.OneTimePasswordGenerator;
import com.sdc2ch.tms.event.ISmsEvent;
import com.sdc2ch.tms.event.ISmsEvent.SmsType;

@Service
class OtpServiceImpl implements IOptService {

	private static final Logger logger = LoggerFactory.getLogger(OtpServiceImpl.class);

	@Autowired I2CHAuthorization auth;
	@Autowired I2ChUserService userSvc;           
	
	
	@Value("${sdc.2ch.otp.expired.min}")
	private int optExpiredMin;
	private OneTimePasswordGenerator gen = new OneTimePasswordGenerator();

	private LoadingCache<String, String> otpCache;
	private I2ChEventPublisher<ISmsEvent> smsPub;
	
	private static final String MESSAGE = "[서울우유2CH인증센터] SMS인증 번호 안내입니다. 인증번호[%s]";
	
	
	@Autowired
	public void onLoad(I2ChEventManager manager) {
		otpCache = CacheMenager.<String, String>builder().expiredTime(optExpiredMin).timeUnit(TimeUnit.MINUTES)
				.maxSize(1000).future(new Future<String, String>() {
					@Override
					public String get(final String key) throws Exception {
						return gen.OTP(randomKey());
					}
				}).build().reloadCache();
		smsPub = manager.regist(ISmsEvent.class);
	}

	@Override
	public String generateOtp(String phoneNm) throws ExecutionException {
		IUser user = userSvc.findByUsername(auth.userContext().getUsername()).orElse(null);
		String otp = otpCache.get(phoneNm);
		

		smsPub.fireEvent(AppSmsEvent.builder()
				.contents(String.format(MESSAGE, otp))
				.sender(auth.userContext().getUsername())
				.mobileNo(phoneNm)
				.user(user)
				.senderTel(phoneNm)
				.smsTy(SmsType.OTP)
				.build());
		return otp;
	}

	@Override
	public boolean isExpired(String phoneNm, String OTP) throws ExecutionException {
		try {
			return otpCache.get(phoneNm).equals(OTP);
		} catch (ExecutionException | UncheckedExecutionException e) {
			throw e;
		} catch (Exception e) {
			logger.error("{}", e.getMessage());
		}
		return false;
	}

	private String randomKey() {
		return UUID.randomUUID().toString();
	}
	
	
	public static void main(String[] args) {
		System.out.println(String.format(MESSAGE, "12345"));
	}

}
