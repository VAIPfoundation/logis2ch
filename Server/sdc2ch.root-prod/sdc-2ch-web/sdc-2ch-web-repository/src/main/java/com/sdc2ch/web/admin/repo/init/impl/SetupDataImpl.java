package com.sdc2ch.web.admin.repo.init.impl;

import static java.util.Optional.ofNullable;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sdc2ch.core.ISetupData;

@Component
public class SetupDataImpl {
	
	@Autowired(required = false) private List<ISetupData> supDatas;
	
	@PostConstruct
	public void setUp() {
		ofNullable(supDatas).ifPresent(d -> {
			d.sort((s1, s2) -> Integer.compare(s1.order(), s2.order()));
			d.forEach(s -> s.install());
		});
	}
	
	public enum INIT_ORDER {
		ONE,TWO,THREE,FOUR,FIVE,SIX,SEVEN,EIGHT
	}

}
