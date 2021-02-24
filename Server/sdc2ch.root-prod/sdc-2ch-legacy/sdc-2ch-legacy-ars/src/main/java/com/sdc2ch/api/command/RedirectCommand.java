package com.sdc2ch.api.command;

import com.sdc2ch.api.version.Version;

public class RedirectCommand extends Command {

	public RedirectCommand(CommandName name, Version vs) {
		super(name, vs);
	}
	
	public RedirectCommand(CommandName name, Version vs, String url) {
		super(name, vs);
		this.url = url;
	}

	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
