package com.sdc2ch.prcss.test;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ComparisonChain;
import com.sdc2ch.aiv.event.IBeaconFireEvent;
import com.sdc2ch.aiv.event.IBeaconFireEvent.InoutType;
import com.sdc2ch.aiv.event.IGpsEvent;
import com.sdc2ch.core.pubsub.I2ChPubSubEventManagerImpl;
import com.sdc2ch.nfc.enums.NfcEventType;
import com.sdc2ch.nfc.event.INfcFireEvent;
import com.sdc2ch.prcss.ds.core.ShippingPlan;
import com.sdc2ch.prcss.ds.core.ShippingPlanContext;
import com.sdc2ch.prcss.ds.core.ShppingScanner;
import com.sdc2ch.prcss.ds.io.ShippingPlanIO;
import com.sdc2ch.prcss.ds.t.chain.ShippingChain;
import com.sdc2ch.prcss.eb.event.IEmptyboxEvent;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.domain.IUserDetails;
import com.sdc2ch.require.domain.IUserRole;
import com.sdc2ch.require.enums.SetupLcType;
import com.sdc2ch.require.pubsub.I2ChEventPublisher;
import com.sdc2ch.service.event.IAllocatedEvent;
import com.sdc2ch.tms.domain.view.TmsPlan;
import com.sdc2ch.tms.enums.FactoryType;
import com.sdc2ch.tms.io.TmsPlanIO;
import com.sdc2ch.tms.service.impl.TmsShippingServiceImpl;

import io.reactivex.subjects.BehaviorSubject;

public class ProcessTest3 {
	
	static TmsShippingServiceImpl shipSvc = new TmsShippingServiceImpl();
	static I2ChPubSubEventManagerImpl manager = new I2ChPubSubEventManagerImpl();
	static I2ChEventPublisher<IAllocatedEvent> alloPub = manager.regist(IAllocatedEvent.class);
	static I2ChEventPublisher<INfcFireEvent> nfcPub = manager.regist(INfcFireEvent.class);
	static I2ChEventPublisher<IGpsEvent> gpsPub = manager.regist(IGpsEvent.class);
	static I2ChEventPublisher<IBeaconFireEvent> bconPub = manager.regist(IBeaconFireEvent.class);
	static I2ChEventPublisher<IEmptyboxEvent> emtPub = manager.regist(IEmptyboxEvent.class);
	static ObjectMapper mapper = new ObjectMapper();

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		mapper.setSerializationInclusion(Include.NON_NULL);

		String date = "20180727";
		String fld = "data" + "/"  +date;
		File folder = new File(fld);
		File[] listOfFiles = folder.listFiles();
		
		

		int index = 0;
		for (File f : listOfFiles) {
			
			if(index < 13) {
				index++;
				continue;
			}
			
			BehaviorSubject<ShippingChain> chainObserver = BehaviorSubject.create();

			List<TmsPlanIO> tmsplan = (List<TmsPlanIO>)(Object)convert(f);
			ShppingScanner sc = ShppingScanner.of();
			
			TmsPlanIO plan = tmsplan.stream().findFirst().get();
			IUser user = createUser(tmsplan.stream().findFirst().get());
			
			ShippingPlanContext context = 
			sc
			.manager(manager)
			.allocatedId(1L)
			.user(user)
			.tmsPlans(tmsplan)
			.scan(shipSvc)
			.build();
			context.onCreate(context);
			context.onSubscribeOn(chainObserver);
			
			chainObserver.subscribe( o -> {
				System.out.println("chainObserver :> " + o);
			});
			
			
			
			context.getChains().stream().forEach(c -> {
				
				String s = c.shipConfig.tmsPlanRowId + "";
				
				System.out.println("!!!!!!!!!!!!!!!!!!!!! " + c.shipConfig.dlvyLcId);
				switch(c.chainName()) {
				case CUSTOMER_CENTER:
					gpsPub.fireEvent(new GPSEvent(c.shipConfig, user));
					emtPub.fireEvent(new EmptyboxEvent(user, s));
					break;
				case FACTORY:






					gpsPub.fireEvent(new GPSEvent(null, user));
					gpsPub.fireEvent(new GPSEvent(null, user));
					gpsPub.fireEvent(new GPSEvent(null, user));
					gpsPub.fireEvent(new GPSEvent(c.shipConfig, user));
					gpsPub.fireEvent(new GPSEvent(null, user));
					bconPub.fireEvent(new BeaconEvent(InoutType.IN, createUser(plan), convert(c.shipConfig.dlvyLcId)));
					bconPub.fireEvent(new BeaconEvent(InoutType.IN, createUser(plan), convert(c.shipConfig.dlvyLcId)));
					bconPub.fireEvent(new BeaconEvent(InoutType.IN, createUser(plan), convert(c.shipConfig.dlvyLcId)));
					bconPub.fireEvent(new BeaconEvent(InoutType.IN, createUser(plan), convert(c.shipConfig.dlvyLcId)));
					bconPub.fireEvent(new BeaconEvent(InoutType.IN, createUser(plan), convert(c.shipConfig.dlvyLcId)));
					bconPub.fireEvent(new BeaconEvent(InoutType.IN, createUser(plan), convert(c.shipConfig.dlvyLcId)));
					bconPub.fireEvent(new BeaconEvent(InoutType.IN, createUser(plan), convert(c.shipConfig.dlvyLcId)));
					nfcPub.fireEvent(nfcFireEvent(user, SetupLcType.OFFICE, convert(c.shipConfig.dlvyLcId)));

					nfcPub.fireEvent(nfcFireEvent(user, SetupLcType.OFFICE, convert(c.shipConfig.dlvyLcId)));
					nfcPub.fireEvent(nfcFireEvent(user, SetupLcType.OFFICE, convert(c.shipConfig.dlvyLcId)));
					nfcPub.fireEvent(nfcFireEvent(user, SetupLcType.OFFICE, convert(c.shipConfig.dlvyLcId)));
					nfcPub.fireEvent(nfcFireEvent(user, SetupLcType.OFFICE, convert(c.shipConfig.dlvyLcId)));
					nfcPub.fireEvent(nfcFireEvent(user, SetupLcType.OFFICE, convert(c.shipConfig.dlvyLcId)));
					bconPub.fireEvent(new BeaconEvent(InoutType.OUT, createUser(plan), convert(c.shipConfig.dlvyLcId)));
					bconPub.fireEvent(new BeaconEvent(InoutType.OUT, createUser(plan), convert(c.shipConfig.dlvyLcId)));
					bconPub.fireEvent(new BeaconEvent(InoutType.OUT, createUser(plan), convert(c.shipConfig.dlvyLcId)));
					bconPub.fireEvent(new BeaconEvent(InoutType.OUT, createUser(plan), convert(c.shipConfig.dlvyLcId)));
					bconPub.fireEvent(new BeaconEvent(InoutType.OUT, createUser(plan), convert(c.shipConfig.dlvyLcId)));
					bconPub.fireEvent(new BeaconEvent(InoutType.OUT, createUser(plan), convert(c.shipConfig.dlvyLcId)));





					break;
				case HOME:
					break;
				case WAREHOUSE:
					gpsPub.fireEvent(new GPSEvent(c.shipConfig, user));
					break;
				default:
					break;
				
				}
				
			});
			
				
			
			
			System.out.println(context);

			

		}

	}
	
	
	private static FactoryType convert(String dlvyLoId) {
		
		try {
			
			
			for(FactoryType t : FactoryType.values()) {
				
				if(t.getCode().equals(dlvyLoId)) {
					return t;
				}
				
			}
			
		}catch (Exception e) {
			
		}
		return FactoryType.FFFF;
	}


	private static class GPSEvent implements IGpsEvent {

		IUser user;
		ShippingPlanIO plan;
		public GPSEvent(IUser user) {
			this.user = user;
		}

		public GPSEvent(ShippingPlanIO plan, IUser createUser) {
			
			this.plan = plan;
			this.user = createUser;
		}

		@Override
		public IUser user() {
			return user;
		}

		@Override
		public double getLat() {
			return plan== null ? 0 : plan.getLat();
		}

		@Override
		public double getLng() {
			return plan== null ? 0 : plan.getLng();
		}

		@Override
		public long getTimeStamp() {
			return System.currentTimeMillis();
		}

		@Override
		public String getSpeed() {
			
			return null;
		}
		
	}
	private static class EmptyboxEvent implements IEmptyboxEvent  {
		
		IUser user;
		String dlvyLcid;
		public EmptyboxEvent(IUser user, String dlvyLcid) {
			this.user = user;
			this.dlvyLcid = dlvyLcid;
		}
		@Override
		public IUser user() {
			return user;
		}
		@Override
		public long getTimeStamp() {
			return 0;
		}
		@Override
		public String getDlvyLcId() {
			return dlvyLcid;
		}
		@Override
		public int getSquareBoxQty() {
			
			return 0;
		}
		@Override
		public int getTriangleBoxQty() {
			
			return 0;
		}
		@Override
		public int getYodelryBoxQty() {
			
			return 0;
		}
		@Override
		public int getPalletQty() {
			
			return 0;
		}
		@Override
		public String getRouteNo() {
			
			return null;
		}
		@Override
		public String getDlvyDe() {
			
			return null;
		}
		@Override
		public String getCause() {
			
			return null;
		}
		@Override
		public String getDlvyLcNm() {
			
			return null;
		}
		
	}
	
	private static class BeaconEvent implements IBeaconFireEvent {


		IUser user;
		InoutType in;
		FactoryType ty;
		public BeaconEvent(InoutType in, IUser user, FactoryType ty) {
			this.user = user;
			this.in = in;
			this.ty= ty;
			
			System.out.println("=============================" + ty.getCode());
		}

		@Override
		public IUser user() {
			return user;
		}
		@Override
		public long getTimestamp() {
			return System.currentTimeMillis();
		}
		@Override
		public String getDeviceId() {
			return "a12345";
		}

		@Override
		public InoutType getInoutTy() {
			
			return in;
		}

		@Override
		public FactoryType getFactoryTy() {
			
			return ty;
		}

		@Override
		public SetupLcType getSetupLcTy() {
			
			return null;
		}
		
	}
	
	private static INfcFireEvent nfcFireEvent(IUser user, SetupLcType type, FactoryType fctryTy) {
		
		System.out.println("NFC FIRE EVENT  " + fctryTy.getName());
		NfcFireEvent event = new NfcFireEvent(user);
		event.setEvent(NfcEventType.IDENTITY_SUCCESS_FINGERPRINT);
		event.setSetupLcTy(type);
		event.setEventTime(System.currentTimeMillis());
		event.setFactoryType(fctryTy);
		return event;
	}

	private static IAllocatedEvent allocatedConfirmEvent(IUser user) {
		
		return new IAllocatedEvent() {
			
			
			
			@Override
			public IUser user() {
				return user;
			}
			
			@Override
			public Long getAllocatedGroupId() {
				return null;
			}
		};
	}

	private static String weekDay(TmsPlan plan) {
		return null;










	}
	
	public int compare(ShippingPlan o1, ShippingPlan o2) {
		return ComparisonChain.start()
		        .compare(o1.routeNo, o2.routeNo)
		        .compare(o1.dlvyLcSeq, o2.dlvyLcSeq)
		        .result();

	}

	@SuppressWarnings("unchecked")
	private static List<TmsPlan> convert(File f) throws Exception {
		return (List<TmsPlan>) mapper.readValue(f, List.class).stream()
				.map(o -> {
					return mapper.convertValue(o, TmsPlan.class);
				}).collect(Collectors.toList());
	}
	
	private static BigDecimal setScale(String latlng) {
		return new BigDecimal(latlng).setScale(6, BigDecimal.ROUND_HALF_UP);
	}
	
	private static IUser createUser(TmsPlanIO plan) {
		
		AtomicInteger inc = new AtomicInteger();
		return new IUser() {
			
			@Override
			public String getUsername() {
				return plan.getDriverCd();
			}
			
			@Override
			public IUserDetails getUserDetails() {
				return null;
			}
			
			@Override
			public List<IUserRole> getRoles() {
				return null;
			}
			
			@Override
			public String getPassword() {
				return null;
			}
			
			@Override
			public String getMobileNo() {
				return inc.incrementAndGet() + "";
			}

			@Override
			public String getFctryCd() {
				return null;
			}
		};
	}

}
