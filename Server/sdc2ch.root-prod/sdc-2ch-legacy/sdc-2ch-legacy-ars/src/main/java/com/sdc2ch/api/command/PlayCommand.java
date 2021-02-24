package com.sdc2ch.api.command;

import com.sdc2ch.api.version.Version;

public class PlayCommand extends Command {

	public PlayCommand(CommandName name, Version vs) {
		super(name, vs);
	}
	
	public enum MimeType {
		
		audiompeg("audio/mpeg"),
		audiowav("audio/wav"),
		audiowave("audio/wave"),
		audioxwav("audio/x-wav"),
		audioaiff("audio/aiff"),
		audioxaiff("audio/x-aiff"),
		audioxgsm("audio/x-gsm"),
		audiogsm("audio/gsm"),
		audioulaw("audio/ulaw");
		
		









		private String type;
		
		MimeType(String type){
			this.type = type;
		}

		public String getType() {
			return type;
		}

	}
	private int loop;
	private MimeType mimeType;
	private String url;
	
	public int getLoop() {
		return loop;
	}
	public void setLoop(int loop) {
		this.loop = loop;
	}
	public String getMimeType() {
		return mimeType.type;
	}
	public void setMimeType(MimeType mimeType) {
		this.mimeType = mimeType;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
