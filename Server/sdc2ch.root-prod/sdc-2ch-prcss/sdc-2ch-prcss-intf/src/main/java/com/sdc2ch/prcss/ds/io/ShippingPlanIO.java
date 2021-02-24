package com.sdc2ch.prcss.ds.io;

import java.time.LocalDateTime;

import com.sdc2ch.tms.enums.DeliveryType;
import com.sdc2ch.tms.enums.FactoryType;
import com.sdc2ch.tms.enums.SeasonType;
import com.sdc2ch.tms.enums.ShippingType;
import com.sdc2ch.tms.enums.TransportType;

 

public interface ShippingPlanIO {
	
	public Long getTmsPlanRowId();
	
	public String getRouteNo();
	
	public String getDlvyDe();
	
	public String getDriverCd();
	
	public String getVrn();
	
	
	public String getDlvyLcId();
	
	
	public String getDlvyLcNm();
	
	
	public int getDlvyLcSeq();
	
	
	public String getPlannedATime();
	
	public String getPlannedDTime();
	
	public FactoryType getFctryTy();
	
	public ShippingType getShppTy();
	
	public SeasonType getSeasonTy();
	
	public DeliveryType getDeliveryTy();
	
	public TransportType getTransportTy();
	
	
	public boolean isEmpty();
	
	
	int getRouteReIdx();
	int getDlvyLoReIdx();
	
	
	String getBundledDlvyLc();

	
	public boolean isFactory();
	
	public boolean isWareHouse();
	
	public boolean isCustomerCenter();
	
	
	public boolean isFirst();
	public double getLat();
	public double getLng();
	
	public String getCarWegit();
	public String getDockNo();
	
	public int getUniqueSequence();
	
	public FactoryType getMyFacrtyTy();
	public int getRadius();
	public String getLdngSt();
	public String getLdngEd();
	public LocalDateTime getScheDlvyStDt();
	public LocalDateTime getScheDlvyEdDt();
	public String getAddr();
	public String getConfRtateRate();
	public String getConfDistance();
	public String getConfTollCost();
	public String getCarOil();
	
	
	public String getTimeZoneNm();
	
}
