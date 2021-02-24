package com.sdc2ch.prcss.ds.t.chain.state;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.sdc2ch.prcss.ds.core.AbstractShippingState;
import com.sdc2ch.prcss.ds.core.IShipping;
import com.sdc2ch.prcss.ds.event.IProcessEvent;
import com.sdc2ch.prcss.ds.t.chain.ShippingChain;
import com.sdc2ch.prcss.ds.t.chain.state.action.ActionEvent;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import lombok.extern.slf4j.Slf4j;

 
@Slf4j
public abstract class ShippingStatus extends AbstractShippingState {
	
	public enum StateNm {
		READY("준비"),
		ENTER("진입"),
		ARRIVED("도착"),
		UNLOAD("하차"),
		LOAD("상차"),
		DEPART("출발"),
		TURN("회차"),
		FINISH("완료"),
		CONFIRM("공상자인수"),
		UNKNOWN("알수없음");
		public String stateNm;
		StateNm(String stateNm){
			this.stateNm =stateNm;
		}
	}
	
	protected ShippingChain chain;
	protected StateNm stateNm;
	protected Disposable disposable;
	
	public abstract com.sdc2ch.prcss.ds.io.ShippingState getShippingState();
	
	private List<ActionEvent> events = new ArrayList<>();
	
	public ShippingStatus(ShippingChain chain) {
		this(chain, StateNm.UNKNOWN);
	}

	public ShippingStatus(ShippingChain chain, StateNm stateNm) {
		super(chain);
		this.chain = chain;
		this.stateNm = stateNm;
	}
	
	@Override
	protected void onChange(AbstractShippingState current) {
		super.onChange(current);
		this.setCurrent(current);
	}
	
	@Override
	public void setCurrent(AbstractShippingState current) {
		this.current = current;
		getParent().setCurrent(this);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<IShipping> getChilds() {
		return (List<IShipping>)(Object)events;
	}
	
	public List<ActionEvent> getActions() {
		return events;
	}
	
	public void add(ActionEvent action) {
		events.add(action);
	}
	public StateNm getStateName() {
		return stateNm;
	}
	
	@Override
	public IShipping onCreate(IShipping context) {

		return this;
	}
	
	public void forceComplete() {
		
		if(!isComplete()) {
			log.info("강제종료 이벤트를 보내야한다. {} {}", this.getParent(), this);
			completed = true;
			
			
			getActions().forEach(a -> a.onComplete());
		}
		
	}
	public ShippingChain getParent() {
		return (ShippingChain) super.getParent();
	}
	
	
	public void onReceive(IProcessEvent event) {
		super.setEvent(event);
		onComplete();
		this.setCurrent(this);	
	}
	
	
	@Override
	protected void onComplete() {
		
		super.completed = true;
		
		if(!super.isFactory()) {
			
			int curIdx = IntStream.range(0, chain.getShippingStates().size()).filter(i -> chain.getShippingStates().get(i) == this).findFirst().orElse(0);
			AtomicInteger inc = new AtomicInteger();
			
			List<ShippingStatus> status =  chain.getShippingStates().stream().filter(s -> !s.isComplete() && inc.incrementAndGet() < curIdx).collect(Collectors.toList());
			
			if(status != null) {
				
				status.forEach(s -> {
					s.forceComplete();
				});
			}
			
			getActions().forEach(a -> a.onComplete());
		}
		
		super.onChange(this);
	}
	
	@Override
	public void onCancel() {
		events.forEach(e -> e.onCancel());
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(stateNm).append("]");
		return sb.toString();
	}
	
	@Override
	public boolean isComplete() {
		return completed;
	}
	
	protected void info(ActionEvent action) {
		log.info("onReactive() -> {} {}, {}", this.getParent(), this , action);
	}
	
	public boolean supported(IProcessEvent event) {
		
		
		List<AbstractShippingState> list = this.flattened().filter(s -> s != this).collect(Collectors.toList());
		
		boolean supported = list.stream().anyMatch(s -> {
			boolean sss = s.supported(event);



			return sss;
		});
		return supported;
	}
	
	static AtomicInteger inc = new AtomicInteger();
	
	public boolean supported(IProcessEvent event, IShipping shipping) {
		
		

		boolean action = false;
		boolean state = false;
		boolean chain = true;
		IShipping _this = this;
		if(action) {
			ShippingStatus shipstate = (ShippingStatus) ((AbstractShippingState) shipping).getParent();
			state = __filter(event, shipstate);



		}




		
		boolean colplete = action && state && chain;

		return colplete;
	}



















	private boolean __filter(IProcessEvent event, ShippingStatus state) {
		List<ShippingStatus> states = chain.getShippingStates();
		int myIdx = IntStream.range(0, states.size()).filter(i -> states.get(i) == state).findFirst().getAsInt();
		boolean supported = IntStream.range(0, states.size()).filter(i -> i < myIdx)
				.anyMatch(i -> states.get(i).supported(event));
		return myIdx==0 || !supported;
	}

	@Override
	public Observable<IProcessEvent> subscribeNFC(IShipping shipping, 
			Predicate<? super IProcessEvent> predicate) {
		return super.subscribeNFC(this, e -> supported(e, (ActionEvent) shipping));
	}
	
	@Override
	public Observable<IProcessEvent> subscribeBCN(IShipping shipping, 
			Predicate<? super IProcessEvent> predicate) {

		return super.subscribeBCN(this, e -> supported(e, (ActionEvent) shipping));

	}












	
	
	private boolean gpsPredicate(IProcessEvent e, IShipping shipping) {
		return !isComplete() && shipping == getActions().stream().filter(a -> a.supported(e)).findFirst().orElse(null);
	}
	private boolean predicate(IProcessEvent e) {
		return !this.isComplete() && events.stream().anyMatch(a -> a.supported(e));
	}
	private boolean predicate2(IProcessEvent e) {
		return e != getCurrentEvent();
	}
	private boolean nfcPredicate(IProcessEvent e) {
		return predicate(e) && predicate2(e);
	}
	@Override
	public IProcessEvent getCurrentEvent() {
		return super.getCurrentEvent();
	}

	public void removeEvent() {
		events.forEach(e -> e.removeEvent());
	}

	public void removeAlarm() {
	}
}
