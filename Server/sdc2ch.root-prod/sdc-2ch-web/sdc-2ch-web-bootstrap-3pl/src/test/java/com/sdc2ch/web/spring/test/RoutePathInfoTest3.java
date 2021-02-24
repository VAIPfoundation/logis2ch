package com.sdc2ch.web.spring.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdc2ch.service.admin.RoutePathInfoService;
import com.sdc2ch.tms.io.TmsPlanIO;
import com.sdc2ch.tms.service.ITmsPlanService;
import com.sdc2ch.web.admin.repo.domain.T_ROUTE_PATH_MATRIX;
import com.sdc2ch.web.admin.repo.domain.T_ROUTE_PATH_MATRIX2;
import com.sdc2ch.web.bootstrap.Seoulmilk2chApplication;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Seoulmilk2chApplication.class)
public class RoutePathInfoTest3 {

	@Autowired
	RoutePathInfoService rpSvc;

	@Autowired
	ITmsPlanService tmsPlanSvc;

	private int inc;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	private HashMap<String, Object> paramMap;
	
	private String fileName = "";

	@Getter
	@Builder
	private static class Point {
		private double lat;
		private double lng;
		private String name;

	}
	@Getter
	@Builder
	private static class RouteInfo {
		private String dlvyDe;
		private String routeNo;
		private String vrn;
		private String orgStr;
		
		private int tmsTollCost;
		private int tmsDistance;
		
		public String getUniqueKey() {
			return dlvyDe + routeNo;
		}
		
	}

	static AtomicInteger inc2 = new AtomicInteger();

	@Test
	public void whenValidName_thenEmployeeShouldBeFound() throws IOException {
		
		createParamMap();
		
		Stream.of("17년도_HY노선_물류비_기초데이터.csv").forEach(s -> {
			
			boolean isTransprot = false;
			
			fileName = s;
			List<RouteInfo> infos = fileRead(s);
			List<T_ROUTE_PATH_MATRIX2> results2 = rpSvc.findMatirx2All();
			List<String> ids = results2.stream().map(m -> m.getDlvyDe() + m.getRouteNo()).collect(Collectors.toList());
			infos.removeIf(p -> ids.contains(p.getUniqueKey()));

			Point point1 = Point.builder().lat(37.095332).lng(127.216006).name("거점1").build();
			Point point2 = Point.builder().lat(37.348031).lng(127.193782).name("거점2").build();

			List<Point> points = Stream.of(point1, point2).collect(Collectors.toList());
			infos.forEach(r -> {
				
				points.forEach(p -> {
					try {
						execute(r, p, isTransprot);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
				
			});
		});
	}

	private void execute(RouteInfo r, Point p, boolean transport) throws JsonProcessingException {
		List<String> centerCd = Arrays.asList("1D1", "2D1", "3D1", "4D1", "5D1");
		List<TmsPlanIO> tmsplans = tmsPlanSvc.findTmPlansByIds(r.getDlvyDe(), r.getRouteNo());

		if(tmsplans != null && !tmsplans.isEmpty()) {

			
			TmsPlanIO plan = tmsplans.get(0);
			
			if(centerCd.contains(plan.getStopCd())) {
				paramMap.put("startX", p.getLng() + "");
				paramMap.put("startY", p.getLat() + "");
				paramMap.put("startName", p.getName());
			}else {
				
				paramMap.put("startName", plan.getDlvyLoNm());
				paramMap.put("startY", scale(plan.getLat()));
				paramMap.put("startX", scale(plan.getLng()));
				
			}
			
			plan = tmsplans.get(tmsplans.size() -1);
			
			if(transport && !centerCd.contains(plan.getStopCd())) {
				paramMap.put("endName", plan.getDlvyLoNm());
				paramMap.put("endY", scale(plan.getLat()));
				paramMap.put("endX", scale(plan.getLng()));
			}else {
				paramMap.put("endName", p.getName());
				paramMap.put("endY", p.getLat() + "");
				paramMap.put("endX", p.getLng() + "");
			}
			
			
			tmsplans.remove(0);
			tmsplans.remove(tmsplans.size() -1);
			if(!tmsplans.isEmpty()) {
				paramMap.put("viaPoints", tmsplans.stream().map(t -> {
					Map<String, String> viaMap = new HashMap<>();
					viaMap.put("viaPointId", t.getStopCd());
					viaMap.put("viaPointName", t.getDlvyLoNm());
					viaMap.put("viaY", t.getLat());
					viaMap.put("viaX", t.getLng());
					return viaMap;
				}).collect(Collectors.toList()));
				;
			}else {
				
				Map<String, String> viaMap = new HashMap<>();
				viaMap.put("viaPointId", plan.getStopCd());
				viaMap.put("viaPointName", plan.getDlvyLoNm());
				viaMap.put("viaY", plan.getLat());
				viaMap.put("viaX", plan.getLng());
				paramMap.put("viaPoints", Arrays.asList(viaMap));
			}
			
			retry(p, r, paramMap);
			
		}
	}

	private String scale(String lat) {
		if(StringUtils.isEmpty(lat)) {
			return "0";
		}
		return new BigDecimal(lat).setScale(6, BigDecimal.ROUND_HALF_DOWN).toEngineeringString();
	}

	private T_ROUTE_PATH_MATRIX retry(Point point1, RouteInfo r, HashMap<String, Object> paramMap) {
		try {



			String jsonParam = mapper.writeValueAsString(paramMap);
			if (inc < 3) {
				Map<String, Object> mapped = send(jsonParam);
				
				if (mapped != null) {

					T_ROUTE_PATH_MATRIX2 matrix = new T_ROUTE_PATH_MATRIX2();

					Map<String, Object> properties = (Map<String, Object>) mapped.get("properties");
					
					String distance = new BigDecimal(properties.get("totalDistance") + "").divide(new BigDecimal(1000), 0, BigDecimal.ROUND_HALF_UP).toString();
					matrix.setEndPos(paramMap.get("endName") + "");
					matrix.setEndLat(paramMap.get("endY") + "");
					matrix.setEndLng(paramMap.get("endX") + "");
					matrix.setJsonData(mapper.writeValueAsString(mapped));
					matrix.setStartPos(paramMap.get("startName") + "");
					matrix.setStartLat(paramMap.get("startY") + "");
					matrix.setStartLng(paramMap.get("startX") + "");
					matrix.setNewTollCost(Integer.valueOf(properties.get("totalFare") +""));
					matrix.setNewDistance(Integer.valueOf(distance));
					matrix.setTmsTollCost(r.getTmsTollCost());
					matrix.setTmsDistance(r.getTmsDistance());
					
					matrix.setDlvyDe(r.getDlvyDe());
					matrix.setRouteNo(r.getRouteNo());
					matrix.setVrn(r.getVrn());

					String json = String.join(",", r.getOrgStr(), properties.get("totalFare") + "", distance + "") + "\r\n";
					try {
						rpSvc.save(matrix);
						FileUtils.write(new File("new_" + point1.getName() + "_" + fileName), json, Charset.forName("EUC-KR"), true);
					}catch (Exception e) {
						log.error("{}", e);
					}
					log.info("procdssing ... {}", inc2.incrementAndGet());
				}
			}

			inc = 0;
		} catch (Exception e) {
			inc++;
			retry(point1, r, paramMap);
			try {
				Thread.sleep(1000 );
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

			if (ins != null) {
				StringBuffer sb = new StringBuffer();

				BufferedReader reader = new BufferedReader(new InputStreamReader(ins, "UTF-8"));

				String s;

				while ((s = reader.readLine()) != null) {
					sb.append(s);
				}
				reader.close();
				HashMap mapped = mapper.readValue(sb.toString().getBytes("UTF-8"), HashMap.class);
				return mapped;
			}

		} finally {
			if (conn != null)
				conn.disconnect();
			if (ins != null)
				ins.close();
		}
		return null;
	}

	private List<RouteInfo> fileRead(String path) {
		ClassPathResource res = new ClassPathResource(path);
		try (BufferedReader stream = Files.newBufferedReader(Paths.get(res.getURI()), Charset.forName("UTF-8"))) {
			return stream.lines().skip(1).filter(s -> {
				System.out.println(s);
				if(s.isEmpty()) {
					return true;
				}
				
				return !s.contains("물류");
			}).map(s -> convert(s)).collect(Collectors.toList());

		} catch  (Exception e) {
			log.error("{}", e);
		}
		return null;

	}

	private RouteInfo convert(String s) {
		List<String> strs = Arrays.asList(s.split(","));
		return RouteInfo.builder()
				.dlvyDe(strs.get(0))
				.routeNo(strs.get(3)).orgStr(s).vrn(strs.get(5) + strs.get(6))
				.tmsDistance(Integer.valueOf(strs.get(36)))
				.tmsTollCost(Integer.valueOf(strs.get(34)))
				.build();
	}
	
	private void createParamMap() throws IOException{
		ClassPathResource res = new ClassPathResource("TmapApiJson.json");
		paramMap = mapper.readValue(res.getURL(), HashMap.class);
	}
	public static void main(String[] args) {


		String str = "일자,공장,차종,노선,방면,차량번호1,차량번호2,노선유형,출발공장,도착공장,회전율,우유(환),우유(중),유음료(환),유음료(중),발효유(환),발효유(중),가공품(환),가공품(중),기타(환),기타(중),환산량,중량,공차여부,법정중량,적정중량,최대중량,법정수량,적정수량,최대수량,운행금액,유류금액,차량지원유류,냉동기지원유류,고속도로비,총비용,거리,주유소,유류단가,운수회사,변경사유";
		
		int idx = 0;
		for(String s : str.split(",")) {
			System.out.println(s +  " = " + idx++);
			
		}
		
	}
}
