package com.sdc2ch.repo.io;

import java.util.Date;
import java.util.List;

public interface AllocatedGroupIO {

	Long getId();
	String getDlvyDe();

	List<RouteIO> getRouteInfo();
	Date getTrnsmisDt();
	String getDriverCd();
	String getVrn();
}
