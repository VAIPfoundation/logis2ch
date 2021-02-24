package com.sdc2ch.web.admin.repo.domain.anals;

import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_20;
import static com.sdc2ch.require.repo.schema.GTConfig.FACTORY_CD_LNG_04;
import static com.sdc2ch.require.repo.schema.GTConfig.ROUTE_NO_LNG_07;
import static com.sdc2ch.require.repo.schema.GTConfig.USER_ID_LNG_10;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sdc2ch.require.repo.T_ID;
import com.sdc2ch.service.grade.io.AnalsStdGradeIO;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "T_ANALS_LDNG_STD_TIME")
@Getter
@Setter
public class T_ANALS_LDNG_STD_TIME extends T_ID implements AnalsStdGradeIO {


	@Column(name = "FCTRY_CD", columnDefinition = FACTORY_CD_LNG_04)
	private String fctryCd;

	@Column(name = "VHCLE_TY")
	private Float vhcleTy;

	@Column(name = "CARALC_TY", columnDefinition = ANY_ENUMS_LNG_20)
	private String caralcTy;

	@Column(name = "ROUTE_NO", columnDefinition = ROUTE_NO_LNG_07)
	private String routeNo;




	@Column(name = "STD_TIME")
	private Long stdTime;




	@Column(name = "ADJUST_TIME")
	private Long adjustTime;

	@Column(name = "USE_YN")
	@ColumnDefault(value = "1")
	private boolean useYn;

	@Column(name = "IS_BASE")
	@ColumnDefault(value = "0")
	private boolean base;

	@Column(name = "REG_USER_ID", columnDefinition = USER_ID_LNG_10)
	private String regUserId;

	@Column(name = "REG_DT")
	private Date regDt;


	@Transient
	@JsonDeserialize
	@JsonSerialize
	private String caralcTyNm;

}
