package com.sdc2ch.prcss.ds;

import com.sdc2ch.tms.io.TmsPlanIO;

public interface IShippingHandler {
	public void onReady();
	public void onClose();
	public void add(TmsPlanIO tmsplan);

}
