package com.sdc2ch.api.command;

import com.sdc2ch.api.version.Version;

public class SayCommand extends Command {

	public SayCommand(CommandName name, Version vs) {
		super(name, vs);
	}
	
	public SayCommand(CommandName name, Version vs, String text) {
		super(name, vs);
		this.text = text;
	}
	
	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
