package com.sdc2ch.web.spring.test;

import static com.sdc2ch.core.lambda.tuple.Tuple.tuple;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import com.sdc2ch.core.lambda.seq.Seq;
import com.sdc2ch.service.admin.RoutePathInfoService;
import com.sdc2ch.tms.io.TmsOilPriceIO;
import com.sdc2ch.tms.service.ITmsContractService;
import com.sdc2ch.tms.service.ITmsOilPriceService;
import com.sdc2ch.tms.service.ITmsPlanService;
import com.sdc2ch.tms.service.ITmsTurnRateService;
import com.sdc2ch.web.admin.repo.AdmQueryBuilder;
import com.sdc2ch.web.admin.repo.domain.T_ROUTE_PATH_MATRIX5;
import com.sdc2ch.web.bootstrap.Seoulmilk2chApplication;
import com.sdc2ch.web.spring.test.util.TmsSettleCost;
import com.sdc2ch.web.spring.test.util.TmsTurnRate;
import com.sdc2ch.web.spring.test.util.TmsTurnRate.TmsTurnRateBuilder;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Seoulmilk2chApplication.class)
public class RoutePathInfoTest_dlvy_new_settle_file_split {

	@Autowired
	RoutePathInfoService rpSvc;

	@Autowired
	ITmsPlanService tmsPlanSvc;
	
	@Autowired
	AdmQueryBuilder builder;
	
	@Autowired ITmsContractService contract;
	@Autowired ITmsOilPriceService oilPriceSvc;
	@Autowired ITmsTurnRateService turnSvc;

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
		private int newTotalTime;
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
	
	private List<TmsOilPriceIO> oilPrices;

	private Map<String, List<TmsTurnRate>> tmsTurnMapped;
	private Map<String, String> routePathMap;

	@Before
	public void init() {
		this.oilPrices = oilPriceSvc.findAll();
		this.tmsTurnMapped = createTurnRate();
		routePathMap = fileReadRoutePathCount("new_POINT_COUNT");
		System.out.println();
	}
	
	private Map<String, List<TmsTurnRate>> createTurnRate() {
		return turnSvc.findAll().stream().map(t -> {
			TmsTurnRate rate = new TmsTurnRate(t.getFctryCd(), t.getCaralcTy(),
					t.getMinDistance(), t.getMaxDistance(), t.getMinLdng(), t.getMaxLdng(),
					t.getMinTime(), t.getMaxTime(), t.getTurnRate());
			return rate;
		}).collect(Collectors.groupingBy(TmsTurnRate::getFctryCd));
	}
	
	public List<TmsTurnRate> getOrgTurnRate(String fctryCd, String caralcTy) {
		return tmsTurnMapped.get(fctryCd).stream().filter(t -> t.getCaralcTy().equals(caralcTy)).collect(Collectors.toList());
	}
	private List<TmsTurnRate> newTurnRate(String carAclTy) {
		List<TmsTurnRate> rates = new ArrayList<>();
		
		rates = tmsTurnMapped.get("2D1").stream().filter(r -> r.getCaralcTy().equals(carAclTy)) .map(r -> {
			TmsTurnRateBuilder builder = TmsTurnRate.create();
			int[] distances = r.getDistanceRules();
			int[] points = r.getDlvyPointRules();
			int[] hours = r.getHourRules();
			if(distances[0] == 160) {
				distances[0] = 120;
			}
			if(distances[0] == 161) {
				distances[0] = 120;
			}
			if(distances[1] == 160) {
				distances[1] = 120;
			}
			if(distances[1] == 161) {
				distances[1] = 120;
			}
			
			return builder.caralcTy(r.getCaralcTy())
			.fctryCd(r.getFctryCd())
			.minDistance(distances[0])
			.maxDistance(distances[1])
			.minHour(hours[0])
			.maxHour(hours[1])
			.minLdng(points[0])
			.maxLdng(points[1])
			.turnRate(r.getTurnRate())
			.build();
		}).collect(Collectors.toList());
		

		return rates;
	}

	@Test
	public void whenValidName_thenEmployeeShouldBeFound() throws IOException {
		

		
		Stream.of("20180402_용인").forEach(s -> {
			
			
			List<RouteInfo> infos = fileRead(s);
			
			String[] headers = header.split(",");
			for(int i = 0 ; i < headers.length ; i++) {
				System.out.println("[" + i + "] : " + headers[i]);
			}
			
			System.out.println(infos.size());
			

			infos.removeIf(i -> !(i.getFctryCd().equals("2D1")));
			infos.removeIf(i -> i.getRouteNo().contains("_"));

			
			
			List<T_ROUTE_PATH_MATRIX5> results2 = rpSvc.findMatirx5();
			results2.removeIf(m -> m.getRouteNo().contains("_"));

			results2.removeIf(m -> {
				if(StringUtils.isEmpty(m.getEndPos())) {
					return true;
				}
				return !m.getEndPos().equals("희창");
			});
			System.out.println(infos.size());
			infos.removeIf(i -> Integer.valueOf(i.getDlvyDe()) > 20180910);
			System.out.println("remove -> " + infos.size());

			infos = leftjoin(infos, results2);
			
			
			List<RouteInfo> _info = new ArrayList<>(infos.size());
			infos.forEach(i -> {
				_info.add(setSettleCostTest(i));
			});
			
			
			try {
				
				StringBuilder sb = new StringBuilder();
				_info.forEach(i -> {
					sb.append(i.toCvs()).append("\r\n");
				});
				FileUtils.write(new File("20180402_배송_용인_희창"), sb.toString(), Charset.forName("UTF-8"), false);
			}catch (Exception e) {
				
			}
			
		});
	}
	
	private List<RouteInfo> leftjoin(List<RouteInfo> info, List<T_ROUTE_PATH_MATRIX5> matrix){
		
		try {
			return Seq.seq(info)
					.flatMap(v1 -> Seq.seq(matrix)
							.filter(v2 -> filter(v1, v2))
							.onEmpty(null)
							.map(v2 -> tuple(v1, v2))
							.map(t -> convert(t.v1, t.v2))
							)
					.collect(Collectors.toList());
		}catch (Exception e) {
			
			e.printStackTrace();
			throw e;
		}

	}
	
	private boolean filter(RouteInfo v1, T_ROUTE_PATH_MATRIX5 v2) {
		return v1.getRouteNo().equals(v2.getRouteNo()) && v1.getDlvyDe().equals(v2.getDlvyDe());
	}

	private RouteInfo convert(RouteInfo v1, T_ROUTE_PATH_MATRIX5 v2) {
		if(v2 != null) {
			v1.setNewDistance(v2.getNewDistance());
			v1.setNewTollCost(v2.getNewTollCost());
			v1.setPathCnt(v2.getRoutePathCnt());
			v1.setNewTotalTime(v2.getNewTotalTime());
		}
		return v1;
	}

	private RouteInfo setSettleCostTest(RouteInfo info) {
		
		
		List<String> values = Arrays.asList(info.getOrgStr().split(","));
		
		
		
		TmsSettleCost cost = buildSettleCost(info.getNewDistance(), info.getNewTollCost(), info);
		



















		
		try {
			if(cost.getNewTurnRate().floatValue() != 0 && cost.getDistance().intValue() == 0) {
				System.out.println(cost.getNewTurnRate());
			}
			setInfo(info, cost);
		}catch (Exception e) {
			
			e.printStackTrace();
		}

		
		return info;
		
		
	}
	
	private void setInfo(RouteInfo info, TmsSettleCost cost) {
		info.setTurnRate(cost.getNewTurnRate() + "");
		info.setCarFuel(cost.carFuel() + "");
		info.setFreezingFuel(cost.freezingFuel() + "");
		info.setFuelCost( cost.fuelCost() + "");
		info.setPathCnt(cost.getPoint().intValue());
		info.setTotalCost(cost.getNewTurnRate().multiply(info.getDayContractCost()).add(cost.fuelCost()).add(cost.getTollCost()).setScale(0, BigDecimal.ROUND_DOWN).toString());
	}

	private TmsSettleCost buildSettleCost(int dis, int toll, RouteInfo info) {
		BigDecimal distance  = new BigDecimal(dis);
		BigDecimal tollCost  = new BigDecimal(toll);
		BigDecimal carOilQty = new BigDecimal(info.getCarOilQty());
		BigDecimal frezingOil = new BigDecimal(info.getFreezingOilQty());
		TmsOilPriceIO oilPrice = findOilPrice(info.getDlvyDe());
		BigDecimal hour = new BigDecimal((info.getNewTotalTime() + 60 * 60 * 2) / 3600);
		
		if(oilPrice == null) {
			oilPrice = neerLastOilPrice(info.getDlvyDe());
		}
		
		BigDecimal kmPerLiter = oilPrice.getOilPrice();
		

		



		
		

		
		return TmsSettleCost.builder()

				.kmPerLiter(kmPerLiter)
				.distance(distance)
				.tollCost(tollCost)
				.point(new BigDecimal(info.getPathCnt()))
				.hour(hour)
				.carOilQty(carOilQty)
				.frezingOilQty(frezingOil)
				.orgTrunRateRules(getOrgTurnRate(info.getFctryCd(), info.getCaralcTy()))
				.newTrunRateRules(newTurnRate(info.getCaralcTy()))
				.build();
	}
	
	private TmsOilPriceIO neerLastOilPrice(String dlvyDe) {
		return oilPrices.stream().filter(o -> convertDate(dlvyDe).isBefore(convertDate(o.getStartDate())))
				.max(Comparator.comparing(TmsOilPriceIO::getStartDate)).orElse(null);
	}

	private List<RouteInfo> fileRead(String path) {
		ClassPathResource res = new ClassPathResource(path);
		try (BufferedReader stream = Files.newBufferedReader(Paths.get(res.getURI()), Charset.forName("UTF-8"))) {
			header = stream.lines().findFirst().get();
			return stream.lines().skip(0).filter(s -> {

				if(s.isEmpty()) {
					return true;
				}
				String[] split = s.split(",");
				if("NULL".equals(split[5])) {
					return false;
				}else if(split[5].contains("물류0")) {
					return false;
				}else {
					return true;
				}

			}).map(s -> convert(s)).collect(Collectors.toList());

		} catch  (Exception e) {
			log.error("{}", e);
		}
		return null;

	}
	
	private Map<String, String> fileReadRoutePathCount(String path) {
		ClassPathResource res = new ClassPathResource(path);
		try (BufferedReader stream = Files.newBufferedReader(Paths.get(res.getURI()), Charset.forName("UTF-8"))) {
			List<String> list = stream.lines().skip(0).filter(s -> {
				System.out.println(s);
				if(s.isEmpty()) {
					return true;
				}
				
				return !s.contains("물류0");

			}).collect(Collectors.toList());
			
			Map<String, String> mapped = new HashMap<>();	
			
			list.forEach(s -> {
				String[] strs = s.split(",");
				mapped.put(strs[0] + strs[1], strs[2]);
			});
			return mapped;
		} catch  (Exception e) {
			log.error("{}", e);
		}
		return null;

	}

	private RouteInfo convert(String s) {
		List<String> strs = Arrays.asList(s.split(","));
		try {
			RouteInfo info = RouteInfo.builder()
					.dlvyDe(strs.get(0))
					.fctryCd(convertFactory(strs.get(1)))
					.routeNo(strs.get(3))
					.vrn(strs.get(5) + strs.get(6))
					.confRtateRate(strs.get(10))
					.carOilQty(findFuelByDefaultKmperLiter(Float.valueOf(strs.get(2).replaceAll("톤", ""))) + "")
					.freezingOilQty("0")
					.caralcTy(strs.get(3).substring(1, 3))
					.tmsDistance(Integer.valueOf(strs.get(36)))
					.tmsTollCost(Integer.valueOf(strs.get(34)))
					.dayContractCost(new BigDecimal(strs.get(30)))
					.tmsTotalCost(strs.get(35))
					.orgStr(s)
					.build();

			return info;
		}catch (Exception e) {
			e.printStackTrace();
			
			throw e;
		}

	}
	
	public static void main(String[] args) throws IOException {
		RoutePathInfoTest_dlvy_new_settle_file_split test = new RoutePathInfoTest_dlvy_new_settle_file_split();
		test.whenValidName_thenEmployeeShouldBeFound() ;
	}
	
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
	    Set<Object> seen = ConcurrentHashMap.newKeySet();
	    return t -> seen.add(keyExtractor.apply(t));
	}

	private LocalDate convertDate(String date) {
		return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"));
	}
	private boolean isContainsDate(LocalDate start, LocalDate end, LocalDate current) {
		return (end.isAfter(current) || end.isEqual(current)) && (start.isBefore(current) || start.isEqual(current));
	}
	
	private TmsOilPriceIO findOilPrice(String dlvyDe) {
		try {
			return oilPrices.stream().filter(
					o -> isContainsDate(convertDate(o.getStartDate()), convertDate(o.getEndDate()), convertDate(dlvyDe)))
					.findFirst().orElse(null);
		}catch (Exception e) {
			System.out.println(e);
		}
		return null;
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
