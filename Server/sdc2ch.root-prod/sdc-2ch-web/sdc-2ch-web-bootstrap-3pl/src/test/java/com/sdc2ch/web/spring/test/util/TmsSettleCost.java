package com.sdc2ch.web.spring.test.util;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class TmsSettleCost {
	
	private BigDecimal kmPerLiter;
	private BigDecimal contractPrice;
	private BigDecimal tollCost;
	private BigDecimal distance;
	private BigDecimal carOilQty;
	private BigDecimal frezingOilQty;
	private BigDecimal point;
	private BigDecimal hour;

	private List<TmsTurnRate> orgTrunRateRules;
	private List<TmsTurnRate> newTrunRateRules;
	
	public BigDecimal getOrgTurnRate() {
		BigDecimal turnRate = BigDecimal.ZERO;
		TmsTurnRate rule = getOrgRule();
		if(rule != null) {
			turnRate = new BigDecimal(rule.getTurnRate());
		}
		return turnRate;
		
	}
	
	public TmsTurnRate getOrgRule() {
		return orgTrunRateRules.stream()
		.filter(r -> contailDistanceRule(r, distance))
		.filter(r -> containsTimeRule(r, hour))
		.filter(r -> containsPointRule(r, point)).findFirst().orElse(null);
	}
	
	public BigDecimal getOrgSettleCost() {
		return getOrgTurnRate().multiply(contractPrice).add(fuelCost()).add(tollCost).setScale(0, BigDecimal.ROUND_DOWN);
	}
	
	public BigDecimal getNewTurnRate() {
		BigDecimal turnRate = distance == null ? BigDecimal.ZERO : distance;
		
		if(turnRate.intValue() == 0) {
			return BigDecimal.ZERO;
		}
		TmsTurnRate rule = getNewRule();
		if(rule != null) {
			turnRate = new BigDecimal(rule.getTurnRate());
		}else {
			turnRate = new BigDecimal(2);
			System.out.println("");
		}
		return turnRate;
		
	}
	
	public TmsTurnRate getNewRule() {
		return newTrunRateRules.stream()

				.filter(r -> contailDistanceRule(r, distance))
				.filter(r -> containsTimeRule(r, hour))
				.filter(r -> containsPointRule(r, point)).findFirst().orElse(null);
	}
	
	public BigDecimal getNewSettleCost() {
		return getNewTurnRate().multiply(contractPrice).add(fuelCost()).add(tollCost).setScale(0, BigDecimal.ROUND_DOWN);
	}
	
	
	
	public BigDecimal fuelCost() {
		return carFuel().multiply(kmPerLiter).add(freezingFuel().multiply(kmPerLiter)).setScale(2, BigDecimal.ROUND_HALF_UP);
	}
	
	public BigDecimal carFuel() {
		return carOilQty.multiply(distance);
	}
	
	public BigDecimal freezingFuel() {
		return frezingOilQty.multiply(distance);
	}

	private boolean containsPointRule(TmsTurnRate rule, BigDecimal point) {
		int start = rule.getDlvyPointRules()[0];
		int end = rule.getDlvyPointRules()[1];
		
		int _start = Integer.min(start, end);
		int _end = Integer.max(start, end);
		return _contains(_start, _end, point.intValue());
	}
	private boolean containsTimeRule(TmsTurnRate rule, BigDecimal hour) {
		int start = rule.getHourRules()[0];
		int end = rule.getHourRules()[1];
		return _contains(start, end, hour.intValue());
	}
	private boolean contailDistanceRule(TmsTurnRate rule, BigDecimal cur) {
		int start = rule.getDistanceRules()[0];
		int end = rule.getDistanceRules()[1];
		return _contains(start, end, cur.intValue());
	}
	
	private boolean _contains(int start, int end, int src) {

		return start <= src && end > src;
	}
}
