package com.sdc2ch.ars.repo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sdc2ch.require.repo.T_ID;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="T_ARS_MENT")
public class T_ARS_MENT extends T_ID {
	
	public T_ARS_MENT() {}
	public T_ARS_MENT(String messageTy, String message) {this.message = message; this.messageTy = messageTy;}
	
	@Column(name="MESSAGE_TY", nullable = false, unique = true)
	private String messageTy;
	@Column(name="MESSAGE")
	private String message;

}
