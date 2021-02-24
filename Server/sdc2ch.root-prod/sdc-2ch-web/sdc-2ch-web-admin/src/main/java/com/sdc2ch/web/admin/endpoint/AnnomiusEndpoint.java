package com.sdc2ch.web.admin.endpoint;

import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sdc2ch.service.admin.IAnalsVhcleService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/smt")
@Slf4j
public class AnnomiusEndpoint {

	@Autowired IAnalsVhcleService vSvc;
	
	@GetMapping(value = "/{vrn}/{yyyyMM}")
	public void get(
			@PathVariable String vrn, 
			@PathVariable String yyyyMM,
			HttpServletResponse res) {
		
		
		try {
			List<String> datas = vSvc.searchHistVhcleCntrlOnlyMongo(vrn, yyyyMM);
			
			String fileName = URLEncoder.encode(vrn + "_" + yyyyMM + ".csv", "UTF-8");
			res.setContentType("application/octet-stream; charset=EUC-KR");
			res.setCharacterEncoding("EUC-KR");
			res.setHeader("Content-Disposition","attachment; filename="+fileName);
			
			StringBuilder sb = new StringBuilder();
			if(datas != null) {
				
				datas.forEach(s -> sb.append(s).append("\r\n"));
			}
			
			IOUtils.write(sb.toString().getBytes("EUC-KR"), res.getOutputStream());
			
		}catch (Exception e) {
			
			log.error("{}", e);
		}
		
		
	}
}
