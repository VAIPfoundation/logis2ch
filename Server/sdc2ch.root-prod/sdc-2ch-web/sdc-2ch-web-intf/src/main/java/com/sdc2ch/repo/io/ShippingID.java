package com.sdc2ch.repo.io;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class ShippingID implements Serializable {

	private static final long serialVersionUID = -2789488150669101256L;

	@Column(name = "VRN")
	private String vrn;
	@Column(name = "DRIVER_CD")
	private String driverCd;
	
	public ShippingID() {
		
	}
	public ShippingID(String vrn, String driverCd) {
		this.driverCd = driverCd;
		this.vrn = vrn;
	}
}
