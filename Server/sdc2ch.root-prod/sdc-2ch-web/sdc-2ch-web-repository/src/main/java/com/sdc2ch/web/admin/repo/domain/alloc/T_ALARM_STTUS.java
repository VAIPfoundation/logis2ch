package com.sdc2ch.web.admin.repo.domain.alloc;

import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_20;
import static com.sdc2ch.require.repo.schema.GTConfig.USER_ID_LNG_255;
import static com.sdc2ch.require.repo.schema.GTConfig.MOBILE_NUM_LNG_13;
import static com.sdc2ch.require.repo.schema.GTConfig.ROUTE_NO_LNG_07;
import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_40;
import static com.sdc2ch.require.repo.schema.GTConfig.USER_ID_LNG_10;
import static com.sdc2ch.require.repo.schema.GTConfig.VRN_LNG_10;
import static com.sdc2ch.require.repo.schema.GTConfig.YYYYMMDD_08;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sdc2ch.require.repo.T_ID;
import com.sdc2ch.web.admin.repo.domain.alloc.type.AlarmType;
import com.sdc2ch.web.admin.repo.domain.alloc.type.TrnsmisType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "T_ALARM_STTUS")
@Getter
@Setter
public class T_ALARM_STTUS extends T_ID {

	@Column(name = "FCTRY_CD", columnDefinition = ANY_ENUMS_LNG_20)
	private String fctryCd;
	@Column(name = "STOP_CD", columnDefinition = ANY_ENUMS_LNG_20)
	private String stopCd;
	@Column(name = "CARALC_TY", columnDefinition = ANY_ENUMS_LNG_20)
	private String caralcTy;
	@Column(name = "ROUTE_NO", columnDefinition = ROUTE_NO_LNG_07)
	private String routeNo;
	@Column(name = "VRN", columnDefinition = VRN_LNG_10)
	private String vrn;
	@Column(name = "DLVY_DE", columnDefinition = YYYYMMDD_08)
	private String dlvyDe;
	@Column(name = "MOBILE_NO", columnDefinition = MOBILE_NUM_LNG_13)
	private String mobileNo;
	@Column(name = "TRNSMIS_USER_ID", columnDefinition = USER_ID_LNG_10)
	private String trnsmisUser;
	@Column(name = "TRNSMIS_DT")
	private Date trnsmisDt;
	@Column(name = "RCVER_USER_ID", columnDefinition = USER_ID_LNG_255)
	private String rcverUser;
	@Column(name = "RECPTN_GB", columnDefinition = ANY_ENUMS_LNG_20)
	private String recptnGb;
	@Column(name = "recptn_dt")
	private Date recptnDt;
	@Column(name = "TRNSMIS_TY", columnDefinition = ANY_ENUMS_LNG_20)
	@Enumerated(EnumType.STRING)
	private TrnsmisType trnsmisTy;
	@Column(name = "ALARM_TY", columnDefinition = ANY_ENUMS_LNG_40)
	@Enumerated(EnumType.STRING)
	private AlarmType alarmTy;

	@JsonSerialize
	public String getAlarmTyNm() {
		return this.alarmTy != null ? this.alarmTy.alarmTyNm : null;
	}

	@JsonSerialize
	public String getTrnsmisTyNm() {
		return this.trnsmisTy != null ? this.trnsmisTy.trnsmisTyNm : null;
	}

}
