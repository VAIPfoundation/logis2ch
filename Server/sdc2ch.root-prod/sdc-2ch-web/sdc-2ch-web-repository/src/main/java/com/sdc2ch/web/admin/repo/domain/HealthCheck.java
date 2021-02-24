package com.sdc2ch.web.admin.repo.domain;

import java.text.DateFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.google.gson.GsonBuilder;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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
