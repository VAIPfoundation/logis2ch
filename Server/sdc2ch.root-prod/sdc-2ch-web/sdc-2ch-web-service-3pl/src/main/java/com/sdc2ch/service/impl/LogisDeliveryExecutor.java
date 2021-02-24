package com.sdc2ch.service.impl;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdc2ch.service.ILgistCostService;
import com.sdc2ch.service.ILgistOdMatrixService;
import com.sdc2ch.service.ILogistExecutService;
import com.sdc2ch.service.model.OdMatrixInfoVo;
import com.sdc2ch.service.util.TmsSettleCost;
import com.sdc2ch.service.util.TmsTurnRate;
import com.sdc2ch.service.util.TmsTurnRate.TmsTurnRateBuilder;
import com.sdc2ch.tms.io.TmsCalculateIO;
import com.sdc2ch.tms.service.ITmsCalculateService;
import com.sdc2ch.tms.service.ITmsPlanService;
import com.sdc2ch.tms.service.ITmsStopService;
import com.sdc2ch.web.admin.repo.domain.lgist.T_LGIST_MODEL;
import com.sdc2ch.web.admin.repo.domain.lgist.T_LGIST_RULE_DTLS;
import com.sdc2ch.web.admin.repo.domain.lgist.T_LGIST_MODEL.TableType;
import com.sdc2ch.web.admin.repo.domain.lgist.T_LGIST_RULE_MSTR;
import com.sdc2ch.web.admin.repo.domain.lgist.T_ROUTE_PATH_MATRIX6;

@Service
public class LogisDeliveryExecutor implements ILogistExecutService {
	
	@Autowired ITmsPlanService tmsSvc;
	@Autowired ITmsStopService stopSvc;
	@Autowired ILgistOdMatrixService naviSvc;
	@Autowired ITmsCalculateService calculateSvc;
	@Autowired ILgistCostService costSvc;
	
	private ExecutorService service = Executors.newFixedThreadPool(4);

	private int executeCount;
	
	@Override
	public void execute(T_LGIST_MODEL model) {
		
		if(executeCount > 4) {
			throw new RuntimeException("최대 4개 모델까지만 분석 가능합니다. 현재 분석 중인 모델 수 = " + executeCount);
		}
		service.submit(Executor.builder()
				.model(model)
				.service(LogisDeliveryExecutor.this)
				.calculateSvc(calculateSvc)
				.tmsSvc(tmsSvc)
				.stopSvc(stopSvc)
				.naviSvc(naviSvc)
				.newShipCost(BigDecimal.ZERO)
				.inc(new AtomicInteger())
				.build());
	}

	@Override
	public boolean supported(TableType lgistType) {
		return TableType.DELIVERY == lgistType;
	}

	public void addExecuteCount() {executeCount++;}
	public void rmExecuteCount()  {executeCount--;}

	public TmsSettleCost saveMatrix(TmsCalculateIO calculate, OdMatrixInfoVo tmapInfo, T_LGIST_RULE_MSTR rule) {
		List<TmsTurnRate> rates = rule.getRuleDtls().stream()
				.sorted(Comparator.comparing(T_LGIST_RULE_DTLS::getSeq))
				.map(dtls -> convert(dtls)).collect(Collectors.toList());
		
		TmsSettleCost cost = TmsSettleCost.builder()
		.kmPerLiter(calculate.getOilCost())
		.contractPrice(calculate.getShipPayment())
		.distance(new BigDecimal(tmapInfo.getTotalDistance()+""))
		.tollCost(new BigDecimal(tmapInfo.getTotalTollCost()+""))
		.point(new BigDecimal(tmapInfo.getRoutePathCount()))
		.hour(new BigDecimal((tmapInfo.getTotalTime() + 60 * 60 * 2) / 3600))
		.carOilQty(new BigDecimal(findFuelByDefaultKmperLiter(calculate.getTon().floatValue())))
		.frezingOilQty(calculate.getSupportFrezOil())
		.newTrunRateRules(rates)
		.build();
		System.out.println(cost);
		return cost;
	}

	private float findFuelByDefaultKmperLiter(float ton) {
		float kmperliter = 0;
		if(ton == 2.5) {
			kmperliter = 0.18f;
		}else if(ton == 3.5) {
			kmperliter = 0.19f;
		}else if(ton == 5) {
			kmperliter = 0.22f;
		}else if(ton == 8) {
			kmperliter = 0.25f;
		}else if(ton == 4.5) {
			kmperliter = 0.34f;
		}else if(ton == 14) {
			kmperliter = 0.36f;
		}else if(ton == 11) {
			kmperliter = 0.36f;
		}

		if(kmperliter == 0) {
			System.out.println(ton);
		}
		return kmperliter;
	}

	private TmsTurnRate convert(T_LGIST_RULE_DTLS dtls) {
		TmsTurnRateBuilder builder = TmsTurnRate.create();
		return builder
		.minDistance(dtls.getMinDstnc())
		.maxDistance(dtls.getMaxDstnc())
		.minHour(dtls.getMinTime())
		.maxHour(dtls.getMaxTime())
		.minLdng(dtls.getMinDlvyCnt())
		.maxLdng(dtls.getMaxDlvyCnt())
		.turnRate(Float.valueOf(dtls.getRtateRate()))
		.build();
	}

	public void saveMatrix(T_ROUTE_PATH_MATRIX6 mat6) {
		costSvc.saveMatrix(mat6);
	}

	public void saveModel(T_LGIST_MODEL model) {
		costSvc.saveModel(model);
	}
}
