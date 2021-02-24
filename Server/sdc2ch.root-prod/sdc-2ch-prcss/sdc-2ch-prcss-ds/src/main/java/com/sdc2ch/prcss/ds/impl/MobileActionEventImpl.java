package com.sdc2ch.prcss.ds.impl;

import com.sdc2ch.prcss.ds.event.ActionEventType;
import com.sdc2ch.prcss.ds.event.IMobileActionEvent;
import com.sdc2ch.require.domain.IUser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor(staticName="of")
public class MobileActionEventImpl implements IMobileActionEvent {

	private final IUser user;
	private final long timeStamp;
	private final String routeNo;
	@Setter
	private MobileEventActionType mobileEventActionType;
	@Override
	public IUser user() {
		return user;
	}
	@Override
	public ActionEventType getActionEventTy() {
		switch (mobileEventActionType) {
		case ALLOCATE_CONFIRM:
			return ActionEventType.USR_CONFIRM;
		case FINISH_JOB:
			return ActionEventType.USR_FIN;
		case START_JOB:
			return ActionEventType.USR_ST;
		case SYS_FINISH_JOB:
			return ActionEventType.SYS_FIN;
		case ENTER_FACTORY:
			return ActionEventType.USR_ENTER;
		case EXIT_FACTORY:
			return ActionEventType.USR_EXITED;
		default:
			break;
		}
		return ActionEventType.UNKNOWN;
	}
	



	
}
