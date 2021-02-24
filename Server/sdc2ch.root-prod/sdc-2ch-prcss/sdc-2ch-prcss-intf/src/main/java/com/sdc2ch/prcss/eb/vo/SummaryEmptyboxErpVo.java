package com.sdc2ch.prcss.eb.vo;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SummaryEmptyboxErpVo extends SummaryEmptyboxVo {


	
	private int squareBoxErpQty;

	
	private int triangleBoxErpQty;

	
	private int yodelryBoxErpQty;

	
	private int palletErpQty;

	
	private List<EmptyboxErpVo> emptyboxErpList = new ArrayList<>();



}
