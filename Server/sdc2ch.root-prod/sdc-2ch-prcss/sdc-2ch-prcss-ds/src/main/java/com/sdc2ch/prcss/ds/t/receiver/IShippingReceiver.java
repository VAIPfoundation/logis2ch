package com.sdc2ch.prcss.ds.t.receiver;

import com.sdc2ch.prcss.ds.core.IShipping;

public interface IShippingReceiver {
	
	void onReady();
	void onClose();
	void add(IShipping shipping);
}
