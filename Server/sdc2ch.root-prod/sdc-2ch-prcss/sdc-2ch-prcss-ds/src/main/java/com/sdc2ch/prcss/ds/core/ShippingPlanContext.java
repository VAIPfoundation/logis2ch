package com.sdc2ch.prcss.ds.core;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.google.common.base.Splitter;
import com.sdc2ch.core.expression.InstanceOf;
import com.sdc2ch.prcss.ds.core.Sort.SortedType;
import com.sdc2ch.prcss.ds.event.ActionEventType;
import com.sdc2ch.prcss.ds.event.IAlaramNotiEvent;
import com.sdc2ch.prcss.ds.event.IAlaramNotiEvent.AlaramType;
import com.sdc2ch.prcss.ds.event.IAllocatedEvent;
import com.sdc2ch.prcss.ds.event.IBeconEvent;
import com.sdc2ch.prcss.ds.event.IChainLocEvent;
import com.sdc2ch.prcss.ds.event.IEmptyboxConfirmEvent;
import com.sdc2ch.prcss.ds.event.IGeoFenceEvent;
import com.sdc2ch.prcss.ds.event.IMobileActionEvent;
import com.sdc2ch.prcss.ds.event.IMobileActionEvent.MobileEventActionType;
import com.sdc2ch.prcss.ds.event.INfcTagEvent;
import com.sdc2ch.prcss.ds.event.IOTEvent;
import com.sdc2ch.prcss.ds.event.IProcessEvent;
import com.sdc2ch.prcss.ds.t.chain.Home;
import com.sdc2ch.prcss.ds.t.chain.ShippingChain;
import com.sdc2ch.prcss.ds.t.chain.state.ReadyState;
import com.sdc2ch.prcss.ds.t.chain.state.ShippingStateBuilder;
import com.sdc2ch.prcss.ds.t.chain.state.ShippingStatus.StateNm;
import com.sdc2ch.prcss.ds.t.chain.state.action.ActionEvent;
import com.sdc2ch.prcss.ds.t.chain.state.action.MobileEventAction;
import com.sdc2ch.prcss.ds.t.receiver.IShippingReceiver;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.pubsub.I2ChEventManager;
import com.sdc2ch.require.pubsub.I2ChEventPublisher;

import io.reactivex.Observable;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.BehaviorSubject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
 
@Slf4j
public class ShippingPlanContext extends AbstractShippingState {
	
	
	private static String CODEC_START_CHAR = "1";
	
	private IUser user;
	private BehaviorSubject<ShippingChain> chainObserver;
	private BehaviorSubject<IProcessEvent> observable = BehaviorSubject.create();
	private ScheduledExecutorService scheduler;
	
	
	private List<ShippingChain> chains = new ArrayList<>();
	
	private Home start;
	
	private Date allocatedDate;
	
	private final Long allocatedGroupId;
	
	private final I2ChEventManager manager;
	
	private IProcessEvent currentEvent;
	
	private ShippingRouteState routeState;
	
	private List<IShippingReceiver> receiver = new ArrayList<>();
	
	private I2ChEventPublisher<IAlaramNotiEvent> alaramPub;
	
	public ShippingPlanContext(I2ChEventManager manager, Long allocatedGroupId, IUser user) {
		super();
		this.manager = manager;
		this.user = user;
		this.allocatedGroupId = allocatedGroupId;
		alaramPub = manager.regist(IAlaramNotiEvent.class);
	}
	
	public void onSubscribeOn(BehaviorSubject<ShippingChain> chainObserver) {
		this.chainObserver = chainObserver;
	}
	
	
	public void fireEvent(IProcessEvent geo) {
		
		observable.onNext(geo);
	}
	
	@Override
	protected void onChange(AbstractShippingState current) {



















		
		start.removeAlarm();
		this.current = current;
		this.routeState.onReceive(current.getShippingPlanIO());
		current.setCurrentRouteNo(routeState.getCurrentRoute());
		log.info("외부이벤트 발생시켜야한다. -> {}, {}, {}", getCurrentState(), getCurrentState().getCurrentState(), getCurrentState().getCurrentState().getCurrentState());
		log.info("이벤트는 뭐냐 ?? . -> {} {}", currentEvent, currentEvent.getActionEventTy());
		try {
			
			Optional.ofNullable(chainObserver).ifPresent(o -> o.onNext((ShippingChain) current));
		}catch (Exception e) {
			log.error("{}", e);
		}

	}
	public void setEvent(IProcessEvent e1) {
		currentEvent = e1;
	}
	
	@Override
	public IShipping onCreate(IShipping context) {
		
		currentEvent = new IAllocatedEvent() {
			@Override public IUser user() {return user;}
			@Override public long getTimeStamp() {return System.currentTimeMillis();}
			@Override public ActionEventType getActionEventTy() {return ActionEventType.MGR_ALLOCATED;}
		};
		
		receiver.forEach(r -> r.onReady());
		
		chains.stream()
			.forEach(c -> ShippingStateBuilder
			.builder()
			.shippingChain(c)
			.build()
			.onCreate(this)
			.getChilds()
			.stream()
			.forEach(state -> state.onCreate(context)
			.getChilds()
			.stream()
			.forEach(a -> a.onCreate(context))
		));
		
		routeState = new ShippingRouteState(this);
		routeState.onCreate();
		
		return setDefaultState();
	}
	
	@Override
	public void onCancel() {
		chains.forEach(c -> c.onCancel());
		receiver.forEach(r -> r.onClose());
		observable.onComplete();
		log.info("취소이벤트 발생 시켜야한다.");
	}
	
	public void onComplete() {
		
	}

	protected void onFinish() {
		
		this.flattened().filter(s -> s instanceof ActionEvent).forEach(a -> a.onComplete());
		observable.onComplete();
		receiver.forEach(r -> r.onClose());
	}
	
	public String encodeHint() {
		String bin = start.flattened().map(s -> s.isComplete() ? "1" : "0").collect(Collectors.joining());
		bin = bin.concat(super.flattened().map(s -> s.isComplete() ? "1" : "0").collect(Collectors.joining()));
		return new BigInteger((CODEC_START_CHAR + bin), 2).toString(16);
	}
	public String decodeHint(String hint) {
		return new BigInteger(hint, 16).toString(2).substring(1);
	}

	@Override
	public IProcessEvent getCurrentEvent() {
		return this.currentEvent;
	}
	
	public ShippingPlanContext addAll(List<ShippingChain> chains) {
		Assert.notEmpty(chains, "List<LogisticsChain> can not be null");
		this.chains.addAll(chains);
		this.chains = chains.stream().sorted(new Sort(sortTypes)).collect(Collectors.toList());
		this.reIndex();
		this.chains = this.chains.stream().sorted(new Sort(Arrays.asList(SortedType.DLVY_LC_RE_INDEX))).collect(Collectors.toList());
		return this;
	}

	
	@Override
	public Observable<IProcessEvent> subscribeGPS(IShipping shipping, 
			Predicate<? super IProcessEvent> predicate) {
		receiver.forEach(r -> r.add(shipping));
		return observable.filter(e -> e instanceof IGeoFenceEvent).filter(predicate);
	}
	
	@Override
	public Observable<IProcessEvent> subscribeNFC(IShipping shipping, 
			Predicate<? super IProcessEvent> predicate) {
		return observable.filter(e -> e instanceof INfcTagEvent).filter(e -> chainFilter(e, shipping) && predicate.test(e));
	}
	@Override
	public Observable<IProcessEvent> subscribeEBX(IShipping shipping, 
			Predicate<? super IProcessEvent> predicate) {
		return observable.filter(e -> e instanceof IEmptyboxConfirmEvent).filter(predicate);
	}
	
	@Override
	public Observable<IProcessEvent> subscribeMBL(IShipping shipping, 
			Predicate<? super IProcessEvent> predicate) {
		
		System.out.println("context -> root :> " + shipping.hashCode());
		return observable.filter(e -> {
			System.out.println("called ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			return e instanceof IMobileActionEvent;
		}).filter( e -> {
			System.out.println("called !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			return predicate.test(e);
		});
	}
	@Override
	public Observable<IProcessEvent> subscribeBCN(IShipping shipping, 
			Predicate<? super IProcessEvent> predicate) {
		return observable.filter(e -> e instanceof IBeconEvent).filter(e -> chainFilter(e, shipping) && predicate.test(e));
	}
	public void addReceiver(IShippingReceiver receiver) {
		this.receiver.add(receiver);
	}
	
	
	
	
	public Long getAllocatedGroupId() {
		return allocatedGroupId;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<IShipping> getChilds() {
		return (List<IShipping>)(Object) chains;
	}
	
	
	public BehaviorSubject<IProcessEvent> getGpsObserver() {
		return observable;
	}
	
	public I2ChEventManager getShippingManager() {
		return manager;
	}

	
	public IUser getUser() {
		return user;
	}
	
	
	public void setAllocatedDate(Date allocatedDate) {
		this.allocatedDate = allocatedDate;
	}
	
	public Date getAllocatedDate() {
		return allocatedDate;
	}
	
	@Override
	public List<ShippingChain> getChains(){
		return chains;
	}

	
	
	private void reIndex() {
		Map<String, List<ShippingChain>> groupby = chains.stream()
				.collect(Collectors.groupingBy(c -> c.shipConfig.routeNo.split("_")[0]));
		List<SortPlan> _tmps = _TmpObj._tempOrder3(groupby);
		int routeIdx = 0;
		int dlvyLcIdx = 0;
		
		try {
			_tmps = _tmps.stream().sorted(Comparator.comparing(SortPlan::getMin)).collect(Collectors.toList());
		}catch (Exception e) {
		}
		
		for (SortPlan o : _tmps) {
			for (ShippingChain c : groupby.get(o.routeNo)) {
				c.shipConfig.setRouteReIdx(routeIdx);
				c.shipConfig.setDlvyLoReIdx(dlvyLcIdx);
				dlvyLcIdx++;
			}
			routeIdx++;
		}
	}
	
	private ShippingPlanContext setDefaultState() {
		
		start = new Home(this, chains.stream().findFirst().get().shipConfig);
		ReadyState state = new ReadyState(start, StateNm.READY);
		
		ActionEvent action = new MobileEventAction(state, MobileEventActionType.ALLOCATE_CONFIRM);
		action.setEventClass(IMobileActionEvent.class);
		state.add(action);
		action.onCreate(this);
		
		action = new MobileEventAction(state, MobileEventActionType.START_JOB);
		action.setEventClass(IMobileActionEvent.class);
		state.add(action);
		action.onCreate(this);
		
		state.onCreate(this);
		start.addState(state);
		start.setCurrentRouteNo(routeState.getCurrentRoute());
		current = start;
		return this;
	}
	
	public ShippingPlanContext onLoad() {
		if(chainObserver != null)
			chainObserver.onNext(start);
		return this;
	}
	
	
	private List<SortedType> sortTypes = Arrays.asList(SortedType.ROUTE_NO, SortedType.DLVY_LC_SEQ);
	
	@Getter
	@Setter
	private static class SortPlan {
		public SortPlan(String k, LocalDateTime min) {
			this.routeNo = k;
			this.min = min;
		}
		private String routeNo;
		private LocalDateTime min;
	}
	
	@ToString
	private static class _TmpObj {
		String routeNo; int minTime;
		private _TmpObj(String routeNo, int minTime) {
			this.routeNo = routeNo;this.minTime = minTime;
		}
		
		@SuppressWarnings("unused")
		private static List<_TmpObj> _tempOrder2(Map<String, List<ShippingChain>> groupBy) {
			
			List<_TmpObj> obj = new ArrayList<>();
			for(String key : groupBy.keySet()) {
				String sTime = Collections.min(groupBy.get(key), new Comparator<ShippingChain>() {
					@Override
					public int compare(ShippingChain o1, ShippingChain o2) {
						return Integer.compare(convert(o1.shipConfig.plannedATime), convert(o2.shipConfig.plannedATime));
					}
				}).shipConfig.plannedATime;
				obj.add(new _TmpObj(key, convert(sTime)));
				
			}
			return obj;
		}
		
		private static Integer convert(String time) {
			
			if(StringUtils.isEmpty(time))
				return 2400;
			
			try {
				return Integer.valueOf(time.replaceAll(":", "").replaceAll(";", ""));
			} catch (Exception e) {
				if (time.length() != 5) {
					return 2400;
				}
				String newTime = time.substring(0, 2)
						+ time.substring(3, 5);
				return Integer.valueOf(newTime);
			}
		}
		
		private static List<SortPlan> _tempOrder3(Map<String, List<ShippingChain>> groupBy) {
			return groupBy.keySet().stream().map(k -> {
				LocalDateTime min = groupBy.get(k)
				.stream()
				.filter(sh -> sh.shipConfig.getScheDlvyStDt() != null)
				.map(sh -> sh.shipConfig.getScheDlvyStDt())
				.min(new Comparator<LocalDateTime>() {
					@Override
					public int compare(LocalDateTime o1, LocalDateTime o2) {
						return o1.compareTo(o2);
					}
				})
				.orElse(LocalDateTime.now());
				return new SortPlan(k, min);
			}).collect(Collectors.toList());
		}
		
		private static List<_TmpObj> _tempOrder(Map<String, List<ShippingChain>> groupBy) {
			
			groupBy.keySet().stream().map(k -> {
				
				groupBy.get(k).stream().filter(sh -> sh.shipConfig.plannedATime != null).map(sh -> {
					LocalDateTime time = sh.shipConfig.getScheDlvyStDt();
					return time;
				});
				return null;
			});
			
			
			
			
			return groupBy.keySet().stream().map(k -> {
				int minTime = 0;
				try {
					minTime = groupBy.get(k).stream().filter(sh -> sh.shipConfig.plannedATime != null).map(sh -> {
						try {
							return Integer.valueOf(sh.shipConfig.plannedATime.replaceAll(":", "").replaceAll(";", ""));
						} catch (Exception e) {
							if (sh.shipConfig.plannedATime.length() != 5) {
								return 2400;
							}
							String newTime = sh.shipConfig.plannedATime.substring(0, 2)
									+ sh.shipConfig.plannedATime.substring(3, 5);
							return Integer.valueOf(newTime);
						}
					}).min(new Comparator<Integer>() {
						@Override
						public int compare(Integer o1, Integer o2) {
							return o1.compareTo(o2);
						}
					}).orElse(0);
				}catch (Exception e) {
					log.error("{}", e);
				}

				return new _TmpObj(k, minTime == 0 ? 2400 : minTime);
			}).collect(Collectors.toList()).stream().sorted(new Comparator<_TmpObj>() {
				@Override
				public int compare(_TmpObj o1, _TmpObj o2) {
					return Integer.compare(o1.minTime, o2.minTime);
				}
			}).collect(Collectors.toList());
		}
	}
	protected void finalize() throws Throwable {
		log.info("do finalize called groupid={} {}", allocatedGroupId, allocatedDate);
	}

	@Override
	public boolean supported(IProcessEvent e) {
		return this.chains.stream().filter(s -> {


			return chainFilter(e, s) && s.supported(e);
		}).findFirst().orElse(null) != null;
	}
	private boolean chainFilter(IProcessEvent e, IShipping shipping) {
		boolean isMyChain = InstanceOf
		.when(e)
		.instanceOf(IOTEvent.class)
		.then(e1 -> e1.getFactoryType() == shipping.getShippingPlanIO() .getMyFacrtyTy())
		.otherwise(InstanceOf.when(e).instanceOf(IChainLocEvent.class).then(e1 -> ((AbstractShippingState) shipping).isEqualLoc(e1)).otherwise(false) );
		return isMyChain;
	}

	public void reloadData(String hint) {
		hint = decodeHint(hint);
		BlockingQueue<Boolean> queue = new LinkedBlockingQueue<>();
		StreamSupport.stream(Splitter.fixedLength(1).split(hint).spliterator(), false).map(s -> "1".equals(s))
				.collect(Collectors.toList()).forEach(queue::offer);
		
		start.flattened().forEach(s -> _reload((AbstractShippingState) s, queue));
		this.flattened().forEach(s -> _reload((AbstractShippingState) s, queue));
		
		log.info("{}, {}", this.getCurrentState(), queue.size());
	}
	
	private void _reload(AbstractShippingState state, BlockingQueue<Boolean> queue) {
		Boolean hint = queue.poll();
		if (hint != null && hint) {
			state.hit();
			state.onCancel();
		}else {
		}
	}
	public void setCurrent(AbstractShippingState current) {
		this.current = current;
	}
	
	public Home getStart() {
		return start;
	}
	
	public void sendNoti(AlaramType ty) {
		alaramPub.fireEvent(new IAlaramNotiEvent() {
			@Override
			public IUser user() {
				return user;
			}
			@Override
			public AlaramType getAlaramTy() {
				return ty;
			}
		});
	}

	public void setScheduler(ScheduledExecutorService scheduler) {
		this.scheduler = scheduler;
	}
	public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
		return scheduler.schedule(command, delay, unit);
	}
	public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
		return scheduler.scheduleAtFixedRate(command, initialDelay, period, unit);
	}
}
