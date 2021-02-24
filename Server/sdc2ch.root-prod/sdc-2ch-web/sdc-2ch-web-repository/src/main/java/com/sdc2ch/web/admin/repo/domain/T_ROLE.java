package com.sdc2ch.web.admin.repo.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sdc2ch.require.repo.T_ID;
import com.sdc2ch.web.admin.repo.enums.RoleEnums;

import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "T_ROLE")
@Getter
@Setter
public class T_ROLE extends T_ID {
	
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "ROLE_NAME")
	private RoleEnums role;
	@Column(name = "ROLE_DESC")
	private String desc;
	
	@ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
	@JoinTable(name = "T_ROLE_T_MENU_MAP")
	@JsonManagedReference
	private Set<T_MENU> menus = new HashSet<>();
	
}
