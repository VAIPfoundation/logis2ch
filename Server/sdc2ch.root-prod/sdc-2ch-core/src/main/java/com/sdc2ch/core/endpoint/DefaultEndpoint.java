package com.sdc2ch.core.endpoint;

import static com.sdc2ch.core.expression.InstanceOf.when;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.of;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.sdc2ch.core.error.exception.CustomBadRequestException;
import com.sdc2ch.core.security.auth.I2CHAuthorization;
import com.sdc2ch.core.security.auth.I2CHUserContext;
import com.sdc2ch.core.security.auth.error.ErrorCode;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.service.I2ChUserService;

public class DefaultEndpoint {

	
	protected static final String SUPPORTED_TYPE = "application/json";
	
	@Autowired
	protected I2CHAuthorization authorization;
	
	@Autowired
	protected I2ChUserService userSvc;

	protected Optional<IUser> getConCurrentUser() {
		return userSvc.findByUsername(authorization.userContext().getUsername());
	}
	protected I2CHUserContext getConCurrentUserContext() {
		return authorization.userContext();
	}
	
	protected GrantedAuthority currentUserGrantedAuthority() {
		return authorization.userContext().getAuthorities().stream().findFirst().orElse(null);
	}
	
	
	protected void verifyData(BindingResult bindingResult) throws CustomBadRequestException {
			
		if(bindingResult.hasErrors()){
	     	String error = when(
	     			bindingResult.getAllErrors().stream().findFirst().get())
	     			.instanceOf(FieldError.class)
	     			.then(f -> of(f.getField(), f.getRejectedValue() == null ?  "" : f.getRejectedValue().toString(), f.getDefaultMessage())
	     					.collect(joining(" -> ", "[","]"))).otherwise("[]");
	     	throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, error);
		}
	}
	
	
	protected void throwException(HttpStatus status, ErrorCode code, String error) throws CustomBadRequestException {
		throw new CustomBadRequestException(status, code, error);
	}
	
	protected String join(Object ... args) {
		return Stream.of(args).map(a -> (a == null ? "" : a.toString())).collect(Collectors.joining(",", "[", "]"));
	}
}
