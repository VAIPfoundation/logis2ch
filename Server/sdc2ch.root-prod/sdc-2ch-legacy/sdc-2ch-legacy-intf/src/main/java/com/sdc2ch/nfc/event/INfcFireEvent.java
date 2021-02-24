package com.sdc2ch.nfc.event;

import com.sdc2ch.nfc.enums.NfcEventType;
import com.sdc2ch.require.enums.SetupLcType;
import com.sdc2ch.require.event.I2ChIncommingEvent;
import com.sdc2ch.tms.enums.FactoryType;

public interface INfcFireEvent extends I2ChIncommingEvent<INfcFireEvent> {
	NfcEventType getEvent();
	SetupLcType getSetupLcTy();
	FactoryType getFactoryType();
	String getNfcDeviceName();
	long getEventTime();
	int getNfcDeviceId();
}
