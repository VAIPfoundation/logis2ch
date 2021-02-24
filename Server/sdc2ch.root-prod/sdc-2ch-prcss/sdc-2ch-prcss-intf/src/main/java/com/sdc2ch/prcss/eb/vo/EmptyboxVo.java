package com.sdc2ch.prcss.eb.vo;

import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sdc2ch.prcss.eb.io.EmptyboxIO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EmptyboxVo implements EmptyboxIO {
	
	private String routeNo;
	private String dlvyLoNm;
	private String dlvyDe;
	private boolean readOnly;
	
	
	private Long id;

	
	private int squareBoxQty;
	
	
	private int triangleBoxQty;
	
	
	private int yodelryBoxQty;
	
	
	private int palletQty;
	
	
	private Cause cause;
	
	
	private ModifyCause modifyCause;
	
	
	private boolean confirm;
	
	
	
	private boolean erpConfirmYn;
	
	private String regUser;
	
	@JsonIgnore
	private byte[] picture;
	
	@JsonIgnore
	private String bundledDlvyLc;
	@JsonIgnore
	private LocalDateTime arriveTime;
	

	private String stopCd;
	

	private String driverCd;
	
	private Date createDt;
	
	public String getDlvyLoId() {
		return stopCd;
	}

	
	public int hashCode() {
		return (ifNull(routeNo) + ifNull(dlvyDe) + ifNull(stopCd)).hashCode();
	}
	
	private String ifNull(Object o) {
		return o == null ? "" : o.toString();
	}

}
