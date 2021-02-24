package com.sdc2ch.web.admin.endpoint;

import java.text.SimpleDateFormat;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sdc2ch.core.endpoint.DefaultEndpoint;
import com.sdc2ch.web.admin.repo.enums.RoleEnums;

@RequestMapping("/adm")
public abstract class AdminAbstractEndpoint extends DefaultEndpoint {
	
	protected RoleEnums currentRoleByUser() {
		return RoleEnums.valueOf(currentUserGrantedAuthority().getAuthority());
	}
	
	
	protected SimpleDateFormat getDateFormat(String fmt) {
		return new SimpleDateFormat(fmt);
	}


	public String convertDate(String date) {
		if(!StringUtils.isEmpty(date)) {
			date = date.replaceAll("-", "");
		}
		return date;
	}
}
