package com.sdc2ch.core.security.auth.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sdc2ch.core.security.auth.error.ErrorCode;

 
@RestController
class CommonErrorEndpoint {
	
	@RequestMapping(value = "/common/error", method = {RequestMethod.GET, RequestMethod.POST})
	public List<Map<String, Object>> errors() {
		return Arrays.asList(ErrorCode.values()).stream().map(c ->{
			Map<String, Object> error = new HashMap<>();
			error.put("Code", c.getErrorCode());
			error.put("Cause", c.getMessage());
			error.put("Error", c.name());
			return error;
		}).collect(Collectors.toList());
	}
}
