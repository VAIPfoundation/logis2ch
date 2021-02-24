package com.sdc2ch.token.ajax;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.sdc2ch.core.security.auth.I2CHUserContext;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.service.I2ChUserService;
import com.sdc2ch.token.JwtUserContext;
import com.sdc2ch.web.admin.repo.enums.RoleEnums;



@Component
public class AjaxAuthenticationProvider implements AuthenticationProvider {
    
    private final PasswordEncoder encoder;
    private final I2ChUserService userService;

    
    @Autowired
    public AjaxAuthenticationProvider(final I2ChUserService userService, final PasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.notNull(authentication, "No authentication data provided");

        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        IUser user = (IUser) userService.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        




        if (!encoder.matches(password, user.getPassword().toLowerCase())) {
        	if (!encoder.matches(password.toUpperCase(), user.getPassword().toLowerCase())) {	
        		throw new BadCredentialsException("Authentication Failed. Username or Password not valid.");
        	}
        }

        if (user.getRoles() == null) throw new InsufficientAuthenticationException("User has no roles assigned");

        List<RoleEnums> roles = RoleEnums.findRoles(user.getRoles().get(0).getRolename());
        if (roles == null) throw new InsufficientAuthenticationException("User has no roles assigned");

        List<GrantedAuthority> authorities = roles.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getRolename()))
                .collect(Collectors.toList());

        I2CHUserContext userContext = JwtUserContext.create(user.getUsername(), authorities);

        return new UsernamePasswordAuthenticationToken(userContext, null, userContext.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    @SuppressWarnings("deprecation")
	public static void main(String[] args) {


    	CharSequence password = "D003829";


        String passwordDB = "ACB94C307C88F3D8E9A23985F4F1AF398C47D0F6898D450A9B3721DEDFF999C4";		

        PasswordEncoder encoder = new MessageDigestPasswordEncoder("SHA-256");






    	System.out.println("is match => " + encoder.matches(password, passwordDB.toLowerCase()));
    	System.out.println(password);
    	System.out.println(encoder.encode("pweb"));
    	System.out.println(passwordDB);
	}

}
