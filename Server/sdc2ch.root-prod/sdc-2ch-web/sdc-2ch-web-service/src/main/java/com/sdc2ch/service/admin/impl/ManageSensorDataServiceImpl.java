package com.sdc2ch.service.admin.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdc2ch.service.admin.IManageSensorDataService;
import com.sdc2ch.service.admin.model.AdminShippingStateHistVo;
import com.sdc2ch.web.admin.repo.AdmQueryBuilder;
import com.sdc2ch.web.admin.repo.domain.v.V_SHIPPING_STATE_HIST;

import ch.qos.logback.core.net.SyslogOutputStream;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
class ManageSensorDataServiceImpl implements IManageSensorDataService {

	
	@Autowired AdmQueryBuilder builder;



	
	@Override
	public List<AdminShippingStateHistVo> searchSensorDataHistDaily(String fromDe, String toDe) {
		return searchSensorDataHistDaily(fromDe, toDe, null, null, null);
	}
	@Override
	public List<AdminShippingStateHistVo> searchSensorDataHistDaily(String fromDe, String toDe, String fctryCd, String vrn, String driverNm) {
		log.info("fromDe={}, toDe={}, fctryCd={}, vrn={}, driverNm={}", fromDe, toDe, fctryCd, vrn, driverNm);
		List<Object[]> rows = new ArrayList<>();
		List<AdminShippingStateHistVo> result = new ArrayList<>();
		
		try {

			Object[] params = { fctryCd, fromDe, toDe, "", "", "" };
			String procName = "[dbo].[SP_2CH_SENSORDATA_DAILY_SEL]";
			rows = builder.storedProcedureResultCall(procName, params);

			result = new ArrayList<>(rows.size());
			if (rows.size()>0) {
				for (Object[] row : rows) {

					AdminShippingStateHistVo info = new AdminShippingStateHistVo();
					int i = 0;

					info.setDlvyDe((String) row[i++]);
					info.setFctryCd((String) row[i++]);
					info.setFctryNm((String) row[i++]);
					info.setMgrAllocated((Integer) row[i++]);
					info.setUsrSt((Integer) row[i++]);
					info.setBcnEnter((Integer) row[i++]);

					info.setNfcTagOffic((Integer) row[i++]);

					info.setNfcTagLdngA1cold((Integer) row[i++]);
					info.setNfcTagLdngA2cold((Integer) row[i++]);
					info.setNfcTagLdngB1cold((Integer) row[i++]);
					info.setNfcTagLdngPrcssgd((Integer) row[i++]);
					info.setNfcTagLdngSterilized((Integer) row[i++]);
					info.setNfcTagLdngCu((Integer) row[i++]);
					info.setNfcTagLdngHosang((Integer) row[i++]);
					info.setNfcTagLdngCheese((Integer) row[i++]);
					info.setNfcTagLdngFlat((Integer) row[i++]);

					info.setBcnExited((Integer) row[i++]);
					info.setGeoEnter((Integer) row[i++]);
					info.setGeoArrived((Integer) row[i++]);
					info.setUsrEb((Integer) row[i++]);
					info.setGeoExited((Integer) row[i++]);
					info.setUsrFin((Integer) row[i++]);
					info.setDlvyCnt((Integer) row[i++]);
					info.setEmptyboxDlvyCnt((Integer) row[i++]);
					info.setRouteCnt((Integer) row[i++]);
					info.setTotal((Integer) row[i++]);
					info.setDayCnt((Integer) row[i++]);

					
					int eventSum = (
						info.getMgrAllocated() + info.getUsrSt() + info.getUsrFin() + info.getNfcTagOffic()	
						+ info.getBcnEnter() + info.getBcnExited()	

						+ info.getNfcTagLdngA1cold() + info.getNfcTagLdngA2cold() + info.getNfcTagLdngB1cold()	
						+ info.getNfcTagLdngPrcssgd() + info.getNfcTagLdngSterilized() + info.getNfcTagLdngCu()	
						+ info.getNfcTagLdngHosang() + info.getNfcTagLdngCheese() + info.getNfcTagLdngFlat()	

						+ info.getGeoEnter() + info.getGeoArrived() + info.getGeoExited()	
						+ info.getUsrEb()	
					);

					
					int eventCnt = (
						4	
						+ info.getRouteCnt()*11	
						+ info.getDlvyCnt()*3	
						+ info.getEmptyboxDlvyCnt()*1	
					);

					
					Double perTotal = ( eventSum / (double) eventCnt );
					info.setEventSum(eventSum);
					info.setEventCnt(eventCnt);
					info.setPerTotal(perTotal);

					result.add(info);
				}
			}
			return result;

		} catch (Exception e) {
			log.info("{}", e);
			return result;
		}
	}



	
	
	@Override
	public List<AdminShippingStateHistVo> searchSensorDataHistVrn(String fromDe, String toDe) {
		return searchSensorDataHistVrn(fromDe, toDe, null, null, null);
	}
	@Override
	public List<AdminShippingStateHistVo> searchSensorDataHistVrn(String fromDe, String toDe, String fctryCd, String vrn, String driverNm) {
		log.info("fromDe={}, toDe={}, fctryCd={}, vrn={}, driverNm={}", fromDe, toDe, fctryCd, vrn, driverNm);
		List<Object[]> rows = new ArrayList<>();
		List<AdminShippingStateHistVo> result = new ArrayList<>();
		
		try {

			Object[] params = { fctryCd, fromDe, toDe, vrn, driverNm, "" };
			String procName = "[dbo].[SP_2CH_SENSORDATA_VRN_SEL]";
			rows = builder.storedProcedureResultCall(procName, params);

			if (rows.size()>0) {
				for (Object[] row : rows) {
					AdminShippingStateHistVo info = new AdminShippingStateHistVo();
					int i = 0;
					info.setDlvyDe((String) row[i++]);
					info.setFctryCd((String) row[i++]);
					info.setFctryNm((String) row[i++]);
					info.setVrn((String) row[i++]);

					info.setDriverCd((String) row[i++]);
					info.setDriverNm((String) row[i++]);
					info.setMobileNo((String) row[i++]);
					info.setVhcleTy((Double) row[i++]);

					info.setMgrAllocated((Integer) row[i++]);
					info.setUsrSt((Integer) row[i++]);
					info.setBcnEnter((Integer) row[i++]);

					info.setNfcTagOffic((Integer) row[i++]);

					info.setNfcTagLdngA1cold((Integer) row[i++]);
					info.setNfcTagLdngA2cold((Integer) row[i++]);
					info.setNfcTagLdngB1cold((Integer) row[i++]);
					info.setNfcTagLdngPrcssgd((Integer) row[i++]);
					info.setNfcTagLdngSterilized((Integer) row[i++]);
					info.setNfcTagLdngCu((Integer) row[i++]);
					info.setNfcTagLdngHosang((Integer) row[i++]);
					info.setNfcTagLdngCheese((Integer) row[i++]);
					info.setNfcTagLdngFlat((Integer) row[i++]);

					info.setBcnExited((Integer) row[i++]);
					info.setGeoEnter((Integer) row[i++]);
					info.setGeoArrived((Integer) row[i++]);
					info.setUsrEb((Integer) row[i++]);
					info.setGeoExited((Integer) row[i++]);
					info.setUsrFin((Integer) row[i++]);
					info.setDlvyCnt((Integer) row[i++]);
					info.setEmptyboxDlvyCnt((Integer) row[i++]);
					info.setRouteCnt((Integer) row[i++]);
					info.setTotal((Integer) row[i++]);
					info.setDayCnt((Integer) row[i++]);

					
					int eventSum = (
						info.getMgrAllocated() + info.getUsrSt() + info.getUsrFin() + info.getNfcTagOffic()	
						+ info.getBcnEnter() + info.getBcnExited()	

						+ info.getNfcTagLdngA1cold() + info.getNfcTagLdngA2cold() + info.getNfcTagLdngB1cold()	
						+ info.getNfcTagLdngPrcssgd() + info.getNfcTagLdngSterilized() + info.getNfcTagLdngCu()	
						+ info.getNfcTagLdngHosang() + info.getNfcTagLdngCheese() + info.getNfcTagLdngFlat()	

						+ info.getGeoEnter() + info.getGeoArrived() + info.getGeoExited()	
						+ info.getUsrEb()	
					);

					
					int eventCnt = (
						4	
						+ info.getRouteCnt()*11	
						+ info.getDlvyCnt()*3	
						+ info.getEmptyboxDlvyCnt()*1	
					);

					
					Double perTotal = ( eventSum / (double) eventCnt );
					info.setEventSum(eventSum);
					info.setEventCnt(eventCnt);
					info.setPerTotal(perTotal);

					result.add(info);
				}
			}
			return result;

		} catch (Exception e) {
			log.info("{}", e);
			return result;
		}
	}



	
	@Override
	public List<AdminShippingStateHistVo> searchSensorDataHistRouteNo(String fromDe, String toDe) {
		return searchSensorDataHistRouteNo(fromDe, toDe, null, null, null, null);
	}
	@Override
	public List<AdminShippingStateHistVo> searchSensorDataHistRouteNo(String fromDe, String toDe, String fctryCd, String vrn, String driverNm, String routeNo) {
		log.info("fromDe={}, toDe={}, fctryCd={}, vrn={}, driverNm={}, routeNo={}", fromDe, toDe, fctryCd, vrn, driverNm, routeNo);
		List<Object[]> rows = new ArrayList<>();
		List<AdminShippingStateHistVo> result = new ArrayList<>();
		
		try {

			Object[] params = { fctryCd, fromDe, toDe, vrn, driverNm, routeNo };
			String procName = "[dbo].[SP_2CH_SENSORDATA_ROUTE_NO_SEL]";
			rows = builder.storedProcedureResultCall(procName, params);
			System.out.println(">>>rows.size()"+rows.size());
			if (rows.size()>0) {
				for (Object[] row : rows) {
					AdminShippingStateHistVo info = new AdminShippingStateHistVo();
					int i = 0;

					info.setDlvyDe((String) row[i++]);
					info.setFctryCd((String) row[i++]);
					info.setFctryNm((String) row[i++]);
					info.setVrn((String) row[i++]);
					info.setRouteNo((String) row[i++]);
					info.setDriverCd((String) row[i++]);
					info.setDriverNm((String) row[i++]);
					info.setMobileNo((String) row[i++]);
					info.setVhcleTy((Double) row[i++]);
					info.setMgrAllocated((Integer) row[i++]);
					info.setUsrSt((Integer) row[i++]);
					info.setBcnEnter((Integer) row[i++]);

					info.setNfcTagOffic((Integer) row[i++]);

					info.setNfcTagLdngA1cold((Integer) row[i++]);
					info.setNfcTagLdngA2cold((Integer) row[i++]);
					info.setNfcTagLdngB1cold((Integer) row[i++]);
					info.setNfcTagLdngPrcssgd((Integer) row[i++]);
					info.setNfcTagLdngSterilized((Integer) row[i++]);
					info.setNfcTagLdngCu((Integer) row[i++]);
					info.setNfcTagLdngHosang((Integer) row[i++]);
					info.setNfcTagLdngCheese((Integer) row[i++]);
					info.setNfcTagLdngFlat((Integer) row[i++]);

					info.setBcnExited((Integer) row[i++]);
					info.setGeoEnter((Integer) row[i++]);
					info.setGeoArrived((Integer) row[i++]);
					info.setUsrEb((Integer) row[i++]);
					info.setGeoExited((Integer) row[i++]);
					info.setUsrFin((Integer) row[i++]);
					info.setDlvyCnt((Integer) row[i++]);
					info.setEmptyboxDlvyCnt((Integer) row[i++]);
					info.setRouteCnt((Integer) row[i++]);
					info.setTotal((Integer) row[i++]);

					result.add(info);
				}
			}
			return result;

		} catch (Exception e) {
			log.info("{}", e);
			return result;
		}
	}



	
	@Override
	public List<AdminShippingStateHistVo> searchSensorDataHistDlvyLc(String fromDe, String toDe) {
		return searchSensorDataHistDlvyLc(fromDe, toDe, null, null, null, null);
	}
	@Override
	public List<AdminShippingStateHistVo> searchSensorDataHistDlvyLc(String fromDe, String toDe, String fctryCd, String vrn, String driverNm, String routeNo) {
		log.info("searchSensorDataHistDlvyLc");
		log.info("fromDe={}, toDe={}, fctryCd={}, vrn={}, driverNm={}, routeNo={}", fromDe, toDe, fctryCd, vrn, driverNm, routeNo);
		List<Object[]> rows = new ArrayList<>();
		List<AdminShippingStateHistVo> result = new ArrayList<>();
		
		try {

			Object[] params = { fctryCd, fromDe, toDe, vrn, driverNm, routeNo };
			String procName = "[dbo].[SP_2CH_SENSORDATA_DLVY_LC_SEL]";
			rows = builder.storedProcedureResultCall(procName, params);


			if (rows.size()>0) {
				for (Object[] row : rows) {
					AdminShippingStateHistVo info = new AdminShippingStateHistVo();
					int i = 0;

					info.setRowid((BigDecimal) row[i++]);
					info.setDlvyDe((String) row[i++]);
					info.setFctryCd((String) row[i++]);
					info.setFctryNm((String) row[i++]);
					info.setRouteNo((String) row[i++]);
					info.setDlvyLcCd((String) row[i++]);
					info.setDlvyLcNm((String) row[i++]);
					info.setDlvySeq((Integer) row[i++]);
					info.setStopSeq((Integer) row[i++]);
					info.setEmptyboxYn((String) row[i++]);
					info.setVrn((String) row[i++]);
					info.setDriverCd((String) row[i++]);
					info.setDriverNm((String) row[i++]);
					info.setMobileNo((String) row[i++]);
					info.setVhcleTy((Double) row[i++]);
					info.setScheStartDate((String) row[i++]);
					info.setScheStartTime((String) row[i++]);
					info.setTotal((Integer) row[i++]);
					info.setDlvyCnt((Integer) row[i++]);
					info.setMgrAllocated((Integer) row[i++]);
					info.setUsrSt((Integer) row[i++]);
					info.setBcnEnter((Integer) row[i++]);

					info.setNfcTagOffic((Integer) row[i++]);

					info.setNfcTagLdngA1cold((Integer) row[i++]);
					info.setNfcTagLdngA2cold((Integer) row[i++]);
					info.setNfcTagLdngB1cold((Integer) row[i++]);
					info.setNfcTagLdngPrcssgd((Integer) row[i++]);
					info.setNfcTagLdngSterilized((Integer) row[i++]);
					info.setNfcTagLdngCu((Integer) row[i++]);
					info.setNfcTagLdngHosang((Integer) row[i++]);
					info.setNfcTagLdngCheese((Integer) row[i++]);
					info.setNfcTagLdngFlat((Integer) row[i++]);

					info.setBcnExited((Integer) row[i++]);
					info.setGeoEnter((Integer) row[i++]);
					info.setGeoArrived((Integer) row[i++]);
					info.setUsrEb((Integer) row[i++]);
					info.setGeoExited((Integer) row[i++]);
					info.setUsrFin((Integer) row[i++]);
					info.setEmptyboxDlvyCnt((Integer) row[i++]);
					result.add(info);
				}
			}
			return result;

		} catch (Exception e) {
			log.info("{}", e);
			return result;
		}
	}


































































































}
