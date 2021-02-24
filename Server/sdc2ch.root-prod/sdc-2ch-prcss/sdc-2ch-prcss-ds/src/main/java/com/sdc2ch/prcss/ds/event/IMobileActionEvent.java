package com.sdc2ch.prcss.ds.event;

public interface IMobileActionEvent extends IProcessEvent {
	
	public enum MobileEventActionType {
		START_JOB,
		FINISH_JOB,
		ALLOCATE_CONFIRM,
		SYS_FINISH_JOB,
		ENTER_FACTORY,
		EXIT_FACTORY
	};
	MobileEventActionType getMobileEventActionType();
	String getRouteNo();
}
