package com.sdc2ch.require;

public class MyApplicationEvent {
	private long timestamp;
	private ApplicationEventType event;

	public MyApplicationEvent(long timestamp, ApplicationEventType event){
		this.timestamp = timestamp;
		this.event = event;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public ApplicationEventType getEvent() {
		return event;
	}

	@Override
	public String toString() {
		return "MyApplicationEvent [timestamp=" + timestamp + ", event=" + event + "]";
	}
	
}
