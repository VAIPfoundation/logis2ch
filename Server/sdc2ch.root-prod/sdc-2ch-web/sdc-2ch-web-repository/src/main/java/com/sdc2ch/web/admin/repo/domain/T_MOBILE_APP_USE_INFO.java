package com.sdc2ch.web.admin.repo.domain;

import static com.sdc2ch.require.repo.schema.GTConfig.MIDDLE_CONTENTS_LNG_300;
import static com.sdc2ch.require.repo.schema.GTConfig.SHOT_CONTENTS_LNG_100;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.sdc2ch.repo.io.MobileApkInfoIO;
import com.sdc2ch.repo.io.MobileAppInfoIO;
import com.sdc2ch.repo.io.TosIO;
import com.sdc2ch.require.repo.T_ID;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class T_MOBILE_APP_USE_INFO extends T_ID implements MobileAppInfoIO {
	
	@Column(name = "USER_DETAIL_FK")
	private String userId;
	@Column(name = "MOBILE_OS_NAME")
	private String osName;
	@Column(name = "MOBILE_OS_VER")
	private String osVer;
	@Column(name = "MOBILE_MODEL", columnDefinition = SHOT_CONTENTS_LNG_100)
	private String model;
	@Column(name = "MOBILE_TELCO", columnDefinition = SHOT_CONTENTS_LNG_100)
	private String telCo;
	@Column(name = "MOBILE_APP_TOKEN", columnDefinition = MIDDLE_CONTENTS_LNG_300)
	private String appTkn;
	@Column(name = "MOBILE_APP_TOKEN_VALID")
	private boolean validTkn;
	
	@ManyToOne(targetEntity = T_MOBILE_APK_INFO.class)
	@JoinColumn(name = "APP_INFO_FK")
	private MobileApkInfoIO apk;
	
	@ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER, targetEntity = T_TOS.class)
	@JoinTable(name = "T_MOBILE_APP_USE_INFO_TOS_MAP")
	private List<TosIO> tosses = new ArrayList<>();

}
