package com.sdc2ch.nfc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sdc2ch.nfc.component.JPAQueryBuilder;
import com.sdc2ch.nfc.domain.entity.QT_dev;
import com.sdc2ch.nfc.domain.entity.T_dev;
import com.sdc2ch.nfc.repository.V_NfcDeviceRepository;
import com.sdc2ch.nfc.service.INfcDeviceService;

@Service
class NfcDeviceServiceImpl implements INfcDeviceService {
	
	@Autowired V_NfcDeviceRepository deviceRepo;
	@Autowired JPAQueryBuilder builder;

	@Override
	public T_dev findById(int id) {
		

		QT_dev tdev = QT_dev.t_dev;
		JPAQueryFactory query = builder.create();
		return query.select(Projections.constructor(T_dev.class, tdev.id, tdev.name)).from(tdev).where(tdev.id.eq(id)).fetchOne();
	}


}
