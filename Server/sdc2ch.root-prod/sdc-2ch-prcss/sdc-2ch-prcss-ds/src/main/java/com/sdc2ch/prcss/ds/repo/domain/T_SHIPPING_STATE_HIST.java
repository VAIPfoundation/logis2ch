package com.sdc2ch.prcss.ds.repo.domain;

import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_20;
import static com.sdc2ch.require.repo.schema.GTConfig.YYYYMMDD_08;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.sdc2ch.prcss.ds.event.ActionEventType;
import com.sdc2ch.prcss.ds.io.ShippingState;
import com.sdc2ch.prcss.ds.t.chain.ShippingChain.ChainNm;
import com.sdc2ch.require.enums.SetupLcType;
import com.sdc2ch.require.repo.T_ID;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class T_SHIPPING_STATE_HIST extends T_ID {
	
	@Column(name = "ALC_GROUP_ID_FK")
	private Long allocatedGroupId;
	@Column(name = "DLVY_LC_ID_FK")
	private Long dlvyLcId;
	@Column(name = "DLVY_DE", columnDefinition = YYYYMMDD_08)
	private String dlvyDe;
	@Column(name = "ROUTE_NO")
	private String routeNo;
	@Column(name = "CHAIN", columnDefinition = ANY_ENUMS_LNG_20)
	@Enumerated(EnumType.STRING)
	private ChainNm chain;
	@Column(name = "STATE", columnDefinition = ANY_ENUMS_LNG_20)
	@Enumerated(EnumType.STRING)
	private ShippingState state;
	@Column(name = "DATA_TY", columnDefinition = ANY_ENUMS_LNG_20)
	@Enumerated(EnumType.STRING)
	private ActionEventType dataTy;
	@Column(name = "SETUP_LC", columnDefinition = ANY_ENUMS_LNG_20)
	@Enumerated(EnumType.STRING)
	private SetupLcType setupLc;

}
