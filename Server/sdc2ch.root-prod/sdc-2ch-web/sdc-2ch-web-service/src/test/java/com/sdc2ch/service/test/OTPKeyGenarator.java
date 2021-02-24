package com.sdc2ch.service.test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.sdc2ch.service.util.OneTimePasswordGenerator;

public class OTPKeyGenarator {
	
	public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, InterruptedException {
		
		OneTimePasswordGenerator gen = new OneTimePasswordGenerator();
		
		
		List<String> key = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString());
		
		for(int i = 0 ; i <= 100 ; i++) {
			System.out.print(i+1 + " > ");
			System.out.println(key.stream().map(s -> gen.OTP(s)).collect(Collectors.toSet()));

			

			Thread.sleep(10000);
		}
		
		
	}

}
