package com.sdc2ch.prcss.ds.core;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import com.sdc2ch.prcss.ds.t.chain.CustomerCenter;
import com.sdc2ch.prcss.ds.t.chain.Factory;
import com.sdc2ch.prcss.ds.t.chain.ShippingChain;
import com.sdc2ch.prcss.ds.t.chain.Warehouse;
import com.sdc2ch.prcss.ds.t.receiver.ShippingBconHandler;
import com.sdc2ch.prcss.ds.t.receiver.ShippingConfirmHandler;
import com.sdc2ch.prcss.ds.t.receiver.ShippingEmptyboxHandler;
import com.sdc2ch.prcss.ds.t.receiver.ShippingFinishHandler;
import com.sdc2ch.prcss.ds.t.receiver.ShippingGpsReceiver;
import com.sdc2ch.prcss.ds.t.receiver.ShippingNfcHandler;
import com.sdc2ch.prcss.ds.t.receiver.ShippingStartHandler;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.pubsub.I2ChEventManager;
import com.sdc2ch.tms.enums.FactoryType;
import com.sdc2ch.tms.enums.ShippingType;
import com.sdc2ch.tms.io.TmsPlanIO;
import com.sdc2ch.tms.service.ITmsShippingService;

import lombok.RequiredArgsConstructor;

 
@RequiredArgsConstructor(staticName="of")
public class ShppingScanner {
	
	
	private List<ShippingPlan> plans = new ArrayList<>();
	private List<TmsPlanIO> tmsplan = new ArrayList<>();
	
	private Long allocatedGroupId;
	private IUser user;
	private I2ChEventManager manager;
	
	public ShppingScanner manager(I2ChEventManager manager) {
		this.manager = manager;
		return this;
	}
	public ShppingScanner allocatedId(Long allocatedGroupId) {
		this.allocatedGroupId = allocatedGroupId;
		return this;
	}
	public ShppingScanner user(IUser user) {
		this.user = user;
		return this;
	}

	
	public ShppingScanner tmsPlans(List<TmsPlanIO> tmsplan) {
		this.tmsplan = tmsplan;
		return this;
	}
	
	
	public ShppingScanner scan(ITmsShippingService shipSvc){
		plans = convert(shipSvc, tmsplan);
		return this;
	}
	
	
	public ShippingPlanContext build(){
		
		if(plans.isEmpty()) {
			System.out.println();
		}
		ShippingPlanContext context = new ShippingPlanContext(manager, allocatedGroupId, user);
		context.addReceiver(new ShippingGpsReceiver(context));
		context.addReceiver(new ShippingBconHandler(context));
		context.addReceiver(new ShippingEmptyboxHandler(context));
		context.addReceiver(new ShippingNfcHandler(context));
		context.addReceiver(new ShippingStartHandler(context));
		context.addReceiver(new ShippingFinishHandler(context));
		context.addReceiver(new ShippingConfirmHandler(context));
		
		

		
		return context.addAll(plans.stream().map(p -> createChain(context, p)).collect(Collectors.toList()));
	}
	
	
	public static ShippingChain createChain(final ShippingPlanContext chainContext, final ShippingPlan config) {
		return config.isFactory() ? createFactory(chainContext, config)
				: config.isCustomerCenter() ? createCustomerCenter(chainContext, config)
						: createWarehouse(chainContext, config);
	}
	
	private static LocalDateTime convertDateAndTimeString(String de, String time) {
		if(StringUtils.isEmpty(de) || StringUtils.isEmpty(time))
			return null;
		return LocalDateTime.parse(de + time, DateTimeFormatter.ofPattern("yyyyMMddHH:mm"));
	}
	
	public static List<ShippingPlan> convert(ITmsShippingService shipSvc, List<TmsPlanIO> tmsplan) {
		tmsplan.removeIf(p -> p == null);
		return tmsplan.stream().map(op -> convert(shipSvc, op)).collect(Collectors.toList());
	}
	
	private static ShippingPlan convert(ITmsShippingService shipSvc, TmsPlanIO op) {
		
		String routeNo = op.getRouteNo();
		ShippingType shppingTp = shipSvc.findShippingType(routeNo);
		FactoryType ft = FactoryType.convert(op.getStopCd());
		
		LocalDateTime scheStDt = convertDateAndTimeString(op.getScheDlvyStDe(), op.getScheDlvyStTime());
		LocalDateTime scheEdDt = convertDateAndTimeString(op.getScheDlvyEdDe(), op.getScheDlvyEdTime());
		
		return ShippingPlan.builder()
		.tmsPlanRowId(op.getId())
		.routeNo(op.getRouteNo())
		.dlvyDe(op.getDlvyDe())
		.dlvyLcId(op.getStopCd())
		.dlvyLcSeq(op.getStopSeq())
		.driverCd(op.getDriverCd())
		.vrn(op.getVrn())
		.scheDlvyEdDt(scheEdDt)
		.scheDlvyStDt(scheStDt)
		.isEmpty(op.getEmpty() == null ? false : op.getEmpty())
		.dlvyLcNm(op.getDlvyLoNm())
		.lat(setScale(op.getLat()).doubleValue())
		.lng(setScale(op.getLng()).doubleValue())
		.radius(FactoryType.FFFF == ft ? 50 : 100)
		.carWegit(op.getCarWegit())
		.dockNo(op.getDockNo())
		.ldngSt(op.getLdngSt())
		.ldngEd(op.getLdngEd())
		.addr(op.getAddr())
		.confRtateRate(op.getConfRtateRate())
		.confDistance(op.getConfDistance())
		.confTollCost(op.getConfTollCost())
		.carOil(op.getCarOil())
		
		.bundledDlvyLc(op.getBundledDlvyLc())
		
		.plannedATime(shipSvc.getPlanedArriveTime(op))
		.plannedDTime(op.getDepartPlanTime())
		
		.deliveryTy(shipSvc.findDeliveryType(routeNo))
		
		.fctryTy(shipSvc.findFactoryType(routeNo))
		
		.seasonTy(shipSvc.findSeasonType(routeNo))
		
		.shppTy(shppingTp)
		
		.transportTy(shipSvc.findTransportType(routeNo))
		
		
		.timeZoneNm(op.getTimeZoneNm()) 
		.build();
	}
	
	private static BigDecimal setScale(String latlng) {
		if(StringUtils.isEmpty(latlng))
			return BigDecimal.ZERO;
		return new BigDecimal(latlng).setScale(6, BigDecimal.ROUND_HALF_UP);
	}
	
	
	private static Factory createFactory(ShippingPlanContext chainContext, ShippingPlan config) {
		return new Factory(chainContext, config);
	}

	private static Warehouse createWarehouse(ShippingPlanContext chainContext, ShippingPlan config) {
		return new Warehouse(chainContext, config);
	}

	private static CustomerCenter createCustomerCenter(ShippingPlanContext chainContext, ShippingPlan config) {
		return new CustomerCenter(chainContext, config);
	}
}
