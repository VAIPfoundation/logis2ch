package com.sdc2ch.service;

import java.util.Date;
import java.util.List;

import com.sdc2ch.web.admin.repo.domain.lgist.T_LGIST_RULE_MSTR;


public interface ILgistRuleService {
	
	
	
	List<T_LGIST_RULE_MSTR> search();
	List<T_LGIST_RULE_MSTR> search(Date start, Date end);
	List<T_LGIST_RULE_MSTR> search(Date start, Date end, String ruleNm);
	
	
	T_LGIST_RULE_MSTR save(T_LGIST_RULE_MSTR mstr);

	
	T_LGIST_RULE_MSTR findById(Long id);
	
}
