package com.sdc2ch.agent.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationPid;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.querydsl.core.types.Projections;
import com.sdc2ch.agent.service.IHealthCheckWatchService;
import com.sdc2ch.agent.service.vo.DetailTreeResVo;
import com.sdc2ch.agent.service.vo.LoginReqVo;
import com.sdc2ch.agent.service.vo.LoginResVo;
import com.sdc2ch.web.admin.repo.AdmQueryBuilder;

import lombok.extern.slf4j.Slf4j;

import com.sdc2ch.prcss.ds.repo.domain.QT_ROUTE_STATE;
import com.sdc2ch.prcss.ds.repo.domain.T_ROUTE_STATE;

@Slf4j
@Service
public class HealthCheckWatchServiceImpl implements IHealthCheckWatchService {

	@Value(value = "${api.healthCheck.watch.readTimeout:10000}")
	int readTimeout;
	
	@Value(value = "${application.pid.2chweb.agent:/etc/smilk2ch/2chweb.agent.pid}")
	String pidFilePath; 
	
	
	private RestTemplate restTemplate;

	@PostConstruct
	private void init() {
		 this.restTemplate = new RestTemplateBuilder().setReadTimeout(readTimeout).build();
		 






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
	@Override
	public DetailTreeResVo chkHealthCheck(String url, String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.set("Authorization", token);
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		DetailTreeResVo res = restTemplate.postForObject(url, entity, DetailTreeResVo.class);
		return res;
	}
	
	
	@Autowired AdmQueryBuilder builder;

	
	@Override
	public T_ROUTE_STATE selectLastDriverCd(String DLVY_DE) {

		QT_ROUTE_STATE tbl = QT_ROUTE_STATE.t_ROUTE_STATE;

		T_ROUTE_STATE rs=null;
		List<T_ROUTE_STATE> rsList = builder
				.create()
					.select(
						Projections.fields(
								T_ROUTE_STATE.class,
								tbl.id.dlvyDe,tbl.id.routeNo,tbl.id.vrn,
								tbl.driverCd,tbl.updateDt
						)
					)
					.from(tbl)
					.where(tbl.id.dlvyDe.eq(DLVY_DE))
					.orderBy(tbl.updateDt.desc())
				.fetch();
		if(rsList!=null && rsList.size()>0) {
			rs = rsList.get(0);
		}
		return rs;
	}
	
	String CHK_ERP = "[dbo].[SP_2CH_EB_PRM_HEALTH_CHK]";
	@Override
	public boolean checkErp() {
		Object[] params = {  };
		Integer result = 0;
		try {
			List<Object[]> resultList = builder.storedProcedureResultCall(CHK_ERP, params);
			if ( resultList != null && resultList.size() > 0) {
				Object resultOne = resultList.get(0);

				result = Integer.valueOf(resultOne.toString());
				if(result==0)
					return false;
			}
		}catch(Exception e) {
			log.error("{}", e);
			return false;
		}

		return true;
	}
}
