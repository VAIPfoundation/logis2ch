package com.sdc2ch.service.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base32;
import org.springframework.util.Assert;

public class OneTimePasswordGenerator {
	private static final long T_ZERO = 0L;

	private static final long T_ONE = 60000L * 3;

	private static final String ALG = "HmacSHA1";

	public String generatePassword(String key) throws NoSuchAlgorithmException, InvalidKeyException {
		Assert.hasText(key, "");

		
		long now = System.currentTimeMillis();
		long c = (now - T_ZERO) / T_ONE;

		
		byte[] cbytes = new byte[8];
		for (int i = 7; i >= 0; i--) {
			cbytes[i] = (byte) c;
			c >>>= 8;
		}
		
		byte[] k = getKeyBytes(key);

		SecretKeySpec signer = new SecretKeySpec(k, ALG);
		Mac mac = Mac.getInstance(ALG);
		mac.init(signer);

		byte[] h = mac.doFinal(cbytes);

		
		int o = h[h.length - 1] & 0x0F;

		
		long i = 0;
		for (int idx = 0; idx < 4; ++idx) {
			i <<= 8;
			i |= (h[o + idx] & 0xFF);
		}

		i &= 0x7FFFFFFF;

		
		i %= 1000000;
		return String.format("%06d", i);
	}

	private byte[] getKeyBytes(String key) {
		StringBuilder trimmed = new StringBuilder();
		for (int i = 0; i < key.length(); ++i) {
			char c = key.charAt(i);
			if (!Character.isWhitespace(c)) {
				trimmed.append(c);
			}
		}

		while ((trimmed.length() % 8) > 0) {
			trimmed.append('=');
		}

		String base32 = trimmed.toString().toUpperCase(Locale.ENGLISH);
		Base32 decoder = new Base32();

		return decoder.decode(base32);
	}
	
	
	public String OTP(String key) {
		try {
			return generatePassword(key);
		} catch (InvalidKeyException | NoSuchAlgorithmException e) {
			
			e.printStackTrace();
		}
		return "";
	}
}
