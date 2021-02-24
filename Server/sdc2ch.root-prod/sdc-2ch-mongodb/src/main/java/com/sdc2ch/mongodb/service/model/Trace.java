package com.sdc2ch.mongodb.service.model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sdc2ch.cvo.io.TraceIO;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Trace implements TraceIO{
	
	private String vrn;
	private Date dataDate;	
	private BigDecimal lat;
	private BigDecimal lng;
	private int dgree;		
	private int speed;		
	private String adres;
	
	public String toCsv() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(vrn).append(",");
		sb.append(dateFormat(dataDate)).append(",");
		sb.append(setScale(lat)).append(",");
		sb.append(setScale(lng)).append(",");
		sb.append(dgree).append(",");
		sb.append(speed).append(",");
		sb.append(adres);
		return sb.toString();
	}
	
	private String setScale(BigDecimal b) {
		if(b == null) {
			return "0.0";
		}else {
			return b.setScale(6, BigDecimal.ROUND_DOWN).toString();
		}
	}
	
	private String dateFormat(Date dt) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dt);
	}

	@Override
	public String toHCsv() {
		StringBuilder sb = new StringBuilder();
		sb.append("차량번호").append(",");
		sb.append("시각").append(",");
		sb.append("위도").append(",");
		sb.append("경도").append(",");
		sb.append("방위각").append(",");
		sb.append("속도").append(",");
		sb.append("위치");
		return sb.toString();
	}

	@Override
	public void setVrn(String vrn) {
		this.vrn = vrn;
	}
}
