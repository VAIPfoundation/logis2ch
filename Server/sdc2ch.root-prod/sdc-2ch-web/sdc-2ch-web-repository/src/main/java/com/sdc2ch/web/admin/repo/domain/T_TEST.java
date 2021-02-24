package com.sdc2ch.web.admin.repo.domain;

import javax.persistence.Entity;

import com.sdc2ch.require.repo.T_ID;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class T_TEST extends T_ID{
	private String MDN;
	private String ADDRESS;

}
