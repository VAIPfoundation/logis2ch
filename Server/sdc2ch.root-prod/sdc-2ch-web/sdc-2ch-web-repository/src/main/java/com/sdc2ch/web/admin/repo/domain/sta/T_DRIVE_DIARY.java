package com.sdc2ch.web.admin.repo.domain.sta;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sdc2ch.require.repo.T_ID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="T_DRIVE_DIARY")
public class T_DRIVE_DIARY extends T_ID {

	@Column(name="ACL_GROUP_ID_FK")
	private Long aclGroupId;

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
	@Column(name="STTUS_CD")
	private String statusCd;
	@Column(name="STTUS_NM")
	private String statusNm;
	@Column(name="EVENT_TY")
	private String eventTy;
	@Column(name="EVENT_DT")
	private Date eventDt;
	@Column(name="EVENT_NM")
	private String eventNm;
	@Column(name="EVENT_CD")
	private String eventCd;
	@Column(name="REMARK")
	private String remark;
	@Column(name="ORDER_NO")
	private int orderNo;
	@Column(name="GRADE")			
	private String grade;
	@Column(name="GRADE_SCOPE1")	
	private String gradeScope1;
	@Column(name="STD_TIME")	
	private Long stdTime;
	@Column(name="ADJUST_TIME")	
	private Long adjustTime;
	@Column(name="EXPC_TIME")	
	private String expcTime;

}
