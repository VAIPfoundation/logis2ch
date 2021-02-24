package com.sdc2ch.service.test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.sdc2ch.service.model.item.ItemShipment;
import com.sdc2ch.service.model.item.ItemShipmentByRoute;
import com.sdc2ch.service.model.item.ItemTransport;
import com.sdc2ch.service.test.LgistTransServiceFileImpl.ModelMapper;
import com.sdc2ch.service.test.vo2.OutgoingEgoBuilder;
import com.sdc2ch.service.test.vo2.ShippmentBuilder;
import com.sdc2ch.service.test.vo2.TransferFactory;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


@Getter
@Builder
@Slf4j
public class TransferDivider {
	
	private TransferVolume volume;
	private List<ItemShipment> productItems;

	public List<TransferNewRoute> start() {
		
		
		List<TransferFactory> productFs = volume.getOperations().transferFactroy();
		
		long dateDiff = getDateDiff();
		List<TransferNewRoute> routes = new ArrayList<>();
		for(long i = 0 ; i < dateDiff; i++) {
			try {
				LocalDate current = volume.getOperations().getFromDe().plusDays(i);
				String currentDe = convertDateToString(current);
				
				ModelMapper mapper = ModelMapper.findByModelId(volume.getId());
				
				OutgoingEgoBuilder outgoing = new OutgoingEgoBuilder(currentDe);
				outgoing.setOutgoings(findOutgoingByDlvyDe(currentDe));
				outgoing.setToFactory(findF(mapper.src));
				outgoing.setProductFactorys(productFs);
				outgoing.setTransFactory("1D1");
				outgoing.build();
				
				ShippmentBuilder shippment = new ShippmentBuilder(currentDe);

				
				List<ItemShipmentByRoute> shippments = findShippmentByDlvyDe(currentDe);
				
				
				Map<String, List<ItemShipmentByRoute>> mapped = shippments.stream().collect(Collectors.groupingBy(ItemShipmentByRoute::getNewPoint));
				
				mapped.keySet().forEach(k -> {
					shippment.putShippments(findF(k), mkNewShipments(currentDe, mapped.get(k)));
				});

				shippment.setProductFactorys(productFs);
				shippment.setToFactory(findF(mapper.src));
				shippment.setTransFactory("1D1");
				shippment.build();
				routes.addAll(shippment.getNewRoutes());
				
			}finally {
				
				productFs.forEach(p -> p.clear());
			}
		}
		return routes;
	}
	
	
	
	
	
	private List<ItemShipment> mkNewShipments(String dlvyDe, List<ItemShipmentByRoute> list) {
		
		return list.stream().map(r -> {
			ItemShipment shipment = new ItemShipment();
			shipment.setBoxCnt(r.getBoxCnt());
			shipment.setPaletteCnt(r.getPaletteCnt());
			shipment.setItemCd(r.getItemCd());
			shipment.setItemNm(r.getItemNm());
			shipment.setDlvyDe(dlvyDe);
			shipment.setShipmentQty(r.getShipQty());
			return shipment;
		}).collect(Collectors.toList());
	}





	
	
	private String convertDateToString(LocalDate date) {
		return volume.getOperations().convertDateToString(date);
	}
	private long getDateDiff() {
		return ChronoUnit.DAYS.between(volume.getOperations().getFromDe(), volume.getOperations().getToDe())+1;
	}
	private List<ItemShipmentByRoute> findShippmentByDlvyDe(String date){
		return volume.getShipmentItems(date);
	}
	private List<ItemTransport> findOutgoingByDlvyDe(String date){
		return volume.getOutgoings(date);
	}
	private ProductFactory findF(String fctryCd) {
		return volume.getOperations().findByFctryCd(fctryCd).getOrgFactory();
	}

}
