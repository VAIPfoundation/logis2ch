package com.sdc2ch.api.version;

public enum Version {
	V20180611("V20180611");
	
	private String version;
	Version(String version){
		this.version = version;
	}
	public String getVersion() {
		return version;
	}
	
	@Override
	public String toString() {
		return version;
	}
}
