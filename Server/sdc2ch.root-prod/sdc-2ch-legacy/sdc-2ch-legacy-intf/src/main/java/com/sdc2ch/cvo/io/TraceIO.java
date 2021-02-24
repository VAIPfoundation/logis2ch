package com.sdc2ch.cvo.io;

import java.math.BigDecimal;
import java.util.Date;

public interface TraceIO {
	public Date getDataDate();	
	public BigDecimal getLat();
	public BigDecimal getLng();
	public int getDgree();		
	public int getSpeed();		
	public String getAdres();
	
	public String toCsv();
	public String toHCsv();
	
	public void setVrn(String vrn);

}
