package com.sdc2ch.prcss.ds.t.receiver;

import java.util.Optional;

import com.sdc2ch.prcss.ds.core.IShipping;
import com.sdc2ch.prcss.ds.core.ShippingPlanContext;
import com.sdc2ch.prcss.ds.event.IProcessEvent;
import com.sdc2ch.prcss.ds.t.chain.ShippingChain;
import com.sdc2ch.require.event.I2ChEvent;

import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractShippingReceiver implements IShippingReceiver {
	
	private Disposable mainDisposable;
	private ShippingPlanContext context;
	private Class<?> receive;
	
	protected AbstractShippingReceiver(ShippingPlanContext context, Class<?> class1) {
		this.context = context;
		this.receive = class1;
	}
	
	protected abstract void onReceive(I2ChEvent<?> e);
	
	@Override
	public void onReady() {
		this.mainDisposable = context.getShippingManager().subscribe(receive)
				.filter(e -> onReceive(e), context.getUser());
	}
	
	@Override
	public void onClose() {
		Optional.of(mainDisposable).ifPresent(d -> d.dispose());
	}

	@Override
	public void add(IShipping shipping) {
		
	}
	
	protected int findSequence(String dlvyLcId) {
		return context.getChains().stream()
				.filter(c -> dlvyLcId.equals(c.shipConfig.tmsPlanRowId + ""))
				.findFirst()
				.orElse((ShippingChain)context.getCurrentShippingState())
				.shipConfig
				.getUniqueSequence();
	}

	public void fireEvent(IProcessEvent event) {
		context.fireEvent(event);
	}
}
