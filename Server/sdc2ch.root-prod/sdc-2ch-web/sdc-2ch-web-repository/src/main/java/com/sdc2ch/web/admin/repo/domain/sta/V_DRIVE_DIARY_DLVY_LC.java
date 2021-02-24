package com.sdc2ch.web.admin.repo.domain.sta;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import com.sdc2ch.require.repo.T_ID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Immutable
@Table(name="V_DRIVE_DIARY_DLVY_LC")
public class V_DRIVE_DIARY_DLVY_LC extends T_ID {


	@Column(name="DLVY_DE")		
	private String dlvyDe;
	@Column(name="FCTRY_CD")	
	private String fctryCd;
	@Column(name="FCTRY_NM")	
	private String fctryNm;
	@Column(name="CARALC_TY")	
	private String caralcTy;
	@Column(name="DLVY_LC_CD")		
	private String dlvyLcCd;
	@Column(name="DLVY_LC_NM")		
	private String dlvyLcNm;
	@Column(name="DLVY_LC_TY")		
	private String dlvyLcTy;
	@Column(name="ARVL_GRAD_CNT_A")
	private int arvlGradCntA;
	@Column(name="ARVL_GRAD_CNT_B")
	private int arvlGradCntB;
	@Column(name="ARVL_GRAD_CNT_C")
	private int arvlGradCntC;

}
