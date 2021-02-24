package com.sdc2ch.api.callevent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EICNCallEvent extends CallEvent {
	private String phoneNumber;
	private String cidNumber;
	private Integer dtmf;
}
