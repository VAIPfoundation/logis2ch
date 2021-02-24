package com.sdc2ch.mobile.res.start;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor(staticName="of")
public class EmptyDetail implements MenuDetails {
	

	@JsonIgnore
	private final String pgmId;

	@Override
	public String getClassName() {
		return null;
	}

}
