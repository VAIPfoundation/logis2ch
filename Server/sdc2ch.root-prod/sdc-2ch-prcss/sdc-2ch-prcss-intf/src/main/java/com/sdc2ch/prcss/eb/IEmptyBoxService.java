package com.sdc2ch.prcss.eb;

import java.util.List;
import java.util.Optional;

import org.hibernate.service.spi.ServiceException;

import com.sdc2ch.prcss.eb.io.EmptyboxIO;
import com.sdc2ch.prcss.eb.vo.DriverSquareboxErpVo;
import com.sdc2ch.prcss.eb.vo.EmptyboxErpVo;
import com.sdc2ch.prcss.eb.vo.EmptyboxVo;
import com.sdc2ch.prcss.eb.vo.SummaryEmptyboxErpVo;
import com.sdc2ch.prcss.eb.vo.SummaryEmptyboxVo;


public interface IEmptyBoxService {

	
	List<EmptyboxVo> findEmptyBoxByRoute(String dlvyDe, String ... routeNos);
	List<EmptyboxVo> findEmptyBoxByRoute(String dlvyDe, boolean erp, String ... routeNos);	

	List<EmptyboxVo> findEmptyBoxByRouteOnly(String dlvyDe, String ... routeNos);
	List<EmptyboxVo> findEmptyBoxByRouteOnly(String dlvyDe, boolean erp, String ... routeNos);	



	
	boolean isCompletedBoxProcess(String dlvyDe, String ... routeNos);	
	boolean isCompletedBoxProcessByErp(String dlvyDe, String ... routeNos);	

	
	EmptyboxIO save(EmptyboxIO eb);

	
	boolean isConfirm(EmptyboxIO eb);

	
	Optional<EmptyboxIO> findEmptyBoxById(Long id) throws ServiceException;


	
	SummaryEmptyboxVo sumEmptyboxByDlvyDeAndRouteNo(String dlvyDe, String routeNo);
	
	SummaryEmptyboxVo sumEmptyboxByDlvyDeAndRouteNo(String dlvyDe, String routeNo, boolean erp);


	
	List<EmptyboxVo> listEmptyboxByDlvyDeAndRouteNo(String dlvyDe, String routeNo);
	
	List<EmptyboxVo> listEmptyboxByDlvyDeAndRouteNo(String dlvyDe, String routeNo, boolean erp);


	
	List<EmptyboxVo> listOnlyEmptyboxByDlvyDeAndRouteNo(String dlvyDe, String routeNo);


	
	List<DriverSquareboxErpVo> listEmptyBoxDriverMonthly(String dlvyDe, String routeNo);


	
	List<EmptyboxErpVo> listEmptyBoxPalletMonthly(String dlvyDe, List<EmptyboxErpVo> ebVoList);


}
