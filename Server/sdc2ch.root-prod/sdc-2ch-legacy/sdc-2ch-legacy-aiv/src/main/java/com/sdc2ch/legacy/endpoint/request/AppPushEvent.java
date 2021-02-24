package com.sdc2ch.legacy.endpoint.request;

import com.sdc2ch.aiv.event.IFirebaseNotificationEvent;
import com.sdc2ch.require.domain.IUser;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AppPushEvent implements IFirebaseNotificationEvent {
	
	private String appKey;
	private String contents;
	private String mobileNo;
	private Object datas;
	private final IUser user;
	private Priority priority;
	private long allocatedGroupId;

	@Override
	public IUser user() {
		return user;
	}

}
