package com.sdc2ch.prcss.ds.t.chain.state;



import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.sdc2ch.prcss.ds.core.AbstractShippingState;
import com.sdc2ch.prcss.ds.core.IShipping;
import com.sdc2ch.prcss.ds.core.ShippingPlanContext;
import com.sdc2ch.prcss.ds.event.IMobileActionEvent.MobileEventActionType;
import com.sdc2ch.prcss.ds.event.IProcessEvent;
import com.sdc2ch.prcss.ds.impl.MobileActionEventImpl;
import com.sdc2ch.prcss.ds.io.ShippingPlanIO;
import com.sdc2ch.prcss.ds.io.ShippingState;
import com.sdc2ch.prcss.ds.t.chain.ShippingChain;

import lombok.extern.slf4j.Slf4j;

 
@Slf4j
public class FinishState extends ShippingStatus {
	
	private ScheduledFuture<?> future;

	public FinishState(ShippingChain chain) {
		super(chain, StateNm.FINISH);
	}
	
	@Override
	public IShipping onCreate(IShipping context) {
		ShippingPlanIO plan = ((AbstractShippingState) chain).getShippingPlanIO(); 
		
		LocalDateTime startTime = LocalDateTime.now();
		LocalDateTime endTime = plan.getScheDlvyEdDt();
		
		if(endTime != null) {
			
			endTime = endTime.plusHours(3);
			
			long minutes = ChronoUnit.MILLIS.between(startTime, endTime);
			
			if(minutes > 0) {
				
				log.info("add time scheduler -> cur -> {} start -> {} to {}", startTime, plan.getScheDlvyStDt(), endTime);
				List<ShippingChain> chains = ((AbstractShippingState) chain).getChains();
				
				
				if(!isQuickStart(chains)) {
					future = super.schedule(new Runnable() {
						@Override
				        public void run() {
				        	MobileActionEventImpl event = MobileActionEventImpl.of(getUser(), System.currentTimeMillis(), null);
				        	event.setMobileEventActionType(MobileEventActionType.SYS_FINISH_JOB);
				        	onReceive(event);
				        }
					}, minutes, TimeUnit.MILLISECONDS);
				} else
				{
					log.info("조조기 Info : {}", plan);
				}
				
			}

			
		}
		return super.onCreate(context);
	}
	
	public void onReceive(IProcessEvent event) {
		log.info("업무종료 이다. {}" ,this);
		if(future != null)
			future.cancel(true);
		super.setEvent(event);
		super.onComplete();
		super.onFinish();
	}
	@Override
	public com.sdc2ch.prcss.ds.io.ShippingState getShippingState() {
		return ShippingState.COMPLETE;
	}
	public void removeEvent() {
		
	}
	
	
	private boolean isQuickStart(List<ShippingChain> shippingChains) {
		if (shippingChains != null && !shippingChains.isEmpty()) {
			for (ShippingChain sChian : shippingChains) {
				if (isQuickStart(sChian.shipConfig.getTimeZoneNm())) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean isQuickStart(String timeZoneNm) {
		
		return "조조기".equals(timeZoneNm);
	}
	
}
