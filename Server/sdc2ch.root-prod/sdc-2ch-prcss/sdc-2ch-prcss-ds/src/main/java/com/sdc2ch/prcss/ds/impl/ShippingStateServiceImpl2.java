package com.sdc2ch.prcss.ds.impl;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.sdc2ch.aiv.event.IFirebaseNotificationEvent;
import com.sdc2ch.aiv.event.IFirebaseNotificationEvent.Priority;
import com.sdc2ch.nfc.event.INfcFireEvent;
import com.sdc2ch.prcss.ds.IShippingContext;
import com.sdc2ch.prcss.ds.IShippingStateService2;
import com.sdc2ch.prcss.ds.core.ShippingPlanContext2;
import com.sdc2ch.prcss.ds.core.ShppingScanner;
import com.sdc2ch.prcss.ds.event.ActionEventType;
import com.sdc2ch.prcss.ds.event.IShippingContextEvent;
import com.sdc2ch.prcss.ds.event.IShippingContextEvent.EventAction;
import com.sdc2ch.prcss.ds.event.IShippingContextEvent.EventBy;
import com.sdc2ch.prcss.ds.event.ShippingStateEvent;
import com.sdc2ch.prcss.ds.handler.ShippingAccDistanceHandler;
import com.sdc2ch.prcss.ds.handler.ShippingContinueDriveHandler;
import com.sdc2ch.prcss.ds.handler.ShippingGpsHandler;
import com.sdc2ch.prcss.ds.io.ShippingPlanIO;
import com.sdc2ch.prcss.ds.io.ShippingState2;
import com.sdc2ch.prcss.ds.repo.T_RouteStateRepository;
import com.sdc2ch.prcss.ds.repo.T_ShippingCommuteTimeStateRepository;
import com.sdc2ch.prcss.ds.repo.T_ShippingInoutStateRepository;
import com.sdc2ch.prcss.ds.repo.T_ShippingLdngStateRepository;
import com.sdc2ch.prcss.ds.repo.T_ShippingRtStateRepository;
import com.sdc2ch.prcss.ds.repo.T_ShippingStateHistRepository;
import com.sdc2ch.prcss.ds.repo.T_ShippingStateHistRepository2;
import com.sdc2ch.prcss.ds.repo.T_ShippingStateRepository;
import com.sdc2ch.prcss.ds.repo.domain.QT_ROUTE_STATE;
import com.sdc2ch.prcss.ds.repo.domain.T_ROUTE_STATE;
import com.sdc2ch.prcss.ds.repo.domain.T_SHIPPING_COMMUTE_TIME_STATE;
import com.sdc2ch.prcss.ds.repo.domain.T_SHIPPING_COMMUTE_TIME_STATE.EventName;
import com.sdc2ch.prcss.ds.repo.domain.T_SHIPPING_INOUT_STATE;
import com.sdc2ch.prcss.ds.repo.domain.T_SHIPPING_INOUT_STATE.EventInoutName;
import com.sdc2ch.prcss.ds.repo.domain.T_SHIPPING_LDNG_STATE;
import com.sdc2ch.prcss.ds.repo.domain.T_SHIPPING_RT_STATE;
import com.sdc2ch.prcss.ds.vo.ShipStateVo2;
import com.sdc2ch.prcss.eb.IEmptyBoxService;
import com.sdc2ch.prcss.eb.event.IEmptyboxEvent;
import com.sdc2ch.prcss.eb.vo.EmptyboxVo;
import com.sdc2ch.repo.io.AllocatedGroupIO;
import com.sdc2ch.repo.io.MobileAppInfoIO;
import com.sdc2ch.repo.io.RouteIO;
import com.sdc2ch.repo.io.RouteStateID;
import com.sdc2ch.repo.io.ShippingID;
import com.sdc2ch.repo.io.TmsCarIO;
import com.sdc2ch.repo.io.TmsDriverIO;
import com.sdc2ch.require.ApplicationEventType;
import com.sdc2ch.require.IApplicationEventListener;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.enums.SetupLcType;
import com.sdc2ch.require.pubsub.I2ChEventManager;
import com.sdc2ch.require.pubsub.I2ChEventPublisher;
import com.sdc2ch.require.service.I2ChUserService;
import com.sdc2ch.service.event.IAllocateCanceledEvent;
import com.sdc2ch.service.event.IAllocateChangeEvent;
import com.sdc2ch.service.event.IAllocatedEvent;
import com.sdc2ch.service.event.IDriverEnterFactoryEvent;
import com.sdc2ch.service.event.IDriverExitFactoryEvent;
import com.sdc2ch.service.event.IDriverFinishJobEvent;
import com.sdc2ch.service.event.IDriverStartJobEvent;
import com.sdc2ch.service.event.IMobileEvent.MobileEventType;

import com.sdc2ch.tms.enums.FactoryType;
import com.sdc2ch.tms.enums.ShippingType;
import com.sdc2ch.tms.io.TmsPlanIO;
import com.sdc2ch.tms.service.ITmsPlanService;
import com.sdc2ch.tms.service.ITmsShippingService;
import com.sdc2ch.tms.service.ITmsSmsService;
import com.sdc2ch.web.service.IAllocatedGroupService;
import com.sdc2ch.web.service.IMobileAppService;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ShippingStateServiceImpl2 implements IShippingStateService2 {

	@Autowired IAllocatedGroupService groupSvc;
	@Autowired ITmsPlanService tmsPlanSvc;
	@Autowired ITmsShippingService shipSvc;
	@Autowired I2ChEventManager manager;
	@Autowired I2ChUserService userSvc;
	@Autowired IMobileAppService appSvc;          
	@Autowired IEmptyBoxService emtSvc;
	@Autowired ITmsSmsService smsSvc;

	@Autowired T_ShippingStateRepository shipRepo;
	@Autowired T_ShippingRtStateRepository shipRtRepo;
	@Autowired T_ShippingStateHistRepository shipHistRepo;
	@Autowired T_ShippingStateHistRepository2 shipHist2Repo;
	@Autowired T_RouteStateRepository routeStateRepo;
	@Autowired T_ShippingCommuteTimeStateRepository commuteReop;
	@Autowired T_ShippingInoutStateRepository inoutReop;
	@Autowired T_ShippingLdngStateRepository ldngReop;

	
	private static final Map<Entry, ShippingPlanContext2> shippingMapped2 = new HashMap<>();

	private I2ChEventPublisher<IFirebaseNotificationEvent> eventPush;
	private I2ChEventPublisher<IShippingContextEvent> contextEvent;
	
	private static final long force_close_time = 10 * 60 * 60 * 1000;

	@Setter
	@Getter
	class Entry {

		public Entry(Long allocatedGroupId, String username) {
			this.allocatedGroupId = allocatedGroupId;
			this.username = username;
		}
		private Long allocatedGroupId;
		private String username;

		@Override
		public String toString() {
			return "[" + username + "]" + allocatedGroupId;
		}
		@Override
		public int hashCode() {
			return (username + allocatedGroupId).hashCode();
		}
		@Override
		public boolean equals(Object o) {
			return o.hashCode() == this.hashCode();
		}
	}


	@Scheduled(fixedRate = 1000 * 60 * 60 * 25)
	private void cleanContext () {
		shippingMapped2.values().removeIf(c -> {

			boolean isafter = LocalDate.now().isAfter(c.getDlvyDe());

			return isafter;
		});
	}

	@Scheduled(fixedRate = 1000 * 60 * 30)
	private void closedContext () {
		shippingMapped2.values().removeIf(c -> {
			TmsPlanIO lastBy = c.getLastByPlan();
			LocalDateTime startTime = LocalDateTime.now();
			LocalDateTime endTime = convert(lastBy.getDlvyDe(), shipSvc.getPlanedArriveTime(lastBy));
			long minutes = ChronoUnit.MILLIS.between(startTime, endTime);
			if(minutes < 0 && minutes + force_close_time < 0) {
				
				c.onForceClosed();

			}
			return minutes < 0 && minutes + force_close_time < 0;
		});
	}

	private LocalDateTime convert(String dlvyDe, String planedArriveTime) {
		String dtime = dlvyDe + planedArriveTime;
		return LocalDateTime.parse(dtime, DateTimeFormatter.ofPattern("yyyyMMddHH:mm"));
	}

	@PostConstruct
	public void init() {
		this.eventPush = manager.regist(IFirebaseNotificationEvent.class);
		this.contextEvent = manager.regist(IShippingContextEvent.class);




	}
	@Autowired
	private void applicationKillEvent(IApplicationEventListener el) {
		el.subscribe(ApplicationEventType.ON_CLOSED).subscribe(e -> {
			
			log.info("{}", shippingMapped2);
		});
	}

	
	@Autowired
	private void subscribeChangeEvent(I2ChEventManager manager) {

		manager.subscribe(IAllocateChangeEvent.class).filter(e -> {

			if(e instanceof IAllocateChangeEvent) {

				try {
					IAllocateChangeEvent event = (IAllocateChangeEvent) e;
					Long allocatedGId = event.getAllocatedGroupId();
					ShippingPlanContext2 context = getContext(allocatedGId);
					if(context != null) {
						AllocatedGroupIO allocatedGroup = allocatedGId == null ? findByAllocatedGid(e.user()) : findByAllocatedGid(allocatedGId, e.user());
						List<TmsPlanIO> tmsplans = tmsPlanSvc.findTmPlansByUserAndDeleveryDate(e.user(), allocatedGroup.getDlvyDe());
						context.update(tmsplans);
					}
				}catch (Exception ex) {
					log.error("{}", ex);
				}
			}
		});
	}
	
	@Autowired
	private void subscribeCanceledEvent(I2ChEventManager manager) {

		manager.subscribe(IAllocateCanceledEvent.class).filter(e -> {
			if(e instanceof IAllocateCanceledEvent) {
				try {
					IAllocateCanceledEvent event = (IAllocateCanceledEvent) e;
					ShippingPlanContext2 context = getContext(event.getAllocatedGroupId());
					if(context != null) {
						context.onCancel();
					}
				}catch (Exception ex) {
					log.error("{}", ex);
				}
			}
		});
	}
	
	@Autowired
	private void subscribeNfcTagEvent(I2ChEventManager manager) {

		manager.subscribe(INfcFireEvent.class).filter(e -> {

			log.info("received NFC Event -> {}", e);

			ShippingStateEvent sEvent = null;
			ShippingPlanContext2 context = null;

			try {

				INfcFireEvent event = (INfcFireEvent) e;

				SetupLcType ty = event.getSetupLcTy();

				
				context = getContext(e.user().getUsername()) == null ? createShippingContext(e.user(), ty == SetupLcType.OFFICE ? EventAction.NFC_TAG_OFFIC : EventAction.NFC_TAG_LDNG, EventBy.NFC, event.getEventTime()) : getContext(e.user().getUsername());

				if(context != null) {

					sEvent = new ShippingStateEvent();
					sEvent.setEventDt(new Date(event.getEventTime()));
					sEvent.setStopCd(event.getFactoryType().getCode());
					sEvent.setEventBy(EventBy.NFC);
					if(context != null) {
						switch (event.getSetupLcTy()) {
						case A1COLD:
						case A2COLD:
						case B1COLD:
						case B2COLD:
						case CHEESE:
						case CU:
						case FLAT:
						case HOSANG:
						case PRCSSGD:
						case STERILIZED:
							sEvent.setState(ShippingState2.LOADING);
							sEvent.setSetupLc(event.getSetupLcTy());
							sEvent.setEventAct(EventAction.NFC_TAG_LDNG);
							save(context.getAllocatedGIO().getId(), event.user().getUsername(), new Date(event.getEventTime()), ShippingState2.LOADING, EventBy.NFC, EventAction.NFC_TAG_LDNG, event.getFactoryType(), event.getSetupLcTy());
							break;
						case OFFICE:
							sEvent.setSetupLc(event.getSetupLcTy());
							sEvent.setEventAct(EventAction.NFC_TAG_OFFIC);
							sEvent.setState(ShippingState2.ENTER);
							break;
						case FRONT:
							break;
						case TEST:
							break;
						default:
							break;
						}
					}
					context.fireEvent(sEvent);
				}

			}catch (Exception ex) {
				log.error("{}", ex);
			}finally {
			}

		});
	}
	
	@Autowired
	@Transactional(readOnly = true)
	private void subscribeDriveStart(I2ChEventManager manager) {

		manager.subscribe(IDriverStartJobEvent.class).filter(e -> {

			try {
				IDriverStartJobEvent event = (IDriverStartJobEvent) e;
				Long allocatedGId = event.getAllocatedGroupId();
				ShippingPlanContext2 context = getContext(allocatedGId) == null ? createShippingContext(allocatedGId, e.user(), EventAction.USR_ST, EventBy.MOBILE_WEB) : getContext(allocatedGId);
				if(context != null) {

				}

			}catch (Exception ex) {
				log.error("{}", ex);
			}
		});
	}
	
	@Autowired
	private void subscribeDriveFinish(I2ChEventManager manager) {
		manager.subscribe(IDriverFinishJobEvent.class).filter(e -> {

			try {

				IDriverFinishJobEvent event = (IDriverFinishJobEvent) e;
				removeContext(event);
			}catch (Exception ex) {
				log.error("{}", ex);
			}

		});
	}
	
	@Autowired
	private void subscribeDepartFactory(I2ChEventManager manager) {

		manager.subscribe(IDriverExitFactoryEvent.class).filter(e -> {
			try {
				IDriverExitFactoryEvent event = (IDriverExitFactoryEvent) e;
				onFactoryInoutEvent(event.getAllocatedGroupId(), event.user(), event.getEventDt(), event.getRouteNo(), event.getMobileEventType(), event.getFctryCd());
			}catch (Exception ex) {
				log.info("{}", ex);
			}
		});
	}
	
	@Autowired
	private void subscribeArriveFactory(I2ChEventManager manager) {
		manager.subscribe(IDriverEnterFactoryEvent.class).filter(e -> {
			try {
				IDriverEnterFactoryEvent event = (IDriverEnterFactoryEvent) e;
				onFactoryInoutEvent(event.getAllocatedGroupId(), event.user(), new Date(), event.getRouteNo(), event.getMobileEventType(), event.getFctryCd());
			}catch (Exception ex) {
				log.info("{}", ex);
			}
		});
	}

	
	@Autowired
	private void subscribeEmptybox(I2ChEventManager manager) {
		manager.subscribe(IEmptyboxEvent.class).filter(e -> {

			try {
				IEmptyboxEvent event = (IEmptyboxEvent)e;
				ShippingPlanContext2 context = getContext(event.user().getUsername());

				if(context != null) {
					
					if(context.getAllocatedGIO().getDlvyDe().equals(event.getDlvyDe())) {
						ShippingStateEvent sEvent = new ShippingStateEvent();
						sEvent.setEventDt(new Date(event.getTimeStamp()));
						sEvent.setPalletQty(event.getPalletQty());
						sEvent.setRouteNo(event.getRouteNo());
						sEvent.setSquareBoxQty(event.getSquareBoxQty());
						sEvent.setState(ShippingState2.TAKEOVER);
						sEvent.setEventAct(EventAction.USR_EB);
						sEvent.setEventBy(EventBy.MOBILE_WEB);
						sEvent.setStopCd(event.getDlvyLcId());
						sEvent.setTriangleBoxQty(event.getTriangleBoxQty());
						sEvent.setYodelryBoxQty(event.getYodelryBoxQty());
						sEvent.setCause(event.getCause());
						sEvent.setDlvyLcNm(event.getDlvyLcNm());
						context.fireEvent(sEvent);
						sendMessage(sEvent, event.user());

					}
				}else {


					TmsDriverIO driver = (TmsDriverIO) event.user().getUserDetails();

					if(driver.getCar() != null) {

						AllocatedGroupIO groups = groupSvc.findByDlvyDeAndVrn(event.getDlvyDe(), driver.getCar().getVrn());

						if(groups != null) {
							context = createShippingContext(groups.getId(), e.user(), EventAction.USR_EB, EventBy.MOBILE_WEB);














							if(context != null) {
								ShippingStateEvent sEvent = new ShippingStateEvent();
								sEvent.setEventDt(new Date(event.getTimeStamp()));
								sEvent.setPalletQty(event.getPalletQty());
								sEvent.setRouteNo(event.getRouteNo());
								sEvent.setSquareBoxQty(event.getSquareBoxQty());
								sEvent.setState(ShippingState2.TAKEOVER);
								sEvent.setEventAct(EventAction.USR_EB);
								sEvent.setEventBy(EventBy.MOBILE_WEB);
								sEvent.setStopCd(event.getDlvyLcId());
								sEvent.setTriangleBoxQty(event.getTriangleBoxQty());
								sEvent.setYodelryBoxQty(event.getYodelryBoxQty());
								sEvent.setCause(event.getCause());
								sEvent.setDlvyLcNm(event.getDlvyLcNm());
								context.fireEvent(sEvent);
								sendMessage(sEvent, event.user());
							}
						}
					}
				}

			}catch (Exception ex) {
				log.error("{}", ex);
			}
		});
	}
	@Autowired
	private void subscribeAllocate(I2ChEventManager manager) {
		manager.subscribe(IAllocatedEvent.class).filter(e -> {

			try {

			}catch (Exception ex) {
				log.error("{}", ex);
			}
		});
	}

	private void onFactoryInoutEvent (Long gid, IUser user, Date eventDt, String routeNo, MobileEventType eventTy, String fctryCd ) {

		try {


			EventInoutName inout = EventInoutName.ARRIVE;
			ShippingState2 state = ShippingState2.EXIT;
			EventAction action = EventAction.USR_EXITED;
			switch (eventTy) {
			case ENTER_FACTORY:
				state = ShippingState2.ENTER;
				action = EventAction.USR_ENTER;
				break;
			case EXIT_FACTORY:
				inout = EventInoutName.DEPART;
				break;
			default:
				break;
			}

			ShippingPlanContext2 context = getContext(gid) == null ? createShippingContext(gid, user, action, EventBy.MOBILE_WEB) : getContext(gid);
			if(context != null) {
				ShippingStateEvent sEvent = new ShippingStateEvent();
				sEvent.setEventDt(eventDt);
				sEvent.setRouteNo(routeNo);
				sEvent.setEventAct(action);
				sEvent.setState(state);
				sEvent.setStopCd(fctryCd);
				sEvent.setEventBy(EventBy.MOBILE_WEB);
				context.fireEvent(sEvent);
				sendMessage(sEvent, user);
			}

			save(gid, user.getUsername(), eventDt, inout, state, EventBy.MOBILE_WEB, action, routeNo, fctryCd);

		}catch (Exception ex) {
			log.info("{}", ex);
		}


	}
	public void onContextCloseFireEvent(IShippingContext context) {
		try {
			contextEvent.fireEvent(ShippingContextEventImpl.of(context));
			ShippingPlanContext2 context2 = (ShippingPlanContext2) context;
			sendMessage(context2.getCurrentEvent(), context2.getUser());
			
			
			save(context.getGroupId(), context.getUser().getUsername(), new Date(), EventName.END, ShippingState2.COMPLETE, EventBy.SYSTEM, EventAction.SYS_FIN);
			
		}catch (Exception e) {
			log.error("{}",e);
		}
	}

	private void sendMessage(ShippingStateEvent event, IUser user) {
		MobileAppInfoIO appInfo = appSvc.findAppInfoByUser(user).orElse(null);
		ActionEventType ty = null;
		if(appInfo != null) {

			try {

				ShippingPlanContext2 context = getContext(user.getUsername());
				TmsDriverIO driver  = (TmsDriverIO) user.getUserDetails();
				TmsCarIO car = driver.getCar();
				ty = ActionEventType.valueOf(event.getEventAct().name());
				if(context != null && !StringUtils.isEmpty(ty.message)) {
					eventPush.fireEvent(AppPushEvent.builder()
					.appKey(appInfo.getAppTkn())
					.contents(String.format(ty.message, car.getVrn(), event.getDlvyLcNm()))
					.datas(context.getAllocatedGIO().getId())
					.priority(Priority.high)
					.mobileNo(user.getMobileNo())
					.user(user)
					.build());
				}
			}catch (Exception e) {
				log.error("{}", e);
			}

		}else {
		
			ty = ActionEventType.convert(event.getEventAct().name());
			if(ty != null && ty.message != null) {
				TmsDriverIO driver  = (TmsDriverIO) user.getUserDetails();
				TmsCarIO car = driver.getCar();
				smsSvc.sendSms(user.getUsername(), String.format(ty.message, car.getVrn(), event.getDlvyLcNm()), user.getMobileNo());
			}

		}

	}
	
	private ShippingPlanContext2 getContext(String username) {
		Entry entry = shippingMapped2.keySet().stream().filter(e -> e.getUsername().equals(username)).max(Comparator.comparingLong(Entry::getAllocatedGroupId)).orElse(null);
		return entry == null ? null : shippingMapped2.get(entry);
	}
	private ShippingPlanContext2 getContext(Long allocatedId) {
		Entry entry = shippingMapped2.keySet().stream().filter(e -> e.getAllocatedGroupId().longValue() == allocatedId.longValue()).max(Comparator.comparingLong(Entry::getAllocatedGroupId)).orElse(null);
		return entry == null ? null : shippingMapped2.get(entry);
	}

	private ShippingPlanContext2 createShippingContext(Long allocatedGId, IUser user , EventAction action, EventBy eventBy) {
		return createShippingContext(allocatedGId, user, action, eventBy, null);
	}
	private ShippingPlanContext2 createShippingContext(Long allocatedGId, IUser user , EventAction action, EventBy eventBy, Long eventTime) {
		
		AllocatedGroupIO allocatedGroup = allocatedGId == null ? findByAllocatedGid(user) : findByAllocatedGid(allocatedGId, user);
		ShippingPlanContext2 context = null;
		if(allocatedGroup != null) {

			long id = commuteReop.getMaxIdByDriverCd(user.getUsername());

			context = build(allocatedGroup, user);
			context.onReady();

			context.setLastCommuteRoqId(id);

			ShippingStateEvent sevent = new ShippingStateEvent();
			Date eventDt = eventTime == null ? new Date() : new Date(eventTime);
			sevent.setEventDt(eventDt);
			sevent.setEventAct(action);
			sevent.setEventBy(eventBy);
			sevent.setState(ShippingState2.START);
			context.fireEvent(sevent);

			save(allocatedGroup.getId(), user.getUsername(), new Date(), EventName.START, ShippingState2.START, eventBy, action);
		}
		return context;
	}
	private ShippingPlanContext2 createShippingContext(IUser user, EventAction action, EventBy eventBy) {
		return createShippingContext(null, user, action, eventBy, null);
	}
	private ShippingPlanContext2 createShippingContext(IUser user, EventAction action, EventBy eventBy, Long eventTime) {
		return createShippingContext(null, user, action, eventBy, eventTime);
	}

	private ShippingPlanContext2 build(AllocatedGroupIO allocatedGroup, IUser user) {
		ShippingPlanContext2 context = null;
		Entry entry = new Entry(allocatedGroup.getId(), user.getUsername());
		List<TmsPlanIO> tmsplans = tmsPlanSvc.findTmPlansByUserAndDeleveryDate(user, allocatedGroup.getDlvyDe());
		context = new ShippingPlanContext2(manager, allocatedGroup, user, this);
		context.addAll(tmsplans);

		addHandler(context);
		
		shippingMapped2.put(entry, context);
		return context;
	}

	private void addHandler(ShippingPlanContext2 context) {

		ShippingGpsHandler gps = new ShippingGpsHandler(context);
		ShippingContinueDriveHandler driving = new ShippingContinueDriveHandler(context);
		ShippingAccDistanceHandler distance = new ShippingAccDistanceHandler(context);
		context.addHandler(gps);
		context.addHandler(driving);
		context.addHandler(distance);
	}

	private AllocatedGroupIO findByAllocatedGid(Long groupId, IUser user) {
		return groupSvc.findAllocatedGroupById(groupId).orElse(findByAllocatedGid(user));
	}
	private AllocatedGroupIO findByAllocatedGid(IUser user) {
		return groupSvc.findLastAllocatedGroupByUser(user).orElse(null);
	}

	private T_SHIPPING_COMMUTE_TIME_STATE save(Long agid, String username, Date eventDt, EventName name, ShippingState2 state, EventBy eventBy, EventAction action) {
		T_SHIPPING_COMMUTE_TIME_STATE comute = new T_SHIPPING_COMMUTE_TIME_STATE();
		comute.setAllocatedGId(agid);
		comute.setState(state);
		comute.setEventDt(eventDt);
		comute.setEventName(name);
		comute.setEventBy(eventBy);
		comute.setEventAct(action);
		comute.setDriverCd(username);	
		return commuteReop.save(comute);
	}
	private T_SHIPPING_LDNG_STATE save(Long agid, String username, Date eventDt, ShippingState2 state, EventBy eventBy, EventAction action, FactoryType fct, SetupLcType slt) {
		T_SHIPPING_LDNG_STATE ldng = new T_SHIPPING_LDNG_STATE();
		ldng.setAllocatedGId(agid);
		ldng.setState(state);
		ldng.setDriverCd(username);
		ldng.setEventDt(eventDt);
		ldng.setEventBy(eventBy);
		ldng.setEventAct(action);
		ldng.setFactryCd(fct.getCode());
		ldng.setSetupLcTy(slt);
		return ldngReop.save(ldng);
	}
	private T_SHIPPING_INOUT_STATE save(Long agid, String username, Date eventDt, EventInoutName name, ShippingState2 state, EventBy eventBy, EventAction action, String routeNo, String stopCd) {
		T_SHIPPING_INOUT_STATE inout = new T_SHIPPING_INOUT_STATE();
		inout.setAllocatedGId(agid);
		inout.setState(state);
		inout.setEventDt(eventDt);
		inout.setEventName(name);
		inout.setEventBy(eventBy);
		inout.setEventAct(action);

		inout.setRouteNo(routeNo);
		inout.setStopCd(stopCd);
		inout.setDriverCd(username);
		return inoutReop.save(inout);
	}

	public String findScheduleTime(TmsPlanIO p) {
		return shipSvc.getPlanedArriveTime(p);
	}

	public Optional<T_SHIPPING_COMMUTE_TIME_STATE> findCommuteById(long lastCommuteRoqId) {
		return commuteReop.findById(lastCommuteRoqId);
	}

	@Override
	public List<ShippingStateEvent> findInoutStateByAllocatedGId(Long gid) {
		List<T_SHIPPING_INOUT_STATE> states = inoutReop.findByAllocatedGId(gid);
		if(states != null) {
			return states.stream().map(s -> {
				ShippingStateEvent event = new ShippingStateEvent();
				event.setEventDt(s.getEventDt());
				event.setEventAct(s.getEventAct());
				event.setEventBy(s.getEventBy());
				event.setState(s.getState());

				event.setRouteNo(s.getRouteNo());
				event.setStopCd(s.getStopCd());
				return event;
			}).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@Override
	public List<ShippingStateEvent> findCommuteTimeStateByAllocatedGId(Long gid) {
		List<T_SHIPPING_COMMUTE_TIME_STATE> states = commuteReop.findByAllocatedGId(gid);
		if(states != null) {
			return states.stream().map(s -> {
				ShippingStateEvent event = new ShippingStateEvent();
				event.setEventDt(s.getEventDt());
				event.setEventAct(s.getEventAct());
				event.setEventBy(s.getEventBy());
				event.setState(s.getState());
				return event;
			}).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@Override
	public List<ShippingStateEvent> findLdngStateByAllocatedGId(Long gid) {
		List<T_SHIPPING_LDNG_STATE> states = ldngReop.findByAllocatedGId(gid);
		if(states != null) {
			return states.stream().map(s -> {
				ShippingStateEvent event = new ShippingStateEvent();
				event.setEventDt(s.getEventDt());
				event.setEventAct(s.getEventAct());
				event.setEventBy(s.getEventBy());
				event.setState(s.getState());
				event.setSetupLc(s.getSetupLcTy());
				event.setStopCd(s.getFactryCd());
				return event;
			}).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}




	public ShipStateVo2 getDeliveryState2(IUser user) {

		ShipStateVo2 vo = null;


		try {

			ShippingPlanContext2 context = getContext(user.getUsername());
			AllocatedGroupIO groups = null;
			
			
			if(context != null) {


				groups = context.getAllocatedGIO();
				ShippingStateEvent event = context.getCurrentEvent();

				vo = new ShipStateVo2(groups.getId());

				if(event != null) {
					vo.setRouteNo(event.getRouteNo());
				}

				if(context.getDlvyDe() != null) {
					vo.setDlvyDe(context.getDlvyDe().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
				}
				vo.setAllocatedDt(groups.getTrnsmisDt());
				vo.setVrn(groups.getVrn());


				TmsPlanIO plan = context.getCurrentStop();

				vo.setState(event.getState());

				if(plan != null) {
					vo.setCurDlvyLoId(plan.getId());
					vo.setCurArrivedScheduledTime(shipSvc.getPlanedArriveTime(plan));
					vo.setCurDlvyLoNm(plan.getDlvyLoNm());
					if(ShippingType.DELEVERY == shipSvc.findShippingType(plan.getRouteNo())) {

						List<String> routeNos = context.findAllByRouteNo();
						int totalInput = context.findInputEmptyBoxCount();
						if(totalInput > 0) {
							List<EmptyboxVo> emptBoxs = emtSvc.findEmptyBoxByRouteOnly(groups.getDlvyDe(), false, routeNos.toArray(new String[routeNos.size()]));
							vo.setEmptyboxTotal(context.findInputEmptyBoxCount());
							if(emptBoxs != null) {
								vo.setEmptyboxComplete(emptBoxs.stream().map(e -> e.getId() == null ? 0: 1).reduce((a, b) -> a+b).orElse(0));
							}
						}
					}
				}


				TmsPlanIO next = context.findNextStop(plan);

				if(next != null) {
					vo.setNextArrivedScheduledTime(shipSvc.getPlanedArriveTime(next));
					vo.setNextDlvylcNm(next.getDlvyLoNm());
				}
			}else {
				

				T_SHIPPING_RT_STATE state = null;
				TmsDriverIO driver = (TmsDriverIO) user.getUserDetails();
				if(driver.getCar() != null) {
					ShippingID id = new ShippingID(driver.getCar().getVrn(), user.getUsername());
					state = shipRtRepo.findById(id).orElse(null);
				}

				if(state != null) {
					switch (state.getState()) {
					case CANCEL:
					case COMPLETE:
						groups = groupSvc.findLastAllocatedGroupByUser(user).orElse(null);
						if(groups != null) {
							if(state.getAllocatedGroupId().longValue() != groups.getId().longValue()) {
								return getRuntimeCreateContext(user, groups, ShippingState2.READY);
							}
						}
						return null;
					default:
						groups = groupSvc.findAllocatedGroupById(state.getAllocatedGroupId()).orElse(null);
						vo = getRuntimeCreateContext(user, groups, state.getState());
						break;
					}
				}else {
					groups = groupSvc.findLastAllocatedGroupByUser(user).orElse(null);
					if(groups != null) {
						vo = getRuntimeCreateContext(user, groups, ShippingState2.READY);
					}
				}
			}
		}catch (Exception e) {
			log.error("{}", e);
		}
		return vo;
	}


	private ShipStateVo2 getRuntimeCreateContext(IUser user, AllocatedGroupIO groups, ShippingState2 state) {
		List<TmsPlanIO> tmsplans = tmsPlanSvc.findTmPlansByUserAndDeleveryDate(user, groups.getDlvyDe());
		ShippingPlanContext2 context = new ShippingPlanContext2(manager, groups, user, this);
		context.addAll(tmsplans);

		TmsPlanIO plan = context.getCurrentStop();

		ShipStateVo2 vo = new ShipStateVo2(groups.getId());
		vo.setDlvyDe(groups.getDlvyDe());
		vo.setAllocatedDt(groups.getTrnsmisDt());
		vo.setVrn(groups.getVrn());

		vo.setState(state);

		if(plan != null) {
			vo.setRouteNo(plan.getRouteNo());
			vo.setCurDlvyLoId(plan.getId());
			vo.setCurArrivedScheduledTime(shipSvc.getPlanedArriveTime(plan));
			vo.setCurDlvyLoNm(plan.getDlvyLoNm());

			if(ShippingType.DELEVERY == shipSvc.findShippingType(plan.getRouteNo())) {
				List<String> routeNos = context.findAllByRouteNo();
				int totalInput = context.findInputEmptyBoxCount();
				if(totalInput > 0) {
					List<EmptyboxVo> emptBoxs = emtSvc.findEmptyBoxByRouteOnly(groups.getDlvyDe(), false, routeNos.toArray(new String[routeNos.size()]));
					vo.setEmptyboxTotal(totalInput);
					if(emptBoxs != null) {
						vo.setEmptyboxComplete(emptBoxs.stream().map(e -> e.getId() == null ? 0: 1).reduce((a, b) -> a+b).orElse(0));
					}
				}
			}
		}

		TmsPlanIO next = context.findNextStop(plan);
		if(next != null) {
			vo.setNextArrivedScheduledTime(shipSvc.getPlanedArriveTime(next));
			vo.setNextDlvylcNm(next.getDlvyLoNm());
		}

		return vo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ShippingPlanIO> findShipPlanByAllocatedGroupId(Long id) {
		AllocatedGroupIO io = groupSvc.findAllocatedGroupById(id).orElse(null);
		if(io != null) {
			return (List<ShippingPlanIO>) (Object)ShppingScanner.convert(shipSvc, tmsPlanSvc.findTmPlansByIds(io.getDlvyDe(),
					io.getRouteInfo().stream().map(RouteIO::getRouteNo).toArray(String[]::new)));
		}
		return Collections.emptyList();
	}

	public void removeContext(IDriverFinishJobEvent event) {

		ShippingPlanContext2 context = getContext(event.getAllocatedGroupId());
		if(context != null) {
			removeContext(context);
		}else  {
			saveRT(event.getAllocatedGroupId(), event.user());
		}
		
		
		
		log.info("removeContext => gId={}, userNm={}, context is null={}", event.getAllocatedGroupId(), event.user().getUsername(), (context==null) );
		save(event.getAllocatedGroupId(), event.user().getUsername(), new Date(), EventName.END, ShippingState2.COMPLETE, EventBy.MOBILE_WEB, EventAction.USR_FIN);

	}

	private void saveRT(Long gid, IUser user) {
		TmsDriverIO driver = (TmsDriverIO) user.getUserDetails();
		if(driver != null && driver.getCar() != null) {
			ShippingID id = new ShippingID(driver.getCar().getVrn(), user.getUsername());
			T_SHIPPING_RT_STATE state = shipRtRepo.findById(id).orElse(null);
			if(state != null) {
				state.setState(ShippingState2.COMPLETE);
			}else {
				state = new T_SHIPPING_RT_STATE();
				state.setState(ShippingState2.COMPLETE);
				state.setAllocatedGroupId(gid);

				AllocatedGroupIO group = groupSvc.findAllocatedGroupById(gid).orElse(null);
				if(group != null) {
					state.setDlvyDe(group.getDlvyDe());
				}
				state.setId(id);
			}

			if(state != null) {
				shipRtRepo.save(state);
			}
		}
	}

	public void removeContext(ShippingPlanContext2 context) {
		if(context != null) {
			context.onClose();
			Entry entry = new Entry(context.getAllocatedGIO().getId(), context.getUser().getUsername());
			ShippingPlanContext2 moved = shippingMapped2.remove(entry);
			if(moved == null) {
				Entry key = shippingMapped2.keySet().stream().filter(e -> e.equals(entry)).findFirst().orElse(null);
				if(key != null) {
					shippingMapped2.remove(key);
				}
			}
			saveRT(context.getAllocatedGIO().getId(), context.getUser());
		}
	}

	public T_SHIPPING_RT_STATE saveShippingRTState(T_SHIPPING_RT_STATE rt) {
		return shipRtRepo.save(rt);
	}


	private void setEventTime(ShippingStateEvent event, T_ROUTE_STATE state, long timestamp) {

		timestamp = timestamp == 0L ? System.currentTimeMillis() : timestamp;
		LocalDateTime eventTime = LocalDateTime.ofInstant(new Date(timestamp).toInstant(), ZoneId.systemDefault());

		log.info("timestamp -> {} to localtime -> {}", timestamp, eventTime);
		log.info("eventAct -> {} to ldngStTime -> {}", event.getEventAct(), state.getLdngStTime());


		switch (event.getEventAct()) {
		case USR_ENTER:
			state.setFrontEntTime(eventTime);
			state.setFixed(true);
			break;
		case USR_EXITED:
			state.setFrontExiTime(eventTime);
			state.setFixed(true);
			break;
		case GEO_EXITED:
			
			if (event.getState() == ShippingState2.EXIT) {
				
				

				
				
			}
			break;
		case BCN_ENTER:
			state.setFrontEntTime(eventTime);
			break;
		case BCN_EXITED:
			state.setFrontExiTime(eventTime);
			break;
		case NFC_TAG_LDNG:

			log.info("EVENT TYPE=NFC_TAG_LDNG,state={},event={},timestamp={}", state,event,timestamp);
			log.info("event.getRouteNo()={},state.getRouteNo()={}, state.getLdngStTime()={}", event.getRouteNo(),state.getRouteNo(), state.getLdngStTime());

			
			
			
			if ( event.getState() == ShippingState2.LOADING ) {

				
				if (state.isFixed() == true) {
					

				} else if(state.getLdngStTime() == null) {	
					
					
					
					
					state.setLdngStTime(eventTime);
					state.setLdngStSetupLc(event.getSetupLc());
				}else {	

					state.setLdngEdTime(eventTime);
					state.setLdngEdSetupLc(event.getSetupLc());
				}
			}

			break;
		case NFC_TAG_OFFIC:
			
			if ( state.getOfficeEntTime() == null ) {
				state.setOfficeEntTime(eventTime);
			}
			break;
		case USR_FIN:
			state.setDrvEdTime(eventTime);
			break;
		case USR_ST:
			state.setDrvStTime(eventTime);
			break;
		default:
			break;
		}
	}

	private void setTimeDiff(T_ROUTE_STATE state) {

		
		if(state.getLdngWaitTimeSec() == 0 && state.getFrontEntTime() != null && state.getLdngStTime() != null) {
			state.setLdngWaitTimeSec(timeDiff(state.getFrontEntTime(), state.getLdngStTime()));
		}
		
		if(state.getLdngTimeSec() == 0 && state.getLdngStTime() != null && state.getLdngEdTime() != null) {
			state.setLdngTimeSec(timeDiff(state.getLdngStTime(), state.getLdngEdTime()));
		}
		
		if(state.getDlvyStWaitTimeSec() == 0 && state.getLdngEdTime() != null && state.getFrontExiTime() != null) {
			state.setDlvyStWaitTimeSec(timeDiff(state.getLdngEdTime(), state.getFrontExiTime()));
		}
		
		if(state.getPlanLdngDiffTimeSec() == 0 && state.getLdngStTime() != null && state.getPlanTime() != null) {
			state.setPlanLdngDiffTimeSec(timeDiff(state.getPlanTime(), state.getLdngStTime()));
		}

	}

	private long timeDiff(LocalDateTime st, LocalDateTime ed) {
		if(st == null || ed == null)
			return 0;
		long diffTime = ChronoUnit.SECONDS.between(st, ed);
		diffTime = Math.abs(diffTime);
		return diffTime;
	}


	
	public void saveRouteState(ShippingPlanContext2 context) {

		if(context != null) {
			TmsPlanIO plan = context.getCurrentStop();
			
			if(plan != null) {

				
				List<T_ROUTE_STATE> states = Lists.newArrayList(routeStateRepo.findAll(predicate(plan.getDriverCd(), plan.getDlvyDe(), null)));

				ShippingStateEvent current = context.getCurrentEvent();

				
				if(states == null || states.isEmpty()) {

					
					List<String> routeNos = context.getGroupByRouteNos();

					
					if(!StringUtils.isEmpty(current.getRouteNo())) {
						routeNos = routeNos.stream().filter(r -> r.equals(current.getRouteNo())).collect(Collectors.toList());
					}

					
					routeNos.stream().forEach(r -> {

						try {
							
							RouteStateID id = new RouteStateID(plan.getVrn(), r, plan.getDlvyDe());	
							T_ROUTE_STATE state = new T_ROUTE_STATE();
							state.setId(id);
							state.setAllocatedGroupId(context.getAllocatedGIO().getId());
							state.setDriverCd(plan.getDriverCd());

							
							setEventTime(current, state, current.getEventDt().getTime());
							setTimeDiff(state);
							routeStateRepo.save(state);
						}catch (Exception e) {
							log.error("{}", e);
						}
					});

				
				} else {
					
					List<T_ROUTE_STATE> notFixedStates = states.stream().filter(s -> !s.isFixed()).collect(Collectors.toList());

					
					if(notFixedStates == null || notFixedStates.isEmpty()) {

						
						if(!StringUtils.isEmpty(current.getRouteNo())) {
							RouteStateID id = new RouteStateID(plan.getVrn(), current.getRouteNo(), plan.getDlvyDe());
							
							T_ROUTE_STATE state = routeStateRepo.findById(id).orElse(new T_ROUTE_STATE());
							state.setId(id);
							state.setAllocatedGroupId(context.getAllocatedGIO().getId());
							state.setDriverCd(plan.getDriverCd());
							setEventTime(current, state, current.getEventDt().getTime());
							setTimeDiff(state);
							routeStateRepo.save(state);

						
						} else {
							
							states.forEach(state -> {
								setEventTime(current, state, current.getEventDt().getTime());
								setTimeDiff(state);
								routeStateRepo.save(state);
							});
						}

					
					} else {
						
						if(!StringUtils.isEmpty(current.getRouteNo())) {
							
							T_ROUTE_STATE state = notFixedStates.stream().filter(s -> s.getRouteNo().equals(current.getRouteNo())).findFirst().orElse(null);

							
							if(state == null) {

								
								RouteStateID id = new RouteStateID(plan.getVrn(), current.getRouteNo(), plan.getDlvyDe());
								state = routeStateRepo.findById(id).orElse(null);

								
								if(state != null) {
									setEventTime(current, state, current.getEventDt().getTime());
									setTimeDiff(state);
									routeStateRepo.save(state);
								}

							
							}else {

								
								notFixedStates.stream().filter(s -> s.getRouteNo().equals(current.getRouteNo())).forEach(s -> {
									setEventTime(current, s, current.getEventDt().getTime());
									setTimeDiff(s);
									routeStateRepo.save(s);
								});
							}

						
						}else {

							
							notFixedStates.stream().forEach(s -> {
								setEventTime(current, s, current.getEventDt().getTime());
								setTimeDiff(s);
								routeStateRepo.save(s);
							});
						}
					}

					

					if ( current.getEventAct() == EventAction.USR_EXITED ) {
						List<T_ROUTE_STATE> states3 = states.stream().filter(s -> !s.isFixed()).collect(Collectors.toList());	

						if(states3 != null && !states3.isEmpty()) {	

							states3.stream().forEach(s -> {

								s.setLdngStTime(null);			
								s.setLdngStSetupLc(null);		
								s.setLdngEdTime(null);			
								s.setLdngEdSetupLc(null);		


								s.setLdngWaitTimeSec(0l);		
								s.setLdngTimeSec(0l);			
								s.setDlvyStWaitTimeSec(0l);		
								s.setPlanLdngDiffTimeSec(0l);	

								routeStateRepo.save(s);
							});
						}

					}

				}
			}
		}
	}

	public Predicate predicate(String driverCd, String dlvyDe, Boolean fixed) {
		QT_ROUTE_STATE qStates = QT_ROUTE_STATE.t_ROUTE_STATE;
		BooleanBuilder where = new BooleanBuilder();
		where.and(qStates.driverCd.eq(driverCd));

		if(fixed != null) {
			where.and(qStates.fixed.eq(fixed));
		}
		where.and(qStates.id.dlvyDe.eq(dlvyDe));
		return where;
	}
}
