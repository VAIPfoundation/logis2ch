package com.sdc2ch.tms.service;

import java.util.List;

import com.sdc2ch.tms.io.TmsLocationIO;
import com.sdc2ch.tms.io.TmsOrderStopIO;

public interface ITmsStopService {

	String SP_2CH_STOP_ORDER_INFO_SEL = "EXEC [dbo].[SP_2CH_STOP_ORDER_INFO_SEL] :dlvyDe, :routeNo ";
	String SP_2CH_STOP_INFO_SEL = "EXEC [dbo].[SP_2CH_STOP_INFO_SEL] :stopCd ";
	TmsLocationIO findStopLocation(String stopCd);

	List<TmsOrderStopIO> findOrderStopInfoByDlvyDeAndRouteNo(String dlvyDe, String routeNo);

	TmsOrderStopIO findStopInfoByStopCd(String stopCd);

}
