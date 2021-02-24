package com.sdc2ch.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sdc2ch.api.command.CallCommand;
import com.sdc2ch.api.command.Command;
import com.sdc2ch.api.command.CommandName;
import com.sdc2ch.api.command.GatherCommand;
import com.sdc2ch.api.command.GatherCommand.GatherType;
import com.sdc2ch.api.command.HangupCommand;
import com.sdc2ch.api.command.PauseCommand;
import com.sdc2ch.api.command.PlayCommand;
import com.sdc2ch.api.command.PlayCommand.MimeType;
import com.sdc2ch.api.command.RecordCommand;
import com.sdc2ch.api.command.RedirectCommand;
import com.sdc2ch.api.command.RejectCommand;
import com.sdc2ch.api.command.RouteCommand;
import com.sdc2ch.api.command.SayCommand;
import com.sdc2ch.api.response.MessageResponse;
import com.sdc2ch.api.version.Version;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/ivr")
public class IVRController {
	
	@ApiOperation(value = "2CH IVR연동 데이터 구조체 - 서버에서 응답하는 메시지의 구조를 정의한다. command name은 하단의 'Model|Example Value'링크의 Model을 누르면 확인 가능하다.")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "command", value = "커맨드명", required = true, dataType = "string", paramType = "path", defaultValue = ""),
			@ApiImplicitParam(name = "callSid", value = "콜러의 세션아이디", required = true, dataType = "string", paramType = "query", defaultValue = "")})
	@RequestMapping(value = "/list/{command}", method = RequestMethod.GET)
	public MessageResponse list(@PathVariable String command, 
			@RequestParam(required = true, value = "callSid") String callSid,
			HttpServletResponse res) {

		MessageResponse response = new MessageResponse();
		List<Command> cmds = new ArrayList<>();
		response.setCallSid(callSid);
		response.setCmds(cmds);
		
		CommandName name = null;
		try {
			name = CommandName.valueOf(command);
			
			switch (name) {
			case CALL:
				
				CallCommand call = new CallCommand(name, Version.V20180611);
				call.setCalled("01092454214");
				cmds.add(call);
				
				break;
			case GATHER:
				GatherCommand gather = new GatherCommand(name, Version.V20180611);
				gather.setGatherType(GatherType.dtmf);
				gather.setNumDigits(1);
				gather.setTimeout(10);
				cmds.add(gather);
				break;
			case HANGUP:
				HangupCommand hangup = new HangupCommand(name, Version.V20180611);
				cmds.add(hangup);
				break;
			case PAUSE:
				PauseCommand pause = new PauseCommand(name, Version.V20180611);
				pause.setLength(1);
				cmds.add(pause);
				break;
			case PLAY:
				
				PlayCommand play = new PlayCommand(name, Version.V20180611);
				play.setLoop(0);
				play.setMimeType(MimeType.audiompeg);
				play.setUrl("http:
				cmds.add(play);
				break;
			case RECORD:
				
				RecordCommand record = new RecordCommand(name, Version.V20180611);
				cmds.add(record);
				break;
			case REDIRECT:
				RedirectCommand redirect = new RedirectCommand(name, Version.V20180611);
				redirect.setUrl("http:
				cmds.add(redirect);
				break;
			case REJECT:
				RejectCommand reject = new RejectCommand(name, Version.V20180611);
				cmds.add(reject);
				break;
			case ROUTE:
				RouteCommand route = new RouteCommand(name, Version.V20180611);
				route.setCalled("0215773339");
				cmds.add(route);
				break;
			case SAY:
				SayCommand say = new SayCommand(name, Version.V20180611);
				say.setText("welcome to 2ch platform");
				cmds.add(say);
				break;
			default:
				break;
			}

		}catch (Exception e) {
			
			try {
				res.sendError(403, "invalid command name");
				return null;
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
			
		}
		
		return response;
	}
	@ApiOperation(value = "2CH IVR연동 example - say command 응답 시나리오")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "callSid", value = "콜러의 세션아이디", required = true, dataType = "string", paramType = "query", defaultValue = "")})
	@RequestMapping(value = "/example/say", method = RequestMethod.GET)
	public MessageResponse examSay( 
			@RequestParam(required = true, value = "callSid") String callSid,
			HttpServletResponse res) {

		MessageResponse response = new MessageResponse();
		List<Command> cmds = new ArrayList<>();
		response.setCallSid(callSid);
		response.setCmds(cmds);

		try {
			
			
			SayCommand say = new SayCommand(CommandName.SAY, Version.V20180611);
			say.setText("welcome to 2ch platform");
			cmds.add(say);
			
			PauseCommand pause = new PauseCommand(CommandName.PAUSE, Version.V20180611);
			pause.setLength(1);
			cmds.add(pause);
			
			RedirectCommand redirect = new RedirectCommand(CommandName.REDIRECT, Version.V20180611);
			redirect.setUrl("http:
			cmds.add(redirect);


		}catch (Exception e) {
			
			try {
				res.sendError(403, "invalid command name");
				return null;
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
			
		}
		
		return response;
	}
	
	@ApiOperation(value = "2CH IVR연동 Example - say command 응답의 redirect url")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "callSid", value = "콜러의 세션아이디", required = true, dataType = "string", paramType = "query", defaultValue = "")})
	@RequestMapping(value = "/example/say/1", method = RequestMethod.GET)
	public MessageResponse examSayRedirect( 
			@RequestParam(required = true, value = "callSid") String callSid,
			HttpServletResponse res) {

		MessageResponse response = new MessageResponse();
		List<Command> cmds = new ArrayList<>();
		response.setCallSid(callSid);
		response.setCmds(cmds);
		
		try {
			
			SayCommand say = new SayCommand(CommandName.SAY, Version.V20180611);
			say.setText("hello world");
			cmds.add(say);
			
			PauseCommand pause = new PauseCommand(CommandName.PAUSE, Version.V20180611);
			pause.setLength(1);
			cmds.add(pause);
			
			HangupCommand hangup = new HangupCommand(CommandName.HANGUP, Version.V20180611);
			cmds.add(hangup);

		}catch (Exception e) {
			
			try {
				res.sendError(403, "invalid command name");
				return null;
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
			
		}
		
		return response;
	}
}
