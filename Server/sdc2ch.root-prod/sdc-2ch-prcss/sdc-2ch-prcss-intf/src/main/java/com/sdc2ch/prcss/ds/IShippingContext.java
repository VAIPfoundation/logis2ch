package com.sdc2ch.prcss.ds;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.sdc2ch.prcss.ds.event.IShippingContextEvent.EventAction;
import com.sdc2ch.prcss.ds.event.IShippingEvent;
import com.sdc2ch.prcss.ds.event.ShippingStateEvent;
import com.sdc2ch.prcss.ds.io.DrivingTripIO;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.pubsub.I2ChEventConsumer;
import com.sdc2ch.tms.io.TmsPlanIO;

public interface IShippingContext {
	IUser getUser();
	List<TmsPlanIO> getShippings();
	List<ShippingStateEvent> searchEvents(String routeNo, EventAction action);
	List<ShippingStateEvent> findByEventActions(EventAction ... actions);
	Long getGroupId();
	List<Long> getDrivingTripTimes();
	double getAccDistance();
	Date getBeforeDayDrivingFinishDate();
	LocalDate getDlvyDe();
	List<DrivingTripIO> getTrips();
	I2ChEventConsumer<?> subscribe(Class<?> receive);
	void fireEvent(IShippingEvent event);
	void batchClose();
}
