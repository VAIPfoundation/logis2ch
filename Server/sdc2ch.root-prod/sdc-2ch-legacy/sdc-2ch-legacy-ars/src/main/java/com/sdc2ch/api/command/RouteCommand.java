package com.sdc2ch.api.command;

import com.sdc2ch.api.version.Version;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouteCommand extends Command {

	private boolean record;
	private String text;
	public RouteCommand(CommandName name, Version vs) {
		super(name, vs);
	}

	private String called;
	
}
