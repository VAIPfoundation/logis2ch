package com.sdc2ch.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.sdc2ch.service.ILgistCostService;
import com.sdc2ch.service.ILgistModelService;
import com.sdc2ch.service.ILgistRuleService;
import com.sdc2ch.service.ILogistExecutService;
import com.sdc2ch.service.common.IComboBoxService;
import com.sdc2ch.service.common.model.ComboBoxVo;
import com.sdc2ch.service.model.ProductQty;
import com.sdc2ch.web.admin.repo.AdmQueryBuilder;
import com.sdc2ch.web.admin.repo.dao.T_RoutePathMatrix6Repository;
import com.sdc2ch.web.admin.repo.domain.lgist.T_LGIST_MODEL;
import com.sdc2ch.web.admin.repo.domain.lgist.T_LGIST_MODEL.TableType;
import com.sdc2ch.web.admin.repo.domain.lgist.T_LGIST_RULE_MSTR;
import com.sdc2ch.web.admin.repo.domain.lgist.T_ROUTE_PATH_MATRIX6;
import com.sdc2ch.web.admin.repo.domain.v.QV_CARALC_MSTR;

@Service
public class LgistCostServiceImpl implements ILgistCostService {

	@Autowired T_RoutePathMatrix6Repository repo;
	@Autowired ILgistRuleService ruleSvc;
	@Autowired ILgistModelService modelSvc;
	@Autowired IComboBoxService comboSvc;
	
	@Autowired List<ILogistExecutService> executors;
	
	@Autowired AdmQueryBuilder builder;
	
	@Override
	public List<ComboBoxVo> searchFactoryCombo() {
		return comboSvc.findFactoryCombo();
	}

	@Override
	public List<ComboBoxVo> searchCarAlcTypeCombo() {
		return comboSvc.findCarAlcTypeCombo();
	}

	@Override
	public List<T_LGIST_MODEL> searchModel(Date start, Date end, String modelNm) {
		return searchModel(start, end, modelNm, TableType.DELIVERY.name());
	}

	@Override
	public List<T_LGIST_MODEL> searchModel(Date start, Date end, String modelNm, String logistTy) {
		return modelSvc.searchModel(start, end, modelNm, logistTy);
	}


	@Override
	public List<ComboBoxVo> searchRouteNoCombo(Date start, Date end, String caralcCd, String fctryCd) {

		
		if(start == null || end == null) {
			return searchAllRouteNoCombo(fctryCd);
		}
		
		QV_CARALC_MSTR mstr = QV_CARALC_MSTR.v_CARALC_MSTR;
		BooleanBuilder where = new BooleanBuilder();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		where.and(mstr.dlvyDe.between(fmt.format(start), fmt.format(end)));
		if( fctryCd != null ) {
			where.and(mstr.fctryCd.eq(fctryCd));
		}
		List<String> routeNos = builder.create().selectDistinct(mstr.routeNo).from(mstr).where(where).fetch();
		System.out.println(routeNos);
		
		return routeNos.stream().map(r -> ComboBoxVo.of(r, r)).sorted(Comparator.comparing(ComboBoxVo::getKey)).collect(Collectors.toList());
	}

	@Override
	public List<ComboBoxVo> getRuleCombo() {
		return ruleSvc.search().stream().map(m -> ComboBoxVo.of(m.getId() + "", m.getRuleNm())).collect(Collectors.toList());
	}

	@Override
	public List<T_LGIST_RULE_MSTR> searchRule(Date start, Date end, String ruleNm) {
		return ruleSvc.search(start, end, ruleNm);
	}

	@Override
	public T_LGIST_RULE_MSTR findByRuleId(Long id) {
		return ruleSvc.findById(id);
	}

	@Override
	public T_LGIST_MODEL saveModel(T_LGIST_MODEL model) {
		return modelSvc.save(model);
	}

	@Override
	public T_LGIST_MODEL findByModelId(Long id) {
		return modelSvc.findById(id);
	}

	@Override
	public List<ComboBoxVo> getModelCombo() {
		return modelSvc.searchModel().stream().map(m -> ComboBoxVo.of(m.getId() + "", m.getModelNm()))
				.collect(Collectors.toList());
	}
	@Override
	public List<ComboBoxVo> searchAllRouteNoCombo(String fctryCd) {
		String query = "[dbo].[SP_2CH_LGIST_FCTRY_ROUTE_LIST]";
		List<ComboBoxVo> combos = new ArrayList<>();
		List<?> results = builder.storedProcedureResultCall(query, fctryCd);
		
		if(results != null) {
			for(Object o : results) {
				combos.add(ComboBoxVo.of(o + "", o + ""));
			}
		}
		return combos;
	}

	@Override
	public void startAnals(Long id) {
		T_LGIST_MODEL model = findByModelId(id);
		executors.stream().filter(e -> e.supported(model.getLgistTy())).collect(Collectors.toList()).forEach(e -> e.execute(model));
	}

	@Override
	public T_LGIST_RULE_MSTR saveRule(T_LGIST_RULE_MSTR model) {
		return ruleSvc.save(model);
	}
	
	@Override
	public List<ComboBoxVo> findRouteNoByFactryCd(String factryCd) {
		return null;
	}
	
	@Override
	public T_ROUTE_PATH_MATRIX6 saveMatrix(T_ROUTE_PATH_MATRIX6 entity) {
		return repo.save(entity);
	}

	@Override
	public List<ProductQty> searchItemProducts(Date start, Date end, String factryCd, String... routeNos) {
		
		String query = "[dbo].[SP_2CH_LGIST_ROUTE_ITEM_PRODUCT]";
		
		String routeNoCsv = Stream.of(routeNos).collect(Collectors.joining("','", "'", "'"));
		List<Object[]> results = builder.storedProcedureResultCall(query, factryCd, format(start), format(end), routeNoCsv);
		List<ProductQty> qtys = new ArrayList<>();
		
		if(results != null) {
			for(Object[] obj : results) {
				ProductQty std = new ProductQty();
				std.setItemCd(nullSafeString(obj[0]));
				std.setItemNm(nullSafeString(obj[1]));
				std.setShipQty(nullSafeDouble(obj[2]));
				std.setPackQty(nullSafeDouble(obj[3]));
				std.setP1(nullSafeString(obj[4]));
				std.setP2(nullSafeString(obj[5]));
				std.setP3(nullSafeString(obj[6]));
				std.setP4(nullSafeString(obj[7]));
				qtys.add(std);
			}
		}
		
		return qtys;
	}
	
	private double nullSafeDouble(Object object) {
		return new BigDecimal(nullSafeString(object)).doubleValue();
	}

	private String nullSafeString(Object object) {
		return object == null ? "" : object.toString();
	}

	private String format(Date dt) {
		return new SimpleDateFormat("yyyyMMdd").format(dt);
	}





























}
