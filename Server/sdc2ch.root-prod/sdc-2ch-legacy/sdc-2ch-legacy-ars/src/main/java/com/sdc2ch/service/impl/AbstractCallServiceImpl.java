package com.sdc2ch.service.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;

import com.sdc2ch.api.command.CommandName;
import com.sdc2ch.api.command.GatherCommand;
import com.sdc2ch.api.command.GatherCommand.GatherType;
import com.sdc2ch.api.command.HangupCommand;
import com.sdc2ch.api.command.PauseCommand;
import com.sdc2ch.api.command.RedirectCommand;
import com.sdc2ch.api.command.RouteCommand;
import com.sdc2ch.api.command.SayCommand;
import com.sdc2ch.api.version.Version;
import com.sdc2ch.ars.enums.SenderType;
import com.sdc2ch.require.service.I2ChUserService;
import com.sdc2ch.service.ICallService;
import com.sdc2ch.tms.service.ITmsPlanService;

public abstract class AbstractCallServiceImpl implements ICallService {

	@Autowired I2ChUserService userSvc;
	@Autowired ITmsPlanService planSvc;
	
	
	protected abstract SenderType supportedType();
	



	public HangupCommand createHangUpCommand() {
		return new HangupCommand(CommandName.HANGUP, Version.V20180611);
	}
	public SayCommand createSayCommand(String text) {
		return new SayCommand(CommandName.SAY, Version.V20180611, text);
	}
	public PauseCommand createPauseCommand(int length) {
		return new PauseCommand(CommandName.PAUSE, Version.V20180611, length);
	}
	
	public RedirectCommand createRedirectCommand(String url) {
		return new RedirectCommand(CommandName.REDIRECT, Version.V20180611, url);
		
	}
	public GatherCommand createGatherCommand(GatherType dtmf, int numDigits, int timeout, String text) {
		GatherCommand gather = new GatherCommand(CommandName.GATHER, Version.V20180611);
		gather.setGatherType(GatherType.dtmf);
		gather.setNumDigits(numDigits);
		gather.setTimeout(timeout);
		gather.setText(text);
		return gather;
	}
	
	public RouteCommand createRouteCommand(String tel, String text) {
		RouteCommand cmd = new RouteCommand(CommandName.ROUTE, Version.V20180611);
		cmd.setCalled(tel);
		cmd.setRecord(true);
		cmd.setText(text);
		return cmd;
		
	}






	
	
	protected String makeUrl(String mdn, int port, String callSid)  {
		StringBuilder sb = new StringBuilder();
		InetAddress inetAddress;
		try {
			inetAddress = InetAddress.getLocalHost();
			return sb.append("http:
		} catch (UnknownHostException e) {
			return "http:
		}
	}
	
	protected boolean containsTime() {
		LocalTime now = LocalTime.now();
		return now.getHour() > 19 || now.getHour() < 7;
	}
}
