package com.sdc2ch.service.event.model;

import java.util.Date;

import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.service.event.IDriverExitFactoryEvent;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor(staticName="of")
public class ExitFactoryEvent implements IDriverExitFactoryEvent {

	private final IUser user;
	private final Date eventDt;
	private final Long allocatedGroupId;
	private final String routeNo;
	private final String dlvyDe;
	private final MobileEventType mobileEventType;
	private final String fctryCd;

	@Override
	public IUser user() {
		return user;
	}

}
