package com.sdc2ch.prcss.ss.repo.domain;

import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_20;
import static com.sdc2ch.require.repo.schema.GTConfig.USER_NAME_LNG_10;
import static com.sdc2ch.require.repo.schema.GTConfig.VRN_LNG_10;
import static com.sdc2ch.require.repo.schema.GTConfig.YYYYMMDD_08;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.sdc2ch.require.repo.T_ID;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "T_ANALS_DRIVING_HIST", 
uniqueConstraints=@UniqueConstraint(columnNames={"ACL_GROUP_ID_FK"}))
@Getter
@Setter
@ToString
public class T_ANALS_DRIVING_HIST extends T_ID {
	
	@Column(name="ACL_GROUP_ID_FK", nullable = false)
	private Long aclGroupId;
	@Column(name = "VRN", columnDefinition = VRN_LNG_10)
	private String vrn;
	@Column(name = "DRIVER_CD", columnDefinition = ANY_ENUMS_LNG_20)
	private String driverCd;
	@Column(name = "driver_nm", columnDefinition = USER_NAME_LNG_10)
	private String driverNm;
	@Column(name = "VHCLE_TY")
	private Float vhcleTy;
	@Column(name = "DLVY_DE", columnDefinition = YYYYMMDD_08)
	private String dlvyDe;
	
	@Column(name = "BE_FIN_DT")
	private Date beFinDt;
	@Column(name = "CUR_START_DT")
	private Date curStartDt;
	@Column(name = "REST_TIME_SEC")
	private long restTimeSec;
	@Column(name = "DRV_TIME_SEC")
	private long drvTimeSec;
	@Column(name = "DRV_DISTANCE")
	private double drvDistance;
	@Column(name = "MAX_DRV_TIME_SEC")
	private long maxDrvTimeSec;


}
