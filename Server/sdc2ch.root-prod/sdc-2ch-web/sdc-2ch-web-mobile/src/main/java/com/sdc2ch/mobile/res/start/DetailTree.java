package com.sdc2ch.mobile.res.start;

import java.util.Map;

import com.sdc2ch.mobile.res.Me;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor(staticName="of")
public class DetailTree {

	private final Long allocatedGroupId;
	private final Me me;
	private final Map<String, MenuDetails> details;

}
