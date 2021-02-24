package com.sdc2ch.prcss.ss.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.sdc2ch.aiv.event.IGpsEvent;
import com.sdc2ch.prcss.ds.IShippingContext;
import com.sdc2ch.prcss.ds.IShippingStateService2;
import com.sdc2ch.prcss.ds.event.IShippingContextEvent;
import com.sdc2ch.prcss.ds.event.IShippingContextEvent.EventAction;
import com.sdc2ch.prcss.ds.event.IShippingContextEvent.EventBy;
import com.sdc2ch.prcss.ds.event.IShippingEvent;
import com.sdc2ch.prcss.ds.event.ShippingAnalsEvent;
import com.sdc2ch.prcss.ds.event.ShippingStateEvent;
import com.sdc2ch.prcss.ds.handler.AbstractShippingHandler;
import com.sdc2ch.prcss.ds.handler.ShippingAccDistanceHandler;
import com.sdc2ch.prcss.ds.handler.ShippingContinueDriveHandler;
import com.sdc2ch.prcss.ds.handler.ShippingGpsHandler;
import com.sdc2ch.prcss.ds.io.DrivingTripIO;
import com.sdc2ch.prcss.ds.io.ShippingState2;
import com.sdc2ch.prcss.eb.IEmptyBoxService;
import com.sdc2ch.prcss.eb.vo.EmptyboxVo;
import com.sdc2ch.prcss.ss.IDriveBatchService;
import com.sdc2ch.repo.builder.IAdmQueryBuilder;
import com.sdc2ch.repo.io.AllocatedGroupIO;
import com.sdc2ch.repo.io.TmsDriverIO;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.pubsub.I2ChEventConsumer;
import com.sdc2ch.require.pubsub.I2ChEventManager;
import com.sdc2ch.require.pubsub.I2ChEventPublisher;
import com.sdc2ch.require.service.I2ChUserService;
import com.sdc2ch.tms.io.TmsPlanIO;
import com.sdc2ch.tms.service.ITmsPlanService;
import com.sdc2ch.tms.service.ITmsShippingService;
import com.sdc2ch.web.service.IAllocatedGroupService;

import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ScheduleComponent implements IDriveBatchService {


	@Autowired IAllocatedGroupService groupSvc;
	@Autowired I2ChUserService userSvc;
	@Autowired ITmsPlanService planSvc;
	@Autowired IEmptyBoxService emtSvc;
	@Autowired IShippingStateService2 stateSvc;
	@Autowired I2ChEventManager manager;
	@Autowired ITmsShippingService shipSvc;


	@Autowired IAdmQueryBuilder builder;

	private I2ChEventPublisher<IShippingContextEvent> contextEvent;

	@Autowired
	public void init() {
		this.contextEvent = manager.regist(IShippingContextEvent.class);
	}


	@Scheduled(cron="0 0 5 * * *")
	public void cron() {
		LocalDate batchDe = LocalDate.now().minusDays(1);
		work(batchDe);
	}


	public void work(LocalDate ld) {
		try {
			LocalDate batchDe = ld;

			List<AllocatedGroupIO> groups = groupSvc.findAllocatedGroupByDlvyDe(batchDe.format(DateTimeFormatter.ofPattern("yyyyMMdd")));

			if(groups != null) {

				List<AllocatedGroupIO> filterGroups = groups.stream().filter(g -> g.getTrnsmisDt() != null).collect(Collectors.toList());















				


				filterGroups.stream().forEach(g -> {

					try {
						String vrn = null;
						IUser user = userSvc.findByUsername(g.getDriverCd()).orElseGet(null);
						BehaviorSubject<ShippingStateEvent> geofenceObserver = BehaviorSubject.create();

						if(user != null) {
							ShippingBatchContext context = new ShippingBatchContext();
							context.setAllocatedGroup(g);
							context.setUser(user);

							List<TmsPlanIO> plans = planSvc.findTmPlansByUserAndDeleveryDate(user, g.getDlvyDe());
							context.addAll(plans);
							List<ShippingStateEvent> events = makeEvents(g, plans);

							Disposable disposable = geofenceObserver.subscribe(e -> events.add(e));

							TmsDriverIO driver = (TmsDriverIO) user.getUserDetails();

							if(driver != null && driver.getCar() != null) {
								vrn = driver.getCar().getVrn();
							}

							if(!StringUtils.isEmpty(vrn)) {

								




								
								
								List<EventAction> startActions = Arrays.asList(EventAction.USR_ST, EventAction.NFC_TAG_OFFIC);
								List<ShippingStateEvent> starts = events.stream().filter(e -> startActions.contains(e.getEventAct())).collect(Collectors.toList());
								List<EventAction> endActions = Arrays.asList(EventAction.USR_FIN, EventAction.SYS_FIN);
								List<ShippingStateEvent> end = events.stream().filter(e -> endActions.contains(e.getEventAct())).collect(Collectors.toList());
								
								Date startDt = findDate(starts, batchDe);
								Date endDt = findDate(end, batchDe.plusDays(1));

								List<Object[]> results = builder.storedProcedureResultCall("[dbo].[SP_2CH_LOCATION_INFO_HIST]", startDt, endDt, vrn);

								if(results != null) {

									List<AbstractShippingHandler> handlers = makeHandler(context, geofenceObserver);

									handlers.stream().forEach(h -> {
										context.getShippings().forEach(plan -> {h.add(plan);});
									});

									for(Object[] datas : results) {
										GpsEvent event = new GpsEvent();
										event.setLat(new BigDecimal(datas[11]+"").doubleValue());
										event.setLng(new BigDecimal(datas[12]+"").doubleValue());
										event.setSpeed(datas[13]+"");
										Object dt = datas[7];

										if(dt instanceof Timestamp) {
											Timestamp ts = (Timestamp)dt;
											event.setTimeStamp(ts.getTime());
										}
										event.setUser(user);
										handlers.stream().forEach(h -> h.handle(event));
									}

								}
							}

							disposable.dispose();

							List<ShippingStateEvent> sorted  = events.stream()
								.filter(e -> e.getEventDt() != null)
								.sorted(Comparator.comparing(ShippingStateEvent::getEventDt))
								.collect(Collectors.toList());



							sorted.forEach(e -> context.fireEvent(e));




















							
							
							
							
							contextEvent.fireEvent(ShippingContextEventImpl.of(context));
						}

					}catch (Exception e) {
						log.error("{}", e);
					}

				});

			}
		}catch (Exception e) {
			
			log.error("{}" , e);
			e.printStackTrace();
		}finally {
		}


	}
	
	private List<AbstractShippingHandler> makeHandler(ShippingBatchContext context, BehaviorSubject<ShippingStateEvent> geofenceObserver) {
		ShippingGpsHandler handler = new ShippingGpsHandler(context);
		handler.addObserver(geofenceObserver);
		return Stream.of(handler, new ShippingContinueDriveHandler(context), new ShippingAccDistanceHandler(context)).collect(Collectors.toList());
	}

	private Date findDate(List<ShippingStateEvent> starts, LocalDate adjst) {

		Date searchDate = Date.from(adjst.atStartOfDay(ZoneId.systemDefault()).toInstant());

		if(starts != null && !starts.isEmpty()) {
			ShippingStateEvent st = starts.stream()
					.filter(e -> e.getEventDt() != null)
					.min(Comparator.comparing(ShippingStateEvent::getEventDt)).orElse(null);
			if(st != null) {
				searchDate = st.getEventDt();
			}
		}

		return searchDate;
	}


	private List<ShippingStateEvent> makeEvents(AllocatedGroupIO g, List<TmsPlanIO> plans) {

		List<ShippingStateEvent> events = new ArrayList<>();

		events.addAll(stateSvc.findCommuteTimeStateByAllocatedGId(g.getId()));
		events.addAll(stateSvc.findInoutStateByAllocatedGId(g.getId()));
		events.addAll(stateSvc.findLdngStateByAllocatedGId(g.getId()));

		if(plans != null) {

			Set<String> routeNos = plans.stream().map(p -> p.getRouteNo()).collect(Collectors.toSet());
			List<EmptyboxVo> emptBoxs = emtSvc.findEmptyBoxByRouteOnly(g.getDlvyDe(), false, routeNos.toArray(new String[routeNos.size()]));

			if(emptBoxs != null) {
				events.addAll(emptBoxs.stream().map(emt -> {
					ShippingStateEvent sEvent = new ShippingStateEvent();
					sEvent.setEventDt(emt.getCreateDt());
					sEvent.setPalletQty(emt.getPalletQty());
					sEvent.setRouteNo(emt.getRouteNo());
					sEvent.setSquareBoxQty(emt.getSquareBoxQty());
					sEvent.setState(ShippingState2.TAKEOVER);
					sEvent.setEventAct(EventAction.USR_EB);
					sEvent.setEventBy(EventBy.MOBILE_WEB);
					sEvent.setStopCd(emt.getStopCd());
					sEvent.setTriangleBoxQty(emt.getTriangleBoxQty());
					sEvent.setYodelryBoxQty(emt.getYodelryBoxQty());
					sEvent.setCause(emt.getCause() == null ? null : emt.getCause().name());
					return sEvent;
				}).collect(Collectors.toSet()));
			}

		}

		events = events.stream()
		.filter(e -> e.getEventDt() != null)
		.sorted(Comparator.comparing(ShippingStateEvent::getEventDt))
		.collect(Collectors.toList());

		return events;
	}


	@Getter
	@RequiredArgsConstructor(staticName = "of")
	private static class SortTime {
		final String routeNo;
		final LocalTime time;
		final String timeZone;
	}

	@Getter
	@Setter
	class ShippingBatchContext implements IShippingContext {

		private String FIRST_SORT_TIME = "조조기";

		private IUser user;
		private AllocatedGroupIO allocatedGroup;

		private LinkedHashMap<String, List<TmsPlanIO>> sortedMap = new LinkedHashMap<>();
		@Getter
		private List<ShippingStateEvent> events = new ArrayList<>();
		private ShippingStateEvent current;
		private List<TmsPlanIO> tmsplans;

		private long lastCommuteRoqId;

		private List<DrivingTripIO> drivingTrips = new ArrayList<>();

		@Setter
		@Getter
		private double accDistance;

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
		public List<ShippingStateEvent> findByEventActions(EventAction... actions) {
			if(actions != null) {
				List<EventAction> acts = Arrays.asList(actions);
				return events.stream().filter(e -> acts.contains(e.getEventAct())).collect(Collectors.toList());
			}
			return null;
		}

		@Override
		public Long getGroupId() {
			return allocatedGroup.getId();
		}

		@Override
		public List<Long> getDrivingTripTimes() {
			return drivingTrips.stream().map(trip -> {
				long t = trip.endTimestamp() - trip.startTimestamp();
				return t > 0 ? t : 0;
			}).collect(Collectors.toList());
		}

		@Override
		public Date getBeforeDayDrivingFinishDate() {
			
			return null;
		}

		@Override
		public LocalDate getDlvyDe() {
			return LocalDate.parse(allocatedGroup.getDlvyDe(), DateTimeFormatter.ofPattern("yyyyMMdd"));
		}

		@Override
		public List<DrivingTripIO> getTrips() {
			return drivingTrips;
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
		private List<SortTime> reindex(List<TmsPlanIO> tmsplans) {
			List<TmsPlanIO> top = tmsplans.stream().filter(p -> p.getStopSeq() == 0).collect(Collectors.toList());
			List<SortTime> reidx = top.stream().map(p -> {
				String sTime = shipSvc.getPlanedArriveTime(p);
				if(StringUtils.isEmpty(sTime)) {
					sTime = "00:00";
				}
				LocalTime time = LocalTime.parse(sTime);
				return SortTime.of(p.getRouteNo(), time, p.getTimeZoneNm());
			}).sorted(Comparator.comparing(SortTime::getTime)).collect(Collectors.toList());
			List<SortTime> reidx2 = new ArrayList<>();

			try {

				reidx2.addAll(reidx.stream().filter(s -> FIRST_SORT_TIME.equals(s.getTimeZone())).collect(Collectors.toList()));
				reidx.removeIf(s -> FIRST_SORT_TIME.equals(s.getTimeZone()));
				reidx2.addAll(reidx);
			}catch (Exception e) {
				e.printStackTrace();
				System.out.println(e);
			}
			return reidx2;
		}

		@Override
		public I2ChEventConsumer<?> subscribe(Class<?> receive) {
			return null;
		}

		public void fireEvent(IShippingEvent shipEvent) {
			switch (shipEvent.getShippingEventTy()) {
			case ANALS_EVENT:
				setAnalsEvent(shipEvent);
				break;
			case STATE_EVENT:
				setStateEvent(shipEvent);
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
						
						if ( event.getEventAct() != EventAction.USR_ENTER ) {
							_evnet.setRouteNo(event.getRouteNo());
						}
					}else {
						break;
					}
				}
			}

			

			
			if(!StringUtils.isEmpty(event.getRouteNo()) && event.getEventAct() == EventAction.USR_EXITED) {
				int idx = events.size();
				for(int i = idx; i > 0 ; i--) {
					ShippingStateEvent _evnet = events.get(i - 1);
					if (_evnet.getEventAct() == EventAction.NFC_TAG_LDNG) {
						if (StringUtils.isEmpty(_evnet.getRouteNo())) {
							_evnet.setRouteNo(event.getRouteNo());
						} else {
							break;
						}
					}
				}
			}
			

			events.add(event);
			this.current = event;
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

		@Override
		public void batchClose() {
			

		}

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

	@Getter
	@RequiredArgsConstructor(staticName = "of")
	public static class ShippingContextEventImpl implements IShippingContextEvent {

		private final IShippingContext context;

		@Override
		public IUser user() {
			return context.getUser();
		}

	}

	@Getter
	@Setter
	public class GpsEvent implements IGpsEvent {
		private IUser user;
		private double lat;
		private double lng;
		private long timeStamp;
		private String speed;
		@Override
		public IUser user() {
			return user;
		}

	}

	@Override
	public String executeBatch(String dlvyDe) {
		if(!StringUtils.isEmpty(dlvyDe)) {
			dlvyDe = dlvyDe.replaceAll("-", "");
			LocalDate date = LocalDate.parse(dlvyDe, DateTimeFormatter.ofPattern("yyyyMMdd"));
			work(date);
			return dlvyDe;
		}
		return "can not execute date by " + dlvyDe;
	}
}
