package com.sdc2ch.service.admin;

import java.util.List;

import com.sdc2ch.service.admin.model.AdminShippingStateHistVo;


public interface IManageSensorDataService {
	
	
	List<AdminShippingStateHistVo> searchSensorDataHistDaily(String fromDe, String toDe);
	List<AdminShippingStateHistVo> searchSensorDataHistDaily(String fromDe, String toDe, String fctryCd, String vrn, String driverNm);
	List<AdminShippingStateHistVo> searchSensorDataHistVrn(String fromDe, String toDe);
	List<AdminShippingStateHistVo> searchSensorDataHistVrn(String fromDe, String toDe, String fctryCd, String vrn, String driverNm);
	List<AdminShippingStateHistVo> searchSensorDataHistRouteNo(String fromDe, String toDe);
	List<AdminShippingStateHistVo> searchSensorDataHistRouteNo(String fromDe, String toDe, String fctryCd, String vrn, String driverNm, String routeNo);
	List<AdminShippingStateHistVo> searchSensorDataHistDlvyLc(String fromDe, String toDe);
	List<AdminShippingStateHistVo> searchSensorDataHistDlvyLc(String fromDe, String toDe, String fctryCd, String vrn, String driverNm, String routeNo);
	
}
