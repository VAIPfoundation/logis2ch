package com.sdc2ch.prcss.ds.impl;

import com.sdc2ch.prcss.ds.event.ActionEventType;
import com.sdc2ch.prcss.ds.event.INfcTagEvent;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.enums.SetupLcType;
import com.sdc2ch.tms.enums.FactoryType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@RequiredArgsConstructor(staticName = "of")
@ToString
public class NfcTagEventImpl implements INfcTagEvent {
	
	private final IUser user;
	private final long timeStamp;
	private final SetupLcType setupLcTy;
	@Setter
	private FactoryType factoryType;
	
	
	@Override 
	public IUser user() {
		return user;
	}


	@Override
	public ActionEventType getActionEventTy() {
		switch (setupLcTy) {
		case OFFICE:
			return ActionEventType.NFC_TAG_OFFIC;
		default:
			break;
		}
		return ActionEventType.NFC_TAG_LDNG;
	}


}
