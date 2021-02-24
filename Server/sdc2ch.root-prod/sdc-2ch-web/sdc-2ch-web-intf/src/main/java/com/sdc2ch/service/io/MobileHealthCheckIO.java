package com.sdc2ch.service.io;

public interface MobileHealthCheckIO {

	String getDataDate();
	String getMdn();
	String getNetwork();
	Boolean getRunningService();
	Boolean getForgroundService();
	String getBatteryUsage();
	Boolean getDozeMode();
	Boolean getLocEnabled();
	Boolean getCallRecvEnabled();
	String getPermissions();

}
