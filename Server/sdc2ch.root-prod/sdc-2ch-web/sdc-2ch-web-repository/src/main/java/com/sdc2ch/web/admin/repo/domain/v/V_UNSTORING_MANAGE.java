package com.sdc2ch.web.admin.repo.domain.v;

import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_20;

import java.util.Date;

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
public class V_UNSTORING_MANAGE {

	@Id
	@Column(name = "ROW_ID", updatable = false, nullable = false)
	private Long id;

	@Column(name = "DLVY_DE", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String dlvyDe;
	@Column(name = "FCTRY_CD", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String fctryCd;
	@Column(name = "FCTRY_NM", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String fctryNm;
	@Column(name = "ROUTE_NO", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String routeNo;
	@Column(name = "BATCH_NO", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String batchNo;
	@Column(name = "CARALC_TY", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String caralcTy;
	@Column(name = "VHCLE_TY", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String vhcleTy;
	@Column(name = "VRN", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String vrn;
	@Column(name = "DRIVER_NM", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String driverNm;


	@Column(name = "DLVY_LC_STTUS", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String dlvyLcSttus;
	@Column(name = "FCTRY_EXPC_START", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)	
	private String fctryExpcStart;
	@Column(name = "FCTRY_EXIT_TIME", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)	
	private String fctryExitTime;
	@Column(name = "DELAY_TIME", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)	
	private String delayTime;
	@Column(name = "DELAY_RESN", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)	
	private String delayResn;
	@Column(name = "LDNG_EXPC_TIME", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)	
	private String ldngExpcTime;
	@Column(name = "PARKNG_TIME", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)	
	private String parkngTime;
	@Column(name = "LDNG_TIME", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)	
	private String ldngTime;
	@Column(name = "LDNG_TIME1", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)	
	private String ldngTime1;
	@Column(name = "LDNG_TIME2", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)	
	private String ldngTime2;
	@Column(name = "LDNG_TIME3", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)	
	private String ldngTime3;
	@Column(name = "DFFRNC", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)	
	private String dffrnc;
	@Column(name = "LDNG_STD_TIME", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)	
	private String ldngStdTime;

}
