package com.sdc2ch.web.admin.vo;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sdc2ch.prcss.eb.io.EmptyboxIO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class IndvdVhcleOrderVo {
	private String routeNo;
	private String dlvyLoNm;
	private String dlvyDe;
	private Long dlvyLoId;
	private boolean readOnly;

	
	private Long id;

	
	private int squareBoxQty;

	
	private int triangleBoxQty;

	
	private int yodelryBoxQty;

	
	private int palletQty;

	
	private boolean confirm;


	
	private boolean erpConfirmYn;

	private String regUser;

}
