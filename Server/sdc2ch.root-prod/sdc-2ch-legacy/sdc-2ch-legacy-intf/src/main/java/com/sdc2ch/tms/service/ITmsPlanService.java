package com.sdc2ch.tms.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.tms.io.TmsPlanIO;

public interface ITmsPlanService {

	public String TMS_ORDER_QUERY = "  SELECT distinct \n" +
			"       delivery_date, \n" +
			"       shipto, \n" +
			"	   route_no, \n" +
			"	   Latitude, \n" +
			"	   Longitude, \n" +
			"	   New_Req_Time, \n" +
			"	   New_Req_Time_SAT, \n" +
			"	   New_Req_Time_SUN,\n" +
			"	   represent_stop_cd,\n" +
			"	   stop_nm,\n" +
			"	   Addr_Basic\n" +
			"  FROM [tms].[dbo].[M_ORDER] as OD WITH (nolock)\n" +
			"  LEFT JOIN TMS.DBO.M_STOP as ST WITH (nolock)\n" +
			"      ON OD.SHIPTO = ST.STOP_CD \n" +
			"  WHERE OD.DELIVERY_DATE = '%s' AND OD.ROUTE_NO in (%s)\n" +
			"  ORDER BY ROUTE_NO, NEW_REQ_TIME;";

	public String TMS_M_PLAN_MST_QUERY = "SELECT m.RowID\n" +
			"      ,m.Center_Cd\n" +
			"      ,M.Delivery_Date\n" +
			"      ,M.Route_No\n" +
			"      ,M.Batch_No\n" +
			"      ,M.Car_Cd\n" +
			"      ,M.Driver_Cd\n" +
			"      ,M.Driver_Nm\n" +
			"      ,M.InitialStationID\n" +
			"      ,M.FinalStationID\n" +
			"      ,M.Car_Weight\n" +
			"      ,M.Car_Type\n" +
			"      ,m.ShipSeq\n" +
			"      ,M.Sche_Start_Date\n" +
			"      ,M.Sche_Start_Time\n" +
			"      ,M.Sche_End_Date\n" +
			"      ,M.Sche_End_Time\n" +
			"      ,M.Conf_Distance\n" +
			"      ,M.Conf_TurnRate\n" +
			"      ,M.Conf_TollCost,\n" +
			"	   M.timezone_cd,\n" +
			"	  CASE WHEN G.StartTime < G.EndTime THEN M.Sche_Start_Date WHEN G.StartTime = G.EndTime THEN M.Sche_Start_Date WHEN G.StartTime > G.EndTime THEN CONVERT(varchar, \n" +
			"               DATEADD(day, 1, M.Sche_Start_Date), 112) END AS LDNG_ED_DE, G.StartTime AS LDNG_ST, G.EndTime AS LDNG_ED,\n" +
			"	    ROUND(ISNULL(H.CarOil_Qty * M.Conf_Distance, 0), 2) AS CarOil\n" +
			"	   ,replace(D.mobile_no, '-', '') as mobile_no\n" +
			"      ,(select CD.Code_nm from tms.dbo.M_CODE_GRP AS CDG INNER JOIN tms.dbo.M_CODE AS CD ON CDG.Group_Cd = CD.Group_Cd where CDG.GROUP_CD = '023' and CD.Code = M.Batch_No) as caralc_ty_nm\n" +
			"  FROM tms.dbo.M_PLAN_MST M \n" +
			"  LEFT JOIN tms.dbo.M_DRIVER AS D ON M.driver_cd = D.driver_cd \n" +
			"  LEFT JOIN tms.dbo.M_OILCOST_BASE AS H ON M.Delivery_Date BETWEEN H.Start_Date AND ISNULL(H.End_Date, '99999999') AND M.Car_Weight = H.Car_Weight AND M.Area_Cd = H.Area_Cd\n" +
			"  LEFT JOIN tms.dbo.M_SHIPSEQ G ON M.ShipSeq = G.ShipSeq AND M.Center_Cd = G.Center_Cd AND G.IsUse = 1\n" +
			"  where M.delivery_date = '%s' AND M.Route_No in (%s)";
	public String TMS_M_PLAN_MST_QUERY_BY_DLVYDEANDDRIVERCD = "SELECT m.RowID\n" +
			"      ,m.Center_Cd\n" +
			"      ,M.Delivery_Date\n" +
			"      ,M.Route_No\n" +
			"      ,M.Batch_No\n" +
			"      ,M.Car_Cd\n" +
			"      ,M.Driver_Cd\n" +
			"      ,M.Driver_Nm\n" +
			"      ,M.InitialStationID\n" +
			"      ,M.FinalStationID\n" +
			"      ,M.Car_Weight\n" +
			"      ,M.Car_Type\n" +
			"      ,M.ShipSeq\n" +
			"      ,M.Sche_Start_Date\n" +
			"      ,M.Sche_Start_Time\n" +
			"      ,M.Sche_End_Date\n" +
			"      ,M.Sche_End_Time\n" +
			"      ,M.Conf_Distance\n" +
			"      ,M.Conf_TurnRate\n" +
			"      ,M.Conf_TollCost,\n" +
			"	   M.timezone_cd,\n" +
			"	  CASE WHEN G.StartTime < G.EndTime THEN M.Sche_Start_Date WHEN G.StartTime = G.EndTime THEN M.Sche_Start_Date WHEN G.StartTime > G.EndTime THEN CONVERT(varchar, \n" +
			"               DATEADD(day, 1, M.Sche_Start_Date), 112) END AS LDNG_ED_DE, G.StartTime AS LDNG_ST, G.EndTime AS LDNG_ED,\n" +
			"	    ROUND(ISNULL(H.CarOil_Qty * M.Conf_Distance, 0), 2) AS CarOil\n" +
			"	   ,replace(D.mobile_no, '-', '') as mobile_no\n" +
			"      ,(select CD.Code_nm from tms.dbo.M_CODE_GRP AS CDG INNER JOIN tms.dbo.M_CODE AS CD ON CDG.Group_Cd = CD.Group_Cd where CDG.GROUP_CD = '023' and CD.Code = M.Batch_No) as caralc_ty_nm\n" +
			"  FROM tms.dbo.M_PLAN_MST M \n" +
			"  LEFT JOIN tms.dbo.M_DRIVER AS D ON M.driver_cd = D.driver_cd \n" +
			"  LEFT JOIN tms.dbo.M_OILCOST_BASE AS H ON M.Delivery_Date BETWEEN H.Start_Date AND ISNULL(H.End_Date, '99999999') AND M.Car_Weight = H.Car_Weight AND M.Area_Cd = H.Area_Cd\n" +
			"  LEFT JOIN tms.dbo.M_SHIPSEQ G ON M.ShipSeq = G.ShipSeq AND M.Center_Cd = G.Center_Cd AND G.IsUse = 1\n" +
			"  where M.delivery_date = '%s' AND M.Driver_cd = '%s'";
	
	List<TmsPlanIO> findAllByDeliveryDate(String dlvyDe);








	
	TmsPlanIO findTmPlanById(Long planRowid);

	TmsPlanIO findTmPlansByUserAndDeleveryDateAndRouteNoAndStopCd(String driverCd, String dlvyDe, String routeNo, String stopCd);

	
	List<TmsPlanIO> findTmPlansByIds(String dlvyDe, String... routes);

	
	List<TmsPlanIO> findTmPlansByUserAndDeleveryDate(IUser user, String dlvyDe);
	List<TmsPlanIO> findTmPlansByUserAndDeleveryDate(String driverCd, String dlvyDe);


	
	String dayOfweekTimeByArriveTime(TmsPlanIO plan);

	
	String convertRouteNo(String routeNo);


	List<TmsPlanIO> findTmPlansByCustomerMobileNo(String dlvyDe, String mobileNo);
	
	List<TmsPlanIO> findTmPlansByCustomerMobileNoByErp(String dlvyDe, String mobileNo);

	
	public boolean updateExitFactory(String userId, String centerCd, String carCd, String dlvyDe, String routeNo, Date date);

	
	boolean chkPickComplate(String carCd, String dlvyDe, String routeNo);

	public LocalDateTime convertDateAndTimeString(TmsPlanIO plan);


}
