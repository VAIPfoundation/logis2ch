package com.sdc2ch.mobile.res.start;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(staticName="of")
public class EmptyBox implements MenuDetails {
	
	@JsonIgnore
	private final String pgmId;
	private final int total;
	private final int current;
	private final int squareBoxQty;
	
	@Override
	public String getClassName() {
		return null;
	}

}
