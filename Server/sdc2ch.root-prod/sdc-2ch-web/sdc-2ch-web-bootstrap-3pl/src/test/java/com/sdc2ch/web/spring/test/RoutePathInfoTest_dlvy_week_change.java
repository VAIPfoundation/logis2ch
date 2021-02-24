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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.assertj.core.util.Lists;
import org.junit.Before;
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
import com.sdc2ch.tms.io.TmsContractIO;
import com.sdc2ch.tms.io.TmsOilPriceIO;
import com.sdc2ch.tms.io.TmsPlanIO;
import com.sdc2ch.tms.service.ITmsContractService;
import com.sdc2ch.tms.service.ITmsOilPriceService;
import com.sdc2ch.tms.service.ITmsPlanService;
import com.sdc2ch.tms.utils.TmsUtils;
import com.sdc2ch.tms.utils.TmsUtils.DayOfWeek;
import com.sdc2ch.web.admin.repo.AdmQueryBuilder;
import com.sdc2ch.web.admin.repo.domain.T_ROUTE_PATH_MATRIX3;
import com.sdc2ch.web.bootstrap.Seoulmilk2chApplication;
import com.sdc2ch.web.spring.test.util.TmsSettleCost;
import com.sdc2ch.web.spring.test.util.TmsTurnRate;

import io.swagger.models.auth.In;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Seoulmilk2chApplication.class)
public class RoutePathInfoTest_dlvy_week_change {

	@Autowired
	RoutePathInfoService rpSvc;

	@Autowired
	ITmsPlanService tmsPlanSvc;
	
	@Autowired
	AdmQueryBuilder builder;
	
	@Autowired ITmsContractService contract;
	@Autowired ITmsOilPriceService oilPriceSvc;

	private int inc;
	
	
	private ObjectMapper mapper = new ObjectMapper();
	

	
	private String fileName = "";
	
	private String header;

	private static HashMap<String, T_ROUTE_PATH_MATRIX3> routeMap = new HashMap<>();
	
	@Getter
	@Setter
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
		private String carOilQty;
		private String freezingOilQty;
		private String tComCd;
		private String ldngTy;
		
		public String getKey() {
			return lat + lng;
		}

		@Override
		public String toString() {
			return "원장정보 [배송일=" + dlvyDe + ", 노선=" + routeNo + ", 회전율=" + confRtateRate + ", 차량타입=" + ldngTy + ", 고객센터코드(새끼코드)=" + stopCd + "(" + bundledDlvyLc + ")" + ", 고객센터명="
					+ dlvyLoNm + "]";
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
		private BigDecimal dayContractCost;
		@Setter
		private int pathCnt;
		
		@Setter
		private int newTollCost;
		@Setter
		private int newDistance;
		@Setter
		private String turnRate;
		@Setter
		private String carFuel;
		@Setter
		private String freezingFuel;
		@Setter
		private String fuelCost;
		
		@Setter
		private String totalCost;
		
		@Setter
		private String tmsTotalCost;
		
		@Setter
		private boolean joined;
		
		@Setter
		private String carOilQty;
		@Setter
		private String freezingOilQty;
		@Setter
		private String fctryCd;
		@Setter
		private String caralcTy;
		@Setter
		private BigDecimal point;
		
		public String toCvs() {
			StringBuilder sb = new StringBuilder(orgStr);
			sb.append(",");
			sb.append(turnRate).append(",");
			sb.append(newDistance).append(",");
			sb.append(carFuel).append(",");
			sb.append(freezingFuel).append(",");
			sb.append(fuelCost).append(",");
			sb.append(newTollCost).append(",");
			sb.append(totalCost);
			return sb.toString();
		}
		
	}

	static AtomicInteger inc2 = new AtomicInteger();

	static Map<String, List<Integer>> moveMapped = movedDayMap();

	
	private List<TmsTurnRate> createTurnRate() {
		List<TmsTurnRate> rates = new ArrayList<>();
		TmsTurnRate rate = null;
		rate = new TmsTurnRate(0, 130, 1, 4, 0, 24, 0.5f);
		rates.add(rate);
		rate = new TmsTurnRate(0, 130, 4, 7, 0, 24, 1f);
		rates.add(rate);
		rate = new TmsTurnRate(0, 130, 7, 99, 0, 24, 1.5f);
		rates.add(rate);
		rate = new TmsTurnRate(130, 160, 1, 3, 0, 24, 0.5f);
		rates.add(rate);
		rate = new TmsTurnRate(130, 160, 3, 6, 0, 24, 1f);
		rates.add(rate);
		rate = new TmsTurnRate(130, 160, 6, 99, 0, 24, 1.5f);
		rates.add(rate);
		rate = new TmsTurnRate(160, 380, 1, 5, 0, 24, 1f);
		rates.add(rate);
		rate = new TmsTurnRate(160, 380, 5, 99, 0, 24, 1.5f);
		rates.add(rate);
		rate = new TmsTurnRate(380, 650, 1, 4, 0, 24, 1.5f);
		rates.add(rate);
		rate = new TmsTurnRate(380, 650, 4, 99, 0, 24, 2f);
		rates.add(rate);
		rate = new TmsTurnRate(650, 1000, 1, 99, 0, 24, 2f);
		rates.add(rate);
		return rates;
	}

	@Test
	public void whenValidName_thenEmployeeShouldBeFound() throws IOException {
		

		
		Stream.of("20181016_원장").forEach(s -> {
			
			fileName = s;
			List<RouteInfo> infos = fileRead(s);
			




			
			List<String> lines = infos.stream().map( i -> {
				int week = dayOfWeek(i.getDlvyDe());
				String day = moveDay(week, i.getFctryCd(), i.getDlvyDe());
				return getTeatory(i.getFctryCd()) + ","+ convertDayOfWeek(dayOfWeek(day)) + "," +convertDayOfWeek(week)+ "," +day + "," + i.getOrgStr();
			}).collect(Collectors.toList());
			
			StringBuilder sb = new StringBuilder();
			lines.forEach(s1 -> sb.append(s1).append("\r\n"));
			
			
			System.out.println(sb.toString());
			try {
				FileUtils.write(new File("20181016_move_day"), sb.toString(), Charset.forName("UTF-8"), false);
			}catch (Exception e) {
				
			}

		});
	}

	private String getTeatory(String fctryCd) {
		return fctryCd.equals("4D1") ? "지방권" : "수도권";
	}

	private String moveDay(int week, String fctryCd, String dlvyDe) {
		int move = moveMapped.get(fctryCd).get(week - 1);
		LocalDate ld = LocalDate.parse(dlvyDe, DateTimeFormatter.ofPattern("yyyyMMdd"));
		System.out.println(ld);
		ld = ld.plusDays(move);
		System.out.println(ld);
		return ld.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
	}

	private List<RouteInfo> fileRead(String path) {
		ClassPathResource res = new ClassPathResource(path);
		try (BufferedReader stream = Files.newBufferedReader(Paths.get(res.getURI()), Charset.forName("UTF-8"))) {

			return stream.lines().skip(0).filter(s -> {
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
		return RouteInfo.builder()
				.dlvyDe(strs.get(0))
				.fctryCd(convertFactory(strs.get(1)))
				.routeNo(strs.get(3))
				.vrn(strs.get(5) + strs.get(6))
				.tmsDistance(Integer.valueOf(strs.get(36)))
				.tmsTollCost(Integer.valueOf(strs.get(34)))
				.orgStr(s)
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
	




	public static void main(String[] args) throws IOException {
		RoutePathInfoTest_dlvy_week_change change = new RoutePathInfoTest_dlvy_week_change();
		change.whenValidName_thenEmployeeShouldBeFound();
	}
	
	private static int dayOfWeek(String dlvyDe) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(TmsUtils.deliveryDeteFormatDate(dlvyDe));
		
		return cal.get(Calendar.DAY_OF_WEEK);
	}
	
	private static Map<String, List<Integer>> movedDayMap(){
		Map<String, List<Integer>> mapped = new HashMap<>();
		mapped.put("1D1", metroRule());
		mapped.put("2D1", metroRule());
		mapped.put("3D1", metroRule());
		mapped.put("4D1", otherRule());	
		return mapped;
	}
	private static String convertDayOfWeek(int week) {
		String day = "";
		if(week == 1) {
			day = "일";
		}else if(week == 2) {
			day = "월";
		}else if(week == 3) {
			day = "화";
		}else if(week == 4) {
			day = "수";
		}else if(week == 5) {
			day = "목";
		}else if(week == 6) {
			day = "금";
		}else if(week == 7) {
			day = "토";
		}
		return day;
	}
	private static List<Integer> metroRule(){
		return Stream.of(-2, 0, -1, 0, -1, 0, -1).collect(Collectors.toList());
	}
	private static List<Integer> otherRule(){
		return Stream.of(-3, 0, 0, -1, 0, -1, -3).collect(Collectors.toList());
	}
	
	
}
