package com.sdc2ch.service.tm;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import com.sdc2ch.service.ILgistTransService;

public class TMMain {
	
	private long modelID;
	private ILgistTransService svc;
	private Model model;
	
	private String closeFactoryCD;
	private String newPointCD;
	private String defaultTransCD;
	
	public TMMain(ILgistTransService svc, long modelID, String closeFactoryCD, String newPointCD, String defaultTransCD) {
		this.modelID = modelID;
		this.svc = svc;
		
		this.closeFactoryCD = closeFactoryCD;
		this.newPointCD = newPointCD;
		this.defaultTransCD = defaultTransCD;
		
		this.model = new Model(svc, closeFactoryCD, modelID);	
	}
	
	public void start(String startDT, String endDT) {
		
		model.load_vehicleMaster();
		model.load_itemMaster();
		model.load_itemTransferMaster();
		
		
	
		
		
		LocalDate firstDate = convertDate(startDT);
		LocalDate secondDate = convertDate(endDT); 
		long dateDiff = ChronoUnit.DAYS.between(firstDate, secondDate)+1;
		
		int term = 100;
		long howmany_term = dateDiff / term;
		if (dateDiff % term > 0) howmany_term += 1;
			
		for (long j=0; j < howmany_term; j++) {
			
			String sub_startDT = firstDate.plusDays((j * term)).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			String sub_endDT = null;
			if (((j + 1) * term) < dateDiff) 
				sub_endDT = firstDate.plusDays(((j + 1)* term) - 1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			else 
				sub_endDT= firstDate.plusDays(dateDiff - 1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));	
			
			
			model._load_alldata(sub_startDT, sub_endDT);
			
			for(long i = (j * term) ; ( i < (j + 1)*term && i < dateDiff); i++) {
				LocalDate current = firstDate.plusDays(i);
				String curDate = current.format(DateTimeFormatter.ofPattern("yyyyMMdd"));		
				System.out.print(current);
				System.out.print(" convert -> ");
				System.out.println(curDate);
				
				
				
				
				
				
				
				model.load_itemOutputQty(curDate);
				model.load_itemRoutes(curDate);
				
				model.report_shipqty(this.defaultTransCD, curDate);
				model.report_transferqty(this.defaultTransCD, curDate);
											
				model.printModel(model.oldRouteList, 0);
							
				model.GGenerateNewRoute(this.defaultTransCD);
				model.optRoute2();
				model.nothasItemGGeneateNewRoute(this.defaultTransCD, curDate);					
				model.removeZeroShipItem(model.newRouteList);
				
				
				if (this.newPointCD != null && this.closeFactoryCD.equals(this.newPointCD) == false) 
					model.changeNewTo(model.newRouteList, this.closeFactoryCD, this.newPointCD);
				
				model.how_many_vehicles(model.newRouteList, curDate, 1);	
				model.clear_data();
			}
			
			model.clear_hashmap();	
			model.summary();
		}
		
		svc.endOfNewRoute(modelID);	
	}
	
	private LocalDate convertDate(String date) {
		return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"));
	}
}