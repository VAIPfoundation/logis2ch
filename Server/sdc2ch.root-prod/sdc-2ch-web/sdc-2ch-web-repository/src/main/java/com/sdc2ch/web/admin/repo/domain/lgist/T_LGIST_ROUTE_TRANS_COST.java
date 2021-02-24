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
@Table(name = "T_LGIST_ROUTE_TRANS_COST")
@Getter
@Setter
public class T_LGIST_ROUTE_TRANS_COST extends com.sdc2ch.require.repo.T_ID {

	@Column(name = "TOT_CAR_CNT")
	private int numOfVehicle; 
	@Column(name = "CAR_TON")
	private BigDecimal carTon; 
	@Column(name = "TOT_TURN_RATE")
	private BigDecimal totTunRate; 
	@Column(name = "CONTRACT_COST")
	private int contractCost; 
	@Column(name = "TOT_DISTANCE")
	private int totDistance; 
	@Column(name = "COST_OF_OIL")
	private int costOfOil; 
	@Column(name = "TOT_OIL_AMT")
	private BigDecimal totOilAmt; 
	@Column(name = "TRANSIT_COST")
	private BigDecimal transitCost; 
	@Column(name = "TOT_OIL_COST")
	private BigDecimal totOilCost;
	@Column(name = "TOT_TOLL_COST")
	private BigDecimal totTollCost;
	@Column(name = "TOT_FULL_COST")
	private BigDecimal totFullCost;
	
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "LGIST_ROUTE_TRANS_MSTR_FK")
	private T_LGIST_ROUTE_TRANS_MSTR lgistRouteTransMstrFk;

	@Override
	public String toString() {
		return "T_LGIST_ROUTE_TRANS_COST [numOfVehicle=" + numOfVehicle + ", carTon=" + carTon + ", totTunRate="
				+ totTunRate + ", contractCost=" + contractCost + ", totDistance=" + totDistance + ", costOfOil="
				+ costOfOil + ", totOilAmt=" + totOilAmt + ", transitCost=" + transitCost + ", totOilCost=" + totOilCost
				+ ", totTollCost=" + totTollCost + ", totFullCost=" + totFullCost + "]";
	}
	
	
}
