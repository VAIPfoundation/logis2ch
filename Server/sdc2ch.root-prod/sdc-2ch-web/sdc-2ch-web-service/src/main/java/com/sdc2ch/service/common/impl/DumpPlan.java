package com.sdc2ch.service.common.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdc2ch.core.ISetupData;
import com.sdc2ch.tms.io.TmsPlanIO;
import com.sdc2ch.tms.service.ITmsPlanService;


public class DumpPlan implements ISetupData {
	
	@Autowired ITmsPlanService tmsSvc;

	@Override
	public void install() {
		
		ObjectMapper mapper = new ObjectMapper();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			
			String date = "20180809";
			List<String> vrns = Arrays.asList("인천83바9348","강원80바1033","강원80바1032","강원80바1026","강원80바1346","강원80바1031","강원80바1141","경기91자6869","강원80바1124","강원80바1045","강원80바1325","인천83바9738","강원80바1324","강원80바1321","강원80바1322","강원80바1013","경기91아6023","강원80바1014","경기91아6026","경기91자6876","경기91자6877","경기91아6033","경기91아6019","강원80바1017","경기91아6025","강원80바1016");
			
			
			
			List<TmsPlanIO> ios = tmsSvc.findAllByDeliveryDate(date);
			mapper.writeValue(baos, ios.stream().filter(tms -> {
				
				if(tms == null)
					return false;
				if(tms.getVrn() == null) {
					System.out.println();
				}
				return  vrns.contains(tms.getVrn());
			}).collect(Collectors.toList()));

			
			System.out.println(baos);
		} catch (JsonGenerationException e) {
			
			e.printStackTrace();
		} catch (JsonMappingException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		System.out.println();
		
	}

	@Override
	public int order() {
		
		return 0;
	}

}
