package com.sdc2ch.prcss.ss.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sdc2ch.prcss.ds.event.IShippingContextEvent.EventBy;
import com.sdc2ch.prcss.ds.event.IShippingContextEvent.EventNm;
import com.sdc2ch.prcss.ds.event.ShippingStateEvent;
import com.sdc2ch.prcss.ss.template.AbstractGradeTemplate.GradeTy;
import com.sdc2ch.prcss.ss.template.AbstractTemplate.State;
import com.sdc2ch.tms.io.TmsPlanIO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TemplateVo {
	private TmsPlanIO plan;
	private String dlvyDe;
	private State state;
	private EventNm eventNm;
	private EventBy eventBy;
	private String routeNo;
	private String factryCd;
	private String factryNm;
	private String stopCd;
	private String stopNm;
	private Date eventDt;
	private String rm;
	private String dataTy;
	private Long gid;
	
	private GradeTy scopeGrad;
	private GradeTy pointGrad;
	
	private boolean readonly;
	
	private List<ShippingStateEvent> events = new ArrayList<>();
	
	public void add(ShippingStateEvent event) {
		this.events.add(event);
	}

}
