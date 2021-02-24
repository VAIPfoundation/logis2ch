package com.sdc2ch.prcss.ds.repo.domain;

import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_20;
import static com.sdc2ch.require.repo.schema.GTConfig.ROUTE_NO_LNG_07;
import static com.sdc2ch.require.repo.schema.GTConfig.TIME_LNG_04;
import static com.sdc2ch.require.repo.schema.GTConfig.VRN_LNG_10;
import static com.sdc2ch.require.repo.schema.GTConfig.YYYYMMDD_08; 
import static com.sdc2ch.require.repo.schema.GTConfig.SHORT_TITLE_LNG_30;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sdc2ch.prcss.ds.io.ShippingState;
import com.sdc2ch.require.repo.T_ID;
import com.sdc2ch.tms.enums.ShippingType;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@Entity
@Table(name = "T_DAILY_VHCLE_DRIVE_DTLS_HIST")
@EqualsAndHashCode(callSuper=false)
public class T_DAILY_VHCLE_DRIVE_DTLS_HIST extends T_ID {
	
	@Column(name = "SHIP_TY", columnDefinition = ANY_ENUMS_LNG_20)	
	@Enumerated(EnumType.STRING)
	private ShippingType shipTy;
	@Column(name = "DLVY_DE", columnDefinition = YYYYMMDD_08)	
	private String dlvyDe;
	@Column(name = "ROUTE_NO", columnDefinition = ROUTE_NO_LNG_07)	
	private String routeNo;
	@Column(name = "VRN", columnDefinition = VRN_LNG_10)	
	private String vrn;
	@Enumerated(EnumType.STRING)
	@Column(name = "PROGRS_STTUS", columnDefinition = ANY_ENUMS_LNG_20)	
	private ShippingState progrssttus;
	@Column(name = "LC")	
	private String lc;
	@Column(name = "EVENT_TY")	
	private String eventty;
	@Column(name = "EVENT_DT")	
	private Date eventdt;
	@Column(name = "RM")	
	private String rm;
	@Column(name = "DATA_TY", columnDefinition = SHORT_TITLE_LNG_30)	
	private String dataTy;
	@Column(name = "CARALC_GROUP_ID_FK")	
	private Long caralcGroupId;
}
