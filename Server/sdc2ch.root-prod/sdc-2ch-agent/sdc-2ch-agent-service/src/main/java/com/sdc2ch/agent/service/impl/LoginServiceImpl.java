package com.sdc2ch.agent.service.impl;

import java.util.Collections;

import javax.annotation.PostConstruct;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sdc2ch.agent.service.ILoginService;
import com.sdc2ch.agent.service.vo.LoginReqVo;
import com.sdc2ch.agent.service.vo.LoginResVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LoginServiceImpl implements ILoginService{
	 private RestTemplate restTemplate;

	 @PostConstruct
	private void init() {
			this.restTemplate = new RestTemplateBuilder().setReadTimeout(30000).build();
	}

	@Override
	public String getToken(String url, String userName, String passWord) {
		
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.set("X-Requested-With", "XMLHttpRequest");
		
		
		HttpEntity<LoginReqVo> entity = new HttpEntity<LoginReqVo>(new LoginReqVo(userName,passWord), headers);
		
		LoginResVo res = restTemplate.postForObject(url, entity, LoginResVo.class);
		return res.getToken();
		
	
	
	}

}
