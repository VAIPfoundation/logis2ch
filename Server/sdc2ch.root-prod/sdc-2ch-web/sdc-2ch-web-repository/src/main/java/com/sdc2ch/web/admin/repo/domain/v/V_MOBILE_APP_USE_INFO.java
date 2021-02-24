package com.sdc2ch.web.admin.repo.domain.v;

import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_20;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class V_MOBILE_APP_USE_INFO {

	@Id
	@Column(name = "ROW_ID", updatable = false, nullable = false)
	private Long id;

	@Column(name = "FCTRY_CD", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String fctryCd;
	@Column(name = "FCTRY_NM", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String fctryNm;
	@Column(name = "VHCLE_TY", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String vhcleTy;
	@Column(name = "VRN", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String vrn;
	@Column(name = "DRIVER_CD", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String driverCd;
	@Column(name = "DRIVER_NM", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String driverNm;
	@Column(name = "MOBILE_NO", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String mobileNo;
	@Column(name = "MOBILE_MODEL", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String mobileModel;
	@Column(name = "MOBILE_OS_NAME", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String mobileOsName;
	@Column(name = "MOBILE_OS_VER", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String mobileOsVer;
	@Column(name = "MOBILE_TELCO", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String mobileTelco;
	@Column(name = "APK_VER", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String apkVer;
	@Column(name = "STPLAT_AGRE_DT", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private Date stplatAgreDt;

}
