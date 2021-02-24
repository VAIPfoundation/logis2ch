package com.sdc2ch.legacy.aiv.push;

import com.sdc2ch.aiv.io.Notification;

public class NotificationDataMessage extends Notification{

	private NotificationData data;

	public NotificationData getData() {
		return data;
	}

	public void setData(NotificationData data) {
		this.data = data;
	}
	
}
