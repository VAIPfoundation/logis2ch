package com.sdc2ch.require.service;

import java.util.Optional;

import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.domain.IUserDetails;

 
public interface I2ChUserDetailsService { 
	
	
	boolean isInDriver(IUser user);

	
	Optional<IUserDetails> findByUser(IUser user);
	
	
	Optional<IUserDetails> findByPhoneNo(String phoneNo);

}
