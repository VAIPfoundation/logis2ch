package com.sdc2ch.prcss.ds.impl;

import com.sdc2ch.prcss.ds.event.ActionEventType;
import com.sdc2ch.prcss.ds.event.IBeconEnterEvent;
import com.sdc2ch.prcss.ds.event.IBeconExitedEvent;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.enums.SetupLcType;
import com.sdc2ch.tms.enums.FactoryType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class BecoEventImpl implements IBeconEnterEvent, IBeconExitedEvent {
	
	private final IUser user;
	@Setter
	private BeconEventType bconEvent;
	@Setter
	private int eventId;
	@Setter
	private long timeStamp;
	@Setter
	private FactoryType factoryType;
	@Setter
	private SetupLcType setupLcTy;

	@Override
	public IUser user() {
		return user;
	}

	@Override
	public ActionEventType getActionEventTy() {
		
		switch (bconEvent) {
		case BCON_ENTER:
			return ActionEventType.BCN_ENTER;
		case BCON_EXITD:
			return ActionEventType.BCN_EXITED;
		default:
			break;
		}
		return ActionEventType.UNKNOWN;
	}

}
