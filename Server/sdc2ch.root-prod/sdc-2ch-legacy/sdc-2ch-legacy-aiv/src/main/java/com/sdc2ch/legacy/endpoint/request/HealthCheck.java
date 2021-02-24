package com.sdc2ch.legacy.endpoint.request;

import java.text.DateFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.google.gson.GsonBuilder;

@Entity
public class HealthCheck {

	@Id
	@GeneratedValue
	private long id;
    private String dataDate;
    private String mdn;
    private NetworkType network;
    private boolean runningService;
    private boolean forgroundService;
    private String batteryUsage;
    private boolean dozeMode;
    private boolean locEnabled;
    private boolean callRecvEnabled;
    private String permissions;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDataDate() {
		return dataDate;
	}
	public void setDataDate(String dataDate) {
		this.dataDate = dataDate;
	}
	public String getMdn() {
		return mdn;
	}
	public void setMdn(String mdn) {
		this.mdn = mdn;
	}
	public NetworkType getNetwork() {
		return network;
	}
	public void setNetwork(NetworkType network) {
		this.network = network;
	}
	public String getBatteryUsage() {
		return batteryUsage;
	}
	public void setBatteryUsage(String batteryUsage) {
		this.batteryUsage = batteryUsage;
	}
	public boolean isDozeMode() {
		return dozeMode;
	}
	public void setDozeMode(boolean dozeMode) {
		this.dozeMode = dozeMode;
	}
	public boolean isRunningService() {
		return runningService;
	}
	public void setRunningService(boolean runningService) {
		this.runningService = runningService;
	}
	public boolean isForgroundService() {
		return forgroundService;
	}
	public void setForgroundService(boolean forgroundService) {
		this.forgroundService = forgroundService;
	}
	public boolean isLocEnabled() {
		return locEnabled;
	}
	public void setLocEnabled(boolean locEnabled) {
		this.locEnabled = locEnabled;
	}
	public boolean isCallRecvEnabled() {
		return callRecvEnabled;
	}
	public void setCallRecvEnabled(boolean callRecvEnabled) {
		this.callRecvEnabled = callRecvEnabled;
	}
	public String getPermissions() {
		return permissions;
	}
	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}
	@Override
	public String toString() {
		return new GsonBuilder()

			     .enableComplexMapKeySerialization()
			     .serializeNulls()
			     .setDateFormat(DateFormat.LONG)

			     .setPrettyPrinting()
			     .setVersion(1.0)
			     .create().toJson(this);
	}

}
