package com.sdc2ch.service.test;

import com.sdc2ch.service.model.NaviPoint;

import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class TransferLogisticsCenter {
	private NaviPoint point;
	private String name;
	
}
