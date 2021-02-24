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
@Table(name="V_DRIVE_DIARY_DAILY")
public class V_DRIVE_DIARY_DAILY extends T_ID {
	@Column(name="ACL_GROUP_ID_FK")	
	private Long aclGroupIdFk;

	@Column(name="FCTRY_CD")	
	private String fctryCd;
	@Column(name="DLVY_DE")		
	private String dlvyDe;
	@Column(name="VRN")			
	private String vrn;
	@Column(name="TRNSPRT_CMPNY")	
	private String trnsprtCmpny;
	@Column(name="LDNG_TY")		
	private String ldngTy;
	@Column(name="VHCLE_TY")	
	private String vhcleTy;
	@Column(name="DRIVER_CD")	
	private String driverCd;
	@Column(name="DRIVER_NM")	
	private String driverNm;
	@Column(name="MOBILE_NO")	
	private String mobileNo;


	@Column(name="LDNG_GRAD_CNT_A")	
	private int ldngGradCntA;
	@Column(name="LDNG_GRAD_CNT_B")	
	private int ldngGradCntB;
	@Column(name="LDNG_GRAD_CNT_C")	
	private int ldngGradCntC;
	@Column(name="DLVY_GRAD_CNT_A")	
	private int dlvyGradCntA;
	@Column(name="DLVY_GRAD_CNT_B")	
	private int dlvyGradCntB;
	@Column(name="DLVY_GRAD_CNT_C")	
	private int dlvyGradCntC;
	@Column(name="ARVL_GRAD_CNT_A")	
	private int arvlGradCntA;
	@Column(name="ARVL_GRAD_CNT_B")	
	private int arvlGradCntB;
	@Column(name="ARVL_GRAD_CNT_C")	
	private int arvlGradCntC;

}
