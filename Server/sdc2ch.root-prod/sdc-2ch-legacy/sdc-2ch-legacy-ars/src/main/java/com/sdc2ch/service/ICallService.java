package com.sdc2ch.service;

import java.util.List;

import com.sdc2ch.api.CallEventSession;
import com.sdc2ch.api.callevent.EICNCallEvent;
import com.sdc2ch.api.command.Command;
import com.sdc2ch.ars.enums.SenderType;

public interface ICallService {
	
	boolean supported(SenderType type);

	List<Command> makeCommand(CallEventSession session);
	List<Command> welcome(CallEventSession session, EICNCallEvent event);

}
