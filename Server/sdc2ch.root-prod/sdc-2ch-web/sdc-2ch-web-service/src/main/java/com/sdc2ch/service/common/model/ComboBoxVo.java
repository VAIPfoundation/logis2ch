package com.sdc2ch.service.common.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class ComboBoxVo {
	private final String key;
	private final String value;
}
