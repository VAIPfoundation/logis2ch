package com.sdc2ch.web.admin.repo.domain;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sdc2ch.require.repo.T_ID;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name="T_MOBILE_HEALTH_CHK")
@Getter
@Setter
@ToString
public class T_MOBILE_HEALTH_CHK {

	@Id
	@Column(name="MDN")
	private String mdn;

	@Column(name="VRN")
	private String vrn;
	@Column(name="FCTRY_CD")
	private String fctryCd;
	@Column(name="DRIVER_CD")
	private String driverCd;
	@Column(name="DATA_DT")
	private Date dataDt;
	@Column(name="STR_DATA_DT")
	private String strDataDt;
	@Column(name="NETWORK_TY")
    private String network;
	@Column(name="IS_RUN_SVC")
    private Boolean runningService;
	@Column(name="IS_FGR_SVC")
    private Boolean forgroundService;
	@Column(name="BTR_USE")
    private String batteryUsage;
	@Column(name="IS_DOZE_MODE")
    private Boolean dozeMode;
	@Column(name="IS_LOC")
    private Boolean locEnabled;
	@Column(name="IS_CALL_RECV")
    private Boolean callRecvEnabled;
	@Lob
	@Column(name="PERMISSION")
    private String permissions;

	@Column(name = "CREATE_DT", updatable = false, nullable = false)
	@JsonIgnore
	protected Date createDt;
	@Column(name = "UPDATE_DT")
	@JsonIgnore
	protected Date updateDt;

	@PrePersist
	protected void onPersist() {
		this.createDt = this.updateDt = Calendar.getInstance().getTime();
	}

    @PreUpdate
    protected void onUpdate() {
            this.updateDt = Calendar.getInstance().getTime();
    }

	
	@Transient
	@JsonSerialize
	private String vhcleTy;
	
	@Transient
	@JsonSerialize
	private String driverNm;

	@Transient
	@JsonSerialize
	private String fctryNm;
}
