package com.sdc2ch.prcss.ss.template;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.util.Assert;

import com.sdc2ch.prcss.ds.event.IShippingContextEvent.EventNm;
import com.sdc2ch.prcss.ss.vo.TemplateVo;
import com.sdc2ch.tms.enums.FactoryType;
import com.sdc2ch.tms.enums.ShippingType;
import com.sdc2ch.tms.io.TmsPlanIO;
import com.sdc2ch.tms.service.ITmsShippingService;

public class StateTemplate extends AbstractTemplate {
	
	private ITmsShippingService shippingSvc;
	public StateTemplate(ITmsShippingService shippingSvc) {
		this.shippingSvc = shippingSvc;
	}
	
	
	
	public class DeliveryTemplate {
		
	}



	public Stream<TemplateVo> getTemplate(TmsPlanIO plan) {
		Assert.notNull(plan, "TmsPlanIO required ");

		ShippingType shippingTy = shippingSvc.findShippingType(plan.getRouteNo());
		switch (shippingTy) {
		case DELEVERY:
			return getDeliveryTemplate(plan).stream();
		case TRANSPORT:
			
			break;
		default:
			break;
		}
		return null;
	}



	private List<TemplateVo> getDeliveryTemplate(TmsPlanIO plan) {
		
		FactoryType factryTy = shippingSvc.findFactoryType(plan.getRouteNo());
		boolean isFactroy = isFactory(plan.getStopCd());
		boolean endof = plan.getStopSeq() != 0;
		
		Stream<DlvyState> stream = null;
		if(isFactroy) {
			stream = Stream.of(DlvyState.TURN, DlvyState.ENTER, DlvyState.LDNG, DlvyState.EXIT).filter(s -> endof ? s == DlvyState.TURN : s != DlvyState.TURN);
		}else {
			stream = Stream.of(DlvyState.DELIVERY);
		}
		
		List<DlvyState> states = stream.collect(Collectors.toList());
		List<TemplateVo> vos = new ArrayList<>();
		for(DlvyState s : states) {
			for(EventNm name : s.getEventNm()) {
				TemplateVo vo = new TemplateVo();
				vo.setPlan(plan);
				vo.setRouteNo(plan.getRouteNo());
				vo.setEventNm(name);
				vo.setDlvyDe(plan.getDlvyDe());
				vo.setFactryCd(plan.getFctryCd());
				vo.setFactryNm(factryTy.getName());
				vo.setState(s);
				vo.setStopCd(plan.getStopCd());
				vo.setStopNm(plan.getDlvyLoNm());
				vos.add(vo);
			}
		}
		return vos;
	}
	
	
	public boolean isFactory(String stopCd) {
		return Arrays.asList(FactoryType.values())
				.stream().anyMatch(f -> f.getCode().equals(stopCd));
	}
	
}
