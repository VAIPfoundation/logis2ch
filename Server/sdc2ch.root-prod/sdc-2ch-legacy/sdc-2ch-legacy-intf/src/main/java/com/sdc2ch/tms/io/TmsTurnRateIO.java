package com.sdc2ch.tms.io;


public interface TmsTurnRateIO {
	public Long getId();
	public String getFctryCd();
	public String getCaralcTy();
	public int getMinDistance();
	public int getMaxDistance();
	public int getMinLdng();
	public int getMaxLdng();
	public int getMinTime();
	public int getMaxTime();
	public float getTurnRate();
}
