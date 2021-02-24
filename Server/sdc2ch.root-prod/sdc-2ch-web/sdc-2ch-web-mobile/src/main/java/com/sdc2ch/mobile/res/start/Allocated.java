package com.sdc2ch.mobile.res.start;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class Allocated implements MenuDetails {

	@JsonIgnore
	private final String pgmId;
	private final String allocatedConfirmTime;
	private final Long allocatedId;
	

	@Override
	public String getClassName() {
		return null;
	}

}
