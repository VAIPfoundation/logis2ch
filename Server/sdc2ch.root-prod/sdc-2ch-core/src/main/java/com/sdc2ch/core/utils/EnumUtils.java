package com.sdc2ch.core.utils;

import java.util.Arrays;

public class EnumUtils {
	
	public static String[] enumToStringNames(Class<? extends Enum<?>> e) {
	    return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
	}

}
