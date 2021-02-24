package com.sdc2ch.prcss.ds.io;

import java.time.LocalDateTime;

public interface RouteStateIO {

	public Long getAllocatedGroupId();
	public LocalDateTime getPlanTime();
	public LocalDateTime getDrvStTime();
	public LocalDateTime getFrontEntTime();
	public LocalDateTime getOfficeEntTime();
	public LocalDateTime getLdngStTime();
	public LocalDateTime getLdngEdTime();
	public LocalDateTime getFrontExiTime();
	public LocalDateTime getDrvEdTime();
	public long getLdngWaitTimeSec();
	public long getLdngTimeSec();
	public long getDlvyStWaitTimeSec();
	public long getPlanLdngDiffTimeSec();

	public String getVrn();
	public String getRouteNo();
	public String getDlvyDe();

}
