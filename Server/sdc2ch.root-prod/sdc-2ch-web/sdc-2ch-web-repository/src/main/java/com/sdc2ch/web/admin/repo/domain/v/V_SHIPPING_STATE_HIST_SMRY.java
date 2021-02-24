package com.sdc2ch.web.admin.repo.domain.v;

import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_20;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Immutable
@ToString
public class V_SHIPPING_STATE_HIST_SMRY {
	
	@Id
	@Column(name = "ROWID", updatable = false, nullable = false)
	private Long id;
	
	@Column(name = "FCTRY_CD", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String fctryCd;
	@Column(name = "FCTRY_NM", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String fctryNm;
	@Column(name = "DLVY_DE", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String dlvyDe;
	@Column(name = "ROUTE_NO", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String routeNo;
	@Column(name = "DLVY_LC_CD", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String dlvyLcCd;
	@Column(name = "DLVY_LC_NM", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String dlvyLcNm;
	@Column(name = "DLVY_SEQ", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String dlvySeq;
	@Column(name = "STOP_SEQ", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String stopSeq;
	@Column(name = "SCHE_START_DATE", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String scheStartDate;
	@Column(name = "SCHE_START_TIME", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String scheStartTime;
	
	@Column(name = "VRN", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String vrn;
	@Column(name = "DRIVER_CD", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String driverCd;
	@Column(name = "DRIVER_NM", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String driverNm;
	@Column(name = "MOBILE_NO", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String mobileNo;
	@Column(name = "VHCLE_TY", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String vhcleTy;
	
	@Column(name = "STATE", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String state;
	@Column(name = "SETUP_LC", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String setupLc;
	@Column(name = "CHAIN", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String chain;
	@Column(name = "CREATE_DT", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String createDt;
	@Column(name = "DATA_TY", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String dataTy;
	@Column(name = "EMPTYBOX_YN", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String emptyboxYn;
	@Column(name = "STTUS", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String sttus;
	
	@Column(name = "TOTAL", updatable = false)
	private Integer total;
	@Column(name = "MGR_ALLOCATED", updatable = false)
	private Integer mgrAllocated;
	@Column(name = "USR_ST", updatable = false)
	private Integer usrSt;
	@Column(name = "BCN_ENTER", updatable = false)
	private Integer bcnEnter;
	@Column(name = "NFC_TAG_OFFIC", updatable = false)
	private Integer nfcTagOffic;
	@Column(name = "NFC_TAG_LDNG", updatable = false)
	private Integer nfcTagLdng;
	@Column(name = "BCN_EXITED", updatable = false)
	private Integer bcnExited;
	@Column(name = "GEO_ENTER", updatable = false)
	private Integer geoEnter;
	@Column(name = "GEO_ARRIVED", updatable = false)
	private Integer geoArrived;
	@Column(name = "USR_EB", updatable = false)
	private Integer usrEb;
	@Column(name = "GEO_EXITED", updatable = false)
	private Integer geoExited;
	@Column(name = "USR_FIN", updatable = false)
	private Integer usrFin;
	
	
}
