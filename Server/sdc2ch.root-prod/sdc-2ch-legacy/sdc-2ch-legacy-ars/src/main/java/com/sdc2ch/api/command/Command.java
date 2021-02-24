package com.sdc2ch.api.command;

import com.sdc2ch.api.version.Version;

import io.swagger.annotations.ApiModelProperty;

public abstract class Command {
	@ApiModelProperty(required = true, value = "커맨드명")
	protected CommandName commandName;
	@ApiModelProperty(required = true, value = "커맨드 버전")
	protected Version version;
	
	public Command(CommandName name, Version vs) {
		commandName = name;
		version = vs;
	}

	public CommandName getCommandName() {
		return commandName;
	}
	public Version getVersion() {
		return version;
	}


}
