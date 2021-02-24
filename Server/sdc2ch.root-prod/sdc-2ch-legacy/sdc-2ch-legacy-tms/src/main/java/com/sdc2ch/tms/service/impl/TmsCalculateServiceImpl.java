package com.sdc2ch.tms.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.sdc2ch.tms.config.TmsQueryBuilder;
import com.sdc2ch.tms.domain.dto.TmsCalculateDto;
import com.sdc2ch.tms.io.TmsCalculateIO;
import com.sdc2ch.tms.service.ITmsCalculateService;

@Service
public class TmsCalculateServiceImpl implements ITmsCalculateService {

	@Autowired TmsQueryBuilder builder;

	@Override
	public List<TmsCalculateIO> search(String fromDe, String toDe, Predicate<TmsCalculateIO> predicate) {
		return search(fromDe, toDe, null, predicate);
	}

	@Override
	public List<TmsCalculateIO> search(String fromDe, String toDe, String fcrtyCd, Predicate<TmsCalculateIO> predicate) {
		
		fromDe = fromDe.replaceAll("-", "");
		toDe = toDe.replaceAll("-", "");
		if(StringUtils.isEmpty(fcrtyCd))
			fcrtyCd = "";
		
		List<Object[]> results = builder.storedProcedureResultCall(ITmsCalculateService.storedProcedureName, fcrtyCd, fromDe, toDe);
		return results == null ? Collections.emptyList() : results.stream().map(r -> convert(r)).filter(c -> {
			
			return predicate.test(c);
			
		}).collect(Collectors.toList());
	}

	private TmsCalculateIO convert(Object[] r) {
		
		List<String> toStrings = new ArrayList<>();
		for(Object o : r) {
			if(o == null) {
				o = "";
			}else if("null".equals(o)) {
				o = "";
			}else {
				o = String.valueOf(o);
			}
			toStrings.add(o + "");
		}
		
		return TmsCalculateDto.builder()
				.dlvyDe(toStrings.get(0))
				.factryCd(convertFactory(toStrings.get(1)))
				.ton(nullSafe(toStrings.get(2).replaceAll("톤", "")))
				.routeNo(toStrings.get(3))
				.dest(toStrings.get(4))
				.vrn(toStrings.get(5) + toStrings.get(6))
				.routeType(toStrings.get(7))
				.startFctryCd(toStrings.get(8))
				.endFctryCd(toStrings.get(9))
				.turnRate(nullSafe(toStrings.get(10)))
				.corverQty(nullSafe(toStrings.get(21)))
				.weight(nullSafe(toStrings.get(22)))
				.shipPayment(nullSafe(toStrings.get(30)))
				.oilPayment(nullSafe(toStrings.get(31)))
				.supportOil(nullSafe(toStrings.get(32)))
				.supportFrezOil(nullSafe(toStrings.get(33)))
				.tmsTollCost(nullSafe(toStrings.get(34)))
				.tmsDistance(nullSafe(toStrings.get(36)))
				.totalShipCost(nullSafe(toStrings.get(35)))
				.oilCost(nullSafe(toStrings.get(38)))
				.desc(toStrings.get(40))
				.build();
	}
	
	private BigDecimal nullSafe(String str) {
		return StringUtils.isEmpty(str) ? BigDecimal.ZERO : new BigDecimal(str);
	}
	
	private String convertFactory(String str) {
		String fctryCd = "";
		if("양주".equals(str)) {
			fctryCd = "1D1";
		}else if("용인".equals(str)) {
			fctryCd = "2D1";
		}else if("안산".equals(str)) {
			fctryCd = "3D1";
		}else if("거창".equals(str)) {
			fctryCd = "4D1";
		}else if("양주신공장".equals(str)) {
			fctryCd = "5D1";
		}
		return fctryCd;
	}
	
}
