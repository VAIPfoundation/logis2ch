package com.sdc2ch.tms.io;

import java.math.BigDecimal;

public interface TmsOilPriceIO {

	String getStartDate();
	String getEndDate();
	BigDecimal getOilPrice();
}
