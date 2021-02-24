package com.sdc2ch.prcss.ds.t.chain.state;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.sdc2ch.prcss.ds.core.IShipping;
import com.sdc2ch.prcss.ds.event.IAlaramNotiEvent.AlaramType;
import com.sdc2ch.prcss.ds.event.IMobileActionEvent;
import com.sdc2ch.prcss.ds.event.IProcessEvent;
import com.sdc2ch.prcss.ds.io.ShippingPlanIO;
import com.sdc2ch.prcss.ds.io.ShippingState;
import com.sdc2ch.prcss.ds.t.chain.ShippingChain;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReadyState extends ShippingStatus {
	
	private ScheduledFuture<?> future;
	
	private enum ReadyProgress {
		READY("준비") {
			@Override
			public ReadyProgress next() {
				return CONFIRM;
			}
		},
		CONFIRM("배차확인") {
			@Override
			public ReadyProgress next() {
				return START;
			}
		},
		START("운행시작") {
			@Override
			public ReadyProgress next() {
				return START;
			}
		};
		public String typeNm;
		ReadyProgress(String typeNm){
			this.typeNm = typeNm;
		}
		
		public abstract ReadyProgress next();
	}
	
	
	private ReadyProgress progress;

	public ReadyState(ShippingChain chain, StateNm stateNm) {
		super(chain, stateNm);
		chain.setCurrent(this);
	}

	@Override
	public IShipping onCreate(IShipping context) {
		progress = ReadyProgress.READY;
		ShippingPlanIO plan = chain.shipConfig;
		LocalDateTime startTime = LocalDateTime.now();
		LocalDateTime endTime = plan.getScheDlvyStDt();
		
		if(endTime != null) {
			log.info("Ready add time scheduler -> cur -> {} start -> {} to {}", startTime, plan.getScheDlvyStDt(), endTime);
			long minutes = ChronoUnit.MILLIS.between(startTime, endTime);
			
			if(minutes > 0) {
				future = super.scheduleAtFixedRate(new Runnable() {
					@Override
			        public void run() {
						log.info("start {} ", chain.shipConfig);
						sendNoti();
						future.cancel(true);
			        }
				}, minutes, 1000, TimeUnit.MILLISECONDS);
			}


		}
		return super.onCreate(context);
	}
	@Override
	public void removeAlarm() {
		if(future != null) {
			log.info("removed Alarm {}", this.getShippingPlanIO());
			future.cancel(true);
		}
		
	}
	
	public void sendNoti() {
		super.sendNoti(AlaramType.NOT_YET_ALLOCATED);
	}
	public void onReceive(IProcessEvent event) {
		
		if(event instanceof IMobileActionEvent) {
			IMobileActionEvent e = (IMobileActionEvent) event;
			
			switch (e.getMobileEventActionType()) {
			case ALLOCATE_CONFIRM:
				progress = ReadyProgress.CONFIRM;
				break;
			case START_JOB:
				progress = ReadyProgress.START;
				break;
			default:
				break;
			}
		}
		super.setEvent(event);
		onComplete();
		log.info("정상처리 이벤트를 보낸다. {}" ,this);
	}
	

	@Override
	public ShippingState getShippingState() {
		ShippingState st = null;
		switch (progress) {
		case CONFIRM:
			st = ShippingState.READY;
			break;
		case READY:
			st = ShippingState.READY;
			break;
		case START:
			st = ShippingState.START;
			break;
		default:
			break;
		}
		return st;
	}
	
	public void hit() {
		progress = progress.next();
		super.hit();
	}
	
	@Override
	protected void onComplete() {
		removeAlarm();
		super.onComplete();
	}






}
