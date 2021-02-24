package com.sdc2ch.legacy.aiv.event;

import com.sdc2ch.aiv.event.IBeaconFireEvent;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.enums.SetupLcType;
import com.sdc2ch.tms.enums.FactoryType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BeaconFireEventImpl implements IBeaconFireEvent {

	private IUser user;
	private InoutType inoutTy;
	private long timestamp;
	private String deviceId;
	private FactoryType fctryTy;
	private SetupLcType setupLcTy;
	
	@Override
	public IUser user() {
		return user;
	}
	@Override
	public FactoryType getFactoryTy() {
		return fctryTy;
	}
}
