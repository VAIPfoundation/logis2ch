package com.sdc2ch.core.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


public class ObjectMapperBuilderUtils {
	
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private ObjectMapperBuilderUtils() {
		mapper = new ObjectMapper();
		setMapperConfig(SerializationFeature.INDENT_OUTPUT);
	}
	
	public void setMapperConfig(SerializationFeature feature) {
		setMapperConfig(feature, df);
	}
	public void setMapperConfig(SerializationFeature feature, DateFormat df) {
		mapper.setDateFormat(df);
		mapper.enable(feature);
	}
	
	private static ObjectMapperBuilderUtils builder = new ObjectMapperBuilderUtils();
	
	
	private ObjectMapper mapper;
	
	
	public static ObjectMapper getInstance() {
		return builder.mapper;
	}

}
