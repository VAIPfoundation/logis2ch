package com.sdc2ch.service.impl;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.sdc2ch.service.ILgistModelService;
import com.sdc2ch.service.ILgistTransService;
import com.sdc2ch.service.model.cost.CarTonType;
import com.sdc2ch.service.model.cost.Contract;
import com.sdc2ch.service.model.cost.PaymentFuelQty;
import com.sdc2ch.service.model.cost.RouteInfo;
import com.sdc2ch.service.model.cost.RouteNo;
import com.sdc2ch.service.model.cost.RouteTonTollCostBuilder;
import com.sdc2ch.service.model.cost.RouteTonTollCostBuilder.RouteTonTollCost;
import com.sdc2ch.service.model.item.Item;
import com.sdc2ch.service.model.item.ItemProduct;
import com.sdc2ch.service.model.item.ItemShipment;
import com.sdc2ch.service.model.item.ItemShipmentByRoute;
import com.sdc2ch.service.model.item.ItemTransfer;
import com.sdc2ch.service.model.item.ItemTransferAllocate;
import com.sdc2ch.service.model.item.ItemTransport;
import com.sdc2ch.service.model.item.ItemTransport.ItemShipType;
import com.sdc2ch.service.transfer.TransferPlan;
import com.sdc2ch.tms.io.TmsOilPriceIO;
import com.sdc2ch.tms.service.ITmsOilPriceService;
import com.sdc2ch.web.admin.repo.AdmQueryBuilder;
import com.sdc2ch.web.admin.repo.dao.lgist.T_LgistModelRepository;
import com.sdc2ch.web.admin.repo.dao.lgist.T_LgistRouteTransMstrRepository;
import com.sdc2ch.web.admin.repo.domain.lgist.T_LGIST_MODEL;
import com.sdc2ch.web.admin.repo.domain.lgist.T_LGIST_PRODUCT_STD;
import com.sdc2ch.web.admin.repo.domain.lgist.T_LGIST_ROUTE_TRANS_COST;
import com.sdc2ch.web.admin.repo.domain.lgist.T_LGIST_ROUTE_TRANS_DTLS;
import com.sdc2ch.web.admin.repo.domain.lgist.T_LGIST_ROUTE_TRANS_MSTR;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LgistTransServiceImpl implements ILgistTransService {
	

	@Autowired AdmQueryBuilder admBuilder;
	@Autowired T_LgistRouteTransMstrRepository repo;
	@Autowired T_LgistModelRepository modelRepo;
	@Autowired ITmsOilPriceService oilPriceSvc;
	@Autowired ILgistModelService modelSvc;
	
	private Map<String, RouteInfo> mapped;
	private List<TmsOilPriceIO> oilPrices;
	
	public enum ModelMapper {
		_2D1_TO_1D1_40(2L, "2D1_TO_1D1_250", "2D1", "1D1", 2),
		_2D1_TO_1D1_100(4L, "2D1_TO_1D1_250", "2D1", "1D1", 4),
		_2D1_TO_3D1_40(5L, "2D1_TO_3D1_40", "2D1", "3D1", 2),
		_2D1_TO_3D1_100(6L, "2D1_TO_3D1_100", "2D1", "3D1", 4),
		_2D1_TO_1D1_150(10L, "2D1_TO_1D1_250", "2D1", "1D1", 10),
		_2D1_TO_1D1_200(9L, "2D1_TO_1D1_250", "2D1", "1D1", 9),
		_2D1_TO_1D1_200_2(12L, "2D1_TO_1D1_250", "2D1", "1D1", 12),
		_2D1_TO_1D1_ALL(99L, "2D1_TO_1D1_250", "2D1", "1D1", 99),
		_2D1_TO_1D1_ALL2(100L, "2D1_TO_1D1_250", "2D1", "1D1", 100),
		
		;
		public Long id;
		public String path;
		public String src;
		public String tar;
		public long fk;
		
		ModelMapper(Long id, String path, String src, String tar, long fk) {
			this.id= id;
			this.path = path;
			this.src = src;
			this.tar = tar;
			this.fk = fk;
		}
		
		public static ModelMapper findByModelId(Long modelId) {
			return Stream.of(ModelMapper.values()).filter(m -> m.id == modelId).findFirst().get();
		}
	}
	
	@PostConstruct
	private void init() {
		mapped = Stream.of(RouteNo.values()).map(r -> convert(r)).collect(Collectors.toMap(info -> info.getRouteNo().genarateKey(), info -> info));
		oilPrices = oilPriceSvc.findAll();
		
	}

	private RouteInfo convert(RouteNo route) {
		return RouteInfo.builder().routeNo(route).contracts(Lists.newArrayList(Contract.values())).build();
	}

	@Override
	public List<Item> findAll() {
		List<Item> items = new ArrayList<>();
		
		List<?> results = admBuilder.storedProcedureResultCall(ILgistTransService.ITEM_LIST);
		if(results != null && !results.isEmpty()) {
			for(Object objs : results) {
				Object[] o = (Object[]) objs;
				Item item = new Item();
				item.setItemCd(castString(o[0]));
				item.setItemNm(castString(o[1]));
				item.setBoxCnt(castInteger(o[2]));
				item.setPaletteCnt(castInteger(o[3]));
				items.add(item);
			}
		}
		
		
		final List<Item> item2 = new ArrayList<>();
		
		ClassPathResource res = new ClassPathResource("paletteqty");
		try (BufferedReader stream = Files.newBufferedReader(Paths.get(res.getURI()), Charset.forName("UTF-8"))) {
			stream.lines().skip(1).forEach(s -> {
				String[] o = s.split(",");
				Item item = new Item();
				item.setItemCd(castString(o[0]));
				item.setItemNm(castString(o[1]));
				item.setBoxCnt(new Integer(o[2]));
				item.setPaletteCnt(new Integer(o[3]));
				item2.add(item);
				
			});

		} catch  (Exception e) {
			log.error("{}", e);
		}
		
		
		List<String> item3 = item2.stream().map(i -> i.getItemCd()).collect(Collectors.toList());
		
		items.removeIf(itm -> item3.contains(itm.getItemCd()) );
		items.addAll(item2);
		
		
		return items;
	}
	

	@Override
	public List<ItemTransport> searchInCommingTransportItems(String fctryCd, String fromDe, String toDe) {
		List<?> results = admBuilder.storedProcedureResultCall(ILgistTransService.incomming, fromDe, toDe, fctryCd);
		List<ItemTransport> transps = new ArrayList<>();
		if(results != null && !results.isEmpty()) {
			for(Object objs : results) {
				
				Object[] o = (Object[]) objs;
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
				transp.setItemVolumeType(ItemShipType.IN);
				transps.add(transp);
			}
		}
		return transps;
	}

	@Override
	public List<ItemTransport> searchOutGoingTransportItems(String fctryCd, String fromDe, String toDe) {
		List<?> results = admBuilder.storedProcedureResultCall(ILgistTransService.outgoing, fromDe, toDe, fctryCd);
		List<ItemTransport> transps = new ArrayList<>();
		if(results != null && !results.isEmpty()) {
			for(Object objs : results) {
				Object[] o = (Object[]) objs;
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
				transps.add(transp);
			}
		}
		return transps;
	}
	
	@Override
	public List<ItemShipment> searchShipmentItems(String fctryCd, String fromDe, String toDe) {
		List<?> results = admBuilder.storedProcedureResultCall(ILgistTransService.SHIP_QTY, fromDe, toDe, fctryCd);
		List<ItemShipment> items = new ArrayList<>();
		if(results != null && !results.isEmpty()) {
			for(Object objs : results) {
				Object[] o = (Object[]) objs;
				ItemShipment shipment = new ItemShipment();
				shipment.setDlvyDe(castString(o[0]));
				shipment.setItemCd(castString(o[1]));
				shipment.setShipmentQty(castDouble(o[2]));
				shipment.setItemNm(castString(o[3]));
				shipment.setPaletteCnt(castInteger(o[4]));
				shipment.setBoxCnt(castInteger(o[5]));
				items.add(shipment);
			}

		}
		
		return items;
	}
	
	private String castString(Object o) {
		return o == null ? "" : o.toString();
	}
	private Double castDouble(Object o) {
		return o == null ? null : (Double) o;
	}
	private Integer castInteger(Object o) {
		return o == null ? null : (Integer) o;
	}


	@Override
	public List<ItemTransfer> searchItemTransferById(Long modelId) {
		return null;
	}
	public List<ItemTransfer> searchItemTransferById(String fctryCd, String fromDe, String toDe, final String toFctryCd) {
		List<ItemTransport> transps = searchInCommingTransportItems(fctryCd, fromDe, toDe);
		return transps.stream().map(it -> convert(it, toFctryCd)).collect(Collectors.toList());
	}

	private ItemTransfer convert(ItemTransport it, String toFctryCd) {
		ItemTransfer itf = new ItemTransfer();
		BeanUtils.copyProperties(it, itf);
		itf.setTo(toFctryCd);
		itf.setRatio(100);
		return itf;
	}

	@Override
	public void endOfNewRoute(Long modelId) {
		Optional<T_LGIST_MODEL> oModel = modelRepo.findById(modelId);
		oModel.ifPresent(m -> {
			m.setSttus(2);
			modelRepo.save(m);
		});
	}


	@Override
	public int insertNewRoute(Long modelId, ItemTransferAllocate itemTransferAllocate) {
		itemTransferAllocate.setModelId(modelId);
		return insertNewRoute(itemTransferAllocate);
	}

	@Override
	public int insertNewRoute(ItemTransferAllocate itemTransferAllocate) {
		T_LGIST_ROUTE_TRANS_MSTR mstr = new T_LGIST_ROUTE_TRANS_MSTR();
		mstr.setModelId(itemTransferAllocate.getModelId());
		mstr.setFrom(itemTransferAllocate.getFrom());
		mstr.setTo(itemTransferAllocate.getTo());
		mstr.setDlvyDe(itemTransferAllocate.getDlvyDe());
		mstr.setCar_ton14(itemTransferAllocate.getTon_14());
		mstr.setCar_ton8(itemTransferAllocate.getTon_8());
		mstr.setCar_ton7_5(itemTransferAllocate.getTon_7_5());
		mstr.setCar_ton5(itemTransferAllocate.getTon_5());
		mstr.setCar_ton4_5(itemTransferAllocate.getTon_4_5());
		mstr.setCar_ton3_5(itemTransferAllocate.getTon_3_5());
		mstr.setCar_ton2_5(itemTransferAllocate.getTon_2_5());
		
		List<T_LGIST_ROUTE_TRANS_DTLS> listDtls = new ArrayList<>();
		for(ItemShipment ship : itemTransferAllocate.getItemShipMents()) {
			T_LGIST_ROUTE_TRANS_DTLS dtls = new T_LGIST_ROUTE_TRANS_DTLS();
			dtls.setItemCd(ship.getItemCd());
			dtls.setItemNm(ship.getItemNm());
			dtls.setPalletQty(setScale(new BigDecimal(ship.getPaletteQty()), 2));
			dtls.setShipmentQty(new BigDecimal(ship.getShipmentQty()).intValue());
			dtls.setLgistRouteTransMstrFk(mstr);
			listDtls.add(dtls);
		}
		List<T_LGIST_ROUTE_TRANS_COST> costs = getT_LGIST_ROUTE_TRANS_COST(itemTransferAllocate);
		costs.forEach(c -> {c.setLgistRouteTransMstrFk(mstr);});
		mstr.setDtls(listDtls);
		mstr.setCosts(costs);
		
		Optional<T_LGIST_MODEL> oModel = modelRepo.findById(itemTransferAllocate.getModelId());
		
		oModel.ifPresent(m -> {
			m.setAnalsCnt(m.getAnalsCnt() + 1);
			costs.forEach(c -> {
				String cost = compute(m, c);
				m.setCostAfter(cost);
			});
			
			modelRepo.save(m);
			
		});
		
		return new BigDecimal(repo.save(mstr).getId()).intValue();
	}
	
	private String compute(T_LGIST_MODEL m, T_LGIST_ROUTE_TRANS_COST c) {
		return c.getTotFullCost().add(new BigDecimal(nullSafe(m.getCostAfter()))).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
	}

	private List<T_LGIST_ROUTE_TRANS_COST> getT_LGIST_ROUTE_TRANS_COST(ItemTransferAllocate ita) {
		
		String key = ita.getFrom() + ita.getTo();
		RouteInfo info = mapped.get(key);
		Contract contract = null;
		List<T_LGIST_ROUTE_TRANS_COST> costs = new ArrayList<>();
		
		TmsOilPriceIO oilPrice = neerLastOilPrice(ita.getDlvyDe());
		
		if(info != null) {
			
			for(CarTonType ty : CarTonType.values()) {
				
				T_LGIST_ROUTE_TRANS_COST cost = null;
				
				switch(ty) {
				case TON_14:
					if(ita.getTon_14() != 0) {
						contract = info.getContracts().stream().filter(c -> (Contract.TON_14 == c)).findFirst().orElse(null);
						cost = getT_LGIST_ROUTE_TRANS_COST(oilPrice, info, ita.getTon_14(), contract);
					}
					break;
				case TON_2_5:
					if(ita.getTon_2_5() != 0) {
						contract = info.getContracts().stream().filter(c -> (Contract.TON_2_5 == c)).findFirst().orElse(null);
						cost = getT_LGIST_ROUTE_TRANS_COST(oilPrice, info, ita.getTon_2_5(), contract);
					}
					break;
				case TON_3_5:
					if(ita.getTon_3_5() != 0) {
						contract = info.getContracts().stream().filter(c -> (Contract.TON_3_5 == c)).findFirst().orElse(null);
						cost = getT_LGIST_ROUTE_TRANS_COST(oilPrice, info, ita.getTon_3_5(), contract);
					}
					break;
				case TON_4_5:
					if(ita.getTon_4_5() != 0) {
						contract = info.getContracts().stream().filter(c -> (Contract.TON_4_5 == c)).findFirst().orElse(null);
						cost = getT_LGIST_ROUTE_TRANS_COST(oilPrice, info, ita.getTon_4_5(), contract);
					}
					break;
				case TON_5:
					if(ita.getTon_5() != 0) {
						contract = info.getContracts().stream().filter(c -> (Contract.TON_5 == c)).findFirst().orElse(null);
						cost = getT_LGIST_ROUTE_TRANS_COST(oilPrice, info, ita.getTon_5(), contract);
					}
					break;
				case TON_7_5:
					if(ita.getTon_7_5() != 0) {
						contract = info.getContracts().stream().filter(c -> (Contract.TON_7_5 == c)).findFirst().orElse(null);
						cost = getT_LGIST_ROUTE_TRANS_COST(oilPrice, info, ita.getTon_7_5(), contract);
					}
					break;
				case TON_8:
					if(ita.getTon_8() != 0) {
						contract = info.getContracts().stream().filter(c -> (Contract.TON_8 == c)).findFirst().orElse(null);
						cost = getT_LGIST_ROUTE_TRANS_COST(oilPrice, info, ita.getTon_8(), contract);
					}
					break;
				default:
					break;
				
				}
				
				if(cost != null) {
					costs.add(cost);
					System.out.println(cost);
				}
			}
		}
		
		return costs;
	}
	
	private TmsOilPriceIO neerLastOilPrice(String dlvyDe) {
		return oilPrices.stream().filter(o -> convertDate(dlvyDe).isAfter(convertDate(o.getStartDate())))
				.max(Comparator.comparing(TmsOilPriceIO::getStartDate)).orElse(null);
	}
	private LocalDate convertDate(String date) {
		return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"));
	}
	private T_LGIST_ROUTE_TRANS_COST getT_LGIST_ROUTE_TRANS_COST(TmsOilPriceIO oilPrice, RouteInfo info, int numOfVehicle , Contract contract) {
		
		if(contract != null) {
			
			T_LGIST_ROUTE_TRANS_COST cost = new T_LGIST_ROUTE_TRANS_COST();
			
			PaymentFuelQty payment = contract.ctt;
			RouteNo routeNo = info.getRouteNo();
			CarTonType carTon = payment.ctt;
			cost.setCarTon(new BigDecimal(carTon.ton));
			cost.setNumOfVehicle(numOfVehicle);
			cost.setContractCost(contract.contractCost);
			
			int oilCost = payment.costOfOil;
			if(oilPrice != null && oilPrice.getOilPrice() != null) {
				oilCost = oilPrice.getOilPrice().intValue();
				
			}
			cost.setCostOfOil(oilCost);
			
			cost.setTotDistance(routeNo.distance * numOfVehicle);
			
			double totOilAmt = payment.liter * routeNo.distance * numOfVehicle;
			cost.setTotOilAmt(setScale(new BigDecimal(totOilAmt), 2));
			double totOilCost = totOilAmt * oilCost;
			cost.setTotOilCost(setScale(new BigDecimal(totOilCost), 2));
			
			
			RouteTonTollCost tollCost = RouteTonTollCostBuilder.getInstance().getTollCost(routeNo, carTon);
			
			if(tollCost != null) {
				long totTollCost = tollCost.getTollCost() * numOfVehicle;
				cost.setTotTollCost(setScale(new BigDecimal(totTollCost), 2));
				
			}

			double turnRate = routeNo.trunRate * numOfVehicle;
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

	@Override
	public List<ItemProduct> searchProductItems(String fctryCd, String productDe) {
		
		String fromDe = productDe;
		LocalDate _date = LocalDate.parse(productDe, DateTimeFormatter.ofPattern("yyyyMMdd")).plusDays(1);
		String toDe = _date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String query = ILgistTransService.ITEM_PRODUCT;
		query = query.replaceAll("#1", fromDe);
		query = query.replaceAll("#2", toDe);
		query = query.replaceAll("#3", fctryCd);
		
		List<?> results = admBuilder.storedProcedureResultCall(query);
		List<ItemProduct> products = new ArrayList<>();
		if(results != null && !results.isEmpty()) {
			for(Object objs : results) {
				Object[] o = (Object[]) objs;
				ItemProduct product = new ItemProduct();
				product.setProductDe(castDateString(o[0]));
				product.setItemCd(castString(o[1]));
				product.setItemNm(castString(o[2]));
				product.setProductQty(castString(o[3]) == null ? null : new BigDecimal(castString(o[3])).doubleValue());
				product.setBoxCnt(castInteger(o[4]));
				product.setPaletteCnt(castInteger(o[5]));
				products.add(product);
			}
		}
		return products;
	}

	private String castDateString(Object object) {
		if(object != null) {
			Timestamp ts = (Timestamp) object;
			return new SimpleDateFormat("yyyyMMdd").format(new Date(ts.getTime()));
		}
		return null;
	}

	@Override
	public List<ItemProduct> searchProductItems(Long modelId, String fctryCd, String productDe) {
		
		String fromDe = productDe;
		LocalDate _date = LocalDate.parse(productDe, DateTimeFormatter.ofPattern("yyyyMMdd")).plusDays(1);
		String toDe = _date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		
		List<?> results = admBuilder.storedProcedureResultCall(ILgistTransService.ITEM_PRODUCT, fromDe, toDe, fctryCd);
		List<ItemProduct> products = new ArrayList<>();
		if(results != null && !results.isEmpty()) {
			for(Object objs : results) {
				Object[] o = (Object[]) objs;
				ItemProduct product = new ItemProduct();
				product.setProductDe(castDateString(o[0]));
				product.setItemCd(castString(o[1]));
				product.setItemNm(castString(o[2]));
				product.setProductQty(castString(o[3]) == null ? null : new BigDecimal(castString(o[3])).doubleValue());
				product.setBoxCnt(castInteger(o[4]));
				product.setPaletteCnt(castInteger(o[5]));
				products.add(product);
			}
		}
		
		return products;
	}

	@Override
	public List<ItemTransport> searchInCommingTransportItems(Long modelId, String fctryCd, String fromDe, String toDe) {
		












		
		List<?> results = admBuilder.storedProcedureResultCall(ILgistTransService.incomming, fromDe, toDe, fctryCd);
		List<ItemTransport> transps = new ArrayList<>();
		if(results != null && !results.isEmpty()) {
			for(Object objs : results) {
				
				Object[] o = (Object[]) objs;
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
				transp.setItemVolumeType(ItemShipType.IN);
				transps.add(transp);
			}
		}
		return transps;
	}

	@Override
	public List<ItemTransport> searchOutGoingTransportItems(Long modelId, String fctryCd, String fromDe, String toDe) {
		












		
		List<?> results = admBuilder.storedProcedureResultCall(ILgistTransService.outgoing, fromDe, toDe, fctryCd);
		List<ItemTransport> transps = new ArrayList<>();
		if(results != null && !results.isEmpty()) {
			for(Object objs : results) {
				Object[] o = (Object[]) objs;
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
				transps.add(transp);
			}
		}
		return transps;
	}

	@Override
	public List<ItemTransfer> searchItemTransferById(Long modelId, String fctryCd, String fromDe, String toDe,
			String toFctryCd) {
		
		return null;
	}
	
	
	private List<String> getModelRouteNo(Long modelId){
		
		System.out.println(ILgistTransService.route_no);
		List<?> results = admBuilder.createSelectNativeQuery(ILgistTransService.route_no, modelId);
		List<String> routes = new ArrayList<>();
		if(results != null && !results.isEmpty()) {
			for(Object objs : results) {
				routes.add(castString(objs));
			}
			
		}
		return routes;
	}

	@Override
	public List<ItemShipment> searchShipmentItems(Long modelId, String fctryCd, String fromDe, String toDe) {
		
		List<String> routes = getModelRouteNo(modelId);
		Object[] params = { fctryCd, fromDe, toDe, routes.stream().collect(Collectors.joining("','", "'", "'"))};
		String query = routes.isEmpty() ? ILgistTransService.SHIP_QTY_SP_ALL : ILgistTransService.SHIP_QTY_SP;
		List<Object[]> results = admBuilder.storedProcedureResultCall(query, params);
		List<ItemShipment> items = new ArrayList<>();
		if(results != null && !results.isEmpty()) {
			for(Object objs : results) {
				Object[] o = (Object[]) objs;
				ItemShipment shipment = new ItemShipment();
				shipment.setDlvyDe(castString(o[0]));
				shipment.setItemCd(castString(o[1]));
				shipment.setShipmentQty(castDouble(o[2]));
				items.add(shipment);
			}

		}
		
		return items;
	}

	@Override
	public List<ItemProduct> searchProductItems(Long modelId, String fctryCd, String fromDe, String toDe) {
		LocalDate _date = LocalDate.parse(toDe, DateTimeFormatter.ofPattern("yyyyMMdd")).plusDays(1);
		toDe = _date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String query = ILgistTransService.ITEM_PRODUCT;



		
		List<?> results = admBuilder.storedProcedureResultCall(query, fromDe, toDe, fctryCd);
		List<ItemProduct> products = new ArrayList<>();
		if(results != null && !results.isEmpty()) {
			for(Object objs : results) {
				Object[] o = (Object[]) objs;
				ItemProduct product = new ItemProduct();
				product.setProductDe(castDateString(o[0]));
				product.setItemCd(castString(o[1]));
				product.setItemNm(castString(o[2]));
				product.setProductQty(castString(o[3]) == null ? null : new BigDecimal(castString(o[3])).doubleValue());
				product.setBoxCnt(castInteger(o[4]));
				product.setPaletteCnt(castInteger(o[5]));
				products.add(product);
			}
		}
		return products;
	}















	@Override
	public List<TransferPlan> getProductPlanItems(Long id) {
		T_LGIST_MODEL model =  modelSvc.findById(id);
		System.out.println(model.getProducts());
		return model.getProducts().stream().map(p -> convertPlanItem(p)).collect(Collectors.toList());
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
	
	private TransferPlan convertPlanItem(T_LGIST_PRODUCT_STD p) {
		
		return TransferPlan.builder()
				.itemCd(p.getItemCd())
				.itemNm(p.getItemNm())
				.ratioBy1D1(new BigDecimal(p.getP1()).doubleValue())
				.ratioBy2D1(new BigDecimal(p.getP2()).doubleValue())
				.ratioBy3D1(new BigDecimal(p.getP3()).doubleValue())
				.ratioBy4D1(new BigDecimal(p.getP4()).doubleValue())
				.build();
	}
	
	private double nullSafe(String s) {
		return StringUtils.isEmpty(s) ? 0.0 : Double.valueOf(s);
	}
	
	
	public List<ItemShipmentByRoute> change(Long modelId, String from, String to) {
		


		String query = ILgistTransService.ITEM_ROUTE_SHIPMENT;



		
		T_LGIST_MODEL model =  modelSvc.findById(modelId);
		List<?> results = admBuilder.storedProcedureResultCall(query, from, to, model.getFctryTarget(), modelId);
		List<ItemShipmentByRoute> shipments = new ArrayList<>();
		if(results != null && !results.isEmpty()) {
			for(Object objs : results) {
				Object[] o = (Object[]) objs;
				ItemShipmentByRoute shipment = new ItemShipmentByRoute();
				shipment.setFctryCd(castString(o[0]));
				shipment.setDlvyDe(castString(o[1]));
				shipment.setRouteNo(castString(o[2]));
				shipment.setItemCd(castString(o[3]));
				shipment.setShipQty(castDouble(o[4]));
				shipment.setHas(castInteger(o[5]));
				shipment.setNewPoint(castString(o[0]));
				shipments.add(shipment);
			}
		}
		
		return shipments;
		
	}
	
	public List<ItemShipmentByRoute> searchAllShipmentQty (Long modelId, String from, String to) {
		List<ItemShipmentByRoute> shipments = change(modelId, from, to);
		T_LGIST_MODEL model =  modelSvc.findById(modelId);
		for(ItemShipmentByRoute r : shipments) {
			if(r.getFctryCd().equals(model.getPointNm()) && r.getHas() != 1) {
				r.setNewPoint(model.getFctryTarget());
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
	
	
}
