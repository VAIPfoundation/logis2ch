package com.sdc2ch.service.event.model;

import java.util.Date;

import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.service.event.IDriverFinishJobEvent;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor(staticName="of")
public class FinishJobEvent implements IDriverFinishJobEvent {

	private final IUser user;
	private final Date eventDt; 
	private final Long allocatedGroupId; 
	private final MobileEventType mobileEventType;

	@Override
	public IUser user() {
		return user;
	}

}
