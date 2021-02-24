package com.sdc2ch.service.test.dao;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferPrinter {
	private String dlvyDe;
	private String from;
	private String to;
	private String vrn;
	private double shippmentQty;
	private double palletQty;
	private BigDecimal carTon; 
	private BigDecimal totTunRate; 
	private int contractCost; 
	private int totDistance; 
	private int costOfOil; 
	private BigDecimal totOilAmt; 
	private BigDecimal transitCost; 
	private BigDecimal totOilCost;
	private BigDecimal totTollCost;
	private BigDecimal totFullCost;
	
	public void print() {
		StringBuilder sb = new StringBuilder();
		sb.append(dlvyDe).append(",");
		sb.append(from).append(",");
		sb.append(to).append(",");
		sb.append(vrn).append(",");
		sb.append(carTon).append(",");
		sb.append(shippmentQty).append(",");
		sb.append(new BigDecimal(palletQty).setScale(2, BigDecimal.ROUND_DOWN)).append(",");
		sb.append(totTunRate).append(",");
		sb.append(contractCost).append(",");
		sb.append(totDistance).append(",");
		sb.append(costOfOil).append(",");
		sb.append(totOilAmt).append(",");
		sb.append(transitCost).append(",");
		sb.append(totOilCost).append(",");
		sb.append(totTollCost).append(",");
		sb.append(totFullCost);
		System.out.println(sb.toString());
	}

	public static void printheader() {
		StringBuilder sb = new StringBuilder();
		sb.append("운행일").append(",");
		sb.append("출발공장").append(",");
		sb.append("도착공장").append(",");
		sb.append("차량번호").append(",");
		sb.append("차량톤수").append(",");
		sb.append("출하량").append(",");
		sb.append("파레트환산량").append(",");
		sb.append("회전율").append(",");
		sb.append("계약용역비").append(",");
		sb.append("운행거리").append(",");
		sb.append("주유단가").append(",");
		sb.append("주유량").append(",");
		sb.append("운송비").append(",");
		sb.append("주유비").append(",");
		sb.append("도로비").append(",");
		sb.append("총비용");
		System.out.println(sb.toString());
	}
}
