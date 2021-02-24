package com.sdc2ch.web.admin.repo.domain.lgist;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "T_ROUTE_PATH_MATRIX7")
@Getter
@Setter
public class T_ROUTE_PATH_MATRIX6 extends T_ID {

	@Override
	public String toString() {
		return "T_ROUTE_PATH_MATRIX6 [dlvyDe=" + dlvyDe + ", routeNo=" + routeNo + ", vrn=" + vrn + ", startPos="
				+ startPos + ", startLat=" + startLat + ", startLng=" + startLng + ", endPos=" + endPos + ", endLat="
				+ endLat + ", endLng=" + endLng + ", tmsDistance=" + tmsDistance + ", newDistance=" + newDistance
				+ ", tmsTollCost=" + tmsTollCost + ", newTollCost=" + newTollCost + ", routePathCnt=" + routePathCnt
				+ ", newTotalTime=" + newTotalTime + ", newTurnRate=" + newTurnRate
				+ ", tmsTurnRate=" + tmsTurnRate + ", newSettleCost=" + newSettleCost + ", newFuelCost=" + newFuelCost
				+ ", weight=" + weight + ", supportedCarOil=" + supportedCarOil + ", supportedFreezOil="
				+ supportedFreezOil + ", paymentCost=" + paymentCost + ", idLgistModelFk=" + idLgistModelFk + "]";
	}

	@Column(name = "DLVY_DE", length = 20)
	private String dlvyDe;
	@Column(name = "ROUTE_NO", length = 20)
	private String routeNo;
	@Column(name = "VRN", length = 20)
	private String vrn;

	@Column(name = "START_POS", length = 200)
	private String startPos;
	@Column(name = "START_LAT", length = 20)
	private String startLat;
	@Column(name = "START_LNG", length = 20)
	private String startLng;
	@Column(name = "END_POS", length = 200)
	private String endPos;
	@Column(name = "END_LAT", length = 20)
	private String endLat;
	@Column(name = "END_LNG", length = 20)
	private String endLng;
	@Column(name = "TMS_DISTANCE")
	private int tmsDistance;
	@Column(name = "NEW_DISTANCE")
	private int newDistance;
	@Column(name = "TMS_TOLL_COST")
	private int tmsTollCost;
	@Column(name = "NEW_TOLL_COST")
	private int newTollCost;
	@Column(name = "ROUTE_PATH_COUNT")
	private int routePathCnt;
	@Column(name = "NEW_TOTAL_TIME")
	private int newTotalTime;



	@Lob
	@Column(name = "JSON_DATA")
	private String jsonData;

	@Column(name = "NEW_TURN_RATE")
	private BigDecimal newTurnRate;
	@Column(name = "TMS_TURN_RATE")
	private BigDecimal tmsTurnRate;
	@Column(name = "NEW_SETTLE_COST")
	private BigDecimal newSettleCost;
	@Column(name = "NEW_FUEL_COST")
	private BigDecimal newFuelCost;




	@Column(name = "ITEM_WEIGHT")
	private BigDecimal weight;

	@Column(name = "SUPP_CAR_OIL")
	private BigDecimal supportedCarOil;
	@Column(name = "SUPP_FREEZ_OIL")
	private BigDecimal supportedFreezOil;
	@Column(name = "DAY_PAYMENT_COST")
	private BigDecimal paymentCost;

	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "ID_LGIST_MODEL_FK", referencedColumnName = "ROW_ID")
	private T_LGIST_MODEL idLgistModelFk;

}
