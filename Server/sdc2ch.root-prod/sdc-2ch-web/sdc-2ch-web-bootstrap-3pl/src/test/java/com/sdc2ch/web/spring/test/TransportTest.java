package com.sdc2ch.web.spring.test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sdc2ch.service.ILgistTransService;
import com.sdc2ch.service.model.item.ItemShipment;
import com.sdc2ch.service.model.item.ItemTransfer;
import com.sdc2ch.service.tm.Route;
import com.sdc2ch.web.bootstrap.Seoulmilk2chApplication;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Seoulmilk2chApplication.class)
public class TransportTest {
	
	@Autowired ILgistTransService svc;
	@Test
	public void findAll() throws IOException {
		
		long modelID = 2L;					
		
		
		
		
		
		
		String closeFactoryCD = "2D1"; 			
		String defaultTransCD = "1D1"; 			
		
		String newPointCD = null;				
		                                        
		String startDT = "20170911";
		
		String endDT = "20180910";
		
		com.sdc2ch.service.tm.TMMain tmMain = new com.sdc2ch.service.tm.TMMain(svc, modelID, closeFactoryCD, newPointCD, defaultTransCD);
		tmMain.start(startDT, endDT);
	}
	
	@Test
	public void searchShipment() throws IOException {

	}
	@Test
	public void getInCommingTransportItem() throws IOException {

	}
	@Test
	public void getOutGoingTransportItem() throws IOException {

	}

	private void rangeTest(String startDe, String endDe) {
		
		LocalDate firstDate = convertDate(startDe);
		LocalDate secondDate = convertDate(endDe);
		long dateDiff = ChronoUnit.DAYS.between(firstDate, secondDate)+1;
		
		for(long i = 0 ; i < dateDiff; i++) {
			LocalDate current = firstDate.plusDays(i);
			System.out.print(current);
			System.out.print(" convert -> ");
			System.out.println(current.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
			
		}
	}
	
	private LocalDate convertDate(String date) {
		
		return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"));
	}
}
