package com.sdc2ch.core.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import com.google.common.hash.Hashing;

public class HashUtils {

	
	public static String encript(String base) {
		
		String SHA256 = "";
        try{
        	 
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();
 
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
 
            
            SHA256 = hexString.toString();
 
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
        
        return SHA256;
	}
	
	public static String sha256(String enc) {
		String sha256hex = Hashing.sha256()
				.hashString(enc, StandardCharsets.UTF_8)
				.toString();
		return sha256hex;
	}
	public static String md5(String enc) {
		String md5 = Hashing.md5()
				.hashString(enc, StandardCharsets.UTF_8)
				.toString();
		return md5;
	}
}
