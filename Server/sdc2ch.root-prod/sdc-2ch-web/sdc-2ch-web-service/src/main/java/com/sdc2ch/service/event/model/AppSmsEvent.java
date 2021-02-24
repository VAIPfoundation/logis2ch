package com.sdc2ch.service.event.model;

import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.tms.event.ISmsEvent;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AppSmsEvent implements ISmsEvent {
	
	private String contents;
	private String mobileNo;
	private final IUser user;
	private final String sender;
	private final String senderTel;
	private SmsType smsTy;

	@Getter
	public static class EventData {
		private long id;
		public EventData(long id){
			this.id = id;
		}
	}

	@Override
	public IUser user() {
		return user;
	}


}
