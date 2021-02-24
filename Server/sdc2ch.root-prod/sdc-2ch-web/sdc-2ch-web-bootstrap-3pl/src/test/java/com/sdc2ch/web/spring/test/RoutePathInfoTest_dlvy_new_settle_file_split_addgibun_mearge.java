package com.sdc2ch.web.spring.test;

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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import com.sdc2ch.service.admin.RoutePathInfoService;
import com.sdc2ch.tms.io.TmsOilPriceIO;
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Seoulmilk2chApplication.class)
public class RoutePathInfoTest_dlvy_new_settle_file_split_addgibun_mearge {

	@Autowired
	RoutePathInfoService rpSvc;

	@Autowired
	ITmsPlanService tmsPlanSvc;
	
	@Autowired
	AdmQueryBuilder builder;

	@Autowired ITmsTurnRateService turnSvc;
	@Autowired ITmsOilPriceService oilPriceSvc;
	
	private Map<String, List<TmsTurnRate>> tmsTurnMapped;
	
	List<RouteVolume> fileRead = fileRead("노선방면");
	
	Map<String, BigDecimal> carTonMap = fileReadTmp("CAR_TON");
	
	Map<String, BigDecimal> contractMap = fileReadContract("20180402_용인");
	
	private List<TmsOilPriceIO> oilPrices;
	
	
	@Before
	public void init() {
		this.oilPrices = oilPriceSvc.findAll();
		this.tmsTurnMapped = createTurnRate();
	}
	
	
	@Test
	public void whenValidName_thenEmployeeShouldBeFound() throws IOException {
		
		List<T_ROUTE_PATH_MATRIX5> results = new ArrayList<>();
		Stream.of("구성","희창").forEach(s -> {
			List<T_ROUTE_PATH_MATRIX5> _results = rpSvc.findMatirx5(s);
			
			if(_results.stream().anyMatch(r -> r.getRouteNo().contains("21DA030"))) {
				System.out.println("");
			}
			

			results.addAll(_results);
		});
		Map<String, List<T_ROUTE_PATH_MATRIX5>> mapped = results.stream().collect(Collectors.groupingBy(m -> m.getRouteNo()));
		
		StringBuilder sb = new StringBuilder();
		mapped.keySet().stream().forEach(k -> {
			Map<String, List<T_ROUTE_PATH_MATRIX5>> _mapped = mapped.get(k).stream().collect(Collectors.groupingBy(T_ROUTE_PATH_MATRIX5::getDlvyDe));
			_mapped.keySet().stream().forEach(_k -> {
				Mearge m = add(k, _k, _mapped.get(_k));
				RouteVolume vol = findVol(k);
				String cvs = m.toCvs() + "," + convertBooleanToString(vol.isVolume1()) + "," + convertBooleanToString(vol.isVolume2());
				sb.append(cvs).append("\r\n");
			});
		});
		System.out.println(sb.toString());
		
		try {
			FileUtils.write(new File("노선방면_구성_희창_비교_V"), sb.toString(), Charset.forName("UTF-8"), false);
		}catch (Exception e) {
			
		}
	}
	
	
	private Map<String, List<TmsTurnRate>> createTurnRate() {
		return turnSvc.findAll().stream().map(t -> {
			TmsTurnRate rate = new TmsTurnRate(t.getFctryCd(), t.getCaralcTy(),
					t.getMinDistance(), t.getMaxDistance(), t.getMinLdng(), t.getMaxLdng(),
					t.getMinTime(), t.getMaxTime(), t.getTurnRate());
			return rate;
		}).collect(Collectors.groupingBy(TmsTurnRate::getFctryCd));
	}
	
	private List<TmsTurnRate> newTurnRate(String caralcTy) {
		List<TmsTurnRate> rates = new ArrayList<>();
		
		rates = tmsTurnMapped.get("2D1").stream().filter(t -> t.getCaralcTy().equals(caralcTy)).map(r -> {
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
	
	private String convertBooleanToString(boolean vol) {
		return vol ? "포함" : "미포함";
	}
	
	private RouteVolume findVol(String routeNo) {
		return fileRead.stream().filter(r -> r.getRouteNo().equals(routeNo)).findFirst().orElse(null);
	}
	private String header;
	private List<RouteVolume> fileRead(String path) {
		ClassPathResource res = new ClassPathResource(path);
		try (BufferedReader stream = Files.newBufferedReader(Paths.get(res.getURI()), Charset.forName("UTF-8"))) {
			header = stream.lines().findFirst().get();
			return stream.lines().skip(0).map(s -> convert(s)).collect(Collectors.toList());
		} catch  (Exception e) {
			log.error("{}", e);
		}
		return null;

	}
	private RouteVolume convert(String s) {
		List<String> strs = Arrays.asList(s.split(",", -1));
		try {
			RouteVolume info = RouteVolume.builder()
					.routeNo(strs.get(3))
					.volume1(StringUtils.isEmpty(strs.get(7)))
					.volume2(StringUtils.isEmpty(strs.get(8)))
					.build();
			return info;
		}catch (Exception e) {
			e.printStackTrace();
			
			throw e;
		}
	}
	










	private Mearge add(String routeNo, String dlvyDe, List<T_ROUTE_PATH_MATRIX5> list) {
		String cvs = list.stream().map(m -> {
			StringBuilder sb = new StringBuilder();
			TmsSettleCost cost = buildSettleCost(m.getNewDistance(), m.getNewTollCost(), m);
			String turnRate = cost.getNewTurnRate().toString();
			
			System.out.println(turnRate);
			String freezingFuel = cost.freezingFuel().toString();
			String fuelCost = cost.fuelCost().toString();
			String carFuel = cost.carFuel().setScale(2, BigDecimal.ROUND_DOWN).toString();
			String totalCost = cost.getNewTurnRate().multiply(contractMap.get(m.getDlvyDe() + m.getVrn())).add(cost.fuelCost()).add(cost.getTollCost()).setScale(0, BigDecimal.ROUND_DOWN).toString();
			sb.append(turnRate).append(",");
			sb.append(m.getNewDistance()).append(",");
			sb.append(carFuel).append(",");
			sb.append(freezingFuel).append(",");
			sb.append(fuelCost).append(",");
			sb.append(m.getNewTollCost()).append(",");
			sb.append(totalCost).append(",");
			sb.append(VolumeMap.get(m.getDlvyDe() + m.getVrn()));
			return sb.toString();
		}).collect(Collectors.joining(","));
		return Mearge.builder()
				.dlvyDe(dlvyDe)
				.routeNo(routeNo)
				.csvString(cvs)
				.build();
	}
	
	private TmsSettleCost buildSettleCost(int newDistance, int newTollCost, T_ROUTE_PATH_MATRIX5 m) {
		BigDecimal distance  = new BigDecimal(newDistance);
		BigDecimal tollCost  = new BigDecimal(newTollCost);
		BigDecimal carOilQty = new BigDecimal(findFuelByDefaultKmperLiter(carTonMap.get(m.getVrn()).floatValue()));
		BigDecimal frezingOil = new BigDecimal(0);
		TmsOilPriceIO oilPrice = findOilPrice(m.getDlvyDe());
		
		
		BigDecimal hour = new BigDecimal((m.getNewTotalTime() + 60 * 60 * 2) / 3600);
		
		if(oilPrice == null) {
			oilPrice = neerLastOilPrice(m.getDlvyDe());
		}
		BigDecimal kmPerLiter = oilPrice.getOilPrice();
		
		return TmsSettleCost.builder()
				.kmPerLiter(kmPerLiter)
				.distance(distance)
				.tollCost(tollCost)
				.point(new BigDecimal(m.getRoutePathCnt()))
				.hour(hour)
				.carOilQty(carOilQty)
				.frezingOilQty(frezingOil)
				.orgTrunRateRules(getOrgTurnRate("2D1", m.getRouteNo().substring(1, 3)))
				.newTrunRateRules(newTurnRate(m.getRouteNo().substring(1, 3)))
				.build();
	}

	
	private TmsOilPriceIO neerLastOilPrice(String dlvyDe) {
		return oilPrices.stream().filter(o -> convertDate(dlvyDe).isBefore(convertDate(o.getStartDate())))
				.max(Comparator.comparing(TmsOilPriceIO::getStartDate)).orElse(null);
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
	
	private boolean isContainsDate(LocalDate start, LocalDate end, LocalDate current) {
		return (end.isAfter(current) || end.isEqual(current)) && (start.isBefore(current) || start.isEqual(current));
	}
	
	private LocalDate convertDate(String date) {
		return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"));
	}
	
	public List<TmsTurnRate> getOrgTurnRate(String fctryCd, String caralcTy) {
		return tmsTurnMapped.get(fctryCd).stream().filter(t -> t.getCaralcTy().equals(caralcTy)).collect(Collectors.toList());
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
	
	
	private Map<String, BigDecimal> fileReadTmp(String path) {
		ClassPathResource res = new ClassPathResource(path);
		Map<String, BigDecimal> mapped = new HashMap<>();
		try (BufferedReader stream = Files.newBufferedReader(Paths.get(res.getURI()), Charset.forName("UTF-8"))) {
			stream.lines().skip(0).forEach(s -> {
				String[] strs = s.split(",");
				mapped.put(strs[5], new BigDecimal(strs[0]));
			});
			return mapped;

		} catch  (Exception e) {
			log.error("{}", e);
		}
		return mapped;

	}
	
	static Map<String, BigDecimal> VolumeMap = new HashMap<>();
	private Map<String, BigDecimal> fileReadContract(String path) {
		ClassPathResource res = new ClassPathResource(path);
		
		Map<String, BigDecimal> mapped = new HashMap<>();
		try (BufferedReader stream = Files.newBufferedReader(Paths.get(res.getURI()), Charset.forName("UTF-8"))) {
			stream.lines().skip(1).filter(s -> {

				if(s.isEmpty()) {
					return true;
				}
				String[] split = s.split(",");
				if("NULL".equals(split[5])) {
					return false;
				}else if(split[3].startsWith("2")) {
					return true;
				}else {
					return false;
				}

			}).forEach(s -> {
				
				System.out.println(s);
				List<String> strs = Arrays.asList(s.split(","));
				String dlvyDe = strs.get(0);
				String vrn = strs.get(5) + strs.get(6);
				BigDecimal contractCost = new BigDecimal(strs.get(30));
				mapped.put(dlvyDe + vrn, contractCost);
				VolumeMap.put(dlvyDe + vrn, new BigDecimal(nullSafe(strs.get(21))));
			});

		} catch  (Exception e) {
			log.error("{}", e);
		}
		return mapped;

	}
	
	private String nullSafe(String string) {
		
		if("NULL".equals(string)) {
			return "0";
		}
		return StringUtils.isEmpty(string) ? "0" : string;
	}

	@Getter
	@Builder
	private static class Mearge {
		private String routeNo;
		private String dlvyDe;
		private String csvString;
		public String toCvs() {
			return routeNo + "," + dlvyDe +  "," + csvString;
		}
	}
	
	@Getter
	@Builder
	private static class RouteVolume {
		private String routeNo;
		private boolean volume1; 
		private boolean volume2;
	}


}
