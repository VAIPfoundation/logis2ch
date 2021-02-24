package com.sdc2ch.web.admin.repo.domain.lgist;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "T_LGIST_ROUTE_TRANS_MSTR")
@Getter
@Setter
public class T_LGIST_ROUTE_TRANS_MSTR extends com.sdc2ch.require.repo.T_ID {

	@Column(name = "DLVY_DE")
	private String dlvyDe;
	@Column(name = "SHIP_FROM")
	private String from;
	@Column(name = "SHIP_TO")
	private String to;
	@Column(name = "CAR_TON14")
	private int car_ton14; 
	@Column(name = "CAR_TON8")
	private int car_ton8;
	@Column(name = "CAR_TON7_5")
	private int car_ton7_5;
	@Column(name = "CAR_TON5")
	private int car_ton5;
	@Column(name = "CAR_TON4_5")
	private int car_ton4_5;
	@Column(name = "CAR_TON3_5")
	private int car_ton3_5;
	@Column(name = "CAR_TON2_5")
	private int car_ton2_5;
	@Column(name = "ANALS_MODEL_ID")
	private Long modelId;
	
	
	@JsonSerialize
	@JsonDeserialize
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "lgistRouteTransMstrFk", cascade=CascadeType.ALL )
	private List<T_LGIST_ROUTE_TRANS_DTLS> dtls;
	
	@JsonSerialize
	@JsonDeserialize
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "lgistRouteTransMstrFk", cascade=CascadeType.ALL )
	private List<T_LGIST_ROUTE_TRANS_COST> costs;
}
