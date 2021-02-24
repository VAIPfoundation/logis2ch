package com.sdc2ch.web.admin.repo.domain.op;

import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_20;
import static com.sdc2ch.require.repo.schema.GTConfig.ANY_NAME_LNG_20;
import static com.sdc2ch.require.repo.schema.GTConfig.FACTORY_CD_LNG_04;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sdc2ch.repo.io.BeaconMappingIO;
import com.sdc2ch.require.enums.SetupLcType;
import com.sdc2ch.require.repo.T_ID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class T_BCON_MAPPING extends T_ID implements BeaconMappingIO {

	@Column(name = "SETUP_LC", columnDefinition = ANY_ENUMS_LNG_20)
	@Enumerated(EnumType.STRING)
	private SetupLcType setupLc;
	@Column(name="BCON_ID", unique=true)
	private String bconId;
	@Column(name = "BCON_NM", columnDefinition = ANY_NAME_LNG_20)
	private String bconName;
	@Column(name = "FCTRY_CD", columnDefinition = FACTORY_CD_LNG_04)
	private String fctryCd;

	
	@JsonSerialize
	private String getWrhousNm() {
		return this.setupLc != null ? this.setupLc.lcName : null;
	}

	
	@JsonSerialize
	private String getFctryNm() {
		return this.findFctryNm(fctryCd);
	}

	private String findFctryNm(String code) {
		switch(code) {
		case "1D1" :
			return "양주공장";
		case "2D1" :
			return "용인공장";
		case "3D1" :
			return "안산공장";
		case "4D1" :
			return "거창공장";
		case "5D1" :
			return "양주신공장";
		}
		return null;
	}


	@JsonSerialize
	private Date getCreateDate() {
		return super.getCreateDt();
	}

	@JsonSerialize
	private Date getUpdateDate() {
		return super.getUpdateDt();
	}

}
