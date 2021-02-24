package com.sdc2ch.legacy.aiv.push;

import com.sdc2ch.aiv.io.Notification;

public class NotificationPushMessage extends Notification{

	
	private final String body;
	private final String title;
	
	public NotificationPushMessage(String body, String title) {
		this.body = body;
		this.title = title;
	}
	public String getBody() {
		return body;
	}
	public String getTitle() {
		return title;
	}

}
