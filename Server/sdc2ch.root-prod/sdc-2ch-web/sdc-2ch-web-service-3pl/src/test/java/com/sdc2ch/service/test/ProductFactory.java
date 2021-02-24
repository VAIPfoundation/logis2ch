package com.sdc2ch.service.test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.sdc2ch.service.model.cost.CarTonType;
import com.sdc2ch.service.model.item.ProductItem;
import com.sdc2ch.service.test.Vehicles.VehiclesBuilder;
import com.sdc2ch.service.transfer.FactoryType;
import com.sdc2ch.service.util.Utils;

import lombok.Getter;


@Getter

public class ProductFactory {
	private String id;
	private String name;
	private FactoryType fctryTy;
	private TransferRatio ratio;
	private Map<CarTonType, Vehicles> numOfRetention;
	
	private List<ProductItem> items; 
	
	@Override
	public String toString() {
		return Utils.toJsonString(this);
	}
	
	public double getProductRatio(String itemId) {
		ProductItem item = items.stream().filter(i -> i.getItemCd().equals(itemId)).findFirst().orElse(null);
		return item == null ? 0.0 : item.getRatio();
	}

	public void clear() {
		
		numOfRetention.values().forEach(v -> {
			v.clear();
		});
		
	}
	static class ProductFactoryBuilder {
		private ProductFactory fctry = new ProductFactory();
		private FactoryType ty;
		private TransferModel model;
		public static ProductFactoryBuilder builder() {
			return new ProductFactoryBuilder();
		}
		public ProductFactoryBuilder id(FactoryType ty) {
			this.ty = ty;
			return this;
		}
		public ProductFactoryBuilder transferModel(TransferModel items) {
			this.model = items;
			return this;
		}
		
		public ProductFactory build() {
			
			fctry.id = ty.id;
			fctry.name = ty.name;
			fctry.fctryTy = ty;
			fctry.items = model.getOperations().getProductItems(fctry);
			fctry.numOfRetention = Stream.of(CarTonType.values())
			.collect(Collectors.toMap(ctt -> ctt,
					ctt -> VehiclesBuilder.builder().factory(fctry).ton(ctt).build()));
			return fctry;
		}
	}

}
