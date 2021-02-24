package com.sdc2ch.repo.io;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class RouteStateID implements Serializable {

	private static final long serialVersionUID = -2789488150669101256L;

	@Column(name = "VRN")
	private String vrn;
	@Column(name = "ROUTE_NO")
	private String routeNo;
	@Column(name = "DLVY_DE")
	private String dlvyDe;
	
	public RouteStateID() {
		
	}
	public RouteStateID(String vrn, String routeNo, String dlvyDe) {
		this.routeNo = routeNo;
		this.vrn = vrn;
		this.dlvyDe = dlvyDe;
	}
}
