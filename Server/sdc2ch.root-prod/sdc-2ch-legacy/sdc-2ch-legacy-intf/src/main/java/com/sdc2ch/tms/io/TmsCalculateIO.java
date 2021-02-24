package com.sdc2ch.tms.io;

import java.math.BigDecimal;

public interface TmsCalculateIO {
	
	public String getDlvyDe();
	public String getFactryCd();
	public BigDecimal getTon();
	public String getRouteNo();
	public String getDest();
	public String getVrn();
	public String getRouteType();
	public String getStartFctryCd();
	public String getEndFctryCd();
	public BigDecimal getTurnRate();
	public BigDecimal getCorverQty();
	public BigDecimal getWeight();
	public BigDecimal getShipPayment();
	public BigDecimal getOilPayment();
	public BigDecimal getSupportOil();
	public BigDecimal getSupportFrezOil();
	public BigDecimal getTmsTollCost();
	public BigDecimal getTmsDistance();
	public BigDecimal getTotalShipCost();
	public BigDecimal getOilCost();
	public String getDesc();

}
