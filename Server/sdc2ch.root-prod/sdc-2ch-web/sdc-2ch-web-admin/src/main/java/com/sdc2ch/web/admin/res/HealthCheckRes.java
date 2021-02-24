package com.sdc2ch.web.admin.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HealthCheckRes {
	private boolean success;
	private String msg;
	private String appId;
	private String appVersion;

}
