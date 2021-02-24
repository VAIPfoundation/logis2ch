package com.sdc2ch.require.domain;

import java.util.List;

public interface IUser {

	public String getPassword();
	public List<IUserRole> getRoles();
	public String getUsername();
	String getMobileNo();
	IUserDetails getUserDetails();
	boolean equals(Object o);
	String getFctryCd();
}
