package com.sdc2ch.api.util;

public class ArsUtil {
	
	public static String convertName(String dlvyLcNm) {
		
		return dlvyLcNm
				.replace("(", " ")
				.replace(")", " ")
				.replace("-", " ");
	}
	
}
