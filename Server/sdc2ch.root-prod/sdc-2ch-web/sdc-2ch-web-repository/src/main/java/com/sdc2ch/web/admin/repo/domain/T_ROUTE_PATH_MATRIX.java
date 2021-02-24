package com.sdc2ch.web.admin.repo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.sdc2ch.require.repo.T_ID;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "T_ROUTE_PATH_MATRIX")
@Getter
@Setter
public class T_ROUTE_PATH_MATRIX extends T_ID {
	
	@Column(name = "ROUTE_PATH_INFO_FK", nullable = false)
	private Long routePathInfoFk;
	@Column(name = "START_POS", length = 20)
	private String startPos;
	@Column(name = "START_LAT", length = 20)
	private String startLat;
	@Column(name = "START_LNG", length = 20)
	private String startLng;
	@Column(name = "END_POS", length = 20)
	private String endPos;
	@Column(name = "END_LAT", length = 20)
	private String endLat;
	@Column(name = "END_LNG", length = 20)
	private String endLng;
	@Column(name = "FREQUENCY", length = 20)
	private String frequency;
	@Column(name = "NEW_TOT_DISTANCE", length = 20)
	private String newTotDistance;
	@Column(name = "NEW_TOLL_COST", length = 20)
	private String newTollCost;
	
	@Column(name = "ORG_TOT_DISTANCE", length = 20)
	private String orgTotDistance;
	@Column(name = "ORG_TOLL_COST", length = 20)
	private String orgTollCost;
	
	@Lob
	@Column(name = "JSON_DATA")
	private String jsonData;

}
