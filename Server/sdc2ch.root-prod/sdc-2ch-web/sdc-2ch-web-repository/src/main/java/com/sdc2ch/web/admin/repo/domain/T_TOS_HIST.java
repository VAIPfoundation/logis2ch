package com.sdc2ch.web.admin.repo.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.sdc2ch.require.repo.T_ID;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class T_TOS_HIST extends T_ID {
	
	@ManyToOne(cascade={CascadeType.MERGE})
	@JoinColumn(name = "APP_USE_INFO_FK", referencedColumnName = "ROW_ID")
	private T_MOBILE_APP_USE_INFO appUseInfo;
	@ManyToOne
	@JoinColumn(name = "TOS_FK", referencedColumnName = "ROW_ID")
	private T_TOS tos;
	
	public T_TOS_HIST() {}
	public T_TOS_HIST(T_MOBILE_APP_USE_INFO appUseInfo, T_TOS t) {
		this.appUseInfo = appUseInfo;
		this.tos = t;
	}
	
	
}
