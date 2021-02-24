package com.sdc2ch.api;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdc2ch.api.callevent.EICNCallEvent;
import com.sdc2ch.api.command.Command;
import com.sdc2ch.api.request.EventRequest;
import com.sdc2ch.api.response.MessageResponse;
import com.sdc2ch.ars.enums.SenderType;
import com.sdc2ch.ars.event.IArsEvent;
import com.sdc2ch.require.service.I2ChUserService;
import com.sdc2ch.service.ICallService;
import com.sdc2ch.tms.service.ITmsPlanService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/ivr")
@Slf4j
public class IVRCallEventEndpoint {

	@Autowired EventSessionManager sessionMgr;
	@Autowired I2ChUserService userSvc;
	@Autowired ITmsPlanService planSvc;

	@Autowired List<ICallService> callSvc;

	@ApiOperation(value = "2CH IVR연동 Example - say command 응답의 redirect url")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "callInfo", value = "콜러의 정보", required = true)})
	@RequestMapping(value = "/call/event", method = RequestMethod.POST)
	public @ResponseBody MessageResponse callEvent (
			@RequestBody(required = true) EventRequest callInfo,
			HttpServletResponse res, HttpServletRequest req) throws Exception {

		log.info("## ARS ## /call/event 진입, call Event is null={}", callInfo.getEvent() == null);

		MessageResponse response = new MessageResponse();

		if(callInfo.getEvent() == null) {
			
			res.sendError(403, "event[] can not be null");
			return null;
		}


		List<Command> cmds = new ArrayList<>();
		response.setCallSid(callInfo.getCallSid());
		response.setCmds(cmds);
		int port = req.getLocalPort();
		callInfo.setPort(port);

		EICNCallEvent event = callInfo.getEvent().stream().findFirst().orElse(null);

		
		String mdn = event.getPhoneNumber();

		CallEventSession session = sessionMgr.getSession(mdn);

		log.info("## ARS ## /call/event mdn={}, sId={}, sessionIsNull?={}", mdn, callInfo.getCallSid(), (session == null) );

		if(session == null)
			session = sessionMgr.createSession(mdn);
		
		SenderType type  = null;
		session.setRequest(callInfo);
		session.addCount();

		
		IArsEvent callEvent = session.getEvent();
		type = callEvent == null ? SenderType.FFFF : callEvent.getSenderType();
		ICallService svc = findService(type);
		if(svc != null) {
			cmds = svc.welcome(session, event);
			response.setCmds(cmds);
		}

		return response;
	}

	@ApiOperation(value = "2CH IVR연동 command 응답의 redirect url")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "callSid", value = "콜러의 세션아이디", required = true, dataType = "string", paramType = "path", defaultValue = "")})
	@RequestMapping(value = "/redirect/{callSid}/{phoneNumber}", method = {RequestMethod.POST, RequestMethod.GET})
	public MessageResponse sayredirect(
			@PathVariable(required = true, value = "callSid") String callSid,
			@PathVariable(required = true, value = "phoneNumber") String phoneNumber,
			HttpServletResponse res) {

		log.info("## ARS ## /redirect/callSid/phoneNumber진입 => /redirect/{}/{}",callSid,phoneNumber);

		MessageResponse response = new MessageResponse();
		List<Command> cmds = new ArrayList<>();
		response.setCallSid(callSid);
		response.setCmds(cmds);

		CallEventSession session = sessionMgr.getSession(phoneNumber);

		SenderType type  = null;
		
		IArsEvent callEvent = session.getEvent();
		type = callEvent == null ? SenderType.FFFF : callEvent.getSenderType();
		ICallService svc = findService(type);
		if(svc != null) {
			cmds = svc.makeCommand(session);
			response.setCmds(cmds);
		}

		return response;
	}

	private ICallService findService(SenderType type) {
		return callSvc.stream().filter(s -> s.supported(type)).findFirst().orElse(null);
	}

	public static void main(String[] args) {
		System.out.println(String.format("%s", "1111"));
	}
}
