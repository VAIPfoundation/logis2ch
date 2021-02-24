package com.sdc2ch.service.event.model;

import java.util.Set;

import com.sdc2ch.ars.enums.CallType;
import com.sdc2ch.ars.enums.SenderType;
import com.sdc2ch.ars.event.IArsEvent;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.tms.io.TmsPlanIO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArsEventImpl implements IArsEvent {

	private CallType callType;
	private String fctryCd;
	private IUser user;
	private String commandName;
	private SenderType senderType;
	private String routeNo;
	private Set<String> callers;
	private TmsPlanIO tmsPlanIo;
	private String driverMobile;
	private String driverNm;
	private String dlvyLcNm;
	private String dlvyLcMobile;
	private String dlvyDe;
	
	@Override
	public IUser user() {
		return user;
	}
}
