package com.sdc2ch.service.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.sdc2ch.service.model.cost.CarTonType;
import com.sdc2ch.service.model.item.ItemTransport;
import com.sdc2ch.service.test.Vehicles.Vehicle;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
public class TransferRouteGenerator {
	
	private ProductFactory from;
	private LinkedBlockingQueue<Vehicle> queue;
	private List<ItemTransport> transferItems;
	

	public List<Vehicle> allocated() {
		
		String to = null;
		List<Vehicle> vehicles = new ArrayList<>();
		
		LinkedBlockingQueue<ItemTransport> transqueue = new LinkedBlockingQueue<>(transferItems);




		
		
		while (!transqueue.isEmpty()) {
			
			try {
				to = check(queue, transqueue);
				
				Vehicle vehicle = setShippment(queue.peek(), transqueue);
				
				if(vehicle.getVolumePercent() < 62.5) {
					

					List<Vehicle> _v = new ArrayList<>(queue);
					
					Set<CarTonType> available = _v.stream().map(v -> v.getTon())
							.sorted(Comparator.comparingDouble(CarTonType::getParretVolume).reversed())
							.collect(Collectors.toSet());
					
					
					CarTonType ton = Utils.neerTon(available, vehicle.getParretQty());
					
					
					if(available.size() == 1 &&  vehicle.getVolumePercent() == 0) {
						available.forEach(c -> {
							
							if(ton == c) {
								
								
								ItemTransport org = transqueue.poll();
								int cnt = new BigDecimal(org.getPaletteQty() / ton.parretVolume).intValue();


								
								for(int i = 0 ; i< cnt ; i++) {
									double p = new BigDecimal((ton.parretVolume - 0.2) * org.getBoxCnt() * org.getPaletteCnt()).setScale(2,  BigDecimal.ROUND_DOWN).doubleValue();
									ItemTransport __new = new ItemTransport();
									BeanUtils.copyProperties(org, __new);
									__new.setShipmentQty(Double.valueOf(p));
									__new.setPaletteQty(new BigDecimal(p / org.getBoxCnt() / org.getPaletteCnt()).setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
									__new.setBoxQty(new BigDecimal(p / org.getBoxCnt()).setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
									org.decrement(__new);
									transqueue.add(org);
									transqueue.add(__new);
								}
								
							}
							
						});
						
					}
					
					boolean isAdded = false;
					for(Vehicle v : _v) {
						if(ton == v.getTon()) {
							
							if(v != vehicle) {
								v.copy(vehicle);
								vehicle.clear();
							}
							vehicles.add(v);
							queue.remove(v);
							isAdded = true;
							break;
						}
					}
					
					
					if(!isAdded) {
						vehicles.add(queue.poll());
					}
				}else {
					vehicles.add(queue.poll());
				}
			}catch (Exception e) {
				e.printStackTrace();
				
			}
		}
		
		return vehicles;
	}
	
	private String check(LinkedBlockingQueue<Vehicle> queue, LinkedBlockingQueue<ItemTransport> transqueue) {
		
		
		if(queue.isEmpty() && !transqueue.isEmpty()) {
			queue.offer(from.getNumOfRetention().get(CarTonType.TON_14).getTempCar());
		}
		List<Vehicle> _v = new ArrayList<>(queue);
		Set<CarTonType> available = _v.stream().map(v -> v.getTon())
				.sorted(Comparator.comparingDouble(CarTonType::getParretVolume).reversed())
				.collect(Collectors.toSet());
		
		ItemTransport ti = transqueue.peek();
		
		
		boolean canAdded = false;
		for(CarTonType ct : available) {
			if(ct.parretVolume > ti.getPaletteQty()) {
				canAdded = true;
				break;
			}
		}
		
		if(!canAdded) {
			
			try {
				CarTonType carton = available.stream().max(Comparator.comparingDouble(ct -> ct.parretVolume)).get();
				int cnt = new BigDecimal(ti.getPaletteQty() / carton.parretVolume).intValue();


				
				for(int i = 0 ; i< cnt ; i++) {
					double p = new BigDecimal((carton.parretVolume - 0.2) * ti.getBoxCnt() * ti.getPaletteCnt()).setScale(2,  BigDecimal.ROUND_DOWN).doubleValue();
					ItemTransport __new = new ItemTransport();
					BeanUtils.copyProperties(ti, __new);
					__new.setShipmentQty(Double.valueOf(p));
					__new.setPaletteQty(new BigDecimal(p / ti.getBoxCnt() / ti.getPaletteCnt()).setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
					__new.setBoxQty(new BigDecimal(p / ti.getBoxCnt()).setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
					ti.decrement(__new);
					transqueue.add(__new);
				}
			}catch (Exception e) {
				
				
				System.out.println();
			}

		}
		return ti.getTo();
		
	}

	private Vehicle setShippment(Vehicle vehicle, LinkedBlockingQueue<ItemTransport> transqueue) {
		ItemTransport org = null;
		
		while((org = transqueue.poll()) != null) {

			if(org.getPaletteQty() > 100) {
				log.info("물량의 크기가 엄청 크기 때문에 차량을 배차할 수 없습니다. {}-> {} {} {}", org.getDlvyDe(), org.getPaletteQty(), org.getItemCd(), org.getItemNm());
				throw new RuntimeException();
			}
			
			ItemTransport _new = new ItemTransport();
			BeanUtils.copyProperties(org, _new);
			_new.setFrom(from.getId());
			
			
			if(_new.getPaletteQty() > 16) {
				
				int cnt = new BigDecimal(_new.getPaletteQty() / 16).intValue();


				
				for(int i = 0 ; i< cnt ; i++) {
					double p = new BigDecimal(15.8 * _new.getBoxCnt() * _new.getPaletteCnt()).setScale(2,  BigDecimal.ROUND_DOWN).doubleValue();
					ItemTransport __new = new ItemTransport();
					BeanUtils.copyProperties(org, __new);
					__new.setShipmentQty(Double.valueOf(p));
					__new.setPaletteQty(new BigDecimal(p / _new.getBoxCnt() / _new.getPaletteCnt()).setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
					__new.setBoxQty(new BigDecimal(p / _new.getBoxCnt()).setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
					_new.decrement(__new);
					transqueue.add(__new);
				}
			}
			
			if(vehicle == null) {
				System.out.println();
			}
			
			if(!vehicle.addVolume(_new)) {
				int lng = transqueue.size();
				for(int i = 0 ; i < lng ; i++) {
					ItemTransport _org = transqueue.poll();
					if(_org == null) {
						break;
					}
					_org.setFrom(from.getId());

					if(!vehicle.addVolume(_org)) {
						transqueue.add(_org);
					}
				}
				transqueue.add(_new);
				break;
			}
		}
		return vehicle;
	}
}
