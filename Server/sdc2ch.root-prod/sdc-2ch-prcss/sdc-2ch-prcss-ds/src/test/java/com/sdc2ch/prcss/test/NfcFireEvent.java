package com.sdc2ch.prcss.test;

import java.text.DateFormat;

import com.google.gson.GsonBuilder;
import com.sdc2ch.nfc.enums.NfcEventType;
import com.sdc2ch.nfc.event.INfcFireEvent;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.enums.SetupLcType;
import com.sdc2ch.tms.enums.FactoryType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NfcFireEvent implements INfcFireEvent {
	
	private long eventTime;
	private NfcEventType event;
	private int nfcDeviceId;
	private String nfcDeviceName;
	private IUser user;
	private SetupLcType setupLcTy;
	private FactoryType factoryType;
	
	public NfcFireEvent(IUser user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return new GsonBuilder()

			     .enableComplexMapKeySerialization()
			     .serializeNulls()
			     .setDateFormat(DateFormat.LONG)

			     .setPrettyPrinting()
			     .setVersion(1.0)
			     .create().toJson(this);
	}


	@Override
	public IUser user() {
		return user;
	}
}
