package com.sdc2ch.service.test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.sdc2ch.service.model.item.ItemShipmentByRoute;
import com.sdc2ch.service.model.item.ItemTransport;
import com.sdc2ch.service.model.item.ProductItem;
import com.sdc2ch.service.test.ProductFactory.ProductFactoryBuilder;
import com.sdc2ch.service.test.vo2.TransferFactory;
import com.sdc2ch.service.transfer.FactoryType;
import com.sdc2ch.service.transfer.TransferPlan;

import lombok.Getter;


@Getter
public class TransferOperations {
	
	private LocalDate fromDe;
	private LocalDate toDe;
	private List<TransferFactory> transferFactorys;
	private TransferLogisticsCenter transTo;
	private TransferModel model;
	private ILgistTransService svc;
	private List<ShipmentVolumeGroup> volumeGroups;
	private DateTimeFormatter formatter;
	
	
	public List<ShipmentVolumeGroup> getVolumeGroups() {
		return volumeGroups != null ? volumeGroups : model.getVolumeMap()
		.keySet().stream()
		.map(k -> svc.searchVolumeItems(fromDe, toDe, model.getVolumeMap().get(k)))
		.collect(Collectors.toList());
	}
	
	public List<TransferFactory> transferFactroy() {
		
		if(transferFactorys == null) {
			transferFactorys = new ArrayList<>();
			for(FactoryType f : FactoryType.values()) {
				ProductFactory productF = ProductFactoryBuilder.builder().id(f).transferModel(model).build();
				for(ProductItem pi : productF.getItems()) {
					for(TransferPlan plan : model.getProductplans()) {
						if(pi.getItemCd().equals(plan.getItemCd())) {
							pi.setRatio(plan.transferRatio(f));
							break;
						}
					}
				}
				TransferFactory fct = new TransferFactory(productF);
				fct.setOperations(this);
				transferFactorys.add(fct);
			}
		}
		return transferFactorys;
	}
	
	public List<ItemTransport> getIncommingShipments(ProductFactory pf) {
		return svc.searchInCommingTransportItems(fromDe, toDe, pf.getId());
		
	}	
	public List<ItemTransport> getOutgoings() {
		return svc.searchOutGoingTransportItems(fromDe, toDe, null);
	}
	
	public void insert(List<TransferNewRoute> newroure) {
		svc.insert(newroure);
	}
	
	
	public List<ProductItem> getProductItems(ProductFactory fctry) {
		return svc.getProductItems(fctry.getId());
	}

	public List<ItemShipmentByRoute> getShipmentItems(Long id) {
		return svc.getShipmentItems(fromDe, toDe, id);
	}
	public String getStringFromDe() {
		return formatter.format(fromDe);
	}
	
	public String getStringToDe() {
		return formatter.format(toDe);
	}
	
	public String convertDateToString(LocalDate current) {
		return formatter.format(current);
	}
	public TransferFactory findByFctryCd(String fctryCd) {
		return transferFactorys.stream().filter(f -> f.getId().equals(fctryCd)).findFirst().orElse(null);
	}
	public static class TransferOptionsBuilder {
		private TransferOperations options = new TransferOperations();
		private LocalDate fromDe;
		private LocalDate toDe;
		private TransferLogisticsCenter to;
		private TransferModel model;
		private DateTimeFormatter formatter;
		
		public static TransferOptionsBuilder builder() {
			return new TransferOptionsBuilder();
		}
		
		public TransferOptionsBuilder fromDe(LocalDate from) {
			this.fromDe = from;
			return this;
		}
		public TransferOptionsBuilder toDe(LocalDate to) {
			this.toDe = to;
			return this;
		}
		public TransferOptionsBuilder transTo(TransferLogisticsCenter to) {
			this.to = to;
			return this;
		}
		public TransferOptionsBuilder model(TransferModel model) {
			this.model = model;
			return this;
		}
		public TransferOptionsBuilder formatter(DateTimeFormatter formatter) {
			this.formatter = formatter;
			return this;
		}
		public TransferOperations build() {
			options.model = model;
			options.svc = model.getSvc();
			options.fromDe = fromDe;
			options.toDe = toDe;
			options.transTo = to;
			options.formatter = formatter;
			return options;
		}


	}


}
