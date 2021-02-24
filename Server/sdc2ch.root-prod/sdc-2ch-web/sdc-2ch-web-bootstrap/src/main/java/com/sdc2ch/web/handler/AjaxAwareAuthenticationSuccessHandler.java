package com.sdc2ch.web.handler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdc2ch.token.jwt.domain.JwtTokenImpl;
import com.sdc2ch.token.jwt.service.IJwtTokenService;
import com.sdc2ch.web.admin.endpoint.AdminMenuEndpoint;
import com.sdc2ch.web.admin.endpoint.AdminUserEndpoint;


@Component
public class AjaxAwareAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	@Autowired private IJwtTokenService tokenSvc;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		JwtTokenImpl tkn = tokenSvc.createToken(authentication);
		ObjectMapper m = new ObjectMapper();
		m.setSerializationInclusion(Include.NON_NULL);
		


		
		Link me = linkTo(AdminUserEndpoint.class).slash("/me").withRel("me");
		Link menu = linkTo(AdminMenuEndpoint.class).slash("/list").withRel("menu");
		Link otken = linkTo(AdminUserEndpoint.class).slash("/refresh/authToken").withRel("token");
		
		
		

		
		
		Resource<JwtTokenImpl> reses = new Resource<>(tkn, me, menu, otken);

		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		m.writeValue(response.getWriter(), reses);

		clearAuthenticationAttributes(request);
	}

	
	protected final void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);

		if (session == null) {
			return;
		}
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}
}
