package com.sdc2ch.repo.io;

import java.math.BigDecimal;

public interface TmsCarIO {
	Long getId();
	String getVrn();
	BigDecimal getWegith();
	String getMobileNo();
	String getDriverCd();
	String getDriverNm();

	String getTrnsprtCmpny();
	String getLdngTy();
}
