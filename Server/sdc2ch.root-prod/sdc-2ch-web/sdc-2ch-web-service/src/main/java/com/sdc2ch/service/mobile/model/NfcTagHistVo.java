package com.sdc2ch.service.mobile.model;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sdc2ch.nfc.enums.NfcEventType;
import com.sdc2ch.require.enums.SetupLcType;
import com.sdc2ch.tms.enums.FactoryType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class NfcTagHistVo {

	private int id;
	private int devdt;
	private int devuid;
	private String usrid;
	private Integer evt;
	private NfcEventType evtTy;
	private Date srvdt;
	private String vrn;
	private String fctryCd;
	private String driverNm;
	private String driverCd;
	private SetupLcType setupLcTy;

	@JsonSerialize
	public String getFctryNm() {
		return this.fctryCd != null ? FactoryType.convert(this.fctryCd).getName() : null;
	}

	@JsonSerialize
	public String getEvtNm() {
		return this.evtTy != null ? evtTy.getDesc() : null;
	}

	@JsonSerialize
	public String getSetupLcTyNm() {
		return this.setupLcTy != null ? setupLcTy.lcName : null;
	}


	
	
	






























}
