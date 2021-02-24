package com.sdc2ch.web.admin.repo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.sdc2ch.require.repo.T_ID;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "T_ROUTE_PATH_MATRIX3")
@Getter
@Setter
public class T_ROUTE_PATH_MATRIX3 extends T_ID {
	
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
	@Column(name = "TURN_RATE", length = 20)
	private double turnRate;
	@Column(name = "FUEL_AMOUTN", length = 20)
	private double fuelAmt;
	@Column(name = "FUEL_COST")
	private int fuelCost;
	@Column(name = "TRANSPORT_COST")
	private double transportCost;
	@Column(name = "ROUTE_PATH_COUNT")
	private int routePathCnt;
	
	@Lob
	@Column(name = "JSON_DATA")
	private String jsonData;

	@Override
	public String toString() {
		return "T_ROUTE_PATH_MATRIX3 [dlvyDe=" + dlvyDe + ", routeNo=" + routeNo + ", vrn=" + vrn + ", startPos="
				+ startPos + ", startLat=" + startLat + ", startLng=" + startLng + ", endPos=" + endPos + ", endLat="
				+ endLat + ", endLng=" + endLng + ", tmsDistance=" + tmsDistance + ", newDistance=" + newDistance
				+ ", tmsTollCost=" + tmsTollCost + ", newTollCost=" + newTollCost + ", turnRate=" + turnRate
				+ ", fuelAmt=" + fuelAmt + ", fuelCost=" + fuelCost + ", transportCost=" + transportCost + "]";
	}
}
