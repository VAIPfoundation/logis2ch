package com.sdc2ch.legacy.endpoint.request;

import java.text.DateFormat;

import com.google.gson.GsonBuilder;

public class RefreshTokenRequest {
	
    private String osvn;
    private String osnm;
    private String model;
    private String mdn;
    private String bdvn;
    private String tokenID;
    private String telco;
    private String appvn;

    public String getOsvn() {
        return osvn;
    }
    public void setOsvn(String osvn) {
        this.osvn = osvn;
    }
    public String getOsnm() {
        return osnm;
    }
    public void setOsnm(String osnm) {
        this.osnm = osnm;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public String getMdn() {
        return mdn;
    }
    public void setMdn(String mdn) {
        this.mdn = mdn;
    }
    public String getBdvn() {
        return bdvn;
    }
    public void setBdvn(String bdvn) {
        this.bdvn = bdvn;
    }
    public String getTokenID() {
        return tokenID;
    }
    public void setTokenID(String tokenID) {
        this.tokenID = tokenID;
    }
	public String getTelco() {
		return telco;
	}
	public void setTelco(String telco) {
		this.telco = telco;
	}
	public String getAppvn() {
		return appvn;
	}
	public void setAppvn(String appvn) {
		this.appvn = appvn;
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
