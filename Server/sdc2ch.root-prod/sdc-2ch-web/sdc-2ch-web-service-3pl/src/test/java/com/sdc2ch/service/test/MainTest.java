package com.sdc2ch.service.test;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sdc2ch.service.model.NaviPoint;
import com.sdc2ch.service.test.TransferFTF.TransferFTFBuilder;
import com.sdc2ch.service.test.TransferModel.TransferModelBuilder;
import com.sdc2ch.service.transfer.TransferPlan;

public class MainTest {
	
	public static void main(String[] args) {
		LgistTransServiceFileImpl service = new LgistTransServiceFileImpl();
		
		
		
		
		
		List<TransferPlan> transferPlans = service.getProductPlanItems(2L);

		
		
		Map<String, Long> volumeMap = new HashMap<>();
		volumeMap.put("2D1", 12L);
		
		
		String fromDe = "20170911";
		String toDe = "20180910";

		String datePattern = "yyyyMMdd";
		
		NaviPoint point = NaviPoint.builder().lat(0.0).lat(0.0).name("2D1").build();
		
		TransferModel model = TransferModelBuilder.builder()
				.fromDate(fromDe)
				.toDate(toDe)
				.transferPlans(transferPlans)
				.toPiont(point)
				.service(service)
				.volumes(volumeMap)
				.formatter(DateTimeFormatter.ofPattern(datePattern))
				.build();
		
		TransferSimulator.builder().tft(TransferFTFBuilder
				.builder().transferModel(model).build()).build().simulate();
		
	}

}
