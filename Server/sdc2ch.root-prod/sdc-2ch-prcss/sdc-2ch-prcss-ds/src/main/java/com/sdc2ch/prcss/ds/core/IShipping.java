package com.sdc2ch.prcss.ds.core;

import java.util.List;

import com.sdc2ch.prcss.ds.event.IProcessEvent;
import com.sdc2ch.prcss.ds.io.ShippingPlanIO;

import io.reactivex.Observable;
import io.reactivex.functions.Predicate;

public interface IShipping {

	Observable<IProcessEvent> subscribeGPS(IShipping shipping, Predicate<? super IProcessEvent> predicate);
	Observable<IProcessEvent> subscribeNFC(IShipping shipping, Predicate<? super IProcessEvent> predicate);
	Observable<IProcessEvent> subscribeBCN(IShipping shipping, Predicate<? super IProcessEvent> predicate);
	Observable<IProcessEvent> subscribeEBX(IShipping shipping, Predicate<? super IProcessEvent> predicate);
	Observable<IProcessEvent> subscribeMBL(IShipping shipping, Predicate<? super IProcessEvent> predicate);

	void onCancel();
	boolean isComplete();
	boolean supported(IProcessEvent e);
	IShipping onCreate(IShipping context);
	List<IShipping> getChilds();
	ShippingPlanIO getShippingPlanIO();
}
