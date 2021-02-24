package model;

import com.sdc2ch.require.event.I2ChEvent;

import io.reactivex.disposables.Disposable;


public interface IShippingActionEvent {
	
	
	public abstract Class<? extends I2ChEvent<?>> observerble();
	
	
	
	public void setDisposable(Disposable disposable);
	
	
	public void dispose();
	
}
