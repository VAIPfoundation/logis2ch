package com.sdc2ch.web.admin.repo.domain.anals;

import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_20;
import static com.sdc2ch.require.repo.schema.GTConfig.BOOLEAN_LEN_01;
import static com.sdc2ch.require.repo.schema.GTConfig.TIME_FIELD_LNG_05;
import static com.sdc2ch.require.repo.schema.GTConfig.USER_NAME_LNG_10;
import static com.sdc2ch.require.repo.schema.GTConfig.VRN_LNG_10;
import static com.sdc2ch.require.repo.schema.GTConfig.YYYYMMDD_08;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sdc2ch.require.repo.T_ID;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "T_ANALS_ARVL_GRAD_HIST")
@Getter
@Setter
public class T_ANALS_ARVL_GRAD_HIST extends T_ID {
	
	@Column(name = "DLVY_DE", columnDefinition = YYYYMMDD_08)
	private String dlvyDe;

	@Column(name = "DLVY_LC_ID") 
	private int dlvyLcId;

	@Column(name = "START_TY", columnDefinition = ANY_ENUMS_LNG_20)
	private String startTy;

	@Column(name = "START_ID")
	private int startId;

	@Column(name = "TMS_STD_TIME", columnDefinition = TIME_FIELD_LNG_05)
	private String tmsStdTime;

	@Column(name = "ENTRY_DT")
	private Date entryDt;

	@Column(name = "ARVL_DT")
	private Date arvlDt;

	@Column(name = "UNLDNG_DT")
	private Date unldngDt;

	@Column(name = "LEAVE_DT")
	private Date leaveDt;

	@Column(name = "GRAD", columnDefinition = BOOLEAN_LEN_01)
	private String grad;

	@Column(name = "DLVY_REQRE_TIME", columnDefinition = TIME_FIELD_LNG_05)
	private String dlvyReqreTime;


	private String col;

	@Column(name = "MOVE_DSTNC")
	private String moveDstnc;

	@Column(name = "VRN", columnDefinition = VRN_LNG_10)
	private String vrn;

	@Column(name = "DRIVER_CD", columnDefinition = ANY_ENUMS_LNG_20)
	private String driverCd;

	@Column(name = "DRIVER_NM", columnDefinition = USER_NAME_LNG_10)
	private String driverNm;

}
