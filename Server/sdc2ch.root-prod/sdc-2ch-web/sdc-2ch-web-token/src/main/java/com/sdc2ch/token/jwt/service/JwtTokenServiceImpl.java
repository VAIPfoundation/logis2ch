package com.sdc2ch.token.jwt.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sdc2ch.core.security.auth.I2CHAuthorization;
import com.sdc2ch.core.security.auth.I2CHUserContext;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.domain.IUserRole;
import com.sdc2ch.require.service.I2ChUserDetailsService;
import com.sdc2ch.require.service.I2ChUserService;
import com.sdc2ch.token.JwtAuthenticationToken;
import com.sdc2ch.token.JwtUserContext;
import com.sdc2ch.token.config.JwtSettings;
import com.sdc2ch.token.exceptions.InvalidJwtToken;
import com.sdc2ch.token.exceptions.InvalidJwtTokenTypeException;
import com.sdc2ch.token.exceptions.JwtTokenCreatedException;
import com.sdc2ch.token.jwt.JwtAuthenticationProvider;
import com.sdc2ch.token.jwt.domain.JwtToken;
import com.sdc2ch.token.jwt.domain.JwtTokenFactory;
import com.sdc2ch.token.jwt.domain.JwtTokenImpl;
import com.sdc2ch.token.jwt.domain.RawAccessJwtToken;
import com.sdc2ch.token.jwt.domain.RefreshToken;
import com.sdc2ch.token.jwt.extractor.TokenExtractor;
import com.sdc2ch.token.jwt.verifier.TokenVerifier;
import com.sdc2ch.web.admin.repo.enums.RoleEnums;

@Service
class JwtTokenServiceImpl implements IJwtTokenService {

	
	private static final int EXPIRED_TIME = 60 * 24 * 365 * 100;

	
	private static final int DRIVER_EXPIRED_TIME = 60 * 24 * 365 * 100;

	@Autowired private JwtSettings jwtSettings;
	@Autowired private JwtTokenFactory tokenFactory;
	@Autowired private JwtAuthenticationProvider jwtProvider;

	@Qualifier("jwtHeaderTokenExtractor")
	@Autowired private TokenExtractor tokenExtractor;
	@Autowired private TokenVerifier tokenVerifier;
	@Autowired private I2ChUserService userSvc;
	@Autowired private I2CHAuthorization auth;
	@Autowired private I2ChUserDetailsService driverSvc;


	@Override
	public JwtToken refreshToken(String token) throws InvalidJwtToken {
		RefreshToken refreshToken = RefreshToken.create(
				RawAccessJwtToken.of(tokenExtractor.extract(token)),
				jwtSettings.getTokenSigningKey()).orElseThrow(() -> new InvalidJwtTokenTypeException());

		if (!tokenVerifier.verify(refreshToken.getJti())) {
			throw new InvalidJwtTokenTypeException();
		}

		String subject = refreshToken.getSubject();
		IUser user = userSvc.findByUsername(subject)
				.orElseThrow(() -> new UsernameNotFoundException("User not found: " + subject));

		if (user.getRoles() == null)
			throw new InsufficientAuthenticationException("User has no roles assigned");

		List<RoleEnums> roles = findRoles(user.getRoles());

		List<GrantedAuthority> authorities = roles.stream()
				.map(authority -> new SimpleGrantedAuthority(authority.getRolename())).collect(Collectors.toList());

		return tokenFactory.createAccessJwtToken(JwtUserContext.create(user.getUsername(), authorities), jwtSettings.getRefreshTokenExpTime());
	}

	private List<RoleEnums> findRoles(List<IUserRole> roles) {
		return RoleEnums.findRoles(roles.get(0).getRolename());
	}

	@Override
	public JwtTokenImpl createToken(Authentication authentication) {
		return createToken(authentication, jwtSettings.getTokenExpirationTime());
	}

	private JwtTokenImpl createToken(Authentication authentication, int expiredTime) {
		JwtUserContext userContext = (JwtUserContext) authentication.getPrincipal();
		JwtToken accessToken = tokenFactory.createAccessJwtToken(userContext, expiredTime);
		JwtToken refreshToken = tokenFactory.createRefreshToken(userContext, jwtSettings.getRefreshTokenExpTime());
		return JwtTokenImpl.of(accessToken.getToken(), refreshToken.getToken());
	}

	@Override
	public boolean checkExpiredToken(String tokenPayload) {
        RawAccessJwtToken token = RawAccessJwtToken.of(tokenExtractor.extract(tokenPayload));
        return jwtProvider.checkExpireToken(token);
	}

	@Override
	public JwtToken unExpiredToken(I2CHUserContext userContext) throws JwtTokenCreatedException {
		check(userContext);
		return tokenFactory.createAccessJwtToken(userContext, EXPIRED_TIME);
	}

	private void check(I2CHUserContext userContext) throws JwtTokenCreatedException {
		if(auth.hashCode() != userContext.hashCode()) throw new JwtTokenCreatedException();
	}

	@Override
	public JwtTokenImpl createToken(IUser user) throws JwtTokenCreatedException{

		user = Optional.of(user).orElseThrow(() -> new JwtTokenCreatedException("user can not be null"));


		int expiredTime = DRIVER_EXPIRED_TIME;

		JwtUserContext context = JwtUserContext.create(
				user.getUsername(),
				findRoles(user.getRoles()).stream()
							   .map(r -> new SimpleGrantedAuthority(r.getRolename()))
							   .collect(Collectors.toList()));

		return createToken(new JwtAuthenticationToken(context, context.getAuthorities()), expiredTime);
	}

}
