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
@Table(name="V_DRIVE_DIARY_DTLS")
public class V_DRIVE_DIARY_DTLS extends T_ID {

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

	@Column(name="ROUTE_NO")	
	private String routeNo;
	@Column(name="CARALC_TY")	
	private String caralcTy;
	@Column(name = "RTATE_RATE", precision=5, scale=3)	
	private BigDecimal rtateRate;

	@Column(name="DLVY_LC_CD")		
	private String dlvyLcCd;
	@Column(name="DLVY_LC_NM")		
	private String dlvyLcNm;


	@Column(name="FT_ENTER")		
	private Date ftEnter;
	@Column(name="LDNG_ST")			
	private Date ldngSt;
	@Column(name="LDNG_ED")			
	private Date ldngEd;
	@Column(name="LDNG_WAIT_TIME")	
	private long ldngWaitTime;
	@Column(name="LDNG_REQRE_TIME")	
	private long ldngReqreTime;
	@Column(name="FT_EXIT")			
	private Date ftExit;
	@Column(name="CC_ENTER")		
	private Date ccEnter;
	@Column(name="CC_ARRIVE")		
	private Date ccArrive;
	@Column(name="CC_EXPC_TIME")	
	private String ccExpcTime;
	@Column(name="CC_EXPC_TIME_DIFF")
	private long ccExpcTimeDiff;
	@Column(name="CC_TAKEOVER")		
	private Date ccTakeover;
	@Column(name="CC_DEPART")		
	private Date ccDepart;
	@Column(name="FT_RECOVER")		
	private Date ftRecover;
	@Column(name="FT_TURN")			
	private Date ftTurn;
	@Column(name="LDNG_GRAD")		
	private String ldngGrad;
	@Column(name="DLVY_GRAD")		
	private String dlvyGrad;
	@Column(name="ARVL_GRAD")		
	private String arvlGrad;
	@Column(name="ORDER_NO")		
	private int orderNo;

}
