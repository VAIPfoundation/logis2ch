package com.sdc2ch.service.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class TmapApi {

	
	public static void main(String[] args) throws Throwable {
		

		
		send(aa());
		

	}
	
	private static void callDB() {
		
	}
	
	
	
	private static void send(String contents) {
		HttpURLConnection conn = null;
		InputStream ins = null;
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			URL url = new URL("https:
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(1000);
			conn.setDoOutput(true);
			conn.setReadTimeout(10000);
			conn.setRequestMethod("POST");
			conn.addRequestProperty("Content-Type", "application/json");
			conn.addRequestProperty("appKey", "97aeaad5-94b9-4f47-8f1d-97e085be2568");
			
			OutputStream outputStream = conn.getOutputStream();
			outputStream.write(contents.getBytes("UTF-8"));
			outputStream.flush();
			outputStream.close();
			
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				log.warn("{}", conn.getContent());
			}
			
			ins = conn.getInputStream();
			
			if(ins != null) {
				StringBuffer sb = new StringBuffer();
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(ins, "UTF-8"));
				
				String s;
				
				while((s = reader.readLine()) != null) {
					log.info("{}", s);
					sb.append(s);
				}
				
				reader.close();
				HashMap mapped = mapper.readValue(sb.toString().getBytes("UTF-8"), HashMap.class);
				System.out.println(mapped);
			}
			if(ins != null)
				ins.close();
			
			
		
			
			
		} catch (IOException e) {
			log.error("{}", e);
		}finally {
			if(conn != null)
				conn.disconnect();
		}
	}
	
	private static String aa () throws JsonParseException, JsonMappingException, IOException {
		
		
		ObjectMapper mapper = new ObjectMapper();
		
		
		ClassPathResource classPathResource = new ClassPathResource("RequestData2.json");
		HashMap mapped = mapper.readValue(classPathResource.getInputStream(), HashMap.class);
		
		System.out.println(mapped);
		return mapper.writeValueAsString(mapped);
	}
	
	
	private static List<String> readFile(String filePath) throws IOException{
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
		String s;
		List<String> readLines = new ArrayList<>();
		while ((s = reader.readLine()) != null) {
			readLines.add(s);
		}
		reader.close();
		return readLines;
	}
}
