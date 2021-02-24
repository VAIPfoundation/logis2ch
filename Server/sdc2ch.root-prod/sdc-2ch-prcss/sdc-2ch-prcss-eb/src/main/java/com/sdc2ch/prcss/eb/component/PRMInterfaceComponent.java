package com.sdc2ch.prcss.eb.component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.sdc2ch.prcss.eb.repo.domain.T_EMPTYBOX_INFO;
import com.sdc2ch.repo.builder.IAdmQueryBuilder;
import com.sdc2ch.require.ApplicationEventType;
import com.sdc2ch.require.IApplicationEventListener;
import com.sdc2ch.tms.io.TmsOrderStopIO;
import com.sdc2ch.tms.service.ITmsStopService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PRMInterfaceComponent {

	@Autowired ITmsStopService stopSvc;
	@Autowired IAdmQueryBuilder builder;
	@Autowired IApplicationEventListener listener;
	private BlockingQueue<T_EMPTYBOX_INFO> queue = new LinkedBlockingQueue<T_EMPTYBOX_INFO>();

	
	@Value(value = "${sdc-2ch-prcss-eb.PRMInterfaceComponent.enable:true}")
	boolean PRMInterfaceComponentEnable;


	@Autowired
	private void init() {

		if(PRMInterfaceComponentEnable==false)
			return;

		listener.subscribe(ApplicationEventType.ON_CLOSED).subscribe(e -> {

			if(!queue.isEmpty()) {

				T_EMPTYBOX_INFO eb = null;
				try {
					while(!queue.isEmpty()) {

						eb = queue.poll();

						TmsOrderStopIO stop = stopSvc.findStopInfoByStopCd(eb.getStopCd());

						if(check(eb) && stop != null) {
							builder.storedProcedureCall("[dbo].[SP_2CH_EB_PRM_REG]"
									, eb.getDlvyDe()		
									, eb.getRouteNo()		
									, eb.getStopCd()		
									, eb.getSquareBoxQty()	
									, eb.getTriangleBoxQty()
									, eb.getYodelryBoxQty()	
									, eb.getPalletQty()		
									, "Y"					
									, stop.getManageCd());	
						}
					}
				}catch (Exception e1) {
					log.error("{}", e1);
				}
			}

		});

	}

	@Scheduled(fixedRate = 1000 * 5)
	private void batch() {

		if(PRMInterfaceComponentEnable==false)
			return;

		if ( queue.size() > 0 ) {
			log.info("PRM Send Queue Size {}", queue.size());
		}
		synchronized (queue) {

			if(!queue.isEmpty()) {

				T_EMPTYBOX_INFO eb = null;
				try {
					while(!queue.isEmpty()) {

						

						eb = queue.poll();

						TmsOrderStopIO stop = stopSvc.findStopInfoByStopCd(eb.getStopCd());

						if(check(eb) && stop != null) {
							boolean results = builder.storedProcedureCall("[dbo].[SP_2CH_EB_PRM_REG]"
									, eb.getDlvyDe()		
									, eb.getRouteNo()		
									, eb.getStopCd()		
									, eb.getSquareBoxQty()	
									, eb.getTriangleBoxQty()
									, eb.getYodelryBoxQty()	
									, eb.getPalletQty()		
									, "Y"					
									, stop.getManageCd());	

							if(!results) {
								queue.offer(eb);
								break;
							}
						}
					}
				}catch (Exception e) {
					log.error("{}", e);
				}
			}
		}
	}

	private boolean check(T_EMPTYBOX_INFO eb) {
		return eb != null && !StringUtils.isEmpty(eb.getDlvyDe()) && !StringUtils.isEmpty(eb.getRouteNo()) && !StringUtils.isEmpty(eb.getStopCd());
	}

	public void offer(T_EMPTYBOX_INFO eb) {
		queue.offer(eb);
	}

}
