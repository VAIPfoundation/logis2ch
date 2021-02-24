package com.sdc2ch.web.admin.repo.domain.lgist;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "T_LGIST_ROUTE_TRANS_DTLS")
@Getter
@Setter
public class T_LGIST_ROUTE_TRANS_DTLS extends com.sdc2ch.require.repo.T_ID {

	@Column(name = "ITEM_CD")
	private String itemCd; 
	@Column(name = "ITEM_NM")
	private String itemNm;
	@Column(name = "SHIPMENT_QTY")
	private int shipmentQty;
	@Column(name = "PALLET_QTY")
	private BigDecimal palletQty;
	
	
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "LGIST_ROUTE_TRANS_MSTR_FK")
	private T_LGIST_ROUTE_TRANS_MSTR lgistRouteTransMstrFk;
}
