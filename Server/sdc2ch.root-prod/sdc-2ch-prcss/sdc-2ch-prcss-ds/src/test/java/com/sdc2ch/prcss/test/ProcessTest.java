package com.sdc2ch.prcss.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdc2ch.tms.domain.view.TmsPlan;

public class ProcessTest {
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		
		ClassPathResource classPathResource = new ClassPathResource("data/PlanData.json");
		
		File folder = new File("data");
		File[] listOfFiles = folder.listFiles();
		
		ObjectMapper mapper = new ObjectMapper();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for(File f : listOfFiles) {
			
			List<TmsPlan> tmsplan = (List<TmsPlan>) mapper.readValue(f, List.class).stream().map(o -> mapper.convertValue(o, TmsPlan.class)).collect(Collectors.toList());
						
			
			tmsplan.stream().collect(Collectors.groupingBy(p -> {
				return p.getVrn();
				
			})).forEach((k, v) -> {
				
				try {
					mapper.writeValue(baos, v);
					FileUtils.writeByteArrayToFile(new File("data/" + f.getName().substring(0, 8)  + "/" + k + ".json"), baos.toByteArray());
					baos.reset();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			});
		}
		
		

		
		
		
		
		
	}

}
