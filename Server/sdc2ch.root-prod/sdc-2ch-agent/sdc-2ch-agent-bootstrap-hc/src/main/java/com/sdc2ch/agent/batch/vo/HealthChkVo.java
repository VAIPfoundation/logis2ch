package com.sdc2ch.agent.batch.vo;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HealthChkVo {

	private boolean success;
	private String message;
	private int statusCd;
	private Date lastSendSmsDt;

}
