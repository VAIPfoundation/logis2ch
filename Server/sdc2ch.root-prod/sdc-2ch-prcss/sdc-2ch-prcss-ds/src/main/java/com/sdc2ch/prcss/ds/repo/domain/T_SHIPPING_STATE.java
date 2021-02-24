package com.sdc2ch.prcss.ds.repo.domain;

import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_20;
import static com.sdc2ch.require.repo.schema.GTConfig.MIDDLE_CONTENTS_LNG_300;
import static com.sdc2ch.require.repo.schema.GTConfig.YYYYMMDD_08;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sdc2ch.prcss.ds.io.ShippingState;
import com.sdc2ch.prcss.ds.t.chain.ShippingChain.ChainNm;
import com.sdc2ch.repo.io.ShippingID;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
public class T_SHIPPING_STATE {
	
	@EmbeddedId
	@Column(name = "ROW_ID")
	private ShippingID id;
	@Column(name = "ALC_GROUP_ID")
	private Long allocatedGroupId;
	@Column(name = "DLVY_LC_ID")
	private Long dlvyLcId;
	
	@Column(name = "ROUTE_NO")
	private String routeNo;
	
	
	@Column(name = "STATE", columnDefinition = ANY_ENUMS_LNG_20)
	@Enumerated(EnumType.STRING)
	private ShippingState state;
	
	@Column(name = "CHAIN", columnDefinition = ANY_ENUMS_LNG_20)
	@Enumerated(EnumType.STRING)
	private ChainNm chain;
	
	@Column(name = "HINT", columnDefinition = MIDDLE_CONTENTS_LNG_300)
	private String hint;
	
	@Column(name = "DLVY_DE", columnDefinition = YYYYMMDD_08)
	private String dlvyDe;
	
	@Column(name = "ALC_DT")
	private Date allocatedDate;

	@Column(name = "UPDATE_DT")
	@JsonIgnore
	protected Date updateDt;

    @PreUpdate
    protected void onUpdate() {
            this.updateDt = Calendar.getInstance().getTime();
    }
}
