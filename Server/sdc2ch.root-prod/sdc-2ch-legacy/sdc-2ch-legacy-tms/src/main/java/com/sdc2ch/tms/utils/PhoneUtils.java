package com.sdc2ch.tms.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneUtils {

	public static boolean validatePhoneNumber(String phoneNo) {
		
		if (phoneNo.matches("\\d{10}"))
			return true;
		
		else if (phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}"))
			return true;
		
		else if (phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}"))
			return true;
		
		else if (phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}"))
			return true;
		
		else
			return false;
	}

	public static boolean validatePhoneNumber2(String phoneNo) {
		return phoneNo.matches("(01[016789])-(\\d{3,4})-(\\d{4})");
	}

	
	public static String formateToPhoneNumber(String number, String format, int maxLength) {
		String onlyDidgits = number.replaceAll("\\D+", "");
		if (onlyDidgits.length() > maxLength) {
			
			onlyDidgits = onlyDidgits.substring(0, maxLength);
		}

		char[] arr = new char[format.length()];
		int i = 0;
		for (int j = 0; j < format.length(); j++) {
			if (i >= onlyDidgits.length())
				break;
			if (format.charAt(j) == 'X')
				arr[j] = onlyDidgits.charAt(i++);
			else
				arr[j] = format.charAt(j);
		}
		String formatedNo = new String(arr);

		Pattern p = Pattern.compile("[0-9]");
		Matcher m = p.matcher("" + (formatedNo.charAt(formatedNo.length() - 1)));

		
		if (!m.matches()) {
			
			formatedNo = new StringBuilder(formatedNo).deleteCharAt(formatedNo.length() - 1).toString();
		}

		return formatedNo;
	}
}
