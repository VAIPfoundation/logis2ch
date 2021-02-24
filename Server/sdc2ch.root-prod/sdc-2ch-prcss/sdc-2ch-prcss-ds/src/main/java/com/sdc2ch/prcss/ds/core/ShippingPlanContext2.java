package com.sdc2ch.prcss.ds.core;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.sdc2ch.prcss.ds.IShippingContext;
import com.sdc2ch.prcss.ds.IShippingHandler;
import com.sdc2ch.prcss.ds.event.IShippingContextEvent.EventAction;
import com.sdc2ch.prcss.ds.event.IShippingContextEvent.EventBy;
import com.sdc2ch.prcss.ds.event.IShippingEvent;
import com.sdc2ch.prcss.ds.event.ShippingAnalsEvent;
import com.sdc2ch.prcss.ds.event.ShippingStateEvent;
import com.sdc2ch.prcss.ds.impl.ShippingStateServiceImpl2;
import com.sdc2ch.prcss.ds.io.DrivingTripIO;
import com.sdc2ch.prcss.ds.io.ShippingState2;
import com.sdc2ch.prcss.ds.repo.domain.T_SHIPPING_COMMUTE_TIME_STATE;
import com.sdc2ch.prcss.ds.repo.domain.T_SHIPPING_RT_STATE;
import com.sdc2ch.repo.io.AllocatedGroupIO;
import com.sdc2ch.repo.io.ShippingID;
import com.sdc2ch.repo.io.TmsDriverIO;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.pubsub.I2ChEventConsumer;
import com.sdc2ch.require.pubsub.I2ChEventManager;
import com.sdc2ch.tms.enums.FactoryType;
import com.sdc2ch.tms.io.TmsPlanIO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
 
@Slf4j
public class ShippingPlanContext2 implements IShippingContext{
	
	

	private static String FIRST_SORT_TIME = "조조기";
	
	private IUser user;
	private ShippingStateServiceImpl2 service;
	
	
	private I2ChEventManager manager;
	private AllocatedGroupIO allocatedGroup;
	private List<IShippingHandler> handlers = new ArrayList<>();
	private LinkedHashMap<String, List<TmsPlanIO>> sortedMap = new LinkedHashMap<>();
	private List<ShippingStateEvent> events = new ArrayList<>();
	private ShippingStateEvent current;
	private List<TmsPlanIO> tmsplans = new ArrayList<>();
	
	private long lastCommuteRoqId;
	
	private List<DrivingTripIO> drivingTrips = new ArrayList<>();
	
	@Getter
	private double accDistance;
	
	@Getter
	@RequiredArgsConstructor(staticName = "of")
	private static class SortTime {
		final String routeNo;
		final LocalTime time;
		final String timeZone;
	}
	public ShippingPlanContext2() {
		super();
	}
	public ShippingPlanContext2(I2ChEventManager manager, AllocatedGroupIO allocatedGroup, IUser user, ShippingStateServiceImpl2 service) {
		super();
		this.manager = manager;
		this.user = user;
		this.allocatedGroup = allocatedGroup;
		this.service = service;
	}


	public I2ChEventManager getEventManager() {
		return manager;
	}
	public IUser getUser() {
		return user;
	}
	public void addAll(List<TmsPlanIO> tmsplans) {
		
		if(tmsplans != null) {
			Map<String, List<TmsPlanIO>> groups = tmsplans.stream().collect(Collectors.groupingBy(p -> p.getRouteNo()));
			List<SortTime> reidx2 = reindex(tmsplans);
			reidx2.forEach(s -> {
				String routeNo = s.getRouteNo();
				List<TmsPlanIO> orgs = groups.get(routeNo);
				List<TmsPlanIO> reindexs = orgs.stream().sorted(Comparator.nullsLast(Comparator.comparing(TmsPlanIO::getStopSeq))).collect(Collectors.toList());
				sortedMap.put(routeNo, reindexs);
			});
		}
		this.tmsplans = tmsplans;
	}
	@Override
	public List<TmsPlanIO> getShippings() {
		return tmsplans;
	}
	@Override
	public List<ShippingStateEvent> searchEvents(String routeNo, EventAction action) {
		return events.stream().filter(e -> {
			return routeNo.equals(e.getRouteNo()) && action == e.getEventAct();
		}).collect(Collectors.toList());
	}
	@Override
	public Long getGroupId() {
		return allocatedGroup.getId();
	}
	public void addHandler(IShippingHandler handler) {
		handlers.add(handler);
	}

	public void fireEvent(IShippingEvent shipEvent) {
		switch (shipEvent.getShippingEventTy()) {
		case ANALS_EVENT:
			setAnalsEvent(shipEvent);
			break;
		case STATE_EVENT:
			setStateEvent(shipEvent);
			service.saveRouteState(this);
			break;
		default:
			break;
		}
		

	}
	
	private void setStateEvent(IShippingEvent shipEvent) {
		ShippingStateEvent event = (ShippingStateEvent) shipEvent;
		if(!StringUtils.isEmpty(event.getRouteNo())) {
			int idx = events.size();
			for(int i = idx; i > 0 ; i--) {
				ShippingStateEvent _evnet = events.get(i - 1);
				if(StringUtils.isEmpty(_evnet.getRouteNo())) {
					_evnet.setRouteNo(event.getRouteNo());
				}else {
					break;
				}
			}
		}

		events.add(event);
		this.current = event;
		saveRT(shipEvent);
	}


	public void saveRT(IShippingEvent shipEvent) {
		
		try {
			
			if(shipEvent instanceof ShippingStateEvent) {
				ShippingStateEvent e = (ShippingStateEvent) shipEvent;
				T_SHIPPING_RT_STATE rt = new T_SHIPPING_RT_STATE();
				rt.setAllocatedGroupId(getGroupId());
				rt.setDlvyDe(allocatedGroup.getDlvyDe());
				
				String routeNo = e.getRouteNo();
				TmsPlanIO plan = getCurrentStop();
				
				if(StringUtils.isEmpty(e.getRouteNo())) {
					routeNo = plan == null? null : plan.getRouteNo();
				}
				
				rt.setRouteNo(routeNo);
				rt.setState(e.getState());
				if(plan != null) {
					rt.setTmsPlanRowIdFk(plan.getId());
				}
				
				TmsDriverIO driver = (TmsDriverIO) user.getUserDetails();
				ShippingID id = new ShippingID(driver.getCar().getVrn(), user.getUsername());
				rt.setId(id);
				service.saveShippingRTState(rt);
			}
			
		}catch (Exception e) {
			log.error("{}", e);
		}
	}

	private void setAnalsEvent(IShippingEvent shipEvent) {
		
		ShippingAnalsEvent event = (ShippingAnalsEvent) shipEvent;
		switch (event.getAnalsTy()) {
		case CONTI_DRV:
			this.accDistance = new BigDecimal(event.getAccDistance()/1000).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
			break;
		case TRIP_DRV:
			DrivingTrip trip = new DrivingTrip();
			trip.startTimestamp = event.getTripDrvStTime();
			trip.endTimestamp = event.getTripDrvEdTime();
			drivingTrips.add(trip);
			break;
		default:
			break;
		}
	}


	public LocalDate getDlvyDe() {
		return parse(allocatedGroup.getDlvyDe());
	}
	public void onClose() {
		onClose(EventAction.USR_FIN, EventBy.MOBILE_WEB);
	}
	
	public void onReady() {
		log.info("created context By {}", allocatedGroup);
		if(!sortedMap.isEmpty()) {
			
			sortedMap.values().forEach(ps -> {
				if(handlers != null) {
					handlers.forEach(h -> {
						h.onReady();
						ps.forEach(p -> h.add(p));
					});
				}
			});
		}

	}
	
	public AllocatedGroupIO getAllocatedGIO() {
		return allocatedGroup;
	}
	public TmsPlanIO getLastByPlan() {
		int count = (int) sortedMap.keySet().stream().count();
		count = count == 0 ? 0 : count - 1;
		String lastBy = sortedMap.keySet().stream().collect(Collectors.toList()).get(count);
		return sortedMap.get(lastBy).stream()
				.filter(p -> p.getStopSeq() != 0 && FactoryType.convert(p.getStopCd()) != FactoryType.FFFF)
				.findFirst()
				.orElse(null);
	}


	public void onForceClosed() {
		onClose(EventAction.SYS_FIN, EventBy.SYSTEM);
	}
	public void onCancel() {
		onClose(EventAction.MGR_CANCEL, EventBy.ADMIN);
	}
	
	public void update(List<TmsPlanIO> tmsplans) {
		
		if(tmsplans != null) {
			
			Map<String, List<TmsPlanIO>> groups = tmsplans.stream().collect(Collectors.groupingBy(p -> p.getRouteNo()));
			List<SortTime> reidx2 = reindex(tmsplans);
			sortedMap.clear();
			reidx2.forEach(s -> {
				String routeNo = s.getRouteNo();
				List<TmsPlanIO> orgs = groups.get(routeNo);
				List<TmsPlanIO> reindexs = orgs.stream().sorted(Comparator.nullsLast(Comparator.comparing(TmsPlanIO::getStopSeq))).collect(Collectors.toList());
				sortedMap.put(routeNo, reindexs);
			});
			onReady();
		}
	}
	
	
	public ShippingStateEvent getCurrentEvent() {
		return current;
	}
	
	private List<SortTime> reindex(List<TmsPlanIO> tmsplans) {
		List<TmsPlanIO> top = tmsplans.stream().filter(p -> p.getStopSeq() == 0).collect(Collectors.toList());
		List<SortTime> reidx = top.stream().map(p -> {
			String sTime = service.findScheduleTime(p);
			if(StringUtils.isEmpty(sTime)) {
				sTime = "00:00";
			}
			LocalTime time = LocalTime.parse(sTime);
			return SortTime.of(p.getRouteNo(), time, p.getTimeZoneNm());
		}).sorted(Comparator.comparing(SortTime::getTime)).collect(Collectors.toList());
		List<SortTime> reidx2 = new ArrayList<>();
		reidx2.addAll(reidx.stream().filter(s -> FIRST_SORT_TIME.equals(s.getTimeZone())).collect(Collectors.toList()));
		reidx.removeIf(s -> FIRST_SORT_TIME.equals(s.getTimeZone()));
		reidx2.addAll(reidx);
		return reidx2;
	}
	
	private LocalDate parse(String dlvyDe) {
		return LocalDate.parse(dlvyDe, DateTimeFormatter.ofPattern("yyyyMMdd"));
	}
	
	private void onClose(EventAction eventNm, EventBy eventBy) {
		if(handlers != null)handlers.forEach(h -> h.onClose());
		ShippingStateEvent event = new ShippingStateEvent();
		event.setEventDt(new Date());
		event.setEventAct(eventNm);
		event.setEventBy(eventBy);
		event.setState(ShippingState2.COMPLETE);
		fireEvent(event);
		service.onContextCloseFireEvent(this);
	}

	@Override
	public List<ShippingStateEvent> findByEventActions(EventAction... actions) {
		if(actions != null) {
			List<EventAction> acts = Arrays.asList(actions);
			return events.stream().filter(e -> acts.contains(e.getEventAct())).collect(Collectors.toList());
		}
		return null;
	}


	public void setLastCommuteRoqId(long id) {
		this.lastCommuteRoqId = id;
	}

	@Override
	public Date getBeforeDayDrivingFinishDate() {
		T_SHIPPING_COMMUTE_TIME_STATE commute = service.findCommuteById(lastCommuteRoqId).orElse(null);
		return commute == null ? null : commute.getEventDt();
	}
	
	class DrivingTrip implements DrivingTripIO {
		private long startTimestamp;
		private long endTimestamp;
		@Override
		public long startTimestamp() {
			return startTimestamp;
		}
		@Override
		public long endTimestamp() {
			return endTimestamp;
		}
		
	}

	@Override
	public List<Long> getDrivingTripTimes() {
		return drivingTrips.stream().map(d -> {
			long time = (d.endTimestamp() - d.startTimestamp()) / 1000;
			return time > 0 ? time : 0;
		}).collect(Collectors.toList());
	}

	@Override
	public List<DrivingTripIO> getTrips() {
		return drivingTrips;
	}

	@Override
	public I2ChEventConsumer<?> subscribe(Class<?> receive) {
		return manager.subscribe(receive);
	}
	
	public TmsPlanIO getCurrentStop() {
		
		if(current == null) {
			return tmsplans.stream().findFirst().orElse(null);
		}
		String routeNo = current.getRouteNo();
		
		if(StringUtils.isEmpty(routeNo)) {
			for(int i = events.size() -1; i >= 0 ; i--) {
				if(!StringUtils.isEmpty(events.get(i).getRouteNo())) {
					routeNo = events.get(i).getRouteNo();
					break;
				}
			}
		}else {
			
			List<TmsPlanIO> _plans = tmsplans.stream().filter(t -> t.getRouteNo().equals(current.getRouteNo()) && t.getStopCd().equals(current.getStopCd())).collect(Collectors.toList());
			
			if(_plans.size() > 1) {
				
				List<ShippingState2> states = Arrays.asList(ShippingState2.LOADING, ShippingState2.ENTER2, ShippingState2.EXIT, ShippingState2.TAKEOVER, ShippingState2.DEPART);
				boolean anyMatch = events.stream().map(e -> states.contains(e.getState())).anyMatch(b -> b);
				
				if(anyMatch) {
					return _plans.get(_plans.size() - 1);
				}else {
					TmsPlanIO plan = _plans.stream().filter(p -> service.findScheduleTime(p) == null).min(Comparator.comparingLong(p -> {
						String sDate = null;
						try {
							if(p.getStopSeq() == 0) {
								sDate = p.getScheDlvyStDe() + p.getScheDlvyStTime();
							}else {
								sDate = p.getScheDlvyEdDe() + service.findScheduleTime(p);
							}
							LocalDateTime ldt = LocalDateTime.parse(sDate, DateTimeFormatter.ofPattern("yyyyMMddHH:mm"));
							return Timestamp.valueOf(ldt).getTime();
						}catch (Exception e) {
						}
						return Long.MAX_VALUE;
					})).orElse(null);
					 
					 return plan == null ? getDefault() : plan;
				}
				
			}else {
				if(_plans.isEmpty()){
					return tmsplans.get(0);
				}
				return _plans.get(0);
			}
		}
		return getDefault();
	}

	private TmsPlanIO getDefault() {
		return tmsplans.isEmpty() ? null : tmsplans.get(0);
	}

	public TmsPlanIO findNextStop(TmsPlanIO plan) {
		int curIdx = 0;
		int size = 0;
		if(tmsplans != null) {
			size = tmsplans.size();
			for(int i = 0 ; i < tmsplans.size() ; i++) {
				TmsPlanIO _plan = tmsplans.get(i);
				if(_plan.getRouteNo().equals(plan.getRouteNo()) && 
						_plan.getStopCd().equals(plan.getStopCd()) && 
						_plan.getStopSeq()== plan.getStopSeq()) {
					curIdx = i + 1;
					break;
				}
			}
		}
		
		if(curIdx != size) {
			return tmsplans.get(curIdx);
		}
		return null;
	}


	public int findInputEmptyBoxCount() {
		if(tmsplans.isEmpty()) {
			return 0;
		}
		return (int) tmsplans.stream().filter(p -> FactoryType.FFFF == FactoryType.convert(p.getStopCd())).count();
	}

	public List<String> findAllByRouteNo() {
		return tmsplans.stream().map(p -> p.getRouteNo()).distinct().collect(Collectors.toList());
	}

	@Override
	public void batchClose() {
		service.removeContext(this);
	}
	public List<String> getGroupByRouteNos() {
		if(tmsplans == null || tmsplans.isEmpty())
			return Collections.emptyList();
		return Lists.newArrayList(tmsplans.stream().map(p -> p.getRouteNo()).collect(Collectors.toCollection(HashSet::new)).iterator());
	}

}
