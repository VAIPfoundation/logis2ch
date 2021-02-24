package com.sdc2ch.web.admin.repo.domain.alloc;

import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_20;
import static com.sdc2ch.require.repo.schema.GTConfig.FACTORY_CD_LNG_04;
import static com.sdc2ch.require.repo.schema.GTConfig.MOBILE_NUM_LNG_13;
import static com.sdc2ch.require.repo.schema.GTConfig.TIME_FIELD_LNG_05;
import static com.sdc2ch.require.repo.schema.GTConfig.USER_ID_LNG_10;
import static com.sdc2ch.require.repo.schema.GTConfig.VRN_LNG_10;
import static com.sdc2ch.require.repo.schema.GTConfig.SHOT_CONTENTS_LNG_100;
import static com.sdc2ch.require.repo.schema.GTConfig.YYYYMMDD_08;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sdc2ch.repo.io.AllocatedGroupIO;
import com.sdc2ch.repo.io.RouteIO;
import com.sdc2ch.require.repo.T_ID;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

 
@Entity
@Getter
@Setter
@Table(name = "T_CARALC_CNFIRM_GROUP2")
@ToString
public class T_CARALC_CNFIRM_GROUP2 extends T_ID implements AllocatedGroupIO {
	
	@Column(name = "FCTRY_CD", columnDefinition = FACTORY_CD_LNG_04, nullable = false)
	private String fctryCd;
	@Column(name = "VRN", columnDefinition = VRN_LNG_10, nullable = false)
	private String vrn;
	@Column(name = "DLVY_DE", columnDefinition = YYYYMMDD_08, nullable = false)
	private String dlvyDe;
	@Column(name = "MOBILE_NO", columnDefinition = MOBILE_NUM_LNG_13, nullable = false)
	private String mobileNo;
	

	@Column(name = "DRIVER_CD", columnDefinition = USER_ID_LNG_10, nullable = false)
	private String driverCd;
	
	@Column(name = "LDNG_EXPC_TIME", columnDefinition = TIME_FIELD_LNG_05)
	private String ldngExpcTime;

	@Column(name = "ADJUST_TIME", columnDefinition = TIME_FIELD_LNG_05)
	private String adjustTime;

	@Column(name = "TRNSMIS_USER_ID", columnDefinition = USER_ID_LNG_10)
	private String trnsmisUser;

	@Column(name = "TRNSMIS_DT")
	private Date trnsmisDt;

	@Column(name = "CNFIRM_USER_ID", columnDefinition = USER_ID_LNG_10)
	private String cnfirmUser;

	@Column(name = "CNFIRM_DT")
	private Date cnfirmDt;

	@Column(name = "CARALC_TYPE", columnDefinition = ANY_ENUMS_LNG_20)
	private String caralcTy;
	
	@Column(name = "ROUTE_HINT", columnDefinition = SHOT_CONTENTS_LNG_100)
	private String routeHint;
	
	@Transient
	@JsonDeserialize @JsonSerialize
	private boolean change;
	@Transient
	@JsonDeserialize @JsonSerialize
	private boolean frontExit;
	
	@Transient
	@JsonSerialize
	@JsonDeserialize
	private List<T_CARALC_CNFIRM2> routes = new ArrayList<>();

	@SuppressWarnings("unchecked")
	@Override
	@Transient
	@JsonIgnore
	public List<RouteIO> getRouteInfo() {
		return (List<RouteIO>)(Object)routes;
	}
	public T_CARALC_CNFIRM_GROUP2 addAll(List<T_CARALC_CNFIRM2> mstrs) {
		
		if(mstrs != null) {
			mstrs = mstrs.stream().sorted(Comparator.nullsFirst(Comparator.comparing(T_CARALC_CNFIRM2::getLdngExpcTime))).collect(Collectors.toList());
		}
		this.routes = mstrs;
		return this;
	}
		
}
