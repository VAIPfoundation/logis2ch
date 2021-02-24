package com.sdc2ch.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdc2ch.service.ILgistTransService;
import com.sdc2ch.service.ILogistExecutService;
import com.sdc2ch.tms.io.TmsCalculateIO;
import com.sdc2ch.tms.service.ITmsCalculateService;
import com.sdc2ch.web.admin.repo.dao.lgist.T_LgistModelRepository;
import com.sdc2ch.web.admin.repo.domain.lgist.T_LGIST_MODEL;
import com.sdc2ch.web.admin.repo.domain.lgist.T_LGIST_ROUTE;
import com.sdc2ch.web.admin.repo.domain.lgist.T_LGIST_MODEL.TableType;

@Service
public class LogisTransportExecutor implements ILogistExecutService {

	
	@Autowired ILgistTransService svc;
	@Autowired T_LgistModelRepository modelRepo;
	@Autowired ITmsCalculateService calculateSvc;
	
	private ExecutorService service = Executors.newFixedThreadPool(4);
	
	@Override
	public boolean supported(TableType lgistType) {
		return TableType.TRANSPORT == lgistType;
	}
	
	@Override
	public void execute(T_LGIST_MODEL model) {
		model.setSttus(1);
		model.setAnalsStartDt(new Date());
		List<TmsCalculateIO> datas = calculateSvc.search(model.getStartDe(), model.getEndDe(), model.getFctryCd(), c -> predicate(c));
		List<String> containsRoute = model.getModels().stream().map(T_LGIST_ROUTE::getRouteNo).collect(Collectors.toList());
		System.out.println("before -> " + datas.size());

		System.out.println("after -> " + datas.size());
		model.setTotalCnt(datas.size());
		model.setCostBefore(datas.stream().map(TmsCalculateIO::getTotalShipCost).reduce((a, b) -> a.add(b)).orElse(BigDecimal.ZERO).toString());
		modelRepo.save(model);
		
		
		service.submit(new Runnable() {
			
			@Override
			public void run() {
				
				com.sdc2ch.service.tm.TMMain tmMain = new com.sdc2ch.service.tm.TMMain(svc, model.getId(), model.getFctryCd(), null, "1D1");	
				tmMain.start(model.getStartDe().replaceAll("-", ""), model.getEndDe().replaceAll("-", ""));
			}
		});
	}

	private boolean predicate(TmsCalculateIO c) {
		return c.getRouteNo().contains("_");
	}

}
