package com.sdc2ch.tms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.sdc2ch.tms.dao.TmsDayOffExeRepository;
import com.sdc2ch.tms.domain.view.QTmsDayOffExe;
import com.sdc2ch.tms.io.TmsDayOffExeIO;
import com.sdc2ch.tms.service.ITmsDayOffExeService;

@Service
public class TmsDayOffExeServiceImpl implements ITmsDayOffExeService {
	@Autowired TmsDayOffExeRepository repo;

	@Override
	public List<TmsDayOffExeIO> findAllByFctryCdAndDlvyMon(String fctryCd, String month) {
		return Lists.newArrayList(repo.findAll(predicate(fctryCd, month)));
	}

	
	private Predicate predicate(String fctryCd, String month) {
		QTmsDayOffExe exe = QTmsDayOffExe.tmsDayOffExe;
		BooleanBuilder where = new BooleanBuilder();
		if(!StringUtils.isEmpty(fctryCd)) {
			where.and(exe.fctryCd.eq(fctryCd));
		}
		if(!StringUtils.isEmpty(month)) {
			where.and(exe.dayOffDe.like(month + "%"));
		}
		return where;
	}
}
