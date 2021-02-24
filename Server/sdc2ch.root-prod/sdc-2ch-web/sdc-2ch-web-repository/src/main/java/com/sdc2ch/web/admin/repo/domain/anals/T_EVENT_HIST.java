package com.sdc2ch.web.admin.repo.domain.anals;

import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_20;
import static com.sdc2ch.require.repo.schema.GTConfig.MOBILE_NUM_LNG_13;
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
public class T_EVENT_HIST extends T_ID {
	
	@Column(name = "DLVY_DE", columnDefinition = YYYYMMDD_08)
	private String dlvyDe;

	@Column(name = "VRN", columnDefinition = VRN_LNG_10)
	private String vrn;

	@Column(name = "DEVICE_NO", columnDefinition = MOBILE_NUM_LNG_13)
	private String deviceNo;

	@Column(name = "EVENT_TY", columnDefinition = ANY_ENUMS_LNG_20)
	private String eventTy;

	@Column(name = "event_dt")
	private Date eventDt;

	@Column(name = "DEVICE_TY", columnDefinition = ANY_ENUMS_LNG_20)
	private String deviceTy;

	@Column(name = "DATA_TY", columnDefinition = ANY_ENUMS_LNG_20)
	private String dataTy;

	@Column(name = "REG_USER_ID")
	private String regUserId;

	@Column(name = "REG_DT")
	private Date regDt;
}
