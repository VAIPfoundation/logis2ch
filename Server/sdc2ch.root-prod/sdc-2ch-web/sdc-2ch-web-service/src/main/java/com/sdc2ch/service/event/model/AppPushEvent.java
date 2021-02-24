package com.sdc2ch.service.event.model;

import com.sdc2ch.aiv.event.IFirebaseNotificationEvent;
import com.sdc2ch.require.domain.IUser;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AppPushEvent implements IFirebaseNotificationEvent {
	
	public enum PushType {
		
		ALLOCATED_CONFIRM,
		
		ALLOCATED_CALCELED
	}
	
	private String appKey;
	private String contents;
	private String mobileNo;
	private Object datas;
	private final IUser user;
	private PushType pushType;
	private Priority priority;

	@Override
	public IUser user() {
		return user;
	}

}
