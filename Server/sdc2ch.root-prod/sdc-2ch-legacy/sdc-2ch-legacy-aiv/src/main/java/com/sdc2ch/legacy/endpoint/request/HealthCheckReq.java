package com.sdc2ch.legacy.endpoint.request;

import com.sdc2ch.service.io.MobileHealthCheckIO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class HealthCheckReq implements MobileHealthCheckIO{
	private String dataDate;
    private String mdn;
    private String network;
    private Boolean runningService;
    private Boolean forgroundService;
    private String batteryUsage;
    private Boolean dozeMode;
    private Boolean locEnabled;
    private Boolean callRecvEnabled;
    private String permissions;
}
