package com.sdc2ch.web.spring.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
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
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
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

import com.sdc2ch.tms.io.TmsPlanIO;
import com.sdc2ch.tms.service.ITmsPlanService;
import com.sdc2ch.web.admin.repo.AdmQueryBuilder;
import com.sdc2ch.web.bootstrap.Seoulmilk2chApplication;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Seoulmilk2chApplication.class)
public class RoutePathInfoTest_dlvy_findPoint {

	@Autowired
	ITmsPlanService tmsPlanSvc;
	@Autowired
	AdmQueryBuilder builder;
	private String fileName = "";
	private String header;

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
		private String batchNo;
		




		@Override
		public String toString() {
			return "원장정보 [배송일=" + dlvyDe + ", 노선=" + routeNo + ", 회전율=" + confRtateRate + ", 차량타입=" + ldngTy + ", 고객센터코드(새끼코드)=" + stopCd + "(" + bundledDlvyLc + ")" + ", 고객센터명="
					+ dlvyLoNm + "]";
		}
		
		
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
		public String toCvs2(BigDecimal point) {
			StringBuilder sb = new StringBuilder();
			sb.append(dlvyDe).append(",");
			sb.append(routeNo).append(",");
			sb.append(point);
			return sb.toString();
		}
		
	}

	@Test
	public void whenValidName_thenEmployeeShouldBeFound() throws IOException {
		
		Stream.of("17년도_HY노선_물류비_기초데이터.csv").forEach(s -> {
			fileName = s;
			List<RouteInfo> infos = fileRead(s);
			
			String[] headers = header.split(",");
			for(int i = 0 ; i < headers.length ; i++) {
				System.out.println("[" + i + "] : " + headers[i]);
			}
			
			fileName = "거점2";
			infos.forEach(m -> {
				List<TmsPlan> tmsplans = findTmsPlan(m.getDlvyDe(), m.getRouteNo());
				if(tmsplans == null || tmsplans.isEmpty()){
					write(m.toCvs2(BigDecimal.ZERO));
				}else {
					TmsPlan first = tmsplans.remove(0);
					setSettleCostTest(m, first, tmsplans);
				}
			});
		});
	}

	private void setSettleCostTest(RouteInfo info, TmsPlan plan, List<TmsPlan> plans) {
		
		BigDecimal point = new BigDecimal(dlvyPoint(plans));
		write(info.toCvs2(point));
	}
	
	private void write(String contents) {
		try {
			FileUtils.write(new File("new_" + "POINT_COUNT" + "_" +  fileName), contents + "\r\n", Charset.forName("UTF-8"), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<TmsPlan> findTmsPlan(String dlvyDe, String routeNo) {
		List<TmsPlanIO> tmsplans = tmsPlanSvc.findTmPlansByIds(dlvyDe, routeNo);
		return convert(tmsplans);
	}
	
	private List<TmsPlan> convert(List<TmsPlanIO> tmsplans) {
		return tmsplans.stream().map(p -> {
			TmsPlan newPlan = new TmsPlan();
			BeanUtils.copyProperties(p, newPlan);
			return newPlan;
		}).collect(Collectors.toList());
		
	}

	private List<RouteInfo> fileRead(String path) {
		ClassPathResource res = new ClassPathResource(path);
		try (BufferedReader stream = Files.newBufferedReader(Paths.get(res.getURI()), Charset.forName("UTF-8"))) {
			header = stream.lines().findFirst().get();
			return stream.lines().skip(0).filter(s -> {
				System.out.println(s);
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
		
		RouteInfo info = RouteInfo.builder()
				.dlvyDe(strs.get(0))
				.routeNo(strs.get(3))
				.vrn(strs.get(5) + strs.get(6))
				.confRtateRate(strs.get(10))
				.tmsDistance(Integer.valueOf(strs.get(36)))
				.tmsTollCost(Integer.valueOf(strs.get(34)))
				.dayContractCost(new BigDecimal(strs.get(30)))
				.tmsTotalCost(strs.get(35))
				.orgStr(s)
				.build();
		
		if(strs.size() > 50) {
			info.setNewDistance(Integer.valueOf(strs.get(42)));
			info.setNewTollCost(Integer.valueOf(strs.get(41)));
		}
		
		return info;
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
	
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
	    Set<Object> seen = ConcurrentHashMap.newKeySet();
	    return t -> seen.add(keyExtractor.apply(t));
	}
	
	private int dlvyPoint(List<TmsPlan> tmsplans) {
		List<TmsPlan> _plans = new ArrayList<>(tmsplans.size());
		tmsplans.forEach(t -> {
			TmsPlan _plan = new TmsPlan();
			BeanUtils.copyProperties(t, _plan);
			_plans.add(_plan);
		});
		return _plans.stream().map(p -> {
			if(StringUtils.isEmpty(p.getBundledDlvyLc())) {
				p.setBundledDlvyLc(p.hashCode() + "");
			}
			return p;
				
		}).collect(Collectors.groupingBy(TmsPlan::getBundledDlvyLc)).size();
	}
}
