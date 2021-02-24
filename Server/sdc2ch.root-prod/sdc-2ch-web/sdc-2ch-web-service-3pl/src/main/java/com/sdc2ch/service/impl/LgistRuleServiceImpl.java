package com.sdc2ch.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.sdc2ch.service.ILgistRuleService;
import com.sdc2ch.web.admin.repo.dao.lgist.T_LgistRuleMstrTableRepository;
import com.sdc2ch.web.admin.repo.domain.lgist.QT_LGIST_RULE_MSTR;
import com.sdc2ch.web.admin.repo.domain.lgist.T_LGIST_RULE_MSTR;

@Service
public class LgistRuleServiceImpl implements ILgistRuleService {
	
	
	@Autowired T_LgistRuleMstrTableRepository ruleMstrRepo;

	
	@Override
	public List<T_LGIST_RULE_MSTR> search() {
		return search(null, null);
	}
	@Override
	public List<T_LGIST_RULE_MSTR> search(Date start, Date end) {
		return search(start, end, null);
	}
	@Override
	public List<T_LGIST_RULE_MSTR> search(Date start, Date end, String ruleNm) {
		return Lists.newArrayList(ruleMstrRepo.findAll(predicate(start, end, ruleNm)));
	}

	@Override
	public T_LGIST_RULE_MSTR save(T_LGIST_RULE_MSTR mstr) {
		return ruleMstrRepo.save(mstr);
	}
	
	private Predicate predicate(Date start, Date end, String ruleNm) {
		QT_LGIST_RULE_MSTR qMstr = QT_LGIST_RULE_MSTR.t_LGIST_RULE_MSTR;
		BooleanBuilder where = new BooleanBuilder();
		where.and(qMstr.useYn.eq(true));
		
		if(start != null) {
			where.and(qMstr.createDt.between(start, end));
		}
		if(!StringUtils.isEmpty(ruleNm)) {
			where.and(qMstr.ruleNm.like("%" + ruleNm + "%"));
		}
		return where;
	}

	@Override
	public T_LGIST_RULE_MSTR findById(Long id) {
		return ruleMstrRepo.findById(id).orElse(new T_LGIST_RULE_MSTR());
	}
}
