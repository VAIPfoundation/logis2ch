package com.sdc2ch.web.admin.repo.init;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdc2ch.core.ISetupData;
import com.sdc2ch.web.admin.repo.dao.T_ToSRepository;
import com.sdc2ch.web.admin.repo.domain.T_TOS;
import com.sdc2ch.web.admin.repo.enums.ToSRegEnums;
import com.sdc2ch.web.admin.repo.init.impl.SetupDataImpl.INIT_ORDER;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AdminTosData implements ISetupData {

	private ObjectMapper mapper;
	private T_ToSRepository repo;
	
	@Autowired
	public void setAutowire(ObjectMapper mapper, T_ToSRepository repo) {
		this.mapper = mapper;
		this.repo = repo;
	}
	
	@SuppressWarnings({ "unchecked", "unlikely-arg-type" })
	@Override
	public void install() {
		ClassPathResource classPathResource = new ClassPathResource("data/TosData.json");
		ClassPathResource location = new ClassPathResource("data/Location.txt");
		ClassPathResource prv = new ClassPathResource("data/Private.txt");
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(location.getInputStream()));
			BufferedReader reader2 = new BufferedReader(new InputStreamReader(prv.getInputStream()));
			
			String s;
			StringBuffer sb = new StringBuffer();
			while((s = reader.readLine()) != null) {
				sb.append(s);
			}
			
			StringBuffer sb2 = new StringBuffer();
			while((s = reader2.readLine()) != null) {
				sb2.append(s);
			}
			
			repo.findById(1L).orElseGet(() -> {
				try {
					mapper.readValue(classPathResource.getURL(), List.class).forEach( d -> {
						T_TOS TOS = mapper.convertValue(d, T_TOS.class);
						String contents = null;
						if(ToSRegEnums.PRIVATE == TOS.getRegType()) {
							try {
								contents = mapper.writeValueAsString(sb.toString());
							} catch (JsonProcessingException e) {
								
								e.printStackTrace();
							}
							TOS.setContents(contents);
							
						}else {
							
							try {
								contents = mapper.writeValueAsString(sb2.toString());
							} catch (JsonProcessingException e) {
								
								e.printStackTrace();
							}
							TOS.setContents(contents);
						}
						repo.save(TOS);
					});
				} catch (Exception e) {
					log.warn("{}", e.getMessage());
				}
				return null;
			});
			
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
		
	}

	@Override
	public int order() {
		return INIT_ORDER.SIX.ordinal();
	}

}
