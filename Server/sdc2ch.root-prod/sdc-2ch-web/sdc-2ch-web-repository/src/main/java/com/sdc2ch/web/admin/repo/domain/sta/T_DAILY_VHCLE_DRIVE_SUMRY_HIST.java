package com.sdc2ch.web.admin.repo.domain.sta;

import static com.sdc2ch.require.repo.schema.GTConfig.USER_ID_LNG_10;
import static com.sdc2ch.require.repo.schema.GTConfig.VRN_LNG_10;
import static com.sdc2ch.require.repo.schema.GTConfig.YYYYMMDD_08;
import static com.sdc2ch.require.repo.schema.GTConfig.TIME_LNG_04;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sdc2ch.require.repo.T_ID;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@Entity
@Table(name = "T_DAILY_VHCLE_DRIVE_SUMRY_HIST")
@EqualsAndHashCode(callSuper=false)
public class T_DAILY_VHCLE_DRIVE_SUMRY_HIST extends T_ID {
	
	
	@Column(name = "DLVY_DE", columnDefinition = YYYYMMDD_08)	
	private String dlvyDe;
	@Column(name = "VRN", columnDefinition = VRN_LNG_10)	
	private String vrn;
	@Column(name = "RTATE_RATE", precision=5, scale=3)	
	private BigDecimal rtateRate;
	@Column(name = "START_REPORT_DT")	
	private Date startReportDt;
	@Column(name = "END_REPORT_DT")	
	private Date endReportDt;
	@Column(name = "BEFORE_DLVY_DE", columnDefinition = YYYYMMDD_08)	
	private String beforeDlvyDe;
	@Column(name = "REST_TIME", columnDefinition = TIME_LNG_04)	
	private String restTime;
	@Column(name = "AC_DRIVE_TIME", columnDefinition = TIME_LNG_04)	
	private String acDriveTime;
	@Column(name = "AC_DRIVE_DSTNC", columnDefinition = TIME_LNG_04)	
	private String acDriveDstnc;
	@Column(name = "MAX_CTNU_DRIVE_TIME", columnDefinition = TIME_LNG_04)	
	private String maxCtnuDriveTime;
	@Column(name = "REG_USER_ID", columnDefinition = USER_ID_LNG_10)	
	private String regUserId;
	@Column(name = "REG_DATE")	
	private Date regDate;
	@Column(name = "CARALC_CNFIRM_FK")	
	private Long caralcCnfirmFk;
	
}
