package com.sdc2ch.repo.io;

import com.sdc2ch.require.enums.SetupLcType;

public interface BeaconMappingIO {

	SetupLcType getSetupLc();
	String getBconId();
	String getFctryCd();
}
