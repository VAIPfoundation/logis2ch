package com.sdc2ch.legacy.aiv.push;

import com.sdc2ch.aiv.io.Notification.commandType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class NotificationData {
	private commandType command;
	private String body;
	private String title;
}
