package com.sdc2ch.agent.service.impl;

import java.util.Collections;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sdc2ch.agent.service.IHealthCheckService;
import com.sdc2ch.agent.service.vo.HealthCheckResVo;

@Service
public class HealthCheckServiceImpl implements IHealthCheckService {

	@Value(value = "${api.healthCheck.readTimeout:20000}")
	int readTimeout;
	
	 private RestTemplate restTemplate;

	 @PostConstruct
	private void init() {
		 this.restTemplate = new RestTemplateBuilder().setReadTimeout(readTimeout).build();
	}

	@Override
	public HealthCheckResVo chkHealthCheck(String url, String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.set("Authorization", token);
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		HealthCheckResVo res = restTemplate.postForObject(url, entity, HealthCheckResVo.class);
		return res;
	}

}
