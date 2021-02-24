package com.sdc2ch.prcss.test;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ComparisonChain;
import com.sdc2ch.core.pubsub.I2ChPubSubEventManagerImpl;
import com.sdc2ch.prcss.ds.core.ShippingPlan;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.domain.IUserDetails;
import com.sdc2ch.require.domain.IUserRole;
import com.sdc2ch.require.pubsub.I2ChEventPublisher;
import com.sdc2ch.service.event.IAllocatedEvent;
import com.sdc2ch.tms.domain.view.TmsPlan;
import com.sdc2ch.tms.io.TmsPlanIO;
import com.sdc2ch.tms.service.impl.TmsShippingServiceImpl;

public class ProcessTest4 {
	
	static TmsShippingServiceImpl shipSvc = new TmsShippingServiceImpl();
	static I2ChPubSubEventManagerImpl manager = new I2ChPubSubEventManagerImpl();
	static I2ChEventPublisher<IAllocatedEvent> alloPub = manager.regist(IAllocatedEvent.class);
	static ObjectMapper mapper = new ObjectMapper();

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

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
				return null;
			}

			@Override
			public String getFctryCd() {
				return null;
			}
		};
	}

}
