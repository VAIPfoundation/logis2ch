package com.sdc2ch.repo.io;

import com.sdc2ch.require.enums.SetupLcType;

public interface NfcMappingIO {
	SetupLcType getSetupLc();
	int getNfcId();
	String getFctryCd();
}
