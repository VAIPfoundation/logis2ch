package com.sdc2ch.web.admin.repo.domain.v;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Immutable
@Table(name="V_ALARM_STTUS")
public class V_ALARM_STTUS {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ROW_ID", updatable = false, nullable = false)
	private Long id;

	
	@Column(name = "FCTRY_CD", updatable = false, nullable = false)
	private String fctryCd;

	
	@Column(name = "ROUTE_NO", updatable = false, nullable = false)
	private String routeNo;

	
	@Column(name = "DLVY_DE", updatable = false, nullable = false)
	private String dlvyDe;

	
	@Column(name = "CARALC_TY", updatable = false, nullable = false)
	private String caralcTy;

	
	@Column(name = "CARALC_TY_NM", updatable = false, nullable = false)
	private String caralcTyNm;

	
	@Column(name = "VRN", updatable = false, nullable = false)
	private String vrn;

	
	@Column(name = "NO_CNFRM_CARALC_DTLS_ALARM_CNT", updatable = false, nullable = false)
	private Integer noCnfrmCaralcDtlsAlarmCnt;

	
	@Column(name = "NO_START_WORK_ALARM_CNT", updatable = false, nullable = false)
	private Integer noStartWorkAlarmCnt;

	
	@Column(name = "NO_PASSAGE_ALARM_CNT", updatable = false, nullable = false)
	private Integer noPassageAlarmCnt;

	
	@Column(name = "NO_LDNG_ALARM_CNT", updatable = false, nullable = false)
	private Integer noLdngAlarmCnt;

	
	@Column(name = "FCTRY_START_ALARM_CNT", updatable = false, nullable = false)
	private Integer fctryStartAlarmCnt;

	
	@Column(name = "ETY_BOX_DCSN_NO_OPRTN_ALARM_CNT", updatable = false, nullable = false)
	private Integer etyBoxDcsnNoOprtnAlarmCnt;

	
	@Column(name = "NO_END_WORK_ALARM_CNT", updatable = false, nullable = false)
	private Integer noEndWorkAlarmCnt;

	
	@Column(name = "RTNGUD_WRHOUSNG_ALARM_CNT", updatable = false, nullable = false)
	private Integer rtngudWrhousngAlarmCnt;

	
	@Column(name = "GPS_OFF_ALARM_CNT", updatable = false, nullable = false)
	private Integer gpsOffAlarmCnt;

}
