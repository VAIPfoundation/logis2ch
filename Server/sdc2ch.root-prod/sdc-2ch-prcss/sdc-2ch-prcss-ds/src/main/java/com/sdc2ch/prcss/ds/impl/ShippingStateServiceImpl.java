package com.sdc2ch.prcss.ds.impl;

import static com.sdc2ch.prcss.ds.core.ShppingScanner.convert;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.sdc2ch.aiv.event.IFirebaseNotificationEvent;
import com.sdc2ch.aiv.event.IFirebaseNotificationEvent.Priority;
import com.sdc2ch.prcss.ds.IShippingStateService;
import com.sdc2ch.prcss.ds.core.IShipping;
import com.sdc2ch.prcss.ds.core.ShippingPlanContext;
import com.sdc2ch.prcss.ds.core.ShppingScanner;
import com.sdc2ch.prcss.ds.event.ActionEventType;
import com.sdc2ch.prcss.ds.event.IMobileActionEvent.MobileEventActionType;
import com.sdc2ch.prcss.ds.event.INfcTagEvent;
import com.sdc2ch.prcss.ds.event.IOTEvent;
import com.sdc2ch.prcss.ds.event.IProcessEvent;
import com.sdc2ch.prcss.ds.io.ShippingPlanIO;
import com.sdc2ch.prcss.ds.io.ShippingState;
import com.sdc2ch.prcss.ds.repo.T_RouteStateRepository;
import com.sdc2ch.prcss.ds.repo.T_ShippingStateHistRepository;
import com.sdc2ch.prcss.ds.repo.T_ShippingStateHistRepository2;
import com.sdc2ch.prcss.ds.repo.T_ShippingStateRepository;
import com.sdc2ch.prcss.ds.repo.domain.T_ROUTE_STATE;
import com.sdc2ch.prcss.ds.repo.domain.T_SHIPPING_STATE;
import com.sdc2ch.prcss.ds.repo.domain.T_SHIPPING_STATE_HIST;
import com.sdc2ch.prcss.ds.t.chain.ShippingChain;
import com.sdc2ch.prcss.ds.t.chain.ShippingChain.ChainNm;
import com.sdc2ch.prcss.ds.t.chain.state.ShippingStatus;
import com.sdc2ch.prcss.ds.t.chain.state.UnLoadingState;
import com.sdc2ch.prcss.ds.t.chain.state.action.ActionEvent;
import com.sdc2ch.prcss.ds.t.chain.state.action.EmptyboxAction;
import com.sdc2ch.prcss.ds.t.chain.state.action.MobileEventAction;
import com.sdc2ch.prcss.ds.vo.ShipEventHistVo;
import com.sdc2ch.prcss.ds.vo.ShipEventHistVo.ShipEventHistVoBuilder;
import com.sdc2ch.prcss.ds.vo.ShipStateVo;
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
import com.sdc2ch.service.event.IAllocatedEvent;
import com.sdc2ch.service.event.IDriverStartJobEvent;
import com.sdc2ch.tms.io.TmsPlanIO;
import com.sdc2ch.tms.service.ITmsPlanService;
import com.sdc2ch.tms.service.ITmsShippingService;
import com.sdc2ch.web.service.IAllocatedGroupService;
import com.sdc2ch.web.service.IMobileAppService;

import io.reactivex.subjects.BehaviorSubject;
import lombok.extern.slf4j.Slf4j;

@Slf4j

public class ShippingStateServiceImpl implements IShippingStateService {

	@Autowired IAllocatedGroupService groupSvc;
	@Autowired ITmsPlanService tmsPlanSvc;
	@Autowired ITmsShippingService shipSvc;
	@Autowired I2ChEventManager manager;
	@Autowired I2ChUserService userSvc;
	@Autowired IMobileAppService appSvc;          


	@Autowired T_ShippingStateRepository shipRepo;
	@Autowired T_ShippingStateHistRepository shipHistRepo;
	@Autowired T_ShippingStateHistRepository2 shipHist2Repo;
	@Autowired T_RouteStateRepository routeStateRepo;

	private BehaviorSubject<ShippingChain> chainObserver = BehaviorSubject.create();

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);

	
	private static final Map<String, ShippingPlanContext> shippingMapped = new HashMap<>();

	private I2ChEventPublisher<IFirebaseNotificationEvent> eventPush;

	@PostConstruct
	public void init() {
		this.eventPush = manager.regist(IFirebaseNotificationEvent.class);
		List<ShippingState> endOf = Arrays.asList(ShippingState.CANCEL, ShippingState.COMPLETE);
		shipRepo.findAll().stream().filter(s -> !endOf.contains(s.getState())).forEach( s -> {
			try {











			}catch (Exception e) {
				log.error("{}", e);
			}
		});
	}

	@Override
	public ShipStateVo createShippingState(Long allocatedGroupId, Date trnsmisDt, String dlvyDe, IUser user) {
		ShippingPlanContext context = null;
		if(shippingMapped.get(user.getUsername()) != null) {
			context = shippingMapped.get(user.getUsername());
			context.onCancel();
			shippingMapped.remove(user.getUsername());
		}
		context = (ShippingPlanContext) createShippingContext(allocatedGroupId, trnsmisDt, dlvyDe, user);
		shippingMapped.put(user.getUsername(), context);
		return getDeliveryState(user);
	}

	@Override
	public ShipStateVo getDeliveryState(IUser user) {

		
		ShipStateVo vo = null;
		ShippingPlanContext context = getContext(user);
		AllocatedGroupIO groups = groupSvc.findLastAllocatedGroupByUser(user).orElse(null);
		
		
		if(context != null) {

			ShippingChain cur = (ShippingChain) context.getCurrentState();
			ShippingChain next = context.getNextChain(cur);

			if(cur != null) {
				vo = new ShipStateVo(context.getAllocatedGroupId());
				vo.setRouteNo(cur.shipConfig.routeNo);
				vo.setDlvyDe(cur.shipConfig.dlvyDe);
				vo.setVrn(cur.shipConfig.vrn);
				vo.setAllocatedDt(context.getAllocatedDate());
				vo.setState(findState(context));
				vo.setCurDlvyLoId(cur.shipConfig.tmsPlanRowId);
				vo.setCurArrivedScheduledTime(cur.shipConfig.plannedATime);
				vo.setCurDlvyLoNm(cur.shipConfig.dlvyLcNm);
				Long[] emptys = findEmptyboxState(context);
				vo.setEmptyboxComplete(new BigDecimal(emptys[1]).intValue());
				vo.setEmptyboxTotal(new BigDecimal(emptys[0]).intValue());
				vo.setStateTimeMap(findShipStateTime(context));

				if(next != null) {
					vo.setNextArrivedScheduledTime(next.shipConfig.plannedATime);
					vo.setNextDlvylcNm(next.shipConfig.dlvyLcNm);
				}
			}
		}

		if(vo != null) {

			
			if(groups != null && vo != null) {
				if(groups.getId() != vo.getAllocatedGroupId()) {
					if(ShippingState.COMPLETE == vo.getState() || ShippingState.CANCEL == vo.getState()) {
						vo = createShippingState(groups.getId(), groups.getTrnsmisDt(), groups.getDlvyDe(), user);
					}
					else {
						String currDlvyde = vo.getDlvyDe();
						String nextDlvyde = groups.getDlvyDe();
						if(!StringUtils.isEmpty(currDlvyde) && !StringUtils.isEmpty(nextDlvyde)) {
							LocalDate curDate = LocalDate.parse(currDlvyde, DateTimeFormatter.ofPattern("yyyyMMdd"));
							LocalDate nexDate = LocalDate.parse(nextDlvyde, DateTimeFormatter.ofPattern("yyyyMMdd"));
							Period period = curDate.until(nexDate);
							if(period.getDays() >= 2) {
								vo = createShippingState(groups.getId(), groups.getTrnsmisDt(), groups.getDlvyDe(), user);
							}
						}
					}
				}
			}
		}
		
		if(vo == null) {

			if(groups != null) {

				TmsDriverIO driver = (TmsDriverIO) user.getUserDetails();
				ShippingID id = new ShippingID(driver.getCar().getVrn(), user.getUsername());
				T_SHIPPING_STATE state = shipRepo.findById(id).orElse(null);
				if(check(groups, state)) {
					vo = new ShipStateVo(state.getAllocatedGroupId());
					vo.setDlvyDe(state.getDlvyDe());
					vo.setAllocatedDt(state.getAllocatedDate());
					vo.setState(state.getState());
					vo.setVrn(id.getVrn());
				}else {


					context = (ShippingPlanContext) createShippingContext(groups.getId(), groups.getTrnsmisDt(), groups.getDlvyDe(), user);
					shippingMapped.put(user.getUsername(), context);
					return getDeliveryState(user);
					



				}
			}
		}
		return vo;
	}

	private boolean check(AllocatedGroupIO groups, T_SHIPPING_STATE state) {
		if(groups != null && state != null) {
			return groups.getId().longValue() == state.getAllocatedGroupId().longValue();
		}
		return false;
	}


	private Map<ShippingState, Long> findShipStateTime(ShippingPlanContext context) {
		List<ActionEvent> events = context.flattened().filter(as -> as instanceof ActionEvent).map(ass -> (ActionEvent)ass) .collect(Collectors.toList());
		Map<ShippingState, Long> mapped = new HashMap<>();
		Long time = null;


		MobileEventAction mobileAction = (MobileEventAction) context.getStart().flattened().filter(e -> e instanceof MobileEventAction)
		.filter(me -> ((MobileEventAction) me).getMobileEventType() == MobileEventActionType.ALLOCATE_CONFIRM).findFirst().orElse(null);
		if(mobileAction != null) {
			time = findEventTime(mobileAction.getEvent());
		}
		mapped.put(ShippingState.READY, time);
		time = null;

		mobileAction = (MobileEventAction) context.getStart().flattened().filter(e -> e instanceof MobileEventAction)
				.filter(me -> ((MobileEventAction) me).getMobileEventType() == MobileEventActionType.START_JOB).findFirst().orElse(null);
		if(mobileAction != null) {
			time = findEventTime(mobileAction.getEvent());
		}
		mapped.put(ShippingState.START, time);
		time = null;

		mobileAction = (MobileEventAction) events.stream().filter(e -> e instanceof MobileEventAction)
				.filter(me -> ((MobileEventAction) me).getMobileEventType() == MobileEventActionType.FINISH_JOB).findFirst().orElse(null);
		if(mobileAction != null) {
			time = findEventTime(mobileAction.getEvent());
		}
		mapped.put(ShippingState.COMPLETE, time);
		time = null;

		EmptyboxAction action = (EmptyboxAction) events.stream().filter(s -> s instanceof EmptyboxAction && s.getParent() instanceof UnLoadingState && s.isComplete()).collect(Collectors.toList()).stream().reduce((a1, a2) -> a2).orElse(null);

		if(action != null) {
			time = findEventTime(action.getEvent());
		}
		mapped.put(ShippingState.TAKEOVER, time);
		return mapped;
	}

	private Long findEventTime(IProcessEvent event) {
		return event == null ? null : event.getTimeStamp();
	}

	private ShippingState findState(ShippingPlanContext context) {
		ShippingChain cur = (ShippingChain) context.getCurrentState();
		if(cur == null)
			return ShippingState.READY;
		ShippingStatus status = (ShippingStatus) cur.getCurrentState();
		return status == null ? ShippingState.READY : status.getShippingState();
	}

	private Long[] findEmptyboxState(ShippingPlanContext context) {
		List<ChainNm> names = Arrays.asList(ChainNm.CUSTOMER_CENTER);
		long total = context.getChains().stream().filter(c -> names.contains(c.chainName())).count();
		long total2 = context.flattened().filter(s -> s instanceof EmptyboxAction).map(a -> {
			return  a.getParent() instanceof UnLoadingState && a.isComplete() ? 1 : 0;
		}).reduce((a, b) -> a + b).orElse(0);
		return new Long[] {total, total2};
	}

	@Autowired
	private void observerble() {
		chainObserver.subscribe(this::saveLastState, this::onError, () -> {
			log.info("oncomplete {}", chainObserver.count());
		});
	}

	private void saveLastState(ShippingChain chain) {
		try {
			T_SHIPPING_STATE state = new T_SHIPPING_STATE();
			ShippingID ID = new ShippingID(chain.shipConfig.vrn, chain.shipConfig.driverCd);
			state.setId(ID);
			state.setAllocatedGroupId(chain.context.getAllocatedGroupId());
			state.setDlvyLcId(chain.shipConfig.getTmsPlanRowId());
			state.setChain(chain.chainName());
			state.setState(findState(chain.context));
			state.setHint(chain.getParent().encodeHint());
			state.setAllocatedDate(chain.context.getAllocatedDate());
			state.setDlvyDe(chain.shipConfig.dlvyDe);
			state.setRouteNo(chain.getCurrentRouteNo());
			shipRepo.save(state);

			String hex = chain.getParent().encodeHint();
			String bin = chain.getParent().decodeHint(hex);
			log.info("saveLastState() {} {} -> {}" , chain, hex, bin);
		}catch (Exception e) {
			log.error("{}" , e);
		}finally {
			sendMessage(chain);
		}

		try {
			saveHist(chain);
		}catch (Exception e) {
			log.error("saveHist() -> {}" , e);
		}
		try {
			saveRouteState(chain);
		}catch (Exception e) {
			log.error("saveRouteState() -> {}" , e);
		}

		try {
			IProcessEvent curEvent = chain.getCurrentEvent();
			ActionEventType curEventTy = curEvent.getActionEventTy();

			if(ActionEventType.USR_FIN == curEventTy || ActionEventType.SYS_FIN == curEventTy) {
				String userName = chain.context.getUser().getUsername();
				shippingMapped.remove(userName);







			}

		}catch (Exception e) {
			log.error("set Next Allocated -> {}" , e);
		}

	}

	private void saveHist(ShippingChain chain) {

		T_SHIPPING_STATE_HIST hist2 = new T_SHIPPING_STATE_HIST();

		hist2.setAllocatedGroupId(chain.context.getAllocatedGroupId());
		hist2.setChain(chain.chainName());
		hist2.setDataTy(chain.getCurrentEvent().getActionEventTy());
		hist2.setDlvyDe(chain.shipConfig.dlvyDe);
		hist2.setRouteNo(chain.getCurrentRouteNo());
		hist2.setDlvyLcId(chain.shipConfig.tmsPlanRowId);
		if(chain.getCurrentEvent() != null && chain.getCurrentEvent() instanceof IOTEvent) {
			hist2.setSetupLc(((IOTEvent)chain.getCurrentEvent()).getSetupLcTy());
		}

		hist2.setState(findState(chain.context));
		shipHist2Repo.save(hist2);
	}
	private void saveRouteState(ShippingChain chain) {
		String curRouteNo = chain.getCurrentRouteNo();
		String vrn = chain.shipConfig.vrn;
		String dlvyDe = chain.shipConfig.dlvyDe;

		RouteStateID id = new RouteStateID(vrn, curRouteNo, dlvyDe);
		IProcessEvent curEvent = chain.getCurrentEvent();
		ActionEventType curEventTy = curEvent.getActionEventTy();
		T_ROUTE_STATE state = routeStateRepo.findById(id).orElse(new T_ROUTE_STATE());
		setEventTime(curEventTy, state, curEvent.getTimeStamp());
		if(state.getId() == null) {
			state.setId(id);
			state.setAllocatedGroupId(chain.context.getAllocatedGroupId());
			ShippingChain firstChain = chain.context.getChains().stream().filter(c -> c.getShippingPlanIO().getRouteNo().equals(curRouteNo) && c.getShippingPlanIO().getRouteReIdx() == 0).findFirst().orElse(null);
			if(firstChain != null) {
				if(firstChain.shipConfig.getScheDlvyStDt() != null)
					state.setPlanTime(firstChain.shipConfig.getScheDlvyStDt());
			}
		}
		setTimeDiff(state);
		routeStateRepo.save(state);
		log.info("saved route state -> {}", state);
	}

	private void onError(Throwable t) {
		log.error("onError() {}" , t);
	}

	private ShippingPlanContext getContext(IUser user) {
		Assert.notNull(user, "IUser user can not be null");
		return shippingMapped.get(user.getUsername());
	}

	@Autowired
	private void applicationKillEvent(IApplicationEventListener el) {
		el.subscribe(ApplicationEventType.ON_CLOSED).subscribe(e -> {
			
			log.info("{}", shippingMapped);
		});
	}

	@Autowired
	private void subscribeCanceledEvent(I2ChEventManager manager) {

		manager.subscribe(IAllocateCanceledEvent.class).filter(e -> {
			if(e instanceof IAllocateCanceledEvent) {

				IAllocateCanceledEvent event = ((IAllocateCanceledEvent) e);
				IUser u = e.user();
				ShippingPlanContext plan = shippingMapped.get(u.getUsername());

				if(plan != null) {
					shippingMapped.remove(u.getUsername());
					System.gc();
					groupSvc.findAllocatedGroupById(event.getAllocatedGroupId()).ifPresent(g -> {
						
						
						plan.onCancel();
						TmsDriverIO driver = (TmsDriverIO) u.getUserDetails();
						ShippingID id = new ShippingID(driver.getCar().getVrn(), u.getUsername());
						T_SHIPPING_STATE state = shipRepo.findById(id).orElse(null);
						state.setState(ShippingState.CANCEL);
						shipRepo.save(state);
					});
				}
			}
		});
	}

	@Autowired
	private void subscribeOfficeTagEvent(I2ChEventManager manager) {

		manager.subscribe(INfcTagEvent.class).filter(e -> {

			try {
				INfcTagEvent nfc = (INfcTagEvent) e;
				if(SetupLcType.OFFICE == nfc.getSetupLcTy()) {
					AllocatedGroupIO groups = groupSvc.findLastAllocatedGroupByUser(e.user()).orElse(null);
					if(groups != null) {

						LocalDate dlvyDe = LocalDate.parse(groups.getDlvyDe(), DateTimeFormatter.ofPattern("yyyyMMdd"));
						LocalDate now = LocalDate.now();
						IUser user = e.user();

						if(shippingMapped.get(user.getUsername()) == null) {
							
							if(dlvyDe.isEqual(now) || dlvyDe.isAfter(now)) {
								ShippingPlanContext context = (ShippingPlanContext) createShippingContext(groups.getId(), groups.getTrnsmisDt(), groups.getDlvyDe(), user);
								shippingMapped.put(user.getUsername(), context);
							}
						}
					}
				}
			}catch (Exception ex) {
				log.error("subscribeOfficeTagEvent() -> {}", e);
			}
		});
	}
	@Autowired
	private void subscribeDriveStart(I2ChEventManager manager) {

		manager.subscribe(IDriverStartJobEvent.class).filter(e -> {

			try {

				IDriverStartJobEvent nfc = (IDriverStartJobEvent) e;
				IUser user = e.user();
				ShippingPlanContext context = null;

				AllocatedGroupIO groups = groupSvc.findAllocatedGroupById(nfc.getAllocatedGroupId()).orElse(null);

				if(shippingMapped.get(user.getUsername()) != null) {
					context = shippingMapped.get(user.getUsername());
					
					if(check(context.getAllocatedGroupId(), nfc.getAllocatedGroupId())) {

						ShippingChain chain = context.getChains().stream().findFirst().orElse(null);

						if(chain != null && groups != null) {

							String oldDlvyDe = chain.shipConfig.dlvyDe;
							String newDlvyDe = groups.getDlvyDe();

							LocalDate oldDate = LocalDate.parse(oldDlvyDe, DateTimeFormatter.ofPattern("yyyyMMdd"));
							LocalDate newDate = LocalDate.parse(newDlvyDe, DateTimeFormatter.ofPattern("yyyyMMdd"));

							
							if(oldDate.isEqual(newDate)) {
								
							}
							
							else if(oldDate.isBefore(newDate)) {
								
							}
							
							else if(oldDate.isAfter(newDate)) {
								
								context.onCancel();
								shippingMapped.remove(user.getUsername());
								context = (ShippingPlanContext) createShippingContext(groups.getId(), groups.getTrnsmisDt(), groups.getDlvyDe(), user);
								shippingMapped.put(user.getUsername(), context);
							}
						}
					}
				}else {
					context = (ShippingPlanContext) createShippingContext(groups.getId(), groups.getTrnsmisDt(), groups.getDlvyDe(), user);
					shippingMapped.put(user.getUsername(), context);
				}

			}catch (Exception ex) {
				log.error("subscribeOfficeTagEvent() -> {}", e);
			}
		});
	}

	private boolean check(Long allocatedGroupId, Long allocatedGroupId2) {
		long src = nullSafeLong(allocatedGroupId);
		long tar = nullSafeLong(allocatedGroupId2);
		return src != tar;
	}

	private long nullSafeLong(Long allocatedGroupId) {
		return allocatedGroupId == null ? 0 : allocatedGroupId.longValue();
	}

	@Autowired
	private void subscribeStartJobEvent(I2ChEventManager manager) {

		manager.subscribe(IAllocatedEvent.class).filter(e -> {

			try {



























			}catch (Exception ex) {
				log.error("{}", ex);
			}
		});

	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ShippingPlanIO> findShipPlanByAllocatedGroupId(Long id) {
		AllocatedGroupIO io = groupSvc.findAllocatedGroupById(id).orElse(null);
		if(io != null) {
			return (List<ShippingPlanIO>) (Object)convert(shipSvc, tmsPlanSvc.findTmPlansByIds(io.getDlvyDe(),
					io.getRouteInfo().stream().map(RouteIO::getRouteNo).toArray(String[]::new)));
		}
		return Collections.emptyList();
	}

	private IShipping createShippingContext(AllocatedGroupIO io, IUser user ) {
		return createShippingContext(io.getId(), io.getTrnsmisDt(), io.getDlvyDe(), user);

	}
	private IShipping createShippingContext(Long allocatedGId, Date allocatedDate, String dlvyDe, IUser user ) {
		ShppingScanner sc = ShppingScanner.of();

		List<TmsPlanIO> tmsplans = tmsPlanSvc.findTmPlansByUserAndDeleveryDate(user, dlvyDe);


		if(tmsplans == null || tmsplans.isEmpty()) {
			
		}
		ShippingPlanContext context =
				sc
				.manager(manager)
				.allocatedId(allocatedGId)
				.user(user)
				.tmsPlans(tmsPlanSvc.findTmPlansByUserAndDeleveryDate(user, dlvyDe))
				.scan(shipSvc)
				.build();
		context.setAllocatedDate(allocatedDate);
		context.onSubscribeOn(chainObserver);
		context.setScheduler(scheduler);
		return context.onCreate(context);
	}

	private void sendMessage(ShippingChain chain) {

		IUser user = chain.context.getUser();
		IProcessEvent event = chain.getCurrentEvent();
		MobileAppInfoIO appInfo = appSvc.findAppInfoByUser(user).orElse(null);

		if(appInfo != null && event.getActionEventTy() != null) {

			TmsDriverIO driver  = (TmsDriverIO) user.getUserDetails();
			TmsCarIO car = driver.getCar();

			if(!StringUtils.isEmpty(event.getActionEventTy().message)) {
				eventPush.fireEvent(AppPushEvent.builder()
				.appKey(appInfo.getAppTkn())
				.contents(String.format(event.getActionEventTy().message, car.getVrn(), chain.shipConfig.dlvyLcNm))
				.datas(chain.context.getAllocatedGroupId())
				.priority(Priority.high)
				.mobileNo(user.getMobileNo())
				.user(user)
				.build());
			}
		}else {
			
		}


	}

	private void setEventTime(ActionEventType curEventTy, T_ROUTE_STATE state, long timestamp) {

		timestamp = timestamp == 0L ? System.currentTimeMillis() : timestamp;
		LocalDateTime eventTime = LocalDateTime.ofInstant(new Date(timestamp).toInstant(), ZoneId.systemDefault());

		log.info("timestamp -> {} to localtime -> {}", timestamp, eventTime);

		switch (curEventTy) {
		case BCN_ENTER:
			state.setFrontEntTime(eventTime);
			break;
		case BCN_EXITED:
			state.setFrontExiTime(eventTime);
			break;
		case NFC_TAG_LDNG:
			if(state.getLdngStTime() == null) {
				state.setLdngStTime(eventTime);
			}else {
				state.setLdngEdTime(eventTime);
			}
			break;
		case NFC_TAG_OFFIC:
			state.setOfficeEntTime(eventTime);
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


	
	@Override
	public List<ShipEventHistVo> getShippingEventList(Long allocatedGId, String dlvyDe, IUser user ) {
		ShppingScanner sc = ShppingScanner.of();
		ShippingPlanContext context =
				sc
				.manager(manager)
				.allocatedId(allocatedGId)
				.user(user)
				.tmsPlans(tmsPlanSvc.findTmPlansByUserAndDeleveryDate(user, dlvyDe))
				.scan(shipSvc)
				.build();

		IShipping shipping = context.onCreate(context);















		List<ShipEventHistVo> list = new ArrayList<ShipEventHistVo>();
		context.flattened().forEach( o -> {
			if(o instanceof ActionEvent) {
				ShipEventHistVoBuilder builder =  ShipEventHistVo.builder();
				ActionEvent event = (ActionEvent) o;
				ShippingStatus status = (ShippingStatus) event.getParent();
				ShippingChain chain = (ShippingChain) status.getParent();

				builder.aclGroupId(chain.context.getAllocatedGroupId())
				.batchNo("")
				.dlvyDe(chain.shipConfig.dlvyDe)
				.dlvyLcCd(chain.shipConfig.dlvyLcId)
				.dlvyLcNm(chain.shipConfig.dlvyLcNm)
				.fctryCd(chain.shipConfig.getFctryTy().getCode())
				.MobileNo("")
				.routeNo(chain.shipConfig.routeNo)
				.vrn(chain.shipConfig.vrn);


				log.info("======");
				log.info("status : {}", status);
				log.info("chain : {}", chain);
				log.info("chain.shipConfig : {}", chain.shipConfig);
				log.info("chain.shipConfig.tmsPlanRowId : {}", chain.shipConfig.tmsPlanRowId);
				log.info("{}", event);
				log.info("{}", event.getShippingPlanIO());
				log.info("======");
			}
		});
		return list;
	}

}
