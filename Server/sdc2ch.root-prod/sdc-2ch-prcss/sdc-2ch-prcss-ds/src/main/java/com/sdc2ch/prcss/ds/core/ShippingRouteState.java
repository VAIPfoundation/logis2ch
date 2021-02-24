package com.sdc2ch.prcss.ds.core;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.sdc2ch.prcss.ds.io.ShippingPlanIO;
import com.sdc2ch.prcss.ds.t.chain.Factory;
import com.sdc2ch.prcss.ds.t.chain.ShippingChain.ChainNm;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShippingRouteState {
	
	private ShippingPlanContext context;
	private List<FactoryState> firstFactorys;
	private ShippingPlanIO current;
	
	ShippingRouteState(ShippingPlanContext context){
		this.context = context;
	}
	
	
	@SuppressWarnings("unlikely-arg-type")
	public void onCreate() {
		setFactoryStates();
		List<FactoryState> _factState = groupByFirstStartFactory();
		context.getChains().stream()
		.filter(c -> c.chainName() == ChainNm.FACTORY)
		.filter(c1 -> {
			boolean ctns = !_factState.stream().anyMatch(f-> f.equals(c1));
			

			return ctns;
		}).forEach(c2 -> {

			c2.removeEvent();
		});
	}
	
	private void setFactoryStates(){
		firstFactorys = findStartFactory();
	}
	
	private List<FactoryState> findStartFactory() {
		AtomicInteger inc = new AtomicInteger();
		return context.getChains()
		.stream()
		.filter(c -> c.chainName() == ChainNm.FACTORY)
		.filter(c -> context.isRouteHeader(c)).map(c -> {
			Factory factry = (Factory) c;
			return FactoryState.builder().factory(factry).factoryIdx(inc.getAndIncrement()).hit(false).build();
		}).collect(Collectors.toList());
	}
	
	private List<FactoryState> groupByFirstStartFactory() {
		Map<String, List<FactoryState>> mapped = firstFactorys.stream()
				.collect(Collectors.groupingBy(FactoryState::getFactoryCd));
		return mapped.keySet().stream()
				.map(t -> mapped.get(t).stream().min(Comparator.comparing(FactoryState::getFactoryIdx)).get())
				.collect(Collectors.toList());
	}
	
	public void onReceive(ShippingPlanIO plan) {
		
		try {
			this.current = plan;
			int findIdx = firstFactorys.stream()
					.filter(f -> _filter(f.getFactory().getShippingPlanIO(), plan))
					.findFirst().get().getFactoryIdx();
			firstFactorys.stream().filter(f -> f.getFactoryIdx() <= findIdx).forEach(f -> f.setHit(true));
		}catch (Exception e) {
			
			log.error("{}", e);
		}

	}
	
	private boolean _filter(ShippingPlanIO src, ShippingPlanIO tag) {
		String srcStr = null;
		String tagStr = null;
		if(tag.isCustomerCenter()) {
			srcStr = src.getRouteNo();
			tagStr = tag.getRouteNo();
		}else if (tag.isFactory()) {
			srcStr = src.getDlvyLcId() + replaceRouteNo(src.getRouteNo());
			tagStr = tag.getDlvyLcId() + replaceRouteNo(tag.getRouteNo());
		}else if(tag.isWareHouse()) {
			srcStr = src.getRouteNo();
			tagStr = tag.getRouteNo();
		}
		return srcStr.equals(tagStr);
	}
	
	private String replaceRouteNo(String routeNo) {
		return routeNo.split("_")[0];
	}


	public String getCurrentRoute() {
		
		try {
			
			int nextRouteIdx = IntStream.range(0, firstFactorys.size()).filter(i -> firstFactorys.get(i).isHit() == false)
					.findFirst().orElseGet(() -> {
						return firstFactorys.stream().filter(f -> f.getFactoryCd().equals(current.getDlvyLcId()))
								.map(f -> f.getFactoryIdx() + 1).findFirst().orElse(firstFactorys.size());
					});
			int idx = nextRouteIdx == 0 ? 0 : nextRouteIdx -1;
			
			
			if(firstFactorys == null || firstFactorys.isEmpty())
				return context.getCurrentRouteNo();
			
			log.info("current route -> {}", firstFactorys.get(idx).getFactory().shipConfig.routeNo);
			return firstFactorys.get(idx).getFactory().shipConfig.routeNo;
		}catch (Exception e) {
			log.error("{}", e);
		}
		return current.getRouteNo();
	}
	
	
	@Builder
	@Getter
	@ToString
	private static class FactoryState {
		private Factory factory;
		private boolean hit;
		private int factoryIdx;
		
		
		public void setHit(boolean hit) {
			this.hit = hit;

		}
		@Override
		public boolean equals(Object o) {
			return this.factory == o;
		}
		
		public String getFactoryCd() {
			return factory.shipConfig.dlvyLcId;
		}
	}

}
