package com.sdc2ch.api.response;

import java.util.List;

import com.sdc2ch.api.command.Command;

import io.swagger.annotations.ApiModelProperty;

public class MessageResponse {
	
    @ApiModelProperty(required = true, value = "caller session id(전화연결이 끊킬때까지 유지)")
	private String callSid;
    @ApiModelProperty(required = true, value = "IVR서버에 전달할 커맨드 객체의 집합")
	private List<Command> cmds;
    @ApiModelProperty(required = true, value = "응답 메시지 버전 - 필드가 변경되면 응답 메시지 버전도 변경됩니다.")
    private String version = "V20180611";
	
	public String getCallSid() {
		return callSid;
	}
	public void setCallSid(String callSid) {
		this.callSid = callSid;
	}
	public List<Command> getCmds() {
		return cmds;
	}
	public void setCmds(List<Command> cmds) {
		this.cmds = cmds;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
}
