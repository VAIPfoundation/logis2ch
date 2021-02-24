package com.sdc2ch.web.admin.repo.domain.sta;

import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_20;
import static com.sdc2ch.require.repo.schema.GTConfig.GRAD_LNG_02;
import static com.sdc2ch.require.repo.schema.GTConfig.ROUTE_NO_LNG_07;
import static com.sdc2ch.require.repo.schema.GTConfig.TIME_FIELD_LNG_05;
import static com.sdc2ch.require.repo.schema.GTConfig.USER_NAME_LNG_10;
import static com.sdc2ch.require.repo.schema.GTConfig.VRN_LNG_10;
import static com.sdc2ch.require.repo.schema.GTConfig.YYYYMMDD_08;
import static com.sdc2ch.require.repo.schema.GTConfig.GRAD_LNG_01;

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
@Table(name="V_ANALS_GRADE_SCOPE_HIST")
public class V_ANALS_GRADE_SCOPE_HIST extends T_ID {

	@Column(name="FCTRY_CD")
	private String fctryCd;

	@Column(name="ACL_GROUP_ID_FK")
	private Long aclGroupId;

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
	@Column(name = "WEEK_DAY")
	private Integer weekDay;
	@Column(name = "WEEK_DAY_KR", columnDefinition = GRAD_LNG_01)
	private String weekDayKr;

	@Column(name = "FROM_DLVY_LC_CD", columnDefinition = TIME_FIELD_LNG_05)
	private String fromDlvyLcCd;
	@Column(name = "FROM_PLAN_DE", columnDefinition = YYYYMMDD_08)
	private String fromPlanDe;
	@Column(name = "FROM_PLAN_TIME", columnDefinition = TIME_FIELD_LNG_05)
	private String fromPlanTime;
	@Column(name = "FROM_PLAN_DT")
	private Date fromPlanDt;
	@Column(name = "FROM_REAL_DE", columnDefinition = YYYYMMDD_08)
	private String fromRealDe;
	@Column(name = "FROM_REAL_TIME", columnDefinition = TIME_FIELD_LNG_05)
	private String fromRealTime;
	@Column(name = "FROM_REAL_DT")
	private Date fromRealDt;

	@Column(name = "TO_DLVY_LC_CD", columnDefinition = TIME_FIELD_LNG_05)
	private String toDlvyLcCd;
	@Column(name = "TO_PLAN_DE", columnDefinition = YYYYMMDD_08)
	private String toPlanDe;
	@Column(name = "TO_PLAN_TIME", columnDefinition = TIME_FIELD_LNG_05)
	private String toPlanTime;
	@Column(name = "TO_PLAN_DT")
	private Date toPlanDt;
	@Column(name = "TO_REAL_DE", columnDefinition = YYYYMMDD_08)
	private String toRealDe;
	@Column(name = "TO_REAL_TIME", columnDefinition = TIME_FIELD_LNG_05)
	private String toRealTime;
	@Column(name = "TO_REAL_DT")
	private Date toRealDt;

	@Column(name = "DIFF_TIME")
	private Long diffTime;


	@Column(name="STTUS_CD")
	private String statusCd;
	@Column(name="STTUS_NM")
	private String statusNm;

	@Column(name = "STD_TIME")
	private Long stdTime;
	@Column(name = "ADJUST_TIME")
	private Long adjustTime;

}
