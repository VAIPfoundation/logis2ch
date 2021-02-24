package com.sdc2ch.prcss.ds.t.chain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.util.Assert;

import com.sdc2ch.core.expression.InstanceOf;
import com.sdc2ch.prcss.ds.core.AbstractShippingState;
import com.sdc2ch.prcss.ds.core.IShipping;
import com.sdc2ch.prcss.ds.core.ShippingPlan;
import com.sdc2ch.prcss.ds.core.ShippingPlanContext;
import com.sdc2ch.prcss.ds.event.IChainLocEvent;
import com.sdc2ch.prcss.ds.event.IOTEvent;
import com.sdc2ch.prcss.ds.event.IProcessEvent;
import com.sdc2ch.prcss.ds.io.ShippingPlanIO;
import com.sdc2ch.prcss.ds.t.chain.state.ShippingStatus;
import com.sdc2ch.prcss.ds.t.chain.state.ShippingStatus.StateNm;

import io.reactivex.Observable;
import io.reactivex.functions.Predicate;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public abstract class ShippingChain extends AbstractShippingState {

	public enum ChainNm {

		HOME("집"), FACTORY("공장"), WAREHOUSE("창고"), CUSTOMER_CENTER("고객");
		public String chainNm;

		ChainNm(String chainNm) {
			this.chainNm = chainNm;
		}
	}

	public final ShippingPlanContext context;
	public final ShippingPlan shipConfig;
	private ChainNm chainNm;

	protected ShippingChain(ShippingPlanContext context, ShippingPlan config) {
		super(context);
		this.shipConfig = config;
		this.context = context;
	}

	
	protected List<ShippingStatus> states = new ArrayList<>();

	@SuppressWarnings("unchecked")
	@Override
	public List<IShipping> getChilds() {
		return (List<IShipping>) (Object) states;
	}

	@Override
	public ShippingPlanIO getShippingPlanIO() {
		return shipConfig;
	}

	@Override
	public AbstractShippingState getChain() {
		return this;
	}

	@Override
	public IShipping onCreate(IShipping context) {
		
		System.out.println(shipConfig);
		states = states.stream().sorted(new Comparator<ShippingStatus>() {
			@Override
			public int compare(ShippingStatus o1, ShippingStatus o2) {
				return Integer.compare(o1.getStateName().ordinal(), o2.getStateName().ordinal());
			}
		}).collect(Collectors.toList());
		return this;
	}

	@Override
	public void onCancel() {
		states.forEach(s -> s.onCancel());
	}

	protected void setChainName(ChainNm chainNm) {
		this.chainNm = chainNm;
	}

	
	public void setStates(List<ShippingStatus> states) {
		Assert.notEmpty(states, "List<LogisticsState> can not be null");
		this.states = states;
	}

	
	public boolean removeState(ShippingStatus state) {
		Assert.notNull(state, "LogisticsState can not be null");

		return states.removeIf(s -> s.getClass() == state.getClass());
	}

	
	public boolean addState(ShippingStatus state) {
		Assert.notNull(state, "LogisticsState can not be null");
		return states.add(state);
	}

	
	public void removeAllState() {
		states.removeIf(s -> {

			return true;
		});
	}

	
	public List<ShippingStatus> getShippingStates() {
		return states;
	}

	public ChainNm chainName() {
		return chainNm;
	}

	private boolean chainFilter(IProcessEvent e, IShipping shipping) {
		boolean isMyChain = InstanceOf.when(e).instanceOf(IOTEvent.class)
				.then(e1 -> e1.getFactoryType() == shipping.getShippingPlanIO().getMyFacrtyTy()).otherwise(InstanceOf
						.when(e).instanceOf(IChainLocEvent.class).then(e1 -> super.isEqualLoc(e1)).otherwise(false));



		return isMyChain;
	}

	private boolean gpsFilter(IProcessEvent e, IShipping shipping) {
		




















		return false;
	}

	@Override
	public IProcessEvent getCurrentEvent() {
		return super.getCurrentEvent();
	}

	static AtomicInteger inc = new AtomicInteger();

	public boolean supported(IProcessEvent event) {
		boolean match = chainFilter(event, this);
		return match && states.stream().anyMatch(s -> {
			boolean supported = s.supported(event);



			return supported;

		});
	}

	public boolean supported(IProcessEvent event, IShipping shipping) {
		boolean first = ((AbstractShippingState) shipping).flattened().anyMatch(s -> {
			boolean sup = s.supported(event);
			System.out.println(s + " :> " + sup);
			return sup;
		});























		return first;
	}

	private boolean __filter(IProcessEvent event, ShippingChain chain) {
		List<ShippingChain> chains = context.getChains();
		int myIdx = IntStream.range(0, chains.size()).filter(i -> chains.get(i) == chain).findFirst().getAsInt();
		boolean supported = IntStream.range(0, chains.size()).filter(i -> i < myIdx)
				.anyMatch(i -> chains.get(i).supported(event));
		return myIdx == 0 || !supported;
	}

	@Override
	public Observable<IProcessEvent> subscribeNFC(IShipping shipping, Predicate<? super IProcessEvent> predicate) {
		return super.subscribeNFC(this, e -> supported(e, shipping));
	}














































	@Override
	public Observable<IProcessEvent> subscribeBCN(IShipping shipping, Predicate<? super IProcessEvent> predicate) {
		return super.subscribeBCN(this, e -> supported(e, shipping));
	}






	private boolean supported2(IProcessEvent e) {
		return true;
	}

	@Override
	protected void onChange(AbstractShippingState current) {

		log.info("onChange -> {}{}, current -> {}{}", current, current.getParent(), this.current, this.current== null ? null : this.current.getParent());

		if(current != this.current) {
			
			if (chainName() != ChainNm.FACTORY && current == states.stream().reduce((f, l) -> l).get()) {
				states.forEach(s -> s.forceComplete());
				completed = true;
			}
			this.setCurrent(current);
			super.onChange(this);
		}
	}
	
	public void onNext() {
		super.onChange(this);
	}
	
	@Override
	public void setCurrent(AbstractShippingState current) {
		this.current = current;
		getParent().setCurrent(this);
	}

	protected void forceComplete() {
		if (!isComplete()) {
			completed = true;
			states.forEach(s -> s.forceComplete());
		}
	}
	
	public boolean isFactory() {
		return chainName() == ChainNm.FACTORY;
	}




	@Override
	public AbstractShippingState findArriveState() {
		return (AbstractShippingState) states.stream().filter(s -> contains(s.getStateName())).findFirst().orElse(null);
	}

	private boolean contains(StateNm stateName) {
		return Arrays.asList(StateNm.ARRIVED, StateNm.TURN).contains(stateName);
	}

	@Override
	public String toString() {
		return new StringJoiner("").add("[").add(shipConfig.dlvyLcId).add("]").add("[")
				.add(shipConfig.tmsPlanRowId + "").add("]").add("[").add(shipConfig.routeNo).add("]").toString();
	}

	public void removeEvent() {
		states.forEach(s -> s.removeEvent());
	}

}
