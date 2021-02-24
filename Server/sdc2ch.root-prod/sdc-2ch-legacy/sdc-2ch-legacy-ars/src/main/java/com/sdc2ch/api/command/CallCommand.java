package com.sdc2ch.api.command;

import com.sdc2ch.api.version.Version;

public class CallCommand extends Command {

	public CallCommand(CommandName name, Version vs) {
		super(name, vs);
	}
	
	private String called;

	public String getCalled() {
		return called;
	}

	public void setCalled(String called) {
		this.called = called;
	}
	

}
