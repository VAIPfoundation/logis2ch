package com.sdc2ch.api.command;

import com.sdc2ch.api.version.Version;

public class GatherCommand extends Command {

	public GatherCommand(CommandName name, Version vs) {
		super(name, vs);
	}
	
	public enum GatherType {
		dtmf,
		speech,
		dtmfOrSpeech
	}
	
	private GatherType gatherType;
	private int timeout;
	private int numDigits;
	private String text;
	
	public GatherType getGatherType() {
		return gatherType;
	}
	public void setGatherType(GatherType gatherType) {
		this.gatherType = gatherType;
	}
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	public int getNumDigits() {
		return numDigits;
	}
	public void setNumDigits(int numDigits) {
		this.numDigits = numDigits;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
