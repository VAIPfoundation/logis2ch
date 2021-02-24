package com.sdc2ch.nfc.test;

import java.util.Base64;
import java.util.Date;

public class HintTest {

	public static void main(String[] args) {
		String str = "152947022005400922610000000188";
		str = "152947020905400922610000000187";
		
		
		Long time = Long.decode(str.substring(0, 13));
		
		
		System.out.println(new Date(time));
		
		
		System.out.println(new String(Base64.getDecoder().decode("96VK8e9xmlBBM90+65EfSjd4jpicrkRTbK2meRb2Ifk=")));
	}
}
