package com.sdc2ch.service.test.vo2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.sdc2ch.service.model.item.ItemShipment;
import com.sdc2ch.service.model.item.ItemTransport;
import com.sdc2ch.service.test.ProductFactory;
import com.sdc2ch.service.test.TransferNewRoute;
import com.sdc2ch.service.test.TransferRouteGenerator;
import com.sdc2ch.service.test.Vehicles.Vehicle;
import com.sdc2ch.service.test.vo.AbstractFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransferFactory extends AbstractFactory {
	
	private List<ItemTransport> transfers = new ArrayList<>();
	private List<ItemTransport> incommings = new ArrayList<>();
	
	private AtomicInteger inc = new AtomicInteger();

	public TransferFactory(ProductFactory f) {
		super(f);
	}
	
	public ProductFactory getOrgFactory() {
		return current;
	}


	public void product(ItemShipment order, ProductFactory to) {
		
		try {
			
			if(current == to) {

				return;
			}
			
			ItemTransport item = new ItemTransport();
			BeanUtils.copyProperties(order, item);
			
			double orderQty = item.getShipmentQty();
			double ratio = current.getProductRatio(order.getItemCd());
			double newProductQty = orderQty * ratio;
			
			item.setBoxQty(setScale(newProductQty / item.getBoxCnt()));
			item.setPaletteQty(setScale(item.getBoxQty() / item.getPaletteCnt()));
			item.setFrom(getId());
			item.setTo(to.getId());
			transfers.add(item);
			
		}catch (Exception e) {
			

		}
		
	}

	public List<TransferNewRoute> getEgo(String dlvyDe) {
		List<TransferNewRoute> routes = new ArrayList<>();
		Map<String, List<ItemTransport>> transMap = transfers.stream().collect(Collectors.groupingBy(t -> t.getTo()));
		List<Vehicle> vehicles = new ArrayList<>();
		sort(current).forEach(k1 -> vehicles.addAll(current.getNumOfRetention().get(k1).listVehicle()));
		
		LinkedBlockingQueue<Vehicle> transqueue = new LinkedBlockingQueue<>(vehicles);
		
		for(String k : transMap.keySet()) {
			TransferNewRoute route = new TransferNewRoute(inc.incrementAndGet());
			List<Vehicle> _new = TransferRouteGenerator.builder().from(current).queue(transqueue).transferItems(transMap.get(k)).build().allocated();
			route.setFrom(current);
			route.setNumOfVehicles(_new);
			route.setDlvyDe(dlvyDe);
			route.setRouteName(route.getFrom().getId() + "," + k);
			route.setTo(operations.findByFctryCd(k).getOrgFactory());
			routes.add(route);
		}
		return routes;
	}
	
	private double setScale(double d) {
		return new BigDecimal(d).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
	}

	public void clear() {
		current.clear();
		transfers.clear();
		incommings.clear();
	}

	public void addIncomming(ItemTransport peek) {
		incommings.add(peek);
	}

	public List<ItemTransport> order(List<TransferFactory> productFs) {
		
		List<ItemTransport> unknowns = new ArrayList<>();
		BlockingQueue<ItemTransport> order = new LinkedBlockingQueue<>(incommings);
		while(!order.isEmpty()) {
			boolean supported = false;
			for(TransferFactory tf : productFs) {
				if(tf.supported(order.peek())) {
					supported = true;
					if(!tf.getId().equals(order.peek().getTo())) {
						ItemTransport trans = order.poll();
						tf.product(trans, find(trans.getTo()));
					}else {

						
						order.poll();
					}
					break;
				}else {

				}
			}
			
			if(!supported) {
				ItemTransport _notsupported = order.poll();


				if(!_notsupported.getTo().equals(current.getId())) {
				}
				unknowns.add(_notsupported);

			}
		}
		
		return unknowns;
	}

	public ProductFactory find(String to) {
		return operations.findByFctryCd(to).getOrgFactory();
	}


}
