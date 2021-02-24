package com.sdc2ch.mobile.req;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SumEbReq {
	@NotNull
	private String routeNo;
	@NotNull
	private String dlvyDe;
	private String vrn;

}
