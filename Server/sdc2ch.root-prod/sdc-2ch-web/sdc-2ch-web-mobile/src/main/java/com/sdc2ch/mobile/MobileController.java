package com.sdc2ch.mobile;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sdc2ch.core.endpoint.DefaultEndpoint;
import com.sdc2ch.token.config.JwtSettings;
import com.sdc2ch.token.exceptions.JwtTokenCreatedException;
import com.sdc2ch.token.jwt.service.IJwtTokenService;
import com.sdc2ch.web.admin.repo.enums.RoleEnums;

@RestController
@RequestMapping("/m")
public abstract class MobileController extends DefaultEndpoint {

	public static final String REQUEST_MAPPING = "/m";
	
	public static final String TOS_OTP_PATH = "/tos/otp";
	public static final String TOS_CURRENT_PATH = "/tos/current";
	public static final String TOS_SAVE_PATH = "/tos/save";
	private String DEFAULT_ACCESS_TOKEN;
	
	@Autowired 
	protected IJwtTokenService jwtSvc;

	@Autowired
	public void setDefaultToken() throws JwtTokenCreatedException {
		this.DEFAULT_ACCESS_TOKEN = jwtSvc.unExpiredToken(authorization.userContext()).getToken();
	}

	public String getDefaultAccessToken() {
		return DEFAULT_ACCESS_TOKEN;
	}
	
	protected boolean checkToken(HttpServletRequest request) {
		String tokenPayload = request.getHeader(JwtSettings.AUTHENTICATION_HEADER_NAME);
		return jwtSvc.checkExpiredToken(tokenPayload);
	}

	protected RoleEnums currentRoleByUser() {
		return RoleEnums.valueOf(currentUserGrantedAuthority().getAuthority());
	}
	
	public String convertDate(String date) {
		if(!StringUtils.isEmpty(date)) {
			date = date.replaceAll("-", "");
		}
		return date;
	}
	
	public String convertCd(String dlvyLcCd) {
		if(!StringUtils.isEmpty(dlvyLcCd) && dlvyLcCd.length() == 5) {
			dlvyLcCd += "000";
		}
		return dlvyLcCd;
	}
}
