package com.sdc2ch.prcss.ds.impl;

import com.sdc2ch.prcss.ds.IShippingContext;
import com.sdc2ch.prcss.ds.event.IShippingContextEvent;
import com.sdc2ch.require.domain.IUser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class ShippingContextEventImpl implements IShippingContextEvent {

	private final IShippingContext context;
	
	@Override
	public IUser user() {
		return context.getUser();
	}

}
