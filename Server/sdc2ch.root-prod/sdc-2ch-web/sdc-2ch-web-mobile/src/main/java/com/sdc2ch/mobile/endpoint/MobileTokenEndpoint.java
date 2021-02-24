package com.sdc2ch.mobile.endpoint;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sdc2ch.core.error.CommonErrorBuilder;
import com.sdc2ch.core.security.auth.error.ErrorCode;
import com.sdc2ch.mobile.MobileController;
import com.sdc2ch.mobile.enums.VerifyEnums;
import com.sdc2ch.mobile.res.Verify;
import com.sdc2ch.service.mobile.IMobileAppInfoService;
import com.sdc2ch.token.config.JwtSettings;
import com.sdc2ch.token.exceptions.InvalidJwtToken;
import com.sdc2ch.token.exceptions.InvalidJwtTokenTypeException;
import com.sdc2ch.token.jwt.domain.JwtToken;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(MobileController.REQUEST_MAPPING + "/user/refresh")
public class MobileTokenEndpoint extends MobileController {
	
	@Autowired IMobileAppInfoService appInfoSvc;

	@ApiOperation(value = "인증 토큰 갱신", response = Verify.class)
	@RequestMapping(value = "/authToken", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody JwtToken refreshToken(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, InvalidJwtToken {
		return jwtSvc.refreshToken(request.getHeader(JwtSettings.AUTHENTICATION_HEADER_NAME));
	}
	
	
	@ApiOperation(value = "모바일앱 토큰 갱신", response = Verify.class)
	@RequestMapping(value = "/appToken", method = RequestMethod.GET, produces = { 
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody Resource<Verify> refreshAppToken(@RequestParam String appToken) {

		Link link = linkTo(MobileTokenEndpoint.class).slash("appToken").withSelfRel();
		Verify v = new Verify();
		v.setType(VerifyEnums.APP_TOKEN);
		v.setName(VerifyEnums.APP_TOKEN.getNm());
		v.setVerify(true);
		
		super.getConCurrentUser().ifPresent(u -> {
			appInfoSvc.findDriverAppInfoById(u.getUsername()).ifPresent(i -> {
				i.setAppTkn(appToken);
				appInfoSvc.save(i);
			});
		});
		return new Resource<Verify>(v, link);
	}
	
	@ExceptionHandler(InvalidJwtTokenTypeException.class)
	public final void handleUserNotFoundException(InvalidJwtTokenTypeException ex, HttpServletResponse response) throws IOException {
		
		try {
			CommonErrorBuilder.of()
				.throwError(MobileController.REQUEST_MAPPING + "/user/refresh/authToken", HttpStatus.FORBIDDEN, ErrorCode.INVALID_TOKEN, "Invalid refresh Token Type checked the your token")
				.sendError(response);
		} catch (Exception e) {
			response.sendError(500, e.getMessage());
		}
	}
}
