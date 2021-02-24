package com.sdc2ch.service.util;

import java.text.DateFormat;

import com.google.gson.GsonBuilder;

public class Utils {
	
	public static String toJsonString(Object pojo) {
		return new GsonBuilder()

			     .enableComplexMapKeySerialization()
			     .serializeNulls()
			     .setDateFormat(DateFormat.LONG)

			     .excludeFieldsWithoutExposeAnnotation()
			     .setPrettyPrinting()
			     .setVersion(1.0)
			     .create().toJson(pojo);
	}

}
