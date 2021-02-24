package com.sdc2ch.agent.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
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


@Component
public class AjaxAwareAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	@Autowired private IJwtTokenService tokenSvc;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		JwtTokenImpl tkn = tokenSvc.createToken(authentication);
		ObjectMapper m = new ObjectMapper();
		m.setSerializationInclusion(Include.NON_NULL);
		


		




		
		

		
		
		Resource<JwtTokenImpl> reses = new Resource<>(tkn);

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
