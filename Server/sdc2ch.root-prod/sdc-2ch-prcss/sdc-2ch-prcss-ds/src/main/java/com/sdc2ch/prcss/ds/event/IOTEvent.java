package com.sdc2ch.prcss.ds.event;

import com.sdc2ch.require.enums.SetupLcType;
import com.sdc2ch.tms.enums.FactoryType;

public interface IOTEvent extends IProcessEvent {
	FactoryType getFactoryType();
	SetupLcType getSetupLcTy();
}
