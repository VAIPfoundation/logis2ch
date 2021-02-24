package com.sdc2ch.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdc2ch.api.CallEventSession;
import com.sdc2ch.api.callevent.EICNCallEvent;
import com.sdc2ch.api.command.Command;
import com.sdc2ch.ars.enums.SenderType;

@Service
public class DefaultCallServiceImpl extends AbstractCallServiceImpl {

	private SenderType supported = SenderType.FFFF;
	
	@Autowired CustomerCallServiceImpl customerCall;
	@Autowired DriverCallServiceImpl driverCall;
	
	@Override
	public boolean supported(SenderType type) {
		return supported == type;
	}
	
	@Override
	public List<Command> makeCommand(CallEventSession session) {
		
		return isDriverScenario(session.getCaller()) ? driverCall.makeCommand(session)
				: customerCall.makeCommand(session);
	}
	
	@Override
	public List<Command> welcome(CallEventSession session, EICNCallEvent event) {
		session.setCaller(event.getPhoneNumber());
		
		return isDriverScenario(event.getPhoneNumber()) ? driverCall.welcome(session, event)
				: customerCall.welcome(session, event);
	}

	@Override
	protected SenderType supportedType() {
		return supported;
	}
	
	
	private boolean isDriverScenario(String mdn) {
		return userSvc.findByMobileNo(mdn).orElse(null) != null;
	}

}
