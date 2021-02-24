package com.sdc2ch.mobile.res.start;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class ReturnGoods implements MenuDetails {

	@JsonIgnore
	private final String pgmId;
	private final String goods;
	private final String dlvyloNm;

	@Override
	public String getClassName() {
		return null;
	}

}
