package com.sdc2ch.tms.io;


public interface TmsContractIO {
	public Long getId();
	public int getContractNo();
	public String getTComCd();
	public String getContType();
	public String getStartDate();
	public String getEndDate();
	public boolean isCancel();
	public String getCancelDate();
	public float getCarWeight();
	public String getCarType();
	public String getOffDutyType();
	public Integer getMonthCost();
	public Integer getDayCost();
}
