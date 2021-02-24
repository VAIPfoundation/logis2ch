package com.sdc2ch.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import com.sdc2ch.service.ILgistOdMatrixService;
import com.sdc2ch.service.factory.CarTon;
import com.sdc2ch.service.model.NaviPoint;
import com.sdc2ch.service.model.OdMatrixInfoVo;
import com.sdc2ch.service.model.TmapApiParam;
import com.sdc2ch.service.util.TmsSettleCost;
import com.sdc2ch.tms.io.TmsCalculateIO;
import com.sdc2ch.tms.io.TmsLocationIO;
import com.sdc2ch.tms.io.TmsPlanIO;
import com.sdc2ch.tms.service.ITmsCalculateService;
import com.sdc2ch.tms.service.ITmsPlanService;
import com.sdc2ch.tms.service.ITmsStopService;
import com.sdc2ch.web.admin.repo.domain.lgist.T_LGIST_MODEL;
import com.sdc2ch.web.admin.repo.domain.lgist.T_LGIST_ROUTE;
import com.sdc2ch.web.admin.repo.domain.lgist.T_ROUTE_PATH_MATRIX6;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
public class Executor implements Runnable {

	private LogisDeliveryExecutor service;
	private ITmsCalculateService calculateSvc;
	private ILgistOdMatrixService naviSvc;
	private ITmsPlanService tmsSvc;
	private ITmsStopService stopSvc;
	private BigDecimal newShipCost;
	private AtomicInteger inc;
	private T_LGIST_MODEL model;
	

	@Setter
	private class ExecutorVo {
		
		private String dlvyDe;
		private List<TmsCalculateIO> tmsCalculateIOs;
		private List<TmsPlanIO> tmsPlans;
		
		private ExecutorVo setDlvyDe(String dlvyDe) {
			this.dlvyDe = dlvyDe;
			return this;
		}
		public ExecutorVo setTmsCalculateIO(List<TmsCalculateIO> list) {
			this.tmsCalculateIOs = list;
			return this;
		}
		public ExecutorVo setTmsPlans(List<TmsPlanIO> list) {
			this.tmsPlans = list;
			return this;
		}
		public TmsCalculateIO findTmsCalculateIO(String routeNo) {
			return tmsCalculateIOs.stream().filter(i -> (i.getDlvyDe()+ i.getRouteNo()).equals(dlvyDe + routeNo)).findFirst().orElse(null);
		}
		
	}
	@Override
	public void run() {
		
		
		service.addExecuteCount();
		T_LGIST_MODEL _model = null;
		try {
			
			if(model == null || model.getId() == null) {
				return;
			}
			_model = model;
			
			
			NaviPoint changePoint = NaviPoint.builder().lat(nullsafeDouble(model.getLat())).lng(nullsafeDouble(model.getLng())).name(model.getPointNm()).build();
			
			List<TmsCalculateIO> datas = calculateSvc.search(model.getStartDe(), model.getEndDe(), model.getFctryCd(), c -> predicate(c));
			model.setSttus(1);
			model.setAnalsStartDt(new Date());
			
			List<String> containsRoute = model.getModels().stream().map(T_LGIST_ROUTE::getRouteNo).collect(Collectors.toList());
			System.out.println("before -> " + datas.size());
			datas.removeIf(c -> !containsRoute.contains(c.getRouteNo()) );
			System.out.println("after -> " + datas.size());
			model.setTotalCnt(datas.size());
			model.setCostBefore(datas.stream().map(TmsCalculateIO::getTotalShipCost).reduce((a, b) -> a.add(b)).orElse(BigDecimal.ZERO).toString());

			
			Map<String, List<TmsCalculateIO>> groupByMapped = datas.stream().collect(Collectors.groupingBy(TmsCalculateIO::getDlvyDe));
			groupByMapped.keySet().stream().map(k -> {
				ExecutorVo exeVo = new ExecutorVo();
				exeVo.setDlvyDe(k).setTmsCalculateIO(groupByMapped.get(k));
				String[] routes = exeVo.tmsCalculateIOs.stream().map(TmsCalculateIO::getRouteNo).toArray(String[]::new);
				return exeVo.setTmsPlans(tmsSvc.findTmPlansByIds(k, routes));
			}).forEach(vo -> {
				Map<String, List<TmsPlanIO>> mapped = vo.tmsPlans.stream().collect(Collectors.groupingBy(TmsPlanIO::getRouteNo));
				mapped.keySet().forEach(routeNo -> {
					TmsPlanIO plan = mapped.get(routeNo).stream().findFirst().get();
					List<TmsPlanIO> plans = mapped.get(routeNo);

					plans.removeIf(p -> p.getStopSeq() == 0 || p.getStopSeq() == plans.size()-1);

					
					if ( plans.size() == 0 ) {
						T_ROUTE_PATH_MATRIX6 mat6 = new T_ROUTE_PATH_MATRIX6();
						mat6.setDlvyDe(plan.getDlvyDe());
						mat6.setRouteNo(routeNo);
						mat6.setStartLat(changePoint.getLat()+"");
						mat6.setStartLng(changePoint.getLng()+"");
						mat6.setStartPos(changePoint.getName());
						mat6.setEndLat(changePoint.getLat()+"");
						mat6.setEndLng(changePoint.getLng()+"");
						mat6.setEndPos(changePoint.getName());
						mat6.setVrn(plan.getVrn());
						mat6.setIdLgistModelFk(model);
						inc.incrementAndGet();
						
						service.saveMatrix(mat6);
						return;
					}
					TmsCalculateIO calculate = vo.findTmsCalculateIO(routeNo);
					
					try {
						List<TmapApiParam> naviparams = makeMetrix(changePoint, plans, CarTon.convert(plan.getCarWegit()));
						
						OdMatrixInfoVo nvo = null;
						
						if(naviparams.size() > 1) {
							nvo = naviparams.stream().map(p -> naviSvc.findNavigationInfo(p)).reduce((a, b) -> {
								return b;
							}).orElse(new OdMatrixInfoVo());
						}else {
							nvo = naviSvc.findNavigationInfo(naviparams.get(0));
						}
						
						TmsSettleCost cost = service.saveMatrix(calculate, nvo, model.getIdLgistRuleMstrFk());
						T_ROUTE_PATH_MATRIX6 mat6 = new T_ROUTE_PATH_MATRIX6();
						mat6.setDlvyDe(plan.getDlvyDe());
						mat6.setEndLat(changePoint.getLat()+"");
						mat6.setEndLng(changePoint.getLng()+"");
						mat6.setEndPos(changePoint.getName());
						mat6.setJsonData(nvo.getNavigationJson());
						mat6.setNewDistance(nvo.getTotalDistance());
						mat6.setNewTollCost(nvo.getTotalTollCost());
						mat6.setNewTotalTime(nvo.getTotalTime());
						mat6.setRouteNo(routeNo);
						mat6.setRoutePathCnt(nvo.getRoutePathCount());
						mat6.setStartLat(changePoint.getLat()+"");
						mat6.setStartLng(changePoint.getLng()+"");
						mat6.setStartPos(changePoint.getName());
						mat6.setTmsDistance(calculate.getTmsDistance().intValue());
						mat6.setTmsTollCost(calculate.getTmsTollCost().intValue());
						mat6.setVrn(plan.getVrn());
						mat6.setTmsTurnRate(calculate.getTurnRate());
						mat6.setNewTurnRate(cost.getNewTurnRate());
						mat6.setNewSettleCost(cost.getNewSettleCost());
						mat6.setNewFuelCost(cost.fuelCost());
						mat6.setSupportedCarOil(calculate.getSupportOil());
						mat6.setSupportedFreezOil(calculate.getSupportFrezOil());
						mat6.setPaymentCost(calculate.getShipPayment());
						mat6.setWeight(calculate.getWeight());
						mat6.setIdLgistModelFk(model);
						
						addNewCost(cost.getNewSettleCost());
						model.setCostAfter(newShipCost.toString());
						model.setAnalsCnt(inc.incrementAndGet());
						System.out.println(mat6);
						
						service.saveMatrix(mat6);
						service.saveModel(model);
					} catch (IOException e) {
						log.error("{}", e);
						inc.incrementAndGet();
					}
				});
			});
			
			model.setAnalsEndDt(new Date());
			model.setSttus(2);
			service.saveModel(model);
		}catch (Exception e) {
			log.error("{}", e);
			if(_model != null) {
				_model.setSttus(3);
				service.saveModel(_model);
			}
		}finally {
			
			service.rmExecuteCount();
			
		}
	}
	
	private void addNewCost(BigDecimal cost) {
		newShipCost = newShipCost.add(cost);
	}

	private boolean predicate(TmsCalculateIO c) {
		return !c.getRouteNo().contains("_") && !c.getVrn().startsWith("물류");
	}

	private double nullsafeDouble(String str) {
		return StringUtils.isEmpty(str) ? 0d : new BigDecimal(str).doubleValue();
	}









	
	
	private List<TmapApiParam> makeMetrix(NaviPoint p, List<TmsPlanIO> plans, CarTon ton) throws IOException {
		
		List<TmsPlan> tmsplans = convertPlan(plans);
		List<OdMatrix> matrixs = new ArrayList<>();
		
		if(tmsplans != null && !tmsplans.isEmpty()) {
			List<TmsPlan> newtmsplans = null;
			if(!tmsplans.isEmpty()) {
				tmsplans.stream().filter(p1 -> (p1.getOrgBundledDlvyLc() != null && !p1.getStopCd().equals(p1.getOrgBundledDlvyLc()))).map(p1 -> {
					System.out.println(p1);
					TmsPlan parent = findStop(p1.getOrgBundledDlvyLc(), tmsplans);
					if(parent != null) {
						p1.setLat(parent.getLat());
						p1.setLng(parent.getLng());
					}else {
						
						String stopCd = p1.getOrgBundledDlvyLc();
						TmsLocationIO loc = stopSvc.findStopLocation(stopCd);
						if(loc != null) {
							NaviPoint _point = convert(stopSvc.findStopLocation(stopCd));
							if(_point != null) {
								p1.setLat(_point.getLat() + "");
								p1.setLng(_point.getLng() + "");
							}
						}else {
							log.info("tms stopCd -> {} , tms bundled Cd -> {}", p1.getStopCd(), p1.getOrgBundledDlvyLc());
							log.error("tms getOrgBundledDlvyLc null -> {}", stopCd);
						}
					}
					return p1;
				}).count();
				
				newtmsplans = tmsplans.stream().filter(distinctByKey(TmsPlan::getKey)).collect(Collectors.toList());
				
				for(int i = 1 ; i <= (newtmsplans.size()-1) ; i++) {
					TmsPlan _start = newtmsplans.get(i-1);
					TmsPlan _end = newtmsplans.get(i);
					matrixs.add(OdMatrix.builder().start(_start).end(_end).edLcCd(_end.getStopCd()).stLcCd(_start.getStopCd()).factryCd(_start.getFctryCd()).build())
					;
				}
				
				if (matrixs.size() > 4) {
					matrixs = matrixs.stream().filter(distinctByKey(OdMatrix::getKey)).collect(Collectors.toList());
					return makePathInfo(p, matrixs, ton);
				} else {
					return Arrays.asList(TmapApiParam.builder()
							.start(p)
							.end(p)
							.carTon(ton)
							.paths(newtmsplans.stream().map(plan -> convert(plan)).collect(Collectors.toList()))
							.build());
				}
			}
		}
		return Collections.emptyList();
	}
	
	



















































	
	
	private List<TmapApiParam> makePathInfo(final NaviPoint p, List<OdMatrix> matrixs, CarTon ton) throws IOException {
		
		NaviPoint start = p;
		NaviPoint end = start;
		List<TmapApiParam> infos = new ArrayList<>();
		List<List<OdMatrix>> partitions = partitionBy(matrixs);
		for(int i = 0 ; i < partitions.size() ; i++) {
			List<OdMatrix> _list = partitions.get(i);
			List<NaviPoint> paths = new ArrayList<>();
			if(i == 0) {
				start = p;
			}
			for(int k = 0 ; k < _list.size() ; k++) {
				
				if(i != 0 && k == 0) {
					start = convert(_list.get(k).getStart());
				}
				
				if(i == partitions.size() -1) {
					paths.add(convert(_list.get(k).getEnd()));
				}else {
					paths.add(convert(_list.get(k).getStart()));
				}
				
				if(k == _list.size() -1) {
					end = convert(_list.get(k).getEnd());
				}
			}
			if(i == partitions.size() -1) {
				end = p;
			}
			System.out.println(start + " -> " + paths + " -> " + end);
			infos.add(TmapApiParam.builder()
					.start(start)
					.end(end)
					.carTon(ton)
					.paths(paths)
					.build());
		}
		return infos;
	}
	
	private NaviPoint convert(TmsLocationIO lc) {
		return NaviPoint.builder().lat(lc.getLat()).lng(lc.getLng()).name(lc.getName()).build();
	}
	private NaviPoint convert(TmsPlan lc) {
		return NaviPoint.builder().lat(Double.valueOf(lc.getLat())).lng(Double.valueOf(lc.getLng())).name(lc.getDlvyLoNm()).build();
	}

	private TmsPlan findStop(String bundledDlvyLc, List<TmsPlan> tmsplans) {
		return tmsplans.stream().filter(p -> p.getStopCd().equals(bundledDlvyLc)).findFirst().orElse(null);
	}
	





	
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
	    Set<Object> seen = ConcurrentHashMap.newKeySet();
	    return t -> seen.add(keyExtractor.apply(t));
	}
	





	
	private List<TmsPlan> convertPlan(List<TmsPlanIO> tmsplans) {
		return tmsplans.stream().map(p -> {
			TmsPlan newPlan = new TmsPlan();
			BeanUtils.copyProperties(p, newPlan);
			return newPlan;
		}).collect(Collectors.toList());
		
	}
	






	
	private List<List<OdMatrix>> partitionBy(List<OdMatrix> matrixs) {
		
		int chunkSize = 5;
		List<List<OdMatrix>> lists = new ArrayList<>();
		for (int i=0; i<matrixs.size(); i+= chunkSize) {
		    int end = Math.min(matrixs.size(), i + chunkSize);
		    lists.add(matrixs.subList(i, end));
		}
		return lists;
	}
	
	@Getter
	@Setter
	@ToString
	private static class TmsPlan {
		private String lat;
		private String lng;
		private String dlvyDe;
		private String carType;
		private String stopCd;
		private String routeNo;
		private String vrn;
		private String fctryCd;
		private String dlvyLoNm;
		private String carWegit;
		private String orgBundledDlvyLc;
		private String confRtateRate;
		
		public String getKey() {
			return lat + lng;
		}
	}
	@Getter
	@Builder
	public static class OdMatrix {
		private String stLcCd;
		private String edLcCd;
		private String factryCd;
		private String groupCd;
		private TmsPlan start;
		private TmsPlan end;
		
		@Override
		public String toString() {
			return "OdMatrix [stLcCd=" + start.getDlvyLoNm() + ", edLcCd=" + end.getDlvyLoNm() + "]";
		}
		
		public String getKey() {
			return start.getLat() + start.getLng();
		}
	}
	
}
