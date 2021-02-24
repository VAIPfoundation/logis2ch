package com.sdc2ch.require.service;

import java.util.Optional;

import com.sdc2ch.require.domain.IUser;


 
public interface I2ChUserService {
	
	public Optional<IUser> findByUsername(String username);
	
	
	public Optional<IUser> findByMobileNo(String mobileNo);
}
