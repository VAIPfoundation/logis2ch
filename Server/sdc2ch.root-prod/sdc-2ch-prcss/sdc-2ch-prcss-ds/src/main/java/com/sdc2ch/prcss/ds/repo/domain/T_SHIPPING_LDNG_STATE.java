package com.sdc2ch.prcss.ds.repo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.sdc2ch.require.enums.SetupLcType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class T_SHIPPING_LDNG_STATE extends T_SHIPPING_STATE2 {

	@Column(name = "SETUP_LC_TY", length = 20)
	@Enumerated(EnumType.STRING)
	private SetupLcType setupLcTy;
	@Column(name = "FCTRY_CD", length = 20)
	private String factryCd;
}
