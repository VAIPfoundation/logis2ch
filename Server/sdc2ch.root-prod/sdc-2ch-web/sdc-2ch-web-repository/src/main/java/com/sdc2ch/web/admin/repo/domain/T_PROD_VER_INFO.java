package com.sdc2ch.web.admin.repo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.sdc2ch.repo.io.ProdVerIO;
import com.sdc2ch.require.repo.T_ID;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class T_PROD_VER_INFO extends T_ID implements ProdVerIO {

	@Column(name = "PROD_TY")
	private String prodTy;
	@Column(name = "IS_CURRENT")
	private boolean current;
	@Column(name = "APK_MAJOR_VER")
	private int majorVer;
	@Column(name = "APK_MINOR_VER")
	private int minorVer;

	public String getVersion() {
		return majorVer + "." + minorVer;
	}

}
