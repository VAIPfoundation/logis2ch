package com.sdc2ch.prcss.eb.vo;

import java.util.ArrayList;
import java.util.List;

import com.sdc2ch.prcss.eb.io.EmptyboxIO.Cause;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SummaryEmptyboxVo {
	
	private String routeNo;
	private String dlvyDe;

	
	private int squareBoxQty;
	
	
	private int triangleBoxQty;
	
	
	private int yodelryBoxQty;
	
	
	private int palletQty;
	
	
	private int driverSquareBoxQty;
	
	
	private List<Cause> causes = new ArrayList<>();
	
	
	private List<EmptyboxVo> emptyboxList = new ArrayList<>();
	
	private String regUser;

}
