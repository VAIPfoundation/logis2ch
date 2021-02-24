package com.sdc2ch.nfc.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sdc2ch.nfc.component.JPAQueryBuilder;
import com.sdc2ch.nfc.domain.entity.QT_lg;
import com.sdc2ch.nfc.domain.entity.T_lg;
import com.sdc2ch.nfc.enums.NfcEventType;
import com.sdc2ch.nfc.repository.V_NfcEventRepository;
import com.sdc2ch.nfc.service.INfcEventService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
class NfcEventServiceImpl implements INfcEventService {

	@Autowired
	V_NfcEventRepository repo;
	@Autowired
	JPAQueryBuilder builder;

	@Override
	public List<T_lg> findAll() {
		return repo.findAll();
	}

	@Override
	public List<T_lg> gt(int id) {
		QT_lg tlg = QT_lg.t_lg;
		JPAQueryFactory query = builder.create();
		return query.selectFrom(tlg).where(tlg.id.gt(id)).orderBy(tlg.id.asc()).fetch();
	}

	@Override
	public List<T_lg> findAll(Integer yyyyMM, Date fromDt, Date toDt) {
		Assert.notNull(yyyyMM, "yyyyMM must not bul null");
		Assert.notNull(fromDt, "fromDt must not bul null");
		Assert.notNull(fromDt, "toDt must not bul null");

		return findAll(yyyyMM, fromDt, toDt, NfcEventType.values());
	}

	@Override
	public List<T_lg> findAll(Integer yyyyMM, Date fromDt, Date toDt, NfcEventType ... event) {
		Assert.notNull(yyyyMM, "yyyyMM must not bul null");
		Assert.notNull(fromDt, "fromDt must not bul null");
		Assert.notNull(fromDt, "toDt must not bul null");

		List<Integer> eventList = new ArrayList<Integer>();
		if(event != null) {
			eventList = Arrays.asList(event).stream().map(NfcEventType::getEventCode).collect(Collectors.toList());
		} else {
			eventList = Arrays.asList(NfcEventType.values()).stream().map(NfcEventType::getEventCode).collect(Collectors.toList());
		}
		return repo.searchByFromDtAndToDt(yyyyMM, fromDt, toDt, eventList);

	}

}
