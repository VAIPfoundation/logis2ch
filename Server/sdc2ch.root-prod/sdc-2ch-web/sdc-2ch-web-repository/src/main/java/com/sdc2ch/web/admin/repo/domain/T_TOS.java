package com.sdc2ch.web.admin.repo.domain;

import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_20;
import static com.sdc2ch.require.repo.schema.GTConfig.MIDDLE_TITLE_LNG_50;
import static com.sdc2ch.require.repo.schema.GTConfig.USER_ID_LNG_10;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sdc2ch.repo.io.TosIO;
import com.sdc2ch.require.repo.T_ID;
import com.sdc2ch.web.admin.repo.enums.ToSRegEnums;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "T_TOS")
@Getter
@Setter
public class T_TOS extends T_ID implements TosIO {

	@Column(name = "TOS_TITLE", columnDefinition = MIDDLE_TITLE_LNG_50, nullable = false)
	private String title;
	@Lob
	@Column(name = "TOS_CONTENTS")
	private String contents;
	@Column(name = "TOS_REG_TYPE", columnDefinition = ANY_ENUMS_LNG_20, nullable = false)
	@Enumerated(EnumType.STRING)
	private ToSRegEnums regType;
	@Column(name = "TOS_VER_MAJOR", length = 6, nullable = false)
	private int major;
	@Column(name = "TOS_VER_MINOR", length = 1, nullable = false)
	private int minor;
	@Column(name = "REG_USER_ID", columnDefinition = USER_ID_LNG_10)
	private String regUser;
	@Column(name = "REG_USER_DT")
	private Date regDate;
	@Column(name = "TOS_CURRENT")
	private boolean current;

	@JsonSerialize
	private Date getCreateDate() {
		return super.getCreateDt();
	}

	@JsonSerialize
	private Date getUpdateDate() {
		return super.getUpdateDt();
	}

	@JsonSerialize
	public String getRegTypeNm() {
		return regType != null ? regType.tosTyNm : null;
	}
}
