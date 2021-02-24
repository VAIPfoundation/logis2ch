package com.sdc2ch.prcss.ss.service.impl;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdc2ch.prcss.ds.IShippingContext;
import com.sdc2ch.prcss.ds.event.IShippingContextEvent;
import com.sdc2ch.prcss.ds.event.IShippingContextEvent.EventAction;
import com.sdc2ch.prcss.ds.event.ShippingStateEvent;
import com.sdc2ch.prcss.ds.io.DrivingTripIO;
import com.sdc2ch.prcss.ss.repo.T_AnalsDrivingHistRepository;
import com.sdc2ch.prcss.ss.repo.T_AnalsDrivingTripHistRepository;
import com.sdc2ch.prcss.ss.repo.domain.T_ANALS_DRIVING_HIST;
import com.sdc2ch.prcss.ss.repo.domain.T_ANALS_DRIVING_TRIP_HIST;
import com.sdc2ch.prcss.ss.service.IDrivingService;
import com.sdc2ch.repo.io.TmsCarIO;
import com.sdc2ch.repo.io.TmsDriverIO;
import com.sdc2ch.require.event.I2ChEvent;
import com.sdc2ch.require.pubsub.I2ChEventConsumer;
import com.sdc2ch.require.pubsub.I2ChEventManager;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DrivingServiceImpl implements IDrivingService {

	@Autowired T_AnalsDrivingHistRepository repo;
	@Autowired T_AnalsDrivingTripHistRepository tripRepo;
	private I2ChEventConsumer<IShippingContextEvent> contextEvent;
	
	@Autowired
	private void regist(I2ChEventManager manager) {
		contextEvent = manager.subscribe(IShippingContextEvent.class);
		contextEvent.filter(e -> onReceive(e));
	}

	private void onReceive(I2ChEvent<IShippingContextEvent> e) {
		
		
		try {
			
			IShippingContextEvent event = (IShippingContextEvent) e;
			IShippingContext context = event.getContext();
			
			TmsDriverIO driver = (TmsDriverIO) context.getUser().getUserDetails();
			TmsCarIO car = driver.getCar();
			
			long maxDrivingTime = context.getDrivingTripTimes().stream().filter(val -> val != null).max(Comparator.comparingLong(Long::longValue)).orElse(0L);
			double accDistance = context.getAccDistance();
			Date lastDrvDate = null;
			Date startDrvDate = null;
			long wrikingTime = 0;
			long restTime = 0;
			
			
			ShippingStateEvent start = findMinByEventAct(context, EventAction.USR_ST, EventAction.NFC_TAG_OFFIC);
			ShippingStateEvent end = findMaxByEventAct(context, EventAction.USR_FIN, EventAction.SYS_FIN, EventAction.USR_EB);
			
			if(start == null) {
				start = findMinByEventAct(context, EventAction.values());
			}
			if(start != null) {
				
				startDrvDate = start.getEventDt();
				long startTime = startDrvDate == null ? 0 : startDrvDate.getTime();
				long endTime = end == null ? 0 : end.getEventDt().getTime();
				
				wrikingTime = (endTime - startTime) / 1000;
				lastDrvDate = context.getBeforeDayDrivingFinishDate();
				
				if(lastDrvDate != null) {
					restTime = (endTime - lastDrvDate.getTime()) / 1000;
				}
				
			}
			
			
			T_ANALS_DRIVING_HIST hist = new T_ANALS_DRIVING_HIST();
			hist.setAclGroupId(context.getGroupId());
			hist.setBeFinDt(lastDrvDate);
			hist.setDlvyDe(context.getDlvyDe().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
			
			hist.setDriverCd(driver.getUserDetailsId());
			hist.setDriverNm(driver.name());
			hist.setVhcleTy(car.getWegith().floatValue());
			hist.setVrn(car.getVrn());
			
			
			hist.setDrvDistance(accDistance);
			hist.setDrvTimeSec(wrikingTime);
			hist.setMaxDrvTimeSec(maxDrivingTime / 1000);
			hist.setRestTimeSec(restTime);
			hist.setCurStartDt(startDrvDate);
			hist.setId(repo.findByAclGroupId(context.getGroupId()).orElse(hist).getId());
			
			repo.save(hist);
			
			List<DrivingTripIO> trips = context.getTrips();
			if(trips != null) {
				trips.stream().filter(t -> t.startTimestamp() > 0 && t.endTimestamp() > 0).forEach(t -> saveTripHist(t, hist));
			}
			
		}catch (Exception ex) {
			log.error("{}" , ex);
		}
		
		
	}
	
	
	private T_ANALS_DRIVING_TRIP_HIST saveTripHist(DrivingTripIO t, T_ANALS_DRIVING_HIST hist) {
		
		T_ANALS_DRIVING_TRIP_HIST trip = new T_ANALS_DRIVING_TRIP_HIST();
		trip.setAclGroupId(hist.getAclGroupId());
		trip.setDlvyDe(hist.getDlvyDe());
		trip.setDriverCd(hist.getDriverCd());
		trip.setDriverNm(hist.getDriverNm());
		trip.setVhcleTy(hist.getVhcleTy());
		trip.setVrn(hist.getVrn());
		trip.setFromDrvDt(new Date(t.startTimestamp()));
		trip.setToDrvDt(new Date(t.endTimestamp()));
		return tripRepo.save(trip);
	}

	private ShippingStateEvent findMinByEventAct(IShippingContext context, EventAction ... act) {
		
		List<ShippingStateEvent> acts = context.findByEventActions(act);
		return acts == null ? null : acts.stream().min(Comparator.comparing(ShippingStateEvent::getEventDt)).orElse(null);
	}
	private ShippingStateEvent findMaxByEventAct(IShippingContext context, EventAction ... act) {
		List<ShippingStateEvent> acts = context.findByEventActions(act);
		return acts == null ? null : acts.stream().max(Comparator.comparing(ShippingStateEvent::getEventDt)).orElse(null);
	}
	
}
