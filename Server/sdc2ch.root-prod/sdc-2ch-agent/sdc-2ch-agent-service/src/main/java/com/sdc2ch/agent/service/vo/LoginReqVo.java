package com.sdc2ch.agent.service.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginReqVo {

	private String username;
	private String password;

}
