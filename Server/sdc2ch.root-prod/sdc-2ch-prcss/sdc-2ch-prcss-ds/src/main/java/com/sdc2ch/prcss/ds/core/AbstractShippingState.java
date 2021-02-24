package com.sdc2ch.prcss.ds.core;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.springframework.util.Assert;

import com.sdc2ch.prcss.ds.event.IAlaramNotiEvent.AlaramType;
import com.sdc2ch.prcss.ds.event.IChainLocEvent;
import com.sdc2ch.prcss.ds.event.IProcessEvent;
import com.sdc2ch.prcss.ds.io.ShippingPlanIO;
import com.sdc2ch.prcss.ds.t.chain.ShippingChain;
import com.sdc2ch.prcss.ds.t.chain.state.ShippingStatus.StateNm;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.tms.enums.TransportType;

import io.reactivex.Observable;
import io.reactivex.functions.Predicate;

public abstract class AbstractShippingState implements IShipping {

	private AbstractShippingState parent;
	protected AbstractShippingState current;
	protected boolean completed;
	private String currentRouteNo;
	
	protected AbstractShippingState(AbstractShippingState parent) {
		this.parent = parent;

	}

	AbstractShippingState() {
	}
	
	public IProcessEvent getCurrentEvent() {
		return parent.getCurrentEvent();
	}
	public IProcessEvent getLastEvent() {
		return parent.getCurrentEvent();
	}

	public ShippingPlanIO getShippingPlanIO() {
		
		if(parent == null)
			return current.getShippingPlanIO();
		return parent.getShippingPlanIO();
	}
	
	public AbstractShippingState getChain() {
		return parent.getChain();
	}

	public AbstractShippingState getParent() {
		return parent;
	}
	
	@Override
	public Observable<IProcessEvent> subscribeEBX(IShipping shipping, 
			Predicate<? super IProcessEvent> predicate) {
		return parent.subscribeEBX(shipping, predicate);
	}
	@Override
	public Observable<IProcessEvent> subscribeGPS(IShipping shipping, 
			Predicate<? super IProcessEvent> predicate) {
		return parent.subscribeGPS(shipping, predicate);
	}

	@Override
	public Observable<IProcessEvent> subscribeNFC(IShipping shipping, 
			Predicate<? super IProcessEvent> predicate) {
		return parent.subscribeNFC(shipping, predicate);
	}
	
	@Override
	public Observable<IProcessEvent> subscribeMBL(IShipping shipping, 
			Predicate<? super IProcessEvent> predicate) {
		return parent.subscribeMBL(shipping, predicate);
	}
	
	@Override
	public Observable<IProcessEvent> subscribeBCN(IShipping shipping, 
			Predicate<? super IProcessEvent> predicate) {
		return parent.subscribeBCN(shipping, predicate);
	}
	
	protected void onChange(AbstractShippingState current) {
		parent.onChange(current);
	}
	protected void onComplete() {
		parent.onComplete();
	}
	protected void onFinish() {
		parent.onFinish();
	}
	public void onReceive(IProcessEvent e1) {
		parent.onReceive(e1);
	};
	
	public AbstractShippingState findArriveState() {
		return parent.findArriveState();
	}
	public AbstractShippingState getCurrentState() {
		return current;
	}
	
	public void setEvent(IProcessEvent e1) {
		parent.setEvent(e1);
	}
	
    @SuppressWarnings("unchecked")
	public Stream<AbstractShippingState> flattened() {
        return Stream.concat(
                Stream.of(this),
                ((List<AbstractShippingState>)(Object)getChilds()).stream().flatMap(AbstractShippingState::flattened));
    }
	
	@Override
	public boolean isComplete() {
		return completed;
	}
	
	public List<ShippingChain> getChains(){
		return parent.getChains();
	}
	
	public String encodeHint() {
		return parent.encodeHint();
	}
	public String decodeHint(String hint) {
		return parent.decodeHint(hint);
	}
	
	
	public boolean isFirstChain(ShippingChain chain) {
		return chain.shipConfig.getDlvyLoReIdx() == 0;
	}
	
	
	public boolean isEndChain(ShippingChain chain) {
		return chain.shipConfig.getDlvyLoReIdx() == getChains().size() -1;
	}
	
	
	public boolean isRouteHeader(ShippingChain chain) {
		return chain.shipConfig.isFactory() && chain.shipConfig.dlvyLcSeq == 0;
	}
	
	
	public boolean isRouteTail(ShippingChain chain) {
		return !isRouteHeader(chain);
	}

	
	public boolean hasNextRoute(ShippingChain chain) {
	    int max = getChains()
	    	      .stream()
	    	      .mapToInt(v -> v.shipConfig.getRouteReIdx())
	    	      .max().orElseThrow(NoSuchElementException::new);
		return chain.shipConfig.getRouteReIdx() + 1 <= max;
	}
	
	public boolean hasPreviousRoute(ShippingChain chain) {
		return chain.shipConfig.getRouteReIdx() - 1 >= 0;
	}

	
	public boolean hasPreviousChain(ShippingChain chain) {
		return getChains().stream().filter(c -> c.shipConfig.getDlvyLoReIdx() == (chain.shipConfig.getDlvyLoReIdx() - 1))
				.findFirst().isPresent();
	}
	
	public boolean hasNextChain(ShippingChain chain) {
		return getChains().stream().filter(c -> c.shipConfig.getDlvyLoReIdx() == (chain.shipConfig.getDlvyLoReIdx() + 1))
				.findFirst().isPresent();
	}
	
	
	public ShippingChain getPreviousChain(ShippingChain chain) {
		return getChains().stream().filter(c -> c.shipConfig.getDlvyLoReIdx() == (chain.shipConfig.getDlvyLoReIdx() - 1))
				.findFirst().orElse(null);
	}
	
	public ShippingChain getNextChain(ShippingChain chain) {
		if(chain == null) return null;
		return getChains().stream().filter(c -> c.shipConfig.getDlvyLoReIdx() == (chain.shipConfig.getDlvyLoReIdx() + 1))
				.findFirst().orElse(null);
	}
	

	
	public boolean isFIF(ShippingChain chain) {
		return findAllByRouteNo(chain).allMatch(c -> c.shipConfig.dlvyLcId.equals(chain.shipConfig.dlvyLcId));
	}
	
	
	public boolean isFTF(ShippingChain chain) {
		return !isFIF(chain);
	}
	
	
	public boolean isFTW(ShippingChain chain) {
		return findAllByRouteNo(chain).anyMatch(c -> c.shipConfig.transportTy == TransportType.HY);
	}
	
	
	public boolean isFTB(ShippingChain chain) {
		return findAllByRouteNo(chain).anyMatch(c -> c.shipConfig.transportTy == TransportType.OEM);
	}
	
	
	public boolean isEqualLoc(IProcessEvent event) {
		if(event instanceof IChainLocEvent) {
			return ((IChainLocEvent) event).getUniqueSequence() == getShippingPlanIO().getUniqueSequence();
		}
		return false;
	}
	
	
	private String convertTransportRouteNo(String routeNo) {
		Assert.notNull(routeNo, "routeNo can not be Null");
		return routeNo.split("_")[0];
	}
	
	private Stream<ShippingChain> findAllByRouteNo(ShippingChain chain) {
		String routeNo = convertTransportRouteNo(chain.shipConfig.routeNo);
		return getChains().stream().filter(c -> convertTransportRouteNo(c.shipConfig.routeNo).equals(routeNo));
	}

	public abstract void setCurrent(AbstractShippingState current);
	public AbstractShippingState getCurrentShippingState() {
		return current;
	}

	public StateNm getStateName() {
		return null;
	}
	
	public void hit() {
		if(parent != null) {
			completed = true;
			parent.setCurrent(this);
		}
		
	}

	public boolean isFactory() {
		return parent.isFactory();
	};
	
	public void setCurrentRouteNo(String currentRouteNo) {
		this.currentRouteNo = currentRouteNo;
	}
	public String getCurrentRouteNo() {
		return currentRouteNo;
	}

	public IUser getUser() {
		if(parent != null)
			parent.getUser();
		return null;
	}
	public void sendNoti(AlaramType ty) {
		if(parent != null)
			parent.sendNoti(ty);
	}
	public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
		if(parent != null)
			return parent.schedule(command, delay, unit);
		return null;
	}
	public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
		if(parent != null)
			return parent.scheduleAtFixedRate(command, initialDelay, period, unit);
		return null;
	}
}
