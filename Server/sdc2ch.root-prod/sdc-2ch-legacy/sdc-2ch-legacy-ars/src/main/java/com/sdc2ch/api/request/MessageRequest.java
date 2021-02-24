package com.sdc2ch.api.request;

public class MessageRequest {
	
	private String recordId;
	private String callSid;
	private String called;
	private String calledVia;
	
	public String getRecordId() {
		return recordId;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getCallSid() {
		return callSid;
	}
	public void setCallSid(String callSid) {
		this.callSid = callSid;
	}
	public String getCalled() {
		return called;
	}
	public void setCalled(String called) {
		this.called = called;
	}
	public String getCalledVia() {
		return calledVia;
	}
	public void setCalledVia(String calledVia) {
		this.calledVia = calledVia;
	}

}
