package com.sdc2ch.service.test;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.sdc2ch.service.impl.LgistTransServiceImpl.ModelMapper;
import com.sdc2ch.service.model.cost.CarTonType;
import com.sdc2ch.service.model.cost.Contract;
import com.sdc2ch.service.model.cost.PaymentFuelQty;
import com.sdc2ch.service.model.cost.RouteInfo;
import com.sdc2ch.service.model.cost.RouteNo;
import com.sdc2ch.service.model.cost.RouteTonTollCostBuilder;
import com.sdc2ch.service.model.cost.RouteTonTollCostBuilder.RouteTonTollCost;
import com.sdc2ch.service.model.item.ItemShipment;
import com.sdc2ch.service.model.item.ItemShipmentByRoute;
import com.sdc2ch.service.model.item.ItemTransferAllocate;
import com.sdc2ch.service.model.item.ItemTransport;
import com.sdc2ch.service.model.item.ItemTransport.ItemShipType;
import com.sdc2ch.service.model.item.ProductItem;
import com.sdc2ch.service.test.Vehicles.Vehicle;
import com.sdc2ch.service.test.dao.TmsOilPrice;
import com.sdc2ch.service.test.dao.TransCost;
import com.sdc2ch.service.test.dao.TransferPrinter;
import com.sdc2ch.service.transfer.FactoryType;
import com.sdc2ch.service.transfer.TransferPlan;
import com.sdc2ch.tms.io.TmsOilPriceIO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LgistTransServiceFileImpl implements ILgistTransService {
	
	private List<TmsOilPriceIO> oilPrices;
	
	public enum ModelMapper {
		_2D1_TO_1D1_40(2L, "2D1_TO_1D1_250", "2D1_40_items_chulha", "2D1", "1D1", 2),
		_2D1_TO_1D1_100(4L, "2D1_TO_1D1_250", "2D1_100_items_chulha", "2D1", "1D1", 4),
		_2D1_TO_1D1_150(10L, "2D1_TO_1D1_250", "2D1_150_items_chulha", "2D1", "1D1", 10),
		_2D1_TO_1D1_200(12L, "2D1_TO_1D1_250", "2D1_200_items_chulha", "2D1", "1D1", 12),
		
		;
		public Long id;
		public String path;
		public String src;
		public String tar;
		public long fk;
		public String data;
		
		ModelMapper(Long id, String path, String data, String src, String tar, long fk) {
			this.id= id;
			this.path = path;
			this.src = src;
			this.tar = tar;
			this.fk = fk;
			this.data = data;
		}
		
		public static ModelMapper findByModelId(Long modelId) {
			return Stream.of(ModelMapper.values()).filter(m -> m.id == modelId).findFirst().get();
		}
	}
	
	
	private ArrayListMultimap<String, ProductItem> productByFactory = ArrayListMultimap.create();
	private Map<String, RouteInfo> mapped;
	
	public LgistTransServiceFileImpl (){
		setFactoryProductItem();
		mapped = Stream.of(RouteNo.values()).map(r -> convert(r)).collect(Collectors.toMap(info -> info.getRouteNo().genarateKey(), info -> info));
		
		oilPrices = loadOilPrice();
	}
	
	private List<TmsOilPriceIO> loadOilPrice() {
		ClassPathResource res = new ClassPathResource("oilprice");
		try (BufferedReader stream = Files.newBufferedReader(Paths.get(res.getURI()), Charset.forName("UTF-8"))) {
			return stream.lines().skip(1).map(s -> convertOil(s)).collect(Collectors.toList());

		} catch  (Exception e) {
			log.error("{}", e);
		}
		return Collections.emptyList();
	}
	private TmsOilPriceIO neerLastOilPrice(String dlvyDe) {
		return oilPrices.stream().filter(o -> convertDate(dlvyDe).isAfter(convertDate(o.getStartDate())))
				.max(Comparator.comparing(TmsOilPriceIO::getStartDate)).orElse(null);
	}
	private LocalDate convertDate(String date) {
		return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"));
	}
	private TmsOilPriceIO convertOil(String s) {
		String[] strs = s.split(",");
		TmsOilPrice price = new TmsOilPrice();
		price.setStartDate(strs[1]);
		price.setEndDate(strs[2]);
		price.setOilPrice(new BigDecimal(new BigDecimal(strs[4]).intValue()));
		return price;
	}

	private RouteInfo convert(RouteNo route) {
		return RouteInfo.builder().routeNo(route).contracts(Lists.newArrayList(Contract.values())).build();
	}
	@Override
	public int write(ItemTransferAllocate itemTransferAllocate) {
		
		return 0;
	}

	@Override
	public void endOfNewRoute(Long modelId) {
		
		
	}

	@Override
	public ShipmentVolumeGroup searchVolumeItems(LocalDate fromDe, LocalDate toDe, Long id) {
		
		log.info("searchVolumeItems called");
		
		ModelMapper mapper = ModelMapper.findByModelId(id);
		
		ClassPathResource res = new ClassPathResource(mapper.path);
		try (BufferedReader stream = Files.newBufferedReader(Paths.get(res.getURI()), Charset.forName("UTF-8"))) {
			return new ShipmentVolumeGroup(mapper, stream.lines().skip(0).collect(Collectors.toList()));
		} catch  (Exception e) {
			log.error("{}", e);
		}
		return new ShipmentVolumeGroup(mapper, Collections.emptyList());
	}

	@Override
	public List<ItemShipment> searchShipmentItems(LocalDate fromDe, LocalDate toDe, ProductFactory pt) {
		
		log.info("searchShipmentItems called");
		return Collections.emptyList();
	}

	public List<String> getRouteIntoItems(Long modelId) {
		log.info("getRouteIntoItems called");
		return Collections.emptyList();
	}


	@Override
	public List<ItemTransport> searchOutGoingTransportItems(LocalDate fromDe, LocalDate toDe, String string) {
		
		log.info("searchOutGoingTransportItems called");
		
		ClassPathResource res = new ClassPathResource("2D1_250_outgoing");
		try (BufferedReader stream = Files.newBufferedReader(Paths.get(res.getURI()), Charset.forName("UTF-8"))) {
			return stream.lines().skip(1).filter(s -> {
				String[] o = s.split(",");
				
				boolean f = StringUtils.isEmpty(o[8]) || "NULL".equals(o[8]);
				if(f) {
					log.info("remove item because palett vol required have a value {}", s);
				}
				return !f;
			}).map(s -> convert(s, ItemShipType.OUT)).collect(Collectors.toList());

		} catch  (Exception e) {
			log.error("{}", e);
		}
		return Collections.emptyList();
	}


	private ItemTransport convert(String s, ItemShipType t) {
		String[] o = s.split(",");
		ItemTransport transp = new ItemTransport();
		transp.setDlvyDe(castString(o[0]));
		transp.setItemCd(castString(o[1]));
		transp.setItemNm(castString(o[2]));
		transp.setFrom(castString(o[3]));
		transp.setTo(castString(o[4]));
		transp.setShipmentQty(castDouble(o[5]));
		transp.setBoxCnt(castInteger(o[6]));
		transp.setBoxQty(castDouble(o[7]));
		transp.setPaletteCnt(castInteger(o[8]));
		transp.setPaletteQty(castDouble(o[9]));
		transp.setItemVolumeType(ItemShipType.OUT);
		return transp;
	}

	private Double castDouble(String string) {
		return StringUtils.isEmpty(string) ? 0 : Double.valueOf(string);
	}

	private Integer castInteger(String string) {
		return StringUtils.isEmpty(string) || "NULL".equals(string) ? null : Integer.valueOf(string);
	}

	private String castString(String string) {
		return string;
	}

























	
	
	@Override
	public List<ItemShipmentByRoute> getShipmentItems(LocalDate fromDe, LocalDate toDe, Long id ) {
		
		log.info("getProductItems called");
		
		List<ItemShipmentByRoute> shipments = searchAllShipmentQty(id);
		ModelMapper mapper = ModelMapper.findByModelId(id);
		for(ItemShipmentByRoute r : shipments) {
			if(r.getFctryCd().equals(mapper.src) && r.getHas() != 1) {
				r.setNewPoint(mapper.tar);
			}
		}
		
		Map<String, List<ItemShipmentByRoute>> groups = shipments.stream().collect(Collectors.groupingBy(s -> s.getDlvyDe() + s.getFctryCd() + s.getItemCd() + s.getNewPoint()));
		
		
		List<ItemShipmentByRoute> newShipments = new ArrayList<>();
		
		for(String key : groups.keySet()) {
			newShipments.add(groups.get(key).stream().reduce((a, b) -> {
				b.setShipQty(b.getShipQty() + a.getShipQty());
				return b;
			}).get());
		}
		






		
		
		
		return newShipments;

		
	}
	public List<ItemShipmentByRoute> searchAllShipmentQty (Long id) {
		
		
		ModelMapper mapper = ModelMapper.findByModelId(id);
		
		System.out.println(mapper);
		ClassPathResource res = new ClassPathResource(mapper.data );
		try (BufferedReader stream = Files.newBufferedReader(Paths.get(res.getURI()), Charset.forName("UTF-8"))) {
			return stream.lines().skip(1).filter(s -> {
				String[] o = s.split(",");
				
				boolean f = StringUtils.isEmpty(o[5]) || "NULL".equals(o[5]);
				if(f) {
					log.info("remove item because palett vol required have a value {}", s);
				}
				return !f;
			}).map(s -> convertItemShipmentByRoute(s)).collect(Collectors.toList());

		} catch  (Exception e) {
			log.error("{}", e);
		}
		return Collections.emptyList();
	}

	private ItemShipment convertProduct(String s) {
		String[] o = s.split(",");
		ItemShipment shipment = new ItemShipment();
		shipment.setDlvyDe(castString(o[0]));
		shipment.setItemCd(castString(o[1]));
		shipment.setItemNm(castString(o[2]));
		shipment.setShipmentQty(castDouble(o[3]));
		shipment.setBoxCnt(castInteger(o[4]));
		shipment.setPaletteCnt(castInteger(o[5]));
		return shipment;
	}
	private ItemShipmentByRoute convertItemShipmentByRoute(String s) {
		String[] o = s.split(",");
		ItemShipmentByRoute shipment = new ItemShipmentByRoute();
		shipment.setFctryCd(castString(o[0]));
		shipment.setDlvyDe(castString(o[1]));
		shipment.setRouteNo(castString(o[2]));
		shipment.setItemCd(castString(o[3]));
		shipment.setShipQty(castDouble(o[4]));
		shipment.setHas(castInteger(o[5]));
		shipment.setNewPoint(castString(o[0]));
		shipment.setBoxCnt(castInteger(o[6]));
		shipment.setPaletteCnt(castInteger(o[7]));
		shipment.setItemNm(castString(o[8]));
		return shipment;
	}


	@Override
	public List<ItemShipment> searchItemshipment(LocalDate fromDe, LocalDate toDe, String[] array) {
		log.info("searchItemshipment called");
		
		return Collections.emptyList();
	}

	@Override
	public List<ItemTransport> searchInCommingTransportItems(LocalDate fromDe, LocalDate toDe, String string) {
		log.info("searchInCommingTransportItems called");
		ClassPathResource res = new ClassPathResource("2D1_250_incomming");
		
		
		try (BufferedReader stream = Files.newBufferedReader(Paths.get(res.getURI()), Charset.forName("UTF-8"))) {
			return stream.lines().skip(1).filter(s -> {
				String[] o = s.split(",");
				
				boolean f = StringUtils.isEmpty(o[8]) || "NULL".equals(o[8]);
				if(f) {
					log.info("remove item because palett vol required have a value {}", s);
				}
				return !f;
			}).map(s -> convert(s, ItemShipType.IN)).collect(Collectors.toList());

		} catch  (Exception e) {
			log.error("{}", e);
		}
		return Collections.emptyList();
	}

	@Override
	public List<ProductItem> getProductItems(String id) {
		return productByFactory.get(id);
	}
	
	private void setFactoryProductItem() {
		ClassPathResource res = new ClassPathResource("product_item");
		try (BufferedReader stream = Files.newBufferedReader(Paths.get(res.getURI()), Charset.forName("UTF-8"))) {
			stream.lines().skip(1).forEach(s -> setItem(s));

		} catch  (Exception e) {
			log.error("{}", e);
		}
	}
	
	private void setItem(String s) {
		String[] products = s.split(",");
		int i = 2;
		for(FactoryType f : FactoryType.values()) {
			String sRatio = products[i++];
			ProductItem item = new ProductItem();
			item.setItemCd(products[0]);
			item.setItemNm(products[1]);
			item.setRatio(StringUtils.isEmpty(sRatio) ? 0.0 : Double.valueOf(sRatio));
			productByFactory.put(f.id, item);
		}
	}
	@Override
	public List<TransferPlan> getProductPlanItems(Long id) {
		
		ModelMapper mapper = ModelMapper.findByModelId(id);
		ClassPathResource res = new ClassPathResource(mapper.path);
		try (BufferedReader stream = Files.newBufferedReader(Paths.get(res.getURI()), Charset.forName("UTF-8"))) {
			return stream.lines().skip(1).map(s -> convertPlanItem(s)).collect(Collectors.toList());

		} catch  (Exception e) {
			log.error("{}", e);
		}
		return Collections.emptyList();
	}
	private TransferPlan convertPlanItem(String s) {
		String[] products = s.split(",");
		return TransferPlan.builder()
		.itemCd(products[0])
		.itemNm(products[1])
		.ratioBy1D1(nullSafe(products[2]))
		.ratioBy2D1(nullSafe(products[3]))
		.ratioBy3D1(nullSafe(products[4]))
		.ratioBy4D1(nullSafe(products[5]))
		.build();
	}
	
	private double nullSafe(String s) {
		return StringUtils.isEmpty(s) ? 0.0 : Double.valueOf(s);
	}
	@Override
	public void insert(List<TransferNewRoute> newroure) {
		
		TransferPrinter.printheader();
		
		for(TransferNewRoute route : newroure) {
			List<TransCost> costs = settleCost(route);
			
			for(TransCost cost : costs) {
				TransferPrinter printer = new TransferPrinter();
				BeanUtils.copyProperties(cost, printer);
				printer.setDlvyDe(route.getDlvyDe());
				printer.setFrom(route.getFrom().getId());
				printer.setTo(route.getTo().getId());
				printer.setVrn(cost.getVehicle().getVrn());
				for(ItemTransport item : cost.getVehicle().getShipInfos()) {
					printer.setShippmentQty(printer.getShippmentQty() + item.getShipmentQty());
					printer.setPalletQty(printer.getPalletQty() +  item.getPaletteQty());
				}
				
				printer.print();
				
			}
		}
	}
	private List<TransCost> settleCost(TransferNewRoute route) {
		
		String key = route.getFrom().getId() + route.getTo().getId();
		RouteInfo info = mapped.get(key);
		Contract contract = null;
		List<TransCost> costs = new ArrayList<>();
		
		if(info != null) {
			
			for(Vehicle vehicle : route.getNumOfVehicles()) {
				TransCost cost = null;
				contract = info.getContracts().stream().filter(c -> c.convert(vehicle.getTon()) == c).findFirst().orElse(null);
				cost = builling(info, contract, route.getDlvyDe());
				if(cost != null) {
					cost.setVehicle(vehicle);
					costs.add(cost);
				}
			}
		}
		
		return costs;
	}	
	private TransCost builling(RouteInfo info, Contract contract, String dlvyDe) {
		
		if(contract != null) {
			
			TransCost cost = new TransCost();
			
			PaymentFuelQty payment = contract.ctt;
			RouteNo routeNo = info.getRouteNo();
			CarTonType carTon = payment.ctt;
			cost.setCarTon(new BigDecimal(carTon.ton));
			cost.setContractCost(contract.contractCost);
			TmsOilPriceIO oilPrice = neerLastOilPrice(dlvyDe);
			
			
			int costOfoil = payment.costOfOil;
			
			if(oilPrice != null && oilPrice.getOilPrice() != null) {
				costOfoil = oilPrice.getOilPrice().intValue();
			}
			cost.setCostOfOil(costOfoil);
			cost.setTotDistance(routeNo.distance);
			
			double totOilAmt = payment.liter * routeNo.distance ;
			cost.setTotOilAmt(setScale(new BigDecimal(totOilAmt), 2));
			double totOilCost = totOilAmt * costOfoil;
			cost.setTotOilCost(setScale(new BigDecimal(totOilCost), 2));
			
			
			RouteTonTollCost tollCost = RouteTonTollCostBuilder.getInstance().getTollCost(routeNo, carTon);
			
			if(tollCost != null) {
				long totTollCost = tollCost.getTollCost();
				cost.setTotTollCost(setScale(new BigDecimal(totTollCost), 2));
				
			}

			double turnRate = routeNo.trunRate;
			cost.setTotTunRate(setScale(new BigDecimal(turnRate), 2));
			
			double transitCost  = turnRate * contract.contractCost;
			cost.setTransitCost(setScale(new BigDecimal(transitCost), 2));
			cost.setTotFullCost(setScale(cost.getTransitCost().add(cost.getTotOilCost()).add(cost.getTotTollCost()), 2));

			
			return cost;
		}
		
		return null;
	}
	private BigDecimal setScale(BigDecimal val, int lng) {
		return val.setScale(lng, BigDecimal.ROUND_HALF_UP);
	}
}
