package com.sdc2ch.api.command;

import com.sdc2ch.api.version.Version;

public class PauseCommand extends Command {

	public PauseCommand(CommandName name, Version vs) {
		super(name, vs);
	}

	public PauseCommand(CommandName name, Version vs, int length) {
		super(name, vs);
		this.length = length;
	}

	private int length;

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
	
}
