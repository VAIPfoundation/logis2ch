package com.sdc2ch.web.admin.repo.domain.v;

import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_20;
import static com.sdc2ch.require.repo.schema.GTConfig.ROUTE_NO_LNG_07;
import static com.sdc2ch.require.repo.schema.GTConfig.VRN_LNG_10;
import static com.sdc2ch.require.repo.schema.GTConfig.YYYYMMDD_08;

import java.math.BigDecimal;
import java.util.Date;

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
public class V_REALTIME_INFO {
	
    
    
    
	@Id
	@Column(name = "ROWID", updatable = false, nullable = false)
	private Long id;

	@Column(name = "FCTRY_CD", columnDefinition = ANY_ENUMS_LNG_20)	
	private String fctryCd;
	@Column(name = "FCTRY_NM", columnDefinition = ANY_ENUMS_LNG_20)	
	private String fctryNm;
	@Column(name = "VHCLE_TY", columnDefinition = ANY_ENUMS_LNG_20)	
	private String vhcleTy;
	@Column(name = "TRNSPRT_CMPNY", columnDefinition = ANY_ENUMS_LNG_20)	
	private String trnsprtCmpny;
	@Column(name = "VRN", columnDefinition = VRN_LNG_10)	
	private String vrn;
	@Column(name = "DRIVER_CD", columnDefinition = ANY_ENUMS_LNG_20)	
	private String driverCd;
	@Column(name = "DRIVER_NM", columnDefinition = ANY_ENUMS_LNG_20)	
	private String driverNm;
	@Column(name = "LDNG_TY", columnDefinition = ANY_ENUMS_LNG_20)	
	private String ldngTy;
	@Column(name = "MOBILE_NO", columnDefinition = ANY_ENUMS_LNG_20)	
	private String mobileNo;
	@Column(name = "ACCURACY", precision=3, scale=2)	
	private BigDecimal accuracy;
	@Column(name = "ADRES", columnDefinition = ANY_ENUMS_LNG_20)	
	private String adres;
	@Column(name = "ALTITUDE")	
	private Integer altitude;
	@Column(name = "DATA_DT", columnDefinition = ANY_ENUMS_LNG_20)	
	private Date dataDt;
	@Column(name = "DGREE", columnDefinition = ANY_ENUMS_LNG_20)	
	private String dgree;
	@Column(name = "DAY_DISTANCE")	
	private Integer dayDistance;
	@Column(name = "EVENT", columnDefinition = ANY_ENUMS_LNG_20)	
	private String event;
	@Column(name = "LAT", precision=9, scale=6)	
	private BigDecimal lat;
	@Column(name = "LNG", precision=9, scale=6)	
	private BigDecimal lng;
	@Column(name = "SPEED", precision=4, scale=1)	
	private BigDecimal speed;
	@Column(name = "UPDATE_DT", columnDefinition = ANY_ENUMS_LNG_20)	
	private String updateDt;
	@Column(name = "DLVY_DE", columnDefinition = YYYYMMDD_08)	
	private String dlvyDe;
	@Column(name = "ROUTE_NO", columnDefinition = ROUTE_NO_LNG_07)	
	private String routeNo;
	@Column(name = "DLVY_LC_ID", columnDefinition = ANY_ENUMS_LNG_20)	
	private String dlvyLcId;
	@Column(name = "ALC_DT", columnDefinition = ANY_ENUMS_LNG_20)	
	private String alcDt;
	@Column(name = "ALC_GROUP_ID", columnDefinition = ANY_ENUMS_LNG_20)	
	private String alcGroupId;
	@Column(name = "CHAIN", columnDefinition = ANY_ENUMS_LNG_20)	
	private String chain;
	@Column(name = "HINT", columnDefinition = ANY_ENUMS_LNG_20)	
	private String hint;
	@Column(name = "STATE", columnDefinition = ANY_ENUMS_LNG_20)	
	private String state;
	
}
