package com.sdc2ch.service.factory;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdc2ch.service.model.NaviPoint;
import com.sdc2ch.service.model.OdMatrixInfoVo;
import com.sdc2ch.service.model.TmapApiParam;
import com.sdc2ch.service.util.HttpClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TmapNavigationApiFactory {
	
	private HttpClient client;
	private String timeZone = "2017-01-01T23:00:00" + "+0900";
	private ObjectMapper mapper = new ObjectMapper();
	private int retryCount = 3;
	
	public TmapNavigationApiFactory(HttpClient client) {
		this.client = client;
	}

	public String createQuery(TmapApiParam param) throws IOException {
		return makeparam(param.getCarTon(), param.getStart(), param.getEnd(), param.getPaths().isEmpty() ? null : param.getPaths());
	}
	
	public OdMatrixInfoVo execute(String contents) throws IOException{
		Map<String, Object> results = retry(contents);
		return parseData(results);
	}
	
	public OdMatrixInfoVo parseData(Map<String, Object> results) throws IOException {
		
		if(results != null) {
			OdMatrixInfoVo vo = new OdMatrixInfoVo();
			Map<String, Object> properties = findProperties(results);
			vo.setTotalDistance(findDistance(properties));
			vo.setTotalTollCost(findTotalFare(properties));
			vo.setTotalTime(findTotalTime(properties));
			vo.setNavigationJson(mapper.writeValueAsString(results));
			return vo;
		}
		
		return null;
	}
	
	

	
	
	private Map<String, Object> retry(String jsonParam) {
		
		for(int i = retryCount ; i >= 0; i--) {
			try {
				Map<String, Object> mapped = client.send(jsonParam);
				return mapped;
			}catch (Exception e) {
				log.error("{}", e);
			}
		}
		return null;
	}
	private String makeparam(CarTon carTon, NaviPoint start,  NaviPoint end, List<NaviPoint> ways) throws IOException {
		
		Map<String, Object> params = new HashMap<>();
		
		Map<String, Object> routesInfo = new HashMap<>();
		Map<String, Object> departure = new HashMap<>();
		Map<String, Object> destination = new HashMap<>();
		
		
		params.put("routesInfo", routesInfo);
		routesInfo.put("departure", departure);
		routesInfo.put("destination", destination);
		routesInfo.put("predictionType", "departure");
		routesInfo.put("predictionTime", timeZone);
		routesInfo.put("searchOption", "00");
		routesInfo.put("tollgateCarType", carTon.name());
		
		departure.put("name", start.getName());
		departure.put("lon", start.getLng() + "");
		departure.put("lat", start.getLat() + "");
		
		destination.put("name", end.getName());
		destination.put("lon", end.getLng() + "");
		destination.put("lat", end.getLat() + "");
		
		if(ways != null) {
			Map<String, Object> wayPoints = new HashMap<>();
			List<Map<String, Object>> wayPoint = new ArrayList<>();
			wayPoints.put("wayPoint", wayPoint);
			routesInfo.put("wayPoints", wayPoints);
			
			for(NaviPoint p : ways) {
				
				Map<String, Object> _wp = new HashMap<>();
				_wp.put("lon", p.getLng() + "");
				_wp.put("lat", p.getLat() + "");
				wayPoint.add(_wp);
			}
		}
		return mapper.writeValueAsString(params);
		  
	}
	
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> findProperties(Map<String, Object> mapped){
		if(mapped != null) {
			List<Map<String, Object>> features = (List<Map<String, Object>>) mapped.get("features");
			if(features != null && !features.isEmpty()) {
				Map<String, Object> featur = features.get(0);
				Map<String, Object> properties = (Map<String, Object>) featur.get("properties");
				return properties;
			}
		}
		return null;
	}
	private int findDistance(Map<String, Object> properties){
		if(properties != null) {
			String distance = new BigDecimal(properties.get("totalDistance") + "").divide(new BigDecimal(1000), 0, BigDecimal.ROUND_HALF_UP).toString();
			return Integer.valueOf(distance);
		}
		return 0;
	}
	private int findTotalTime(Map<String, Object> properties) {
		if(properties != null) {
			return Integer.valueOf(properties.get("totalTime") + "");
		}
		return 0;
	}
	private int findTotalFare(Map<String, Object> properties){
		if(properties != null) {
			return Integer.valueOf(properties.get("totalFare") + "");
		}
		return 0;
	}
	
}
