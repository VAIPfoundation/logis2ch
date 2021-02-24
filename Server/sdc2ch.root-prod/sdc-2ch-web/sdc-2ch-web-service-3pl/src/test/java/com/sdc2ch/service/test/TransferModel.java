package com.sdc2ch.service.test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import com.sdc2ch.service.model.NaviPoint;
import com.sdc2ch.service.test.TransferOperations.TransferOptionsBuilder;
import com.sdc2ch.service.transfer.TransferPlan;
import com.sdc2ch.service.util.Utils;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TransferModel {
	
	private long modelId; 
	private TransferOperations operations; 
	private ILgistTransService svc;
	private Map<String, Long > volumeMap;
	private List<TransferPlan> productplans;
	
	@Override
	public String toString() {
		return Utils.toJsonString(this);
	}


	public static class TransferModelBuilder {
		
		private TransferModel model = new TransferModel();
		private String fromDe;
		private String toDe;
		private NaviPoint toPoint;
		private ILgistTransService svc;
		private DateTimeFormatter formatter;
		private Map<String, Long> volumeMap;
		private List<TransferPlan> products;
		public static TransferModelBuilder builder() {
			return new TransferModelBuilder();
		}
		
		public TransferModelBuilder fromDate(String from) {
			this.fromDe = from;
			return this;
		}
		public TransferModelBuilder toPiont(NaviPoint to) {
			this.toPoint = to;
			return this;
		}
		public TransferModelBuilder toDate(String to) {
			this.toDe = to;
			return this;
		}
		public TransferModelBuilder formatter(DateTimeFormatter formatter) {
			this.formatter = formatter;
			return this;
		}
		public TransferModelBuilder service(ILgistTransService svc) {
			this.svc= svc;
			return this;
		}
		public TransferModelBuilder volumes(Map<String, Long> volumeMap) {
			this.volumeMap= volumeMap;
			return this;
		}
		public TransferModelBuilder transferPlans(List<TransferPlan> products) {
			this.products= products;
			return this;
		}
		private LocalDate convert(String date) {
			return LocalDate.parse(date, formatter);
		}
		public TransferModel build() {
			model.svc = svc;
			model.volumeMap = this.volumeMap;
			model.productplans = products;
			model.operations = TransferOptionsBuilder.builder()
			.fromDe(convert(fromDe))
			.model(model)
			.toDe(convert(toDe))
			.formatter(formatter)
			.transTo(TransferLogisticsCenter.builder().name(toPoint.getName()).point(toPoint).build())
			.build();
			return model;
		}
	}
}
