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
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdc2ch.service.admin.RoutePathInfoService;
import com.sdc2ch.tms.io.TmsPlanIO;
import com.sdc2ch.tms.service.ITmsPlanService;
import com.sdc2ch.web.admin.repo.AdmQueryBuilder;
import com.sdc2ch.web.admin.repo.domain.T_ROUTE_PATH_MATRIX5;
import com.sdc2ch.web.bootstrap.Seoulmilk2chApplication;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Seoulmilk2chApplication.class)
public class RoutePathInfoTest_dlvy_tmap_api {

	@Autowired
	RoutePathInfoService rpSvc;

	@Autowired
	ITmsPlanService tmsPlanSvc;
	
	@Autowired
	AdmQueryBuilder builder;

	private int inc;
	
	
	private ObjectMapper mapper = new ObjectMapper();
	

	
	private String fileName = "";

	private static HashMap<String, T_ROUTE_PATH_MATRIX5> routeMap = new HashMap<>();
	
	@Getter
	@Setter
	@ToString
	private static class TmsPlan {
		private String lat;
		private String lng;
		private String dlvyDe;
		private String carType;
		private String stopCd;
		private String routeNo;
		private String vrn;
		private String fctryCd;
		private String dlvyLoNm;
		private String carWegit;
		private String bundledDlvyLc;
		private String confRtateRate;
		
		public String getKey() {
			return lat + lng;
		}
		
	}
	
	@Getter
	@Builder
	private static class Point {
		private double lat;
		private double lng;
		private String name;
		@Setter
		@Getter
		private String date;
		@Setter
		@Getter
		private String carType;
		@Override
		public String toString() {
			return "" + name + "";
		}
		
	}
	
	@Getter
	@Builder
	public static class OdMatrix {
		private String stLcCd;
		private String edLcCd;
		private String factryCd;
		private String groupCd;
		private TmsPlan start;
		private TmsPlan end;
		
		@Override
		public String toString() {
			return "OdMatrix [stLcCd=" + start.getDlvyLoNm() + ", edLcCd=" + end.getDlvyLoNm() + "]";
		}
		
		public String getKey() {
			return start.getLat() + start.getLng();
		}
	}
	
	
	@Getter
	@Builder
	private static class RouteInfo {
		private String dlvyDe;
		private String routeNo;
		private String vrn;
		@Setter
		private String orgStr;
		
		private int tmsTollCost;
		private int tmsDistance;
		@Setter
		private String confRtateRate;
		@Setter
		private int pathCnt;
		
		private String fctryCd;
		
		public String getUniqueKey() {
			return dlvyDe + routeNo;
		}
		
	}

	static AtomicInteger inc2 = new AtomicInteger();


	
	@Test
	public void whenValidName_thenEmployeeShouldBeFound() throws IOException {
		

		
		Stream.of("20180402_용인").forEach(s -> {
			
			fileName = s;
			List<RouteInfo> infos = fileRead(s);
			
			System.out.println("ORG -> " + infos.size());
			infos = infos.stream().filter(i -> !(i.getRouteNo().contains("9N") || i.getRouteNo().contains("9M")))
					.filter(i -> !(i.getFctryCd().equals("3D1") || i.getFctryCd().equals("4D1")))
					.filter(i -> !i.getFctryCd().equals("1D1"))
					.collect(Collectors.toList());	
			
			System.out.println("FILTER -> " + infos.size());
			List<T_ROUTE_PATH_MATRIX5> results2 = rpSvc.findMatirx5("희창");
			List<String> ids = results2.stream().map(m -> m.getDlvyDe() + m.getRouteNo()).collect(Collectors.toList());
			infos.removeIf(p -> ids.contains(p.getUniqueKey()));
			System.out.println("REMOVE -> " + infos.size());
			




			Point point2 = Point.builder().lat(37.330537).lng(127.146325).name("희창").build();

			infos.forEach(i -> {
				System.out.println(i.getDlvyDe() + " -> " + i.getRouteNo());
			});


			List<Point> points = Stream.of(point2).collect(Collectors.toList());
			
			infos.forEach(r -> {
				
				points.forEach(p -> {
					try {
						execute(r, p);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
				
			});
		});
	}

	private void execute(RouteInfo r, Point p) throws IOException {
		List<TmsPlan> tmsplans = findTmsPlan(r.getDlvyDe(), r.getRouteNo());
		
		if(tmsplans != null && !tmsplans.isEmpty()) {
			List<TmsPlan> newtmsplans = null;
			Point start = p;
			Point end = start;
			List<Point> paths = new ArrayList<>();
			
			int orgSize = tmsplans.size();
			TmsPlan first = tmsplans.remove(0);
			TmsPlan last = tmsplans.remove(tmsplans.size() -1);
			
			r.setConfRtateRate(first.getConfRtateRate());
			List<OdMatrix> matrixs = new ArrayList<>();
			if(!tmsplans.isEmpty()) {
				tmsplans.stream().filter(p1 -> (p1.getBundledDlvyLc() != null && !p1.getStopCd().equals(p1.getBundledDlvyLc()))).map(p1 -> {
					System.out.println(p1);
					TmsPlan parent = findStop(p1.getBundledDlvyLc(), tmsplans);
					if(parent != null) {
						p1.setLat(parent.getLat());
						p1.setLng(parent.getLng());
					}else {
						
						String stopCd = p1.getBundledDlvyLc();
						Point point = findLocation(stopCd);
						if(point != null) {
							p1.setLat(point.getLat() + "");
							p1.setLng(point.getLng() + "");
						}
					}
					return p1;
				}).count();
				
				newtmsplans = tmsplans.stream().filter(distinctByKey(TmsPlan::getKey)).collect(Collectors.toList());
				
				for(int i = 1 ; i <= (newtmsplans.size()-1) ; i++) {
					TmsPlan _start = newtmsplans.get(i-1);
					TmsPlan _end = newtmsplans.get(i);
					matrixs.add(OdMatrix.builder().start(_start).end(_end).edLcCd(_end.getStopCd()).stLcCd(_start.getStopCd()).factryCd(_start.getFctryCd()).build())
					;
				}
			}
			
			setQueryTime(p, first);
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
			end.setCarType(carty);
			
			if(matrixs.isEmpty()) {
				
				if(tmsplans.isEmpty()) {
					return ;
				}
				TmsPlan cur = tmsplans.get(0);
				paths = Arrays.asList(convert(cur));
			}else {
							
				if(matrixs.size() > 4) {
					
					System.out.println(orgSize + " -> " + matrixs.size());
					
					matrixs = matrixs.stream().filter(distinctByKey(OdMatrix::getKey)).collect(Collectors.toList());
					
					StringBuffer sb = new StringBuffer();
					for(int i = 0 ; i < matrixs.size() ; i++) {
						if(i == 0) {
							sb.append(p.getName());
						}
						sb.append(matrixs.get(i).getStLcCd());
						
						if(i == matrixs.size() -1 ) {
							sb.append(matrixs.get(i).getEdLcCd());
						}
					}
					
					
					System.out.println(sb.toString());
					
					if(routeMap.get(sb.toString()) == null) {
						insertPartitionRoute(sb.toString(), p, r, matrixs);
					}else {
						r.setPathCnt(matrixs.size() + 1);
						update(sb.toString(), r);
					}
					
					return ;
				}else {
					for(TmsPlan plan : newtmsplans) {
						paths.add(convert(plan));
					}
					
				}
			}
			
			if(!paths.isEmpty()) {
				saveOrUpdate(start, end, paths, r);
			}
		}else {
			
			System.out.println(r.getDlvyDe() + " " + r.getRouteNo());
		}
	}
	
	
	private void insertPartitionRoute(final String key, final Point p, RouteInfo info, List<OdMatrix> matrixs) throws IOException {
		
		Point start = p;
		Point end = start;
		int distance = 0;
		int fare = 0;
		int pathCount = 0;
		int totalTime = 0;
		
		List<List<OdMatrix>> partitions = partitionBy(matrixs);
		for(int i = 0 ; i < partitions.size() ; i++) {
			List<OdMatrix> _list = partitions.get(i);
			List<Point> paths = new ArrayList<>();
			if(i == 0) {
				start = p;
			}
			for(int k = 0 ; k < _list.size() ; k++) {
				
				if(i != 0 && k == 0) {
					start = convert(_list.get(k).getStart());
				}
				
				if(i == partitions.size() -1) {
					paths.add(convert(_list.get(k).getEnd()));
				}else {
					paths.add(convert(_list.get(k).getStart()));
				}
				
				if(k == _list.size() -1) {
					end = convert(_list.get(k).getEnd());
				}
			}
			if(i == partitions.size() -1) {
				
				end = p;
				
			}
			setQueryTime(start, null);
			setQueryTime(end, null);
			System.out.println(start + " -> " + paths + " -> " + end);
			
			Map<String, Object> result = callTmapApi(start, end, paths);
			Map<String, Object> properties = findProperties(result);
			     
			distance += findDistance(properties);
			fare += findTotalFare(properties);
			totalTime += findTotalTime(properties);
			pathCount += paths.size();
		}
		
		if(distance != 0) {
			info.setPathCnt(pathCount);
			T_ROUTE_PATH_MATRIX5 matrix3 = createMarix(start, end, info, distance, fare, totalTime, null);
			insertMatrix(matrix3, info.getOrgStr());
			routeMap.put(key, matrix3);
		}
	}

	private void saveOrUpdate(Point start, Point end, List<Point> paths, RouteInfo info) throws IOException {
		
		String key = start.getName()+"_"+ paths.stream().map(Point::getName).collect(Collectors.joining(""));
		info.setPathCnt(paths.size());
		if(routeMap.get(key) == null) {
			
			Map<String, Object> mapped = callTmapApi(start, end, paths);
			
			if(mapped != null) {
				T_ROUTE_PATH_MATRIX5 matrix3 = execute(start, end, info, mapped);
				insertMatrix(matrix3, info.getOrgStr());
				routeMap.put(key, matrix3);
			}
		}else {
			update(key, info);
		}
		
	}
	
	private void update(String key, RouteInfo info) throws IOException {
		T_ROUTE_PATH_MATRIX5 matrix3 = routeMap.get(key);
		matrix3.setId(null);
		matrix3.setRouteNo(info.getRouteNo());
		matrix3.setDlvyDe(info.getDlvyDe());
		matrix3.setVrn(info.getVrn());
		matrix3.setRoutePathCnt(info.getPathCnt());
		insertMatrix(matrix3, String.join(",", info.getOrgStr(), matrix3.getNewTollCost() + "", matrix3.getNewDistance() + ""));
	}

	private Map<String, Object> callTmapApi(Point start, Point end, List<Point> paths) throws IOException {
		String jsonParam = makeparan(start, end, paths);
		System.out.println(jsonParam);
		return retry(jsonParam, "https:
	}

	private List<TmsPlan> findTmsPlan(String dlvyDe, String routeNo) {
		List<TmsPlanIO> tmsplans = tmsPlanSvc.findTmPlansByIds(dlvyDe, routeNo);
		return convert(tmsplans);
	}

	private TmsPlan findStop(String bundledDlvyLc, List<TmsPlan> tmsplans) {
		return tmsplans.stream().filter(p -> p.getStopCd().equals(bundledDlvyLc)).findFirst().orElse(null);
	}
	
	private Point convert(TmsPlan plan) {
		return Point.builder()
				.lat(new BigDecimal(scale(plan.getLat())).doubleValue())
				.lng(new BigDecimal(scale(plan.getLng())).doubleValue()).name(plan.getDlvyLoNm()).build();
	}
	
	private List<TmsPlan> convert(List<TmsPlanIO> tmsplans) {
		return tmsplans.stream().map(p -> {
			TmsPlan newPlan = new TmsPlan();
			BeanUtils.copyProperties(p, newPlan);
			return newPlan;
		}).collect(Collectors.toList());
		
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

	private void setQueryTime(Point p, TmsPlan first) {
		p.setDate("2017-01-01T23:00:00" + "+0900");
	}

	private String scale(String lat) {
		if(StringUtils.isEmpty(lat)) {
			return "0";
		}
		return new BigDecimal(lat).setScale(6, BigDecimal.ROUND_HALF_DOWN).toEngineeringString();
	}

	private T_ROUTE_PATH_MATRIX5 execute(Point start, Point end, RouteInfo r, Map<String, Object> mapped ) {
		try {

			Map<String, Object> properties = findProperties(mapped);
			if(properties != null) {
				return createMarix(start, end, r, findDistance(properties), findTotalFare(properties),  findTotalTime(properties), mapper.writeValueAsString(mapped));
			}

		} catch (Exception e) {
			log.error("{}", e);

		}
		return null;
	}
	
	private T_ROUTE_PATH_MATRIX5 createMarix(Point start, Point end, RouteInfo info, int distance, int totalFare, int totalTime, String jsonData) {
		
		T_ROUTE_PATH_MATRIX5 matrix = new T_ROUTE_PATH_MATRIX5();
		String json = info.getOrgStr();
		matrix.setEndPos(end.getName());
		matrix.setEndLat(end.getLat() + "");
		matrix.setEndLng(end.getLng() + "");
		matrix.setJsonData(jsonData);
		matrix.setStartPos(start.getName());
		matrix.setStartLat(start.getLat() + "");
		matrix.setStartLng(start.getLng() + "");
		matrix.setNewTollCost(totalFare);
		matrix.setNewDistance(distance);
		matrix.setTmsTollCost(info.getTmsTollCost());
		matrix.setTmsDistance(info.getTmsDistance());
		matrix.setNewTotalTime(totalTime);
		
		matrix.setDlvyDe(info.getDlvyDe());
		matrix.setRouteNo(info.getRouteNo());
		matrix.setVrn(info.getVrn());
		matrix.setRoutePathCnt(info.getPathCnt());
		json = String.join(",", json, matrix.getNewTollCost() + "", matrix.getNewDistance() + "");
		info.setOrgStr(json);
		
		return matrix;
		
	}
	
	
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
	
	private void insertMatrix(T_ROUTE_PATH_MATRIX5 save, String json) throws IOException {
		try {
			rpSvc.save(save);
		}catch (Exception e) {
			log.error("{}", e);
		}finally {
			FileUtils.write(new File("new_" + save.getEndPos() + "_" +  fileName), json + "\r\n", Charset.forName("UTF-8"), true);
			log.info("procdssing ... {}", inc2.incrementAndGet());
		}
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
			conn.setReadTimeout(30000);
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

				if(s.isEmpty()) {
					return true;
				}
				
				return !s.contains("물류0");
			}).map(s -> convert(s)).collect(Collectors.toList());

		} catch  (Exception e) {
			log.error("{}", e);
		}
		return null;

	}

	private RouteInfo convert(String s) {
		List<String> strs = Arrays.asList(s.split(","));
		int startIdx = 0;
		return RouteInfo.builder()
				.dlvyDe(strs.get(startIdx + 0))
				.fctryCd(convertFactory(strs.get(startIdx + 1)))
				.routeNo(strs.get(startIdx + 3))
				.orgStr(s)
				.vrn(strs.get(startIdx + 5) + strs.get(startIdx + 6))
				.tmsDistance(Integer.valueOf(strs.get(startIdx + 36)))
				.tmsTollCost(Integer.valueOf(strs.get(startIdx + 34)))
				.build();
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
	




	public static void main(String[] args) {
		
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		fmt.setTimeZone(TimeZone.getTimeZone("UTC"));
		System.out.println(fmt.format(new Date()) + "+0900");
		
		
		LocalDateTime time = LocalDateTime.now(ZoneOffset.UTC);
		System.out.println(time);
		
		
		LocalTime lt = LocalTime.parse("23:28");
		
		System.out.println(lt.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
		
		System.out.println(createQuery("100100100"));
		
		
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
		routesInfo.put("tollgateCarType", "largevan");
		
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
	
	private static String createQuery(String stopCd) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT").append(" ");
		sb.append("       CASE WHEN Latitude is null THEN Tmp_Latitude ELSE Latitude END AS LAT").append(" ");
		sb.append("     , CASE WHEN Longitude is null THEN Tmp_Longitude ELSE Longitude END AS LNG").append(" ");
		sb.append("     , Stop_Nm").append(" ");
		sb.append("  FROM [TMS].[DBO].[M_STOP]").append(" ");
		sb.append(" WHERE STOP_CD = '%s'").append(" ");
		return String.format(sb.toString(), stopCd);
	}
	
	private Point findLocation(String stopCd) {
		List<?> results = builder.createSelectNativeQuery(createQuery(stopCd));
		Point point = null;
		if(results != null && !results.isEmpty()) {
			Object[] o = (Object[]) results.get(0);
			String lat =  o[0] + "";
			String lng =  o[1] + "";
			String name = o[2] + "";
			point = Point.builder().lat(new BigDecimal(scale(lat)).doubleValue()).lng(new BigDecimal(scale(lng)).doubleValue()).name(name).build();
		}
		return point;
	}
	
	private List<List<OdMatrix>> partitionBy(List<OdMatrix> matrixs) {
		
		int chunkSize = 5;
		List<List<OdMatrix>> lists = new ArrayList<>();
		for (int i=0; i<matrixs.size(); i+= chunkSize) {
		    int end = Math.min(matrixs.size(), i + chunkSize);
		    lists.add(matrixs.subList(i, end));
		}
		return lists;
	}
	
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
	    Set<Object> seen = ConcurrentHashMap.newKeySet();
	    return t -> seen.add(keyExtractor.apply(t));
	}

}
