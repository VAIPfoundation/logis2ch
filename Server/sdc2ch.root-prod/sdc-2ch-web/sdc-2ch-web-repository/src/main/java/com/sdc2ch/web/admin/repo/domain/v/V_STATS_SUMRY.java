package com.sdc2ch.web.admin.repo.domain.v;

import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_20;
import static com.sdc2ch.require.repo.schema.GTConfig.GRAD_LNG_01;
import static com.sdc2ch.require.repo.schema.GTConfig.ROUTE_NO_LNG_07;
import static com.sdc2ch.require.repo.schema.GTConfig.TIME_LNG_06;
import static com.sdc2ch.require.repo.schema.GTConfig.VRN_LNG_10;
import static com.sdc2ch.require.repo.schema.GTConfig.YYYYMMDD_08;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Immutable
public class V_STATS_SUMRY {
	@Id
	@Column(name = "ROWID", updatable = false, nullable = false)
	private Long id;
	@Column(name = "DLVY_DE", columnDefinition = YYYYMMDD_08)	
	private String dlvyDe;
	@Column(name = "FCTRY_CD", columnDefinition = ANY_ENUMS_LNG_20)	
	private String fctryCd;
	@Column(name = "FCTRY_NM", columnDefinition = ANY_ENUMS_LNG_20)	
	private String fctryNm;
	@Column(name = "VRN", columnDefinition = VRN_LNG_10)	
	private String vrn;
	@Column(name = "VHCLE_TY", columnDefinition = ANY_ENUMS_LNG_20)	
	private String vhcleTy;
	@Column(name = "LDNG_TY", columnDefinition = ANY_ENUMS_LNG_20)	
	private String ldngTy;
	@Column(name = "DRIVER_CD", columnDefinition = ANY_ENUMS_LNG_20)	
	private String driverCd;
	@Column(name = "DRIVER_NM", columnDefinition = ANY_ENUMS_LNG_20)	
	private String driverNm;
	@Column(name = "MOBILE_NO", columnDefinition = ANY_ENUMS_LNG_20)	
	private String mobileNo;


	@Column(name = "RTATE_RATE", precision=3, scale=2)	
	private BigDecimal rtateRate;
	@Column(name = "TRNSPRT_CMPNY", columnDefinition = ANY_ENUMS_LNG_20)	
	private String trnsprtCmpny;

	@Column(name = "END_REPORT_TIME", columnDefinition = ANY_ENUMS_LNG_20)	
	private String endReportTime;
	@Column(name = "OVER_CTNU_DRIVE_CNT")	
	private int overCtnuDriveCnt;
	@Column(name = "MAX_CTNU_DRIVE_TIME", columnDefinition = TIME_LNG_06)	
	private String maxCtnuDriveTime;
	@Column(name = "AVG_CTNU_DRIVE_TIME", columnDefinition = TIME_LNG_06)	
	private String avgCtnuDriveTime;
	@Column(name = "UNDER_REST_CNT", columnDefinition = ANY_ENUMS_LNG_20)	
	private int underRestCnt;
	@Column(name = "AVG_UNDER_REST_TIME")	
	private String avgUnderRestTime;
	@Column(name = "ALRAM_CNT")	
	private int alarmCnt;
	@Column(name = "AVG_UNLDNG_TIME", columnDefinition = TIME_LNG_06)	
	private String avgUnldngTime;
	@Column(name = "AVG_LDNG_REQRE_TIME", columnDefinition = TIME_LNG_06)	
	private String avgLdngReqreTime;
	@Column(name = "AVG_DLVY_REQRE_TIME", columnDefinition = TIME_LNG_06)	
	private String avgDlvyReqreTime;
	@Column(name = "AVG_WORK_TIME", columnDefinition = TIME_LNG_06)	
	private String avgWorkTime;
	@Column(name = "TOT_WORK_TIME", columnDefinition = TIME_LNG_06)	
	private String totWorkTime;
	@Column(name = "AVG_ARVL_EROR_TIME", columnDefinition = TIME_LNG_06)	
	private String avgArvlErorTime;
	@Column(name = "TOT_ARVL_DELAY_CNT")	
	private int totArvlDelayCnt;






	@Column(name = "ROUTE_NO", columnDefinition = ROUTE_NO_LNG_07)	
	private String routeNo;
	@Column(name = "CARALC_TY", columnDefinition = ANY_ENUMS_LNG_20)	
	private String caralcTy;
	@Column(name = "PASSAGE_IN_TIME", columnDefinition = TIME_LNG_06)	
	private String passageInTime;
	@Column(name = "PASSAGE_OUT_TIME", columnDefinition = TIME_LNG_06)	
	private String passageOutTime;
	@Column(name = "LDNG_WAIT_TIME", columnDefinition = TIME_LNG_06)	
	private String ldngWaitTime;
	@Column(name = "LDNG_START_TIME", columnDefinition = TIME_LNG_06)	
	private String ldngStartTime;
	@Column(name = "LDNG_END_TIME", columnDefinition = TIME_LNG_06)	
	private String ldngEndTime;
	@Column(name = "LDNG_REQRE_TIME", columnDefinition = TIME_LNG_06)	
	private String ldngReqreTime;
	@Column(name = "LDNG_GRAD", columnDefinition = GRAD_LNG_01)	
	private String ldngGrad;
	@Column(name = "RTN_DRIVE_TIME", columnDefinition = TIME_LNG_06)	
	private String rtnDriveTime;
	@Column(name = "DLVY_RL_TIME", columnDefinition = TIME_LNG_06)	
	private String dlvyRlTime;
	@Column(name = "AVG_DLVY_TIME", columnDefinition = TIME_LNG_06)	
	private String avgDlvyTime;
	@Column(name = "SM_DLVY_TIME", columnDefinition = TIME_LNG_06)	
	private String smDlvyTime;
	@Column(name = "DLVY_START_TIME", columnDefinition = TIME_LNG_06)	
	private String dlvyStartTime;
	@Column(name = "DLVY_END_TIME", columnDefinition = TIME_LNG_06)	
	private String dlvyEndTime;
	@Column(name = "DLVY_GRAD", columnDefinition = GRAD_LNG_01)	
	private String dlvyGrad;
	@Column(name = "DLVY_LC_NM")	
	private String dlvyLcNm;
	@Column(name = "DLVY_LC_CD")	
	private String dlvyLcCd;
	@Column(name = "ENTRY_TIME", columnDefinition = TIME_LNG_06)	
	private String entryTime;
	@Column(name = "ARVL_TIME", columnDefinition = TIME_LNG_06)	
	private String arvlTime;
	@Column(name = "PLAN_END_TIME", columnDefinition = TIME_LNG_06)	
	private String planEndTime;
	@Column(name = "TAKEOVER_TIME", columnDefinition = TIME_LNG_06)	
	private String takeoverTime;
	@Column(name = "LEAVE_TIME", columnDefinition = TIME_LNG_06)	
	private String leaveTime;
	@Column(name = "ARVL_EXPC_TIME", columnDefinition = TIME_LNG_06)	
	private String arvlExpcTime;
	@Column(name = "ARVL_EXPC_TIME_EROR", columnDefinition = TIME_LNG_06)	
	private String arvlExpcTimeEror;
	@Column(name = "DRIVER_DLVY_GRAD", columnDefinition = GRAD_LNG_01)	
	private String driverDlvyGrad;
	@Column(name = "ARVL_GRAD", columnDefinition = GRAD_LNG_01)	
	private String arvlGrad;
	@Column(name = "DRIVE_DSTNC")	
	private int driveDstnc;
	@Column(name = "DLVY_SEQ")	
	private int dlvySeq;

	@Column(name = "DRIVE_TIME", columnDefinition = TIME_LNG_06)	
	private String driveTime;





}
