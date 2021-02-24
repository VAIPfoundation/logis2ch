package com.sdc2ch.web.admin.endpoint;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdc2ch.core.error.exception.CustomTokenExpiredException;
import com.sdc2ch.core.security.auth.I2CHUserContext;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.token.config.JwtSettings;
import com.sdc2ch.token.domain.Me;
import com.sdc2ch.token.jwt.domain.JwtToken;
import com.sdc2ch.token.jwt.domain.JwtTokenImpl;
import com.sdc2ch.token.jwt.service.IJwtTokenService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/adm/user")
public class AdminUserEndpoint extends AdminAbstractEndpoint {

	private IJwtTokenService tokenSvc;
	
	public AdminUserEndpoint() {
		log.info("{}", this);
	}
	
	@Autowired
	public void setTokenService(IJwtTokenService tokenSvc) {
		this.tokenSvc = tokenSvc;
	}

	@GetMapping(value = "/me", produces = { MediaType.APPLICATION_JSON_VALUE })
	@PreAuthorize("hasAuthority('USER')")
	public @ResponseBody Resource<Me> get() {
		I2CHUserContext context = authorization.userContext();
		Me me = Me.of(context.getUsername(), context.getAuthorities());
		me.add(linkTo(AdminUserEndpoint.class).slash("/details").withRel("details"));
		return new Resource<>(me, linkTo(AdminUserEndpoint.class).slash("me").withSelfRel());
	}

	@GetMapping(value = "/refresh/authToken", produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody JwtToken refreshToken(HttpServletRequest request, HttpServletResponse response) throws CustomTokenExpiredException {
		
		String tk = request.getHeader(JwtSettings.AUTHENTICATION_HEADER_NAME);
		
		return tokenSvc.refreshToken(tk);
		



		

	}

	@GetMapping(value = "/login/token", produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody void loginToken(HttpServletRequest request, HttpServletResponse response) throws CustomTokenExpiredException, IOException {

		I2CHUserContext context = super.getConCurrentUserContext();
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(context, null, context.getAuthorities());
		JwtTokenImpl tkn = tokenSvc.createToken(token);
		ObjectMapper m = new ObjectMapper();
		m.setSerializationInclusion(Include.NON_NULL);

		Link me = linkTo(AdminUserEndpoint.class).slash("/me").withRel("me");
		Link menu = linkTo(AdminMenuEndpoint.class).slash("/list").withRel("menu");
		Link otken = linkTo(AdminUserEndpoint.class).slash("/refresh/authToken").withRel("token");

		Resource<JwtTokenImpl> reses = new Resource<>(tkn, me, menu, otken);

		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		m.writeValue(response.getWriter(), reses);
		
	}

	
	
	
	
	
	
	
	
	









}
