package com.sdc2ch.api.command;

import com.sdc2ch.api.version.Version;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecordCommand extends Command {
	private boolean record;

	public RecordCommand(CommandName name, Version vs) {
		super(name, vs);
	}

}
