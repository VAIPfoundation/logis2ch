package com.sdc2ch.service.model;

import java.util.List;

import com.sdc2ch.service.factory.CarTon;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TmapApiNavigationParam {
	private NaviPoint start;
	private NaviPoint end;
	private List<NaviPoint> paths;
	private CarTon carTon;
}
