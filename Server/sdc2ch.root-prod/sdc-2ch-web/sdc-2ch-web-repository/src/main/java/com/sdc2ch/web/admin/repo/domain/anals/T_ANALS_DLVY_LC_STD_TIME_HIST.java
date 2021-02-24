package com.sdc2ch.web.admin.repo.domain.anals;

import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_20;
import static com.sdc2ch.require.repo.schema.GTConfig.FACTORY_CD_LNG_04;
import static com.sdc2ch.require.repo.schema.GTConfig.ROUTE_NO_LNG_07;
import static com.sdc2ch.require.repo.schema.GTConfig.TIME_FIELD_LNG_05;
import static com.sdc2ch.require.repo.schema.GTConfig.USER_ID_LNG_10;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sdc2ch.require.repo.T_ID;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "T_ANALS_DLVY_LC_STD_TIME_HIST")
@Getter
@Setter
public class T_ANALS_DLVY_LC_STD_TIME_HIST extends T_ID {
	
	@Column(name = "FCTRY_CD", columnDefinition = FACTORY_CD_LNG_04)
	private String fctryCd;

	@Column(name = "VHCLE_TY", columnDefinition = ANY_ENUMS_LNG_20)
	private String vhcleTy;

	@Column(name = "DLVY_TY", columnDefinition = ANY_ENUMS_LNG_20)
	private String dlvyTy;

	@Column(name = "DLVY_LC_CD", columnDefinition = ROUTE_NO_LNG_07)
	private String DLVY_LC_CD;

	@Column(name = "DLVY_LC_NM", columnDefinition = ROUTE_NO_LNG_07)
	private String dlvyLcNm;

	@Column(name = "STD_TIME", columnDefinition = TIME_FIELD_LNG_05)
	private String stdTime;

	@Column(name = "ADJUST_TIME", columnDefinition = TIME_FIELD_LNG_05)
	private String adjustTime;

	@Column(name = "REG_USER_ID", columnDefinition = USER_ID_LNG_10)
	private String regUserId;

	@Column(name = "REG_DATE")
	private Date regDate;



}
