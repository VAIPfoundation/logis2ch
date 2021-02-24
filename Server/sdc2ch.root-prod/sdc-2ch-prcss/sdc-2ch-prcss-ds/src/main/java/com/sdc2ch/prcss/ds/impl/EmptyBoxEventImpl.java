package com.sdc2ch.prcss.ds.impl;

import com.sdc2ch.prcss.ds.event.ActionEventType;
import com.sdc2ch.prcss.ds.event.IEmptyboxConfirmEvent;
import com.sdc2ch.require.domain.IUser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class EmptyBoxEventImpl implements IEmptyboxConfirmEvent {
	
	private final IUser user;
	@Setter
	private long timeStamp;
	@Setter
	private int uniqueSequence;

	@Override
	public IUser user() {
		return user;
	}

	@Override
	public ActionEventType getActionEventTy() {
		return ActionEventType.USR_EB;
	}

}
