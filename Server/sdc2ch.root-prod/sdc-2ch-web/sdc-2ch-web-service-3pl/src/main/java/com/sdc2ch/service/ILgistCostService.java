package com.sdc2ch.service;

import java.util.Date;
import java.util.List;

import com.sdc2ch.service.common.model.ComboBoxVo;
import com.sdc2ch.service.model.ProductQty;
import com.sdc2ch.web.admin.repo.domain.lgist.T_LGIST_MODEL;
import com.sdc2ch.web.admin.repo.domain.lgist.T_LGIST_RULE_MSTR;
import com.sdc2ch.web.admin.repo.domain.lgist.T_ROUTE_PATH_MATRIX6;


public interface ILgistCostService {

	
	List<ComboBoxVo> searchFactoryCombo();

	
	List<ComboBoxVo> searchCarAlcTypeCombo();


	
	List<T_LGIST_MODEL> searchModel(Date start, Date end, String modelNm);
	List<T_LGIST_MODEL> searchModel(Date start, Date end, String modelNm, String logistTy);
	List<ProductQty> searchItemProducts(Date start, Date end, String factryCd, String ... routeNos );
	List<T_LGIST_RULE_MSTR> searchRule(Date start, Date end, String ruleNm);


	
	List<ComboBoxVo> searchRouteNoCombo(Date start, Date end, String caralcCd, String fctryCd);
	List<ComboBoxVo> searchAllRouteNoCombo(String fctryCd);



	T_LGIST_RULE_MSTR findByRuleId(Long id);
	T_LGIST_MODEL findByModelId(Long id);

	
	T_LGIST_MODEL saveModel(T_LGIST_MODEL model);
	T_LGIST_RULE_MSTR saveRule(T_LGIST_RULE_MSTR model);


	
	List<ComboBoxVo> getRuleCombo();
	
	List<ComboBoxVo> getModelCombo();
	
	
	List<ComboBoxVo> findRouteNoByFactryCd(String factryCd);

	void startAnals(Long id);
	
	public T_ROUTE_PATH_MATRIX6 saveMatrix(T_ROUTE_PATH_MATRIX6 entity);
	
	


}
