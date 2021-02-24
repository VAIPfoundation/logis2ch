package com.sdc2ch.web.admin.repo.domain;

import static com.sdc2ch.require.repo.schema.GTConfig.MIDDLE_TITLE_LNG_50;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

import com.sdc2ch.repo.io.MobileApkInfoIO;
import com.sdc2ch.require.repo.T_ID;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class T_MOBILE_APK_INFO extends T_ID implements MobileApkInfoIO {
	
	@Column(name = "APK_NAME", columnDefinition = MIDDLE_TITLE_LNG_50)
	private String name;
	@Column(name = "APK_SIZE")
	private int size;
	@Lob
	@Column(name = "APK_BIN")
	private byte[] bin;
	@Column(name = "APK_MAJOR_VER")
	private int majorVer;
	@Column(name = "APK_MINOR_VER")
	private int minorVer;
	@Column(name = "ISCURRENT")
	private boolean current;
	@Column(name = "APK_SHA256")
	private String sha256;
	@Column(name = "APK_MD5")
	private String md5;
	@Column(name = "APK_EXTENSION")
	private String extension;
	
	public String getVersion() {
		return majorVer + "." + minorVer;
	}

}
