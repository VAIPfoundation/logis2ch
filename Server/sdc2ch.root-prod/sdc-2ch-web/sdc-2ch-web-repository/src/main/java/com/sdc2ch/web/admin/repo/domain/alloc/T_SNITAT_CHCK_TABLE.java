package com.sdc2ch.web.admin.repo.domain.alloc;

import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_20;
import static com.sdc2ch.require.repo.schema.GTConfig.ANY_NAME_LNG_20;
import static com.sdc2ch.require.repo.schema.GTConfig.MIDDLE_CONTENTS_LNG_300;
import static com.sdc2ch.require.repo.schema.GTConfig.MIDDLE_TITLE_LNG_50;
import static com.sdc2ch.require.repo.schema.GTConfig.USER_NAME_LNG_10;
import static com.sdc2ch.require.repo.schema.GTConfig.VRN_LNG_10;
import static com.sdc2ch.require.repo.schema.GTConfig.YYYYMMDD_08;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sdc2ch.require.repo.T_ID;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "T_SNITAT_CHCK_TABLE" , uniqueConstraints=@UniqueConstraint(columnNames={"FCTRY_CD","REG_DE", "VRN"}))
public class T_SNITAT_CHCK_TABLE extends T_ID {

	@Column(name = "FCTRY_CD", columnDefinition = ANY_ENUMS_LNG_20)
	private String fctryCd;

	@Column(name = "VHCLE_TY", columnDefinition = ANY_ENUMS_LNG_20)
	private String vhcleTy;

	@Column(name = "VRN", columnDefinition = VRN_LNG_10)
	private String vrn;

	@Column(name = "DRIVER_NM", columnDefinition = USER_NAME_LNG_10)
	private String driverNm;

	@Column(name = "DRIVER_CD", columnDefinition = MIDDLE_TITLE_LNG_50)
	private String driverCd;

	@Column(name = "TRNSPRT_CMPNY", columnDefinition = ANY_NAME_LNG_20)
	private String trnsprtCmpny;

	@Column(name = "REG_DE", columnDefinition = YYYYMMDD_08)
	private String regDe;

	@Column(name = "ITEM01", columnDefinition = ANY_ENUMS_LNG_20)
	private String item01;

	@Column(name = "ITEM02", columnDefinition = ANY_ENUMS_LNG_20)
	private String item02;

	@Column(name = "ITEM03", columnDefinition = ANY_ENUMS_LNG_20)
	private String item03;

	@Column(name = "ITEM04", columnDefinition = ANY_ENUMS_LNG_20)
	private String item04;

	@Column(name = "ITEM05", columnDefinition = ANY_ENUMS_LNG_20)
	private String item05;

	@Column(name = "ITEM06", columnDefinition = ANY_ENUMS_LNG_20)
	private String item06;

	@Column(name = "ITEM07", columnDefinition = ANY_ENUMS_LNG_20)
	private String item07;

	@Column(name = "ITEM08", columnDefinition = ANY_ENUMS_LNG_20)
	private String item08;

	@Column(name = "ITEM09", columnDefinition = ANY_ENUMS_LNG_20)
	private String item09;

	@Column(name = "ITEM10", columnDefinition = ANY_ENUMS_LNG_20)
	private String item10;

	@Column(name = "RM", columnDefinition = MIDDLE_CONTENTS_LNG_300)
	private String rm;

	@Transient
	@JsonSerialize @JsonDeserialize
	private int resultCd;
	@Transient
	@JsonSerialize @JsonDeserialize
	private String resultMsg;




	@Transient
	@JsonSerialize @JsonDeserialize
	private Integer day;
	@Transient
	@JsonSerialize @JsonDeserialize
	private int month1;
	@Transient
	@JsonSerialize @JsonDeserialize
	private int month2;
	@Transient
	@JsonSerialize @JsonDeserialize
	private int month3;
	@Transient
	@JsonSerialize @JsonDeserialize
	private int month4;
	@Transient
	@JsonSerialize @JsonDeserialize
	private int month5;
	@Transient
	@JsonSerialize @JsonDeserialize
	private int month6;
	@Transient
	@JsonSerialize @JsonDeserialize
	private int month7;
	@Transient
	@JsonSerialize @JsonDeserialize
	private int month8;
	@Transient
	@JsonSerialize @JsonDeserialize
	private int month9;
	@Transient
	@JsonSerialize @JsonDeserialize
	private int month10;
	@Transient
	@JsonSerialize @JsonDeserialize
	private int month11;
	@Transient
	@JsonSerialize @JsonDeserialize
	private int month12;

	@Transient
	@JsonSerialize @JsonDeserialize
	private String dlvyTy;
	@Transient
	@JsonSerialize @JsonDeserialize
	private String dayoffTy;
	@Transient
	@JsonSerialize @JsonDeserialize
	private Double dayoffCnt;
	@Transient
	@JsonSerialize @JsonDeserialize
	private String weekDayKr;
	@Transient
	@JsonSerialize @JsonDeserialize
	private Integer item01cnt1;
	@Transient
	@JsonSerialize @JsonDeserialize
	private Integer item01cnt2;
	@Transient
	@JsonSerialize @JsonDeserialize
	private Integer item01cnt3;
	@Transient
	@JsonSerialize @JsonDeserialize
	private Integer item02cnt1;
	@Transient
	@JsonSerialize @JsonDeserialize
	private Integer item02cnt2;
	@Transient
	@JsonSerialize @JsonDeserialize
	private Integer item02cnt3;
	@Transient
	@JsonSerialize @JsonDeserialize
	private Integer item03cnt1;
	@Transient
	@JsonSerialize @JsonDeserialize
	private Integer item03cnt2;
	@Transient
	@JsonSerialize @JsonDeserialize
	private Integer item03cnt3;
	@Transient
	@JsonSerialize @JsonDeserialize
	private Integer item04cnt1;
	@Transient
	@JsonSerialize @JsonDeserialize
	private Integer item04cnt2;
	@Transient
	@JsonSerialize @JsonDeserialize
	private Integer item04cnt3;
	@Transient
	@JsonSerialize @JsonDeserialize
	private Integer item05cnt1;
	@Transient
	@JsonSerialize @JsonDeserialize
	private Integer item05cnt2;
	@Transient
	@JsonSerialize @JsonDeserialize
	private Integer item05cnt3;
	@Transient
	@JsonSerialize @JsonDeserialize
	private Integer item06cnt1;
	@Transient
	@JsonSerialize @JsonDeserialize
	private Integer item06cnt2;
	@Transient
	@JsonSerialize @JsonDeserialize
	private Integer item06cnt3;
	@Transient
	@JsonSerialize @JsonDeserialize
	private Integer item07cnt1;
	@Transient
	@JsonSerialize @JsonDeserialize
	private Integer item07cnt2;
	@Transient
	@JsonSerialize @JsonDeserialize
	private Integer item07cnt3;
	@Transient
	@JsonSerialize @JsonDeserialize
	private Integer item08cnt1;
	@Transient
	@JsonSerialize @JsonDeserialize
	private Integer item08cnt2;
	@Transient
	@JsonSerialize @JsonDeserialize
	private Integer item08cnt3;
	@Transient
	@JsonSerialize @JsonDeserialize
	private Integer item09cnt1;
	@Transient
	@JsonSerialize @JsonDeserialize
	private Integer item09cnt2;
	@Transient
	@JsonSerialize @JsonDeserialize
	private Integer item09cnt3;
	@Transient
	@JsonSerialize @JsonDeserialize
	private Integer item10cnt1;
	@Transient
	@JsonSerialize @JsonDeserialize
	private Integer item10cnt2;
	@Transient
	@JsonSerialize @JsonDeserialize
	private Integer item10cnt3;
	@Transient
	@JsonSerialize @JsonDeserialize
	private Long carCnt;

}





