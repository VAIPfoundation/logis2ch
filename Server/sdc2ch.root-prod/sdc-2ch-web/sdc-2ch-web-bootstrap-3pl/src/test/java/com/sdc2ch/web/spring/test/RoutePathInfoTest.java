package com.sdc2ch.web.spring.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdc2ch.service.admin.RoutePathInfoService;
import com.sdc2ch.web.admin.repo.domain.T_ROUTE_PATH_INFO;
import com.sdc2ch.web.admin.repo.domain.T_ROUTE_PATH_MATRIX;
import com.sdc2ch.web.bootstrap.Seoulmilk2chApplication;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Seoulmilk2chApplication.class)
public class RoutePathInfoTest {

	@Autowired
	RoutePathInfoService rpSvc;
	
	private int inc;
	
	@Getter
	@Builder
	private static class Point {
		private double lat;
		private double lng;
		private String name;
		
	}
	
	static AtomicInteger inc2 = new AtomicInteger();

	@SuppressWarnings("unused")
	@Test
	public void whenValidName_thenEmployeeShouldBeFound() {
		List<T_ROUTE_PATH_INFO> results = rpSvc.findAll();
		List<T_ROUTE_PATH_MATRIX> results2 = rpSvc.findMatirxAll();
		List<Long> ids = results2.stream().map(m -> m.getId()).collect(Collectors.toList());
		results.removeIf(p -> ids.contains(p.getId().longValue()));
		
		Point point1 = Point.builder().lat(37.095332).lng(127.216006).name("거점1").build();
		Point point2 = Point.builder().lat(37.348031).lng(127.193782).name("거점2").build();
		
		Stream.of(point1, point2).forEach(p -> {
			results.stream().map(r -> {
				for(int i = 0 ; i< 3 ; i++) {
					return retry(p, r);
				}
				return null;
			}).collect(Collectors.toList());
		});
		

	}
	
	private T_ROUTE_PATH_MATRIX retry(Point point1, T_ROUTE_PATH_INFO r) {
		try {
			ObjectMapper mapper = new ObjectMapper();


			Map<String, String> parameterMap = mapper.readValue(r.getPathInfo().getBytes(), HashMap.class);
			
			String startX = parameterMap.get("startX");
			String startY = parameterMap.get("startY");
			
			log.info("org -> {},{}", startX, startY);
			
			parameterMap.put("startX", point1.getLng() + "");
			parameterMap.put("startY", point1.getLat() + "");
			
			String contents = mapper.writeValueAsString(parameterMap);
			
			log.info("new -> {},{}", startX, startY);
			if(inc < 3) {
				Map<String, Object> mapped = send(contents);
				if(mapped != null) {
					
					T_ROUTE_PATH_MATRIX matrix = new T_ROUTE_PATH_MATRIX();
					
					Map<String, Object> properties = (Map<String, Object>) mapped.get("properties");
					matrix.setEndLat(point1.getLat() + "");
					matrix.setEndLng(point1.getLng() + "");
					matrix.setEndPos(point1.getName());
					matrix.setFrequency(r.getFrequency());
					matrix.setRoutePathInfoFk(r.getId());
					matrix.setJsonData(mapper.writeValueAsString(mapped));
					matrix.setStartLat(point1.getLat() + "");
					matrix.setStartLng(point1.getLng() + "");
					matrix.setStartPos(point1.getName());
					matrix.setNewTollCost(properties.get("totalFare") + "");
					matrix.setNewTotDistance(properties.get("totalDistance") + "");
					
					LatLng latlng = LatLng.convert(r.getRouteNo().substring(0, 1));
					parameterMap.put("startX", latlng.lng + "");
					parameterMap.put("startY", latlng.lat + "");
					contents = mapper.writeValueAsString(parameterMap);
					
					mapped = send(contents);
					
					matrix.setOrgTollCost(properties.get("totalFare") + "");
					matrix.setOrgTotDistance(properties.get("totalDistance") + "");
					

					
					log.info("procdssing ... {}", inc2.incrementAndGet());
				}
			}
			
			inc = 0;
		}catch (Exception e) {
			inc++;
			retry(point1, r);
			try {
				Thread.sleep(1000*60);
			} catch (InterruptedException e1) {
				
				e1.printStackTrace();
			}
			log.error("{}", e);
			
		}
		return null;
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	private static Map<String, Object> send(String contents) throws IOException {
		HttpURLConnection conn = null;
		InputStream ins = null;
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			URL url = new URL("https:
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(1000);
			conn.setDoOutput(true);
			conn.setReadTimeout(10000);
			conn.setRequestMethod("POST");
			conn.addRequestProperty("Content-Type", "application/json");

			conn.addRequestProperty("appKey", "ea8ff587-6ade-4287-88af-7ad7ac2d60aa");	
			
			OutputStream outputStream = conn.getOutputStream();
			outputStream.write(contents.getBytes("UTF-8"));
			outputStream.flush();
			outputStream.close();
			
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				log.warn("{}", conn.getContent());
			}
			
			ins = conn.getInputStream();
			
			if(ins != null) {
				StringBuffer sb = new StringBuffer();
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(ins, "UTF-8"));
				
				String s;
				
				while((s = reader.readLine()) != null) {
					sb.append(s);
				}
				
				reader.close();
				HashMap mapped = mapper.readValue(sb.toString().getBytes("UTF-8"), HashMap.class);
				
				return mapped;
			}
			
		}finally {
			if(conn != null)
				conn.disconnect();
			if(ins != null)
				ins.close();
		}
		return null;
	}
	
	
	private enum LatLng {
		D1(37.820556, 127.059763),
		D2(37.296220, 127.111362),
		D3(37.322633, 127.757383),
		D4(37.670671, 127.926573)
		;
		public double lat,lng;
		LatLng(double lat, double lng) {
			this.lat = lat;
			this.lng = lng;
		}
		
		
		public static LatLng convert(String cd) {
			return Stream.of(LatLng.values()).filter(l -> l.name().equals("D" + cd)).findFirst().get();
		}
		
	}
}
