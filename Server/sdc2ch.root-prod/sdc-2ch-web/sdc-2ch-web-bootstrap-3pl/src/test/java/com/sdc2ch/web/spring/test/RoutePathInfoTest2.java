package com.sdc2ch.web.spring.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.HexDump;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdc2ch.service.admin.RoutePathInfoService;
import com.sdc2ch.web.admin.repo.domain.T_ROUTE_PATH_INFO;
import com.sdc2ch.web.admin.repo.domain.T_ROUTE_PATH_MATRIX;
import com.sdc2ch.web.bootstrap.Seoulmilk2chApplication;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Seoulmilk2chApplication.class)
public class RoutePathInfoTest2 {

	@Autowired
	RoutePathInfoService rpSvc;

	@Test
	public void findT_ROUTE_PATH_INFOAndUpdatePaths() {
		List<T_ROUTE_PATH_INFO> results = rpSvc.findAll();
		ObjectMapper mapper = new ObjectMapper();
		results.forEach(r -> {
			T_ROUTE_PATH_INFO info = r;

			try {
				@SuppressWarnings("unchecked")
				Map<String, Object> infoMap = mapper.readValue(info.getPathInfo().getBytes("UTF-8"), HashMap.class);
				Object o = infoMap.get("viaPoints");
				List<?> points;
				
				StringJoiner pointNamePath = new StringJoiner(",");
				StringJoiner pointPath = new StringJoiner(",");
				if(o instanceof ArrayList) {
					points = (List<?>) o;
					
					points.forEach(p -> {
						HashMap<String, String> paths = (HashMap<String, String>) p;
						pointNamePath.add(paths.get("viaPointName"));
						System.out.println(paths.get("viaPointId").replaceAll("\\W", ""));
						pointPath.add(paths.get("viaPointId").replaceAll("\\W", ""));
					});
					
					String replace = new String(Hex.encode(pointPath.toString().getBytes())).replace("cefb", "");
					
					String newchar = new String(Hex.decode(replace));
					info.setPathId(newchar);
					info.setPaths(pointNamePath.toString());
					rpSvc.save(info);
				}

			} catch (IOException e) {
				log.error("{}", e);
			}

		});

	}
	
	public static void main(String[] args) {
		
		StringJoiner sj = new StringJoiner("\\\\ ▶ ︎");
		
		sj.add("?1");
		sj.add("2");
		sj.add("3");
		sj.add("4");
		
		String str = sj.toString().replaceAll("\\\\", "");
		System.out.println(str);
		
		
		
	}

}
