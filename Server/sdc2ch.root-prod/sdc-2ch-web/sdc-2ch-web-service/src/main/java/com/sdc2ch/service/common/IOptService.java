package com.sdc2ch.service.common;

import java.util.concurrent.ExecutionException;

 
public interface IOptService {
	
	
	String generateOtp(String phoneNo) throws ExecutionException;

	
	boolean isExpired(String phoneNm, String OTP) throws ExecutionException;
}
