package com.sdc2ch.service.test;

import java.util.List;

import com.sdc2ch.service.test.LgistTransServiceFileImpl.ModelMapper;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class ShipmentVolumeGroup {
	private ModelMapper mapper;
	private List<String> items;
}
