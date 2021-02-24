package model;

import com.sdc2ch.require.event.I2ChEvent;


public interface ILogicProcessChain {

	void process(I2ChEvent<?> event);
	
	void regist(IShippingState state);
}
