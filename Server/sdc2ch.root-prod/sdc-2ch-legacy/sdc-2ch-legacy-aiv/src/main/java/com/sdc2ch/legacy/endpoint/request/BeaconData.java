package com.sdc2ch.legacy.endpoint.request;

import java.util.Date;

import com.sdc2ch.service.io.BeaconDataIO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BeaconData implements BeaconDataIO {

	private String deviceId;
	private String mdn;
	private String inoutType;
	private long timestamp;

	@Override
	public Date getDataDt() {
		return new Date(timestamp);
	}

	@Override
	public String getBconId() {
		return deviceId;
	}
}
