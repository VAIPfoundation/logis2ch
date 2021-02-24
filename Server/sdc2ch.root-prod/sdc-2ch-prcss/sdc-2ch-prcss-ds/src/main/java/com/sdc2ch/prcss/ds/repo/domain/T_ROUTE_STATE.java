package com.sdc2ch.prcss.ds.repo.domain;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PreUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sdc2ch.prcss.ds.io.RouteStateIO;
import com.sdc2ch.repo.io.RouteStateID;
import com.sdc2ch.require.enums.SetupLcType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@ToString
public class T_ROUTE_STATE implements RouteStateIO {

	@EmbeddedId
	@Column(name = "ROW_ID")
	private RouteStateID id;
	@Column(name = "CARALC_GROUP_ID_FK")
	private Long allocatedGroupId;
	@Column(name = "DRIVER_CD")
	private String driverCd;

	@Column(name = "TMS_PLAN_TIME")
	private LocalDateTime planTime; 
	@Column(name = "DRV_ST_TIME")
	private LocalDateTime drvStTime;
	@Column(name = "FRONT_ENT_TIME")
	private LocalDateTime frontEntTime;
	@Column(name = "OFFICE_ENT_TIME")
	private LocalDateTime officeEntTime;
	@Column(name = "LDNG_ST_TIME")
	private LocalDateTime ldngStTime;
	@Column(name = "LDNG_ST_LC")
	@Enumerated(EnumType.STRING)
	private SetupLcType ldngStSetupLc;
	
	@Column(name = "LDNG_ED_TIME")
	private LocalDateTime ldngEdTime;
	@Column(name = "LDNG_ED_LC")
	@Enumerated(EnumType.STRING)
	private SetupLcType ldngEdSetupLc;
	
	@Column(name = "FRONT_EXI_TIME")
	private LocalDateTime frontExiTime;
	@Column(name = "DRV_ED_TIME")
	private LocalDateTime drvEdTime;

	@Column(name = "LDNG_WAIT_TIME_SEC")
	private long ldngWaitTimeSec;
	@Column(name = "LDNG_TIME_SEC")
	private long ldngTimeSec;
	@Column(name = "DLVY_ST_WAIT_TIME_SEC")
	private long dlvyStWaitTimeSec;
	@Column(name = "PLAN_LDNG_DIFF_TIME_SEC")
	private long planLdngDiffTimeSec;

	@Column(name = "IS_FACTRY_EXIT")
	private boolean fixed;
	@Column(name = "UPDATE_DT")
	@JsonIgnore
	protected Date updateDt;
	
    @PreUpdate
    protected void onUpdate() {
            this.updateDt = Calendar.getInstance().getTime();
    }


    public String getVrn() {
		return this.id.getVrn();
	}

    public String getRouteNo() {
		return this.id.getRouteNo();
	}

    public String getDlvyDe() {
		return this.id.getDlvyDe();
	}


}
