package model;

import java.util.List;

import com.sdc2ch.require.event.I2ChEvent;

import io.reactivex.Observable;

 
public interface IShippingState {
	














	
	
	public void setActionEvents(List<IShippingActionEvent> actionEvents);
	
	
	public void addActionEvents(IShippingActionEvent actionEvent);
	
	
	public void removeActionEvents(IShippingActionEvent actionEvent);
	
	
	public void actionAlarm(I2ChEvent<?> event);
	
	
	public Observable<I2ChEvent<?>> filter(Class<? extends I2ChEvent<?>> class1);
	
	
	public void disposable();
	
	
	public List<IShippingActionEvent> getActionEvent();
	
}
