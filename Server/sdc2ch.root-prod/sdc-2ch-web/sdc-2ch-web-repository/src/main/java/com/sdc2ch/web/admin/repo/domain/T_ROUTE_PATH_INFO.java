package com.sdc2ch.web.admin.repo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sdc2ch.require.repo.T_ID;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "T_ROUTE_PATH_INFO")
@Getter
@Setter
public class T_ROUTE_PATH_INFO extends T_ID {
	
	@Column(name = "ROUTE_NO", length = 20)
	private String routeNo;
	@Column(name = "FREQUENCY", length = 20)
	private String frequency;
	@Column(name = "TOT_DISTANCE", length = 20)
	private String totDistance;
	@Column(name = "TOLL_COST", length = 20)
	private String tollCost;
	@Column(name = "ROUTE_PATH_INFO", length = 2500)
	private String pathInfo;
	@Column(name = "ROUTE_PATHS")
	private String paths;
	@Column(name = "ROUTE_PATH_ID")
	private String pathId;
}
