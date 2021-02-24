package com.sdc2ch.agent.service.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class HealthCheckResVo {
	private boolean success;
	private String msg;
	private String appId;
	private String appVersion;
}
