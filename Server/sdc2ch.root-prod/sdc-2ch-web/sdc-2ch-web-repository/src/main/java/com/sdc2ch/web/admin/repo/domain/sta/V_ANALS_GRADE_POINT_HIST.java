package com.sdc2ch.web.admin.repo.domain.sta;

import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_20;
import static com.sdc2ch.require.repo.schema.GTConfig.GRAD_LNG_02;
import static com.sdc2ch.require.repo.schema.GTConfig.ROUTE_NO_LNG_07;
import static com.sdc2ch.require.repo.schema.GTConfig.TIME_FIELD_LNG_05;
import static com.sdc2ch.require.repo.schema.GTConfig.USER_NAME_LNG_10;
import static com.sdc2ch.require.repo.schema.GTConfig.VRN_LNG_10;
import static com.sdc2ch.require.repo.schema.GTConfig.YYYYMMDD_08;
import static com.sdc2ch.require.repo.schema.GTConfig.GRAD_LNG_01;
import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ID_LNG_20;
import static com.sdc2ch.require.repo.schema.GTConfig.LONG_TITLE_NLG_100;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Immutable;

import com.sdc2ch.require.repo.T_ID;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Immutable
@Table(name="V_ANALS_GRADE_POINT_HIST")
public class V_ANALS_GRADE_POINT_HIST extends T_ID {

	@Column(name="ACL_GROUP_ID_FK", nullable = false)
	private Long aclGroupId;

	@Column(name="FCTRY_CD")
	private String fctryCd;
	@Column(name="FCTRY_NM")
	private String fctryNm;

	@Column(name = "GRAD", columnDefinition = GRAD_LNG_02)
	private String grad;
	@Column(name = "VRN", columnDefinition = VRN_LNG_10)
	private String vrn;
	@Column(name = "DRIVER_CD", columnDefinition = ANY_ENUMS_LNG_20)
	private String driverCd;
	@Column(name = "driver_nm", columnDefinition = USER_NAME_LNG_10)
	private String driverNm;
	@Column(name = "ROUTE_NO", columnDefinition = ROUTE_NO_LNG_07)
	private String routeNo;
	@Column(name = "VHCLE_TY", nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String vhcleTy;
	@Column(name = "CARALC_TY", columnDefinition = ANY_ENUMS_LNG_20)
	private String caralcTy;
	@Column(name = "DLVY_DE", columnDefinition = YYYYMMDD_08)
	private String dlvyDe;

	@Column(name = "GEO_ENTER_DT")
	private Date geoEnterDt;
	@Column(name = "GEO_ARRIVE_DT")
	private Date geoArriveDt;
	@Column(name = "UNLOAD_DT")
	private Date unLoadDt;
	@Column(name = "GEO_EXIT_DT")
	private Date geoExitDt;
	@Column(name = "PLAN_ARRIVE_DT")
	private Date planArriveDt;
	@Column(name = "PLAN_ARRIVE_HHMI")
	private Date planArriveHhmi;

	@Column(name = "IS_CHILD")
	private boolean child;
	@Column(name = "FROM_DLVY_LC_CD", columnDefinition = ANY_ID_LNG_20)
	private String fromDlvyLcCd;
	@Column(name = "FROM_DLVY_LC_NM", columnDefinition = LONG_TITLE_NLG_100)
	private String fromDlvyLcNm;

	@Column(name = "TO_DLVY_LC_CD", columnDefinition = ANY_ID_LNG_20)
	private String toDlvyLcCd;
	@Column(name = "TO_DLVY_LC_NM", columnDefinition = LONG_TITLE_NLG_100)
	private String toDlvyLcNm;
	@Column(name = "WEEK_DAY")
	private Integer weekDay;
	@Column(name = "WEEK_DAY_KR", columnDefinition = GRAD_LNG_01)
	private String weekDayKr;


	@Column(name = "FROM_EXIT_DT")
	private Date fromExitDt;
	@Column(name = "DIFF_TIME")
	private Date diffTime;
	@Column(name = "DUE_MIN")
	private Date dueMin;


	@Column(name = "DLVY_REQRE_TIME")
	private Date dlvyReqreTime;


}
