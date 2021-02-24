package com.sdc2ch.service.transfer;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class TransferPlan {
	private String itemCd;
	private String itemNm;
	private double ratioBy1D1;
	private double ratioBy2D1;
	private double ratioBy3D1;
	private double ratioBy4D1;
	
	public double transferRatio(FactoryType t) {
		switch (t) {
		case _1D1:
			return ratioBy1D1;
		case _2D1:
			return ratioBy2D1;
		case _3D1:
			return ratioBy3D1;
		case _4D1:
			return ratioBy4D1;
		case FFFF:
		default:
			break;
		}
		return 0;
	}

}
