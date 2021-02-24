package com.sdc2ch.web.admin.repo.domain.v;

import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_20;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.annotations.Immutable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Getter;
import lombok.Setter;

 
@Entity
@Getter
@Setter
@Immutable
public class V_CARALC_PLAN {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ROWID", updatable = false, nullable = false)
	private Long id;
	@Column(name = "CENTER_CD", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String centerCd;
	@Column(name = "FCTRY_CD", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String fctryCd;
	@Column(name = "FCTRY_NM", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String fctryNm;
	@Column(name = "TRNSPRT_CMPNY", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String trnsprtCmpny;
	@Column(name = "DELIVERY_DATE", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String deliveryDate;
	@Column(name = "DLVY_DE", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String dlvyDe;
	@Column(name = "BATCH_NO", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String batchNo;
	@Column(name = "ROUTE_NO", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String routeNo;
	@Column(name = "CARALC_TY", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String caralcTy;
	@Column(name = "STOP_SEQ", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String stopSeq;
	@Column(name = "DLVY_SEQ", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String dlvySeq;
	@Column(name = "Stop_Cd", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String stopCd;
	@Column(name = "DLVY_LC_CD", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String dlvyLcCd;
	@Column(name = "STOP_TYPE", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String stopType;
	@Column(name = "CAR_CD", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String carCd;
	@Column(name = "VRN", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String vrn;
	@Column(name = "DRIVER_CD", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String driverCd;
	@Column(name = "DRIVER_NM", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String driverNm;
	@Column(name = "MOBILE_NO", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String mobileNo;
	@Column(name = "dock_no", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String dockNo;
	@Column(name = "SHIPSEQ", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String shipseq;
	@Column(name = "CAR_WEIGHT", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String carWeight;
	@Column(name = "VHCLE_TY", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String vhcleTy;
	@Column(name = "LDNG_TY", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String ldngTy;
	@Column(name = "RTATE_RATE", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String rtateRate;
	@Column(name = "CONF_RTATE_RATE", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String confRtateRate;
	@Column(name = "CONF_TOLLCOST", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String confTollcost;
	@Column(name = "CONF_DISTANCE", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String confDistance;
	@Column(name = "CAROIL", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String carOil;
	@Column(name = "ARRIVE_ETIME", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String arriveEtime;
	@Column(name = "ARVL_EXPC_TIME", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String arvlExpcTime;
	@Column(name = "DEPART_ETIME", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String departEtime;
	@Column(name = "NEW_REQ_TIME_SAT", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String newReqTimeSat;
	@Column(name = "NEW_REQ_TIME_SUN", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String newReqTimeSun;
	@Column(name = "NEW_REQ_TIME", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String newReqTime;
	@Column(name = "REPRESENT_STOP_CD", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String representStopCd;
	@Column(name = "STOP_NM", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String stopNm;
	@Column(name = "DLVY_LC_NM", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String dlvyLcNm;
	@Column(name = "ADDR_BASIC", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String addrBasic;
	@Column(name = "longitude", precision=9, scale=6, updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String longitude;
	@Column(name = "latitude", precision=9, scale=6, updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String latitude;
	@Column(name = "IsEmpty", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String isempty;
	@Column(name = "CVO_RDATE", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String cvoRdate;
	@Column(name = "CVO_RTIME", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String cvoRtime;
	@Column(name = "CVO_ARRDATE", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String cvoArrdate;
	@Column(name = "CVO_ARRTIME", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String cvoArrtime;
	@Column(name = "CENTER_NM", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String centerNm;
	@Column(name = "SITE_NM", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String siteNm;
	@Column(name = "DLVY_LC_BRANCH_NM", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String dlvyLcBranchNm;
	@Column(name = "DLVY_LC_BRANCH_CD", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String dlvyLcBranchCd;
	@Column(name = "DLVY_LC_GROUP_CD", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String dlvyLcGroupCd;
	@Column(name = "DLVY_LC_GROUP_NM", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String dlvyLcGroupNm;
	@Column(name = "CUSTOMER_MOBILE_NO", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String cMobileNo;
	@Column(name = "CUSTOMER_STAFF_MOBILE_NO", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String csMobileNo;
	@Column(name = "SCHE_START_DATE", updatable = false, nullable = false)
	private String scheStartDate;
	@Column(name = "SCHE_START_TIME", updatable = false, nullable = false)
	private String scheStartTime;
	@Column(name = "SCHE_END_DATE", updatable = false, nullable = false)
	private String scheEndDate;
	@Column(name = "SCHE_END_TIME", updatable = false, nullable = false)
	private String scheEndTime;

	@Column(name = "LDNG_ST", updatable = false, nullable = false)
	private String ldngSt;
	@Column(name = "LDNG_ED", updatable = false, nullable = false)
	private String ldngEd;


	@JsonSerialize
	@JsonDeserialize
	@Transient
	private Integer boxQty;






}
