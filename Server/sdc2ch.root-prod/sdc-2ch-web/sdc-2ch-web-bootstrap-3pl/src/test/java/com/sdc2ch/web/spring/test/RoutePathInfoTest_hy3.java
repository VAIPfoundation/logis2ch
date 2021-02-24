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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdc2ch.service.admin.RoutePathInfoService;
import com.sdc2ch.tms.io.TmsPlanIO;
import com.sdc2ch.tms.service.ITmsPlanService;
import com.sdc2ch.web.admin.repo.domain.T_ROUTE_PATH_MATRIX;
import com.sdc2ch.web.admin.repo.domain.T_ROUTE_PATH_MATRIX3;
import com.sdc2ch.web.bootstrap.Seoulmilk2chApplication;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Seoulmilk2chApplication.class)
public class RoutePathInfoTest_hy3 {

	@Autowired
	RoutePathInfoService rpSvc;

	@Autowired
	ITmsPlanService tmsPlanSvc;
	
	private Point hyPoint = getHyPoint();

	private int inc;
	
	private ObjectMapper mapper = new ObjectMapper();
	

	
	private String fileName = "";

	@Getter
	@Builder
	private static class Point {
		private double lat;
		private double lng;
		@Setter
		private String name;
		@Setter
		@Getter
		private String date;
		@Setter
		@Getter
		private String carType;
	}
	
	@Getter
	@Builder
	@ToString
	public static class OdMatrix {
		private String stLcCd;
		private String edLcCd;
		private String factryCd;
	}
	
	
	@Getter
	@Builder
	@ToString
	private static class RouteInfo {
		private String dlvyDe;
		private String routeNo;
		private String vrn;
		@Setter
		private String orgStr;
		
		private int tmsTollCost;
		private int tmsDistance;
		private String turnRate;
		
		public String getUniqueKey() {
			return dlvyDe + routeNo;
		}
		
	}

	static AtomicInteger inc2 = new AtomicInteger();
	
	private static HashMap<String, T_ROUTE_PATH_MATRIX3> routeMap = new HashMap<>();

	@Test
	public void whenValidName_thenEmployeeShouldBeFound() throws IOException {
		

		
		Stream.of("17년도_HY노선_물류비_기초데이터_hy.csv").forEach(s -> {
			
			boolean isTransprot = "17년도_HY노선_물류비_기초데이터_hy.csv".equals(s);
			
			fileName = s;
			List<RouteInfo> infos = fileRead(s);

			List<T_ROUTE_PATH_MATRIX3> results2 = rpSvc.findMatirx3All();
			List<String> ids = results2.stream().map(m -> m.getDlvyDe() + m.getRouteNo()).collect(Collectors.toList());
			infos.removeIf(p -> ids.contains(p.getUniqueKey()));

			Point point1 = Point.builder().lat(37.095332).lng(127.216006).name("거점1").build();
			Point point2 = Point.builder().lat(37.348031).lng(127.193782).name("거점2").build();

			List<Point> points = Stream.of(point1, point2).collect(Collectors.toList());

			
			infos.forEach(System.out::println);
			
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

	private Point getHyPoint() {
		return Point.builder().lat(37.097379).lng(127.549978).name("HY냉장").build();
	}

	private void execute(RouteInfo r, Point p, boolean transport) throws IOException {
 		List<String> centerCd = Arrays.asList("1D1", "2D1", "3D1", "4D1", "5D1");
		List<TmsPlanIO> tmsplans = tmsPlanSvc.findTmPlansByIds(r.getDlvyDe(), r.getRouteNo());

		if(tmsplans != null && !tmsplans.isEmpty()) {
			
			int orgSize = tmsplans.size();
			TmsPlanIO first = tmsplans.remove(0);
			TmsPlanIO last = tmsplans.remove(tmsplans.size() -1);
			
			List<OdMatrix> matrixs = new ArrayList<>();
			if(!tmsplans.isEmpty()) {
				
				for(int i = 1 ; i <= (tmsplans.size()-1) ; i++) {
					TmsPlanIO start = tmsplans.get(i-1);
					TmsPlanIO end = tmsplans.get(i);
					matrixs.add(OdMatrix.builder().edLcCd(end.getStopCd()) .stLcCd(start.getStopCd()).factryCd(start.getFctryCd()).build())
					;
				}
			}
			
			if(matrixs.isEmpty()) {
				
				String dlvyde = first.getScheDlvyStDe();
				String dlvytm = first.getScheDlvyStTime();
				String utcFmtDate = null;
				SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHH:mm:ss");
				
				SimpleDateFormat fmt2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				fmt.setTimeZone(TimeZone.getTimeZone("UTC"));
				
				if(!StringUtils.isEmpty(dlvytm)) {
					try {
						LocalTime lt = LocalTime.parse(dlvytm);
						Date date = fmt.parse(dlvyde + "" + lt.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
						utcFmtDate = fmt2.format(date) + "+0900";
					}catch (Exception e) {
						dlvytm = dlvytm.replaceAll(";", ":");
						try {
							LocalTime lt = LocalTime.parse(dlvytm);
							Date date = fmt.parse(dlvyde + "" + lt.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
							utcFmtDate = fmt2.format(date) + "+0900";
						}catch (Exception e1) {
							LocalTime lt = LocalTime.now();
							Date date;
							try {
								date = fmt.parse(dlvyde + "" + lt.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
								utcFmtDate = fmt2.format(date) + "+0900";
							} catch (ParseException e2) {
								utcFmtDate = fmt2.format(new Date()) + "+0900";
							}
						}
					}
				}
				p.setDate("2017-01-02T17:00:00+0900");
				
				if(tmsplans.isEmpty()) {
					
					boolean isFactory = centerCd.contains(first.getStopCd());
					
					Point start = null;
					Point end = null;
					if(isFactory) {
						start = Point.builder()
								.lat(new BigDecimal(scale(first.getLat())).doubleValue())
								.lng(new BigDecimal(scale(first.getLng())).doubleValue()).name(first.getStopCd()).build();
					}else {
						if(first.getStopCd().contains("HY냉장")) {
							start = p;

						}else {
							
							double x = 0 , y = 0;
							
							if(first.getLat() == null) {
								if(first.getStopCd().contains("47608")) {
									x = 127.077862;
									y = 36.954020;
								}else if(first.getStopCd().contains("44783")) {
									
									x = 127.216254;
									y = 36.324742;
									
								}
								start = Point.builder()
										.lat(y)
										.lng(x).name(last.getDlvyLoNm()).build();
							}else {
								start = Point.builder()
										.lat(new BigDecimal(scale(first.getLat())).doubleValue())
										.lng(new BigDecimal(scale(first.getLng())).doubleValue()).name(first.getDlvyLoNm()).build();
							}
							
							
						}
						
					}
					
					start.setDate("2017-01-02T17:00:00+0900");
					BigDecimal big = BigDecimal.ZERO;
					
					if(!StringUtils.isEmpty(first.getCarWegit())) {
						try {
							big = new BigDecimal(first.getCarWegit());
						}catch (Exception e) {
							big = BigDecimal.ZERO;
						}
					}
					String carty = findCarType(big);
					start.setCarType(carty);
					
					isFactory = centerCd.contains(last.getStopCd());
					
					if(isFactory) {
						end = Point.builder()
								.lat(new BigDecimal(scale(last.getLat())).doubleValue())
								.lng(new BigDecimal(scale(last.getLng())).doubleValue()).name(last.getStopCd()).build();
					}else {
						
						if(last.getStopCd().contains("HY냉장")) {
							end = p;
						}else {
							
							double x = 0 ,y = 0;
							
							if(last.getLat() == null) {
								
								if(last.getStopCd().contains("47608")) {
									
									x = 127.077862;
									y = 36.954020;
									
								}else if(last.getStopCd().contains("44783")) {
									
									x = 127.216254;
									y = 36.324742;
									
								}
								end = Point.builder()
										.lat(y)
										.lng(x).name(last.getDlvyLoNm()).build();
							}else {
								
								end = Point.builder()
										.lat(new BigDecimal(scale(last.getLat())).doubleValue())
										.lng(new BigDecimal(scale(last.getLng())).doubleValue()).name(last.getStopCd()).build();
							}
						}
					}
					end.setDate("2017-01-02T17:00:00+0900");
					end.setCarType(carty);
					String jsonParam = makeparan(start, end, null);
					String key = start.getCarType() + start.getName() + end.getName();
					
					System.out.println(jsonParam + " -> " + key);







					
					if(routeMap.get(key) == null) {
						Map<String, Object> mapped = retry(jsonParam, "https:
						T_ROUTE_PATH_MATRIX3 matrix3 = execute(start, end, r, mapped);
						insertMatrix(p, matrix3, r.getOrgStr());
						routeMap.put(key, matrix3);
					}else {
						T_ROUTE_PATH_MATRIX3 matrix3 = routeMap.get(key);
						matrix3.setId(null);
						BigDecimal turnRate = BigDecimal.ZERO;
						if(!StringUtils.isEmpty(r.getTurnRate())) {
							turnRate = new BigDecimal(r.getTurnRate());
						}
						matrix3.setTurnRate(turnRate.doubleValue());
						matrix3.setDlvyDe(r.getDlvyDe());
						matrix3.setRouteNo(r.getRouteNo());
						matrix3.setVrn(r.getVrn());
						insertMatrix(p, matrix3, String.join(",", r.getOrgStr(), matrix3.getNewTollCost() + "", matrix3.getNewDistance() + ""));
					}
				}else {
					TmsPlanIO cur = tmsplans.get(0);
					Point point1 = Point.builder()
							.lat(new BigDecimal(scale(cur.getLat())).doubleValue())
							.lng(new BigDecimal(scale(cur.getLng())).doubleValue()).build();
					String jsonParam = makeparan(p, p, Arrays.asList(point1));
					
					System.out.println(jsonParam);



				}

			}else {









				
				
			}
			

			
		}
	}

	private String findCarType(BigDecimal big) {
		
		int carTon = big.intValue();
		String carType = null;
		if(carTon <= 8) {
			carType = "largevan";
		}else if(carTon <= 11) {
			carType = "largetruck";
		}else if(carTon <= 20) {
			carType = "specialtruck";
		}else {
			carType = "car";
		}
		return carType;
	}

	private String scale(String lat) {
		if(StringUtils.isEmpty(lat)) {
			return "0";
		}
		return new BigDecimal(lat).setScale(6, BigDecimal.ROUND_HALF_DOWN).toEngineeringString();
	}

	private T_ROUTE_PATH_MATRIX3 execute(Point start, Point end, RouteInfo r, Map<String, Object> mapped ) {
		try {
			
			T_ROUTE_PATH_MATRIX3 matrix = new T_ROUTE_PATH_MATRIX3();
			String json = r.getOrgStr();
			if(mapped != null) {
				List<Map<String, Object>> features = (List<Map<String, Object>>) mapped.get("features");
				
				if(features != null && !features.isEmpty()) {
					
					Map<String, Object> featur = features.get(0);
					Map<String, Object> properties = (Map<String, Object>) featur.get("properties");
					String distance = new BigDecimal(properties.get("totalDistance") + "").divide(new BigDecimal(1000), 0, BigDecimal.ROUND_HALF_UP).toString();
					matrix.setEndPos(end.getName());
					matrix.setEndLat(end.getLat() + "");
					matrix.setEndLng(end.getLng() + "");
					matrix.setJsonData(mapper.writeValueAsString(mapped));
					matrix.setStartPos(start.getName());
					matrix.setStartLat(start.getLat() + "");
					matrix.setStartLng(start.getLng() + "");
					matrix.setNewTollCost(Integer.valueOf(properties.get("totalFare") +""));
					matrix.setNewDistance(Integer.valueOf(distance));
					matrix.setTmsTollCost(r.getTmsTollCost());
					matrix.setTmsDistance(r.getTmsDistance());
					
					BigDecimal turnRate = BigDecimal.ZERO;
					if(!StringUtils.isEmpty(r.getTurnRate())) {
						turnRate = new BigDecimal(r.getTurnRate());
					}
					matrix.setTurnRate(turnRate.doubleValue());
					matrix.setDlvyDe(r.getDlvyDe());
					matrix.setRouteNo(r.getRouteNo());
					matrix.setVrn(r.getVrn());

					json = String.join(",", json, matrix.getNewTollCost() + "", matrix.getNewDistance() + "");
					r.setOrgStr(json);
					return matrix;
				}

			}

		} catch (Exception e) {
			log.error("{}", e);

		}
		return null;
	}
	
	private void insertMatrix(Point p , T_ROUTE_PATH_MATRIX3 save, String json) throws IOException {
		try {
			rpSvc.save(save);
		}catch (Exception e) {
			log.error("{}", e);
		}finally { 
			FileUtils.write(new File("new_" + p.name + "_" +  fileName), json + "\r\n", Charset.forName("UTF-8"), true);
			log.info("procdssing ... {}", inc2.incrementAndGet());
		}
	}

	private Map<String, Object> retry(String jsonParam, String u) {
		
		for(int i = 3 ; i >= 0; i--) {
			try {
				Map<String, Object> mapped = send(jsonParam, u);
				return mapped;
			}catch (Exception e) {
				log.error("{}", e);
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private static Map<String, Object> send(String contents, String u) throws IOException {
		HttpURLConnection conn = null;
		InputStream ins = null;

		ObjectMapper mapper = new ObjectMapper();
		try {

			URL url = new URL(u);
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
				.turnRate(strs.get(10))
				.build();
	}
	




	public static void main(String[] args) {
		
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		fmt.setTimeZone(TimeZone.getTimeZone("UTC"));
		System.out.println(fmt.format(new Date()) + "+0900");
		
		
		LocalDateTime time = LocalDateTime.now(ZoneOffset.UTC);
		System.out.println(time);
		
		
		LocalTime lt = LocalTime.parse("23:28");
		
		System.out.println(lt.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
		
		
	}
	
	private String makeparan(Point start,  Point end, List<Point> ways) throws IOException {
		
		Map<String, Object> params = new HashMap<>();
		
		Map<String, Object> routesInfo = new HashMap<>();
		Map<String, Object> departure = new HashMap<>();
		Map<String, Object> destination = new HashMap<>();
		
		
		params.put("routesInfo", routesInfo);
		routesInfo.put("departure", departure);
		routesInfo.put("destination", destination);
		routesInfo.put("predictionType", "departure");
		routesInfo.put("predictionTime", start.getDate());
		routesInfo.put("searchOption", "00");
		routesInfo.put("tollgateCarType", StringUtils.isEmpty(start.getCarType()) ? "largevan" : start.getCarType());
		
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
			
			for(Point p : ways) {
				
				Map<String, Object> _wp = new HashMap<>();
				_wp.put("lon", p.getLng() + "");
				_wp.put("lat", p.getLat() + "");
				wayPoint.add(_wp);
			}
		}
		return mapper.writeValueAsString(params);
		
	}

}
