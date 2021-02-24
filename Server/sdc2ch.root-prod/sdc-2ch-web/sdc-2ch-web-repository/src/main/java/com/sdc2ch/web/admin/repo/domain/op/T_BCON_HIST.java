package com.sdc2ch.web.admin.repo.domain.op;

import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_20;
import static com.sdc2ch.require.repo.schema.GTConfig.DEVICE_ID_LNG_64;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sdc2ch.require.enums.SetupLcType;
import com.sdc2ch.require.repo.T_ID;
import com.sdc2ch.service.io.BeaconDataIO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(indexes = { @Index(name = "T_BCON_HIST_MDN_01", columnList = "MDN"),
		@Index(name = "T_BCON_HIST_MDN_02", columnList = "MDN,DATA_DT")})
public class T_BCON_HIST extends T_ID implements BeaconDataIO {


	@Column(name = "MDN")
	private String mdn; 		
	@Column(name = "INOUT_TY", columnDefinition = ANY_ENUMS_LNG_20)
	private String inoutTy;
	@Column(name = "BCON_ID", columnDefinition = DEVICE_ID_LNG_64)
	private String bconId;
	@Column(name = "DATA_DT")
	private Date dataDt;

    @Transient
	@Override
	public String getInoutType() {
		return inoutTy;
	}

    @Transient
    @JsonSerialize
    private String vrn;
    @Transient
    @JsonSerialize
    private String driverNm;
    @Transient
    @JsonSerialize
    private String driverCd;
    @Transient
    @JsonSerialize
    private String fctryCd;
    @Transient
    @JsonSerialize
    private SetupLcType setupLc;

    
	@JsonSerialize
	private String getWrhousNm() {
		return this.setupLc != null ? this.setupLc.lcName : null;
	}

	
	@JsonSerialize
	private String getFctryNm() {
		return this.fctryCd != null ? this.findFctryNm(this.fctryCd) : null;
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
}
