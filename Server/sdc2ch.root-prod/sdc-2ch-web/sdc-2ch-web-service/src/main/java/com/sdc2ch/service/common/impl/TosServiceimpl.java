package com.sdc2ch.service.common.impl;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.querydsl.core.BooleanBuilder;
import com.sdc2ch.service.common.ITosService;
import com.sdc2ch.web.admin.repo.AdmQueryBuilder;
import com.sdc2ch.web.admin.repo.dao.T_ToSRepository;
import com.sdc2ch.web.admin.repo.domain.QT_TOS;
import com.sdc2ch.web.admin.repo.domain.T_TOS;
import com.sdc2ch.web.admin.repo.enums.ToSRegEnums;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
class TosServiceimpl implements ITosService {

	@Autowired T_ToSRepository tosRepo;
	@Autowired AdmQueryBuilder builder;
	@Override
	public List<T_TOS> findByCurrentTos() {
		return tosRepo.findAll().stream().filter(t -> t.isCurrent()).collect(toList());
	}

	@Override
	public List<T_TOS> findByIds(Long... ids) {
		return findByIds(Arrays.asList(ids));
	}

	@Override
	public List<T_TOS> findByIds(List<Long> ids) {
		return tosRepo.findAllById(ids);
	}

	@Override
	public Optional<T_TOS> findById(Long id) {
		return tosRepo.findById(id);
	}

	@Override
	public List<T_TOS> findByRegType(ToSRegEnums regType){
		BooleanBuilder where = new BooleanBuilder();
		QT_TOS tos = QT_TOS.t_TOS;
		if(regType != null) {
			where.and(tos.regType.eq(regType));
		}
		return findByAll(where);
	}

	public List<T_TOS> findByAll(BooleanBuilder where){
		return (List<T_TOS>)tosRepo.findAll(where);
	}

	@Override
	public T_TOS save(Long id, String title, String contents, ToSRegEnums regType, Integer major, String regUser) {
		T_TOS tos = new T_TOS();
		tos.setId(id);
		tos.setTitle(title);
		tos.setContents(contents);
		tos.setRegType(regType);
		tos.setMajor(major);
		tos.setRegUser(regUser);
		return this.save(tos);
	}

	public List<T_TOS> save(List<T_TOS> toss){
		toss.stream().forEach(this::save);
		return toss;
	}

	@Transactional
	public T_TOS save(T_TOS tos){
		if(tos.getId() != null) {
			return tos;
		}

		if(tos.getMajor() == 0) {
			tos.setMajor(this.getMaxMajorVersion(tos.getRegType()));
		}
		Integer minor = this.getMaxMinorVersion(tos.getRegType(), tos.getMajor());
		tos.setMinor(minor != null ? minor + 1 : 1);
		this.updateCurrentByRegType(false, tos.getRegType());
		tos.setCurrent(true);
		tos.setRegDate(new Date());
		return tosRepo.save(tos);
	}

	public void updateCurrentByRegType(boolean current, ToSRegEnums regType) {
		tosRepo.updateCurrentByRegType(current, regType.toString());
	}

	@Override
	public Integer getMaxMinorVersion(ToSRegEnums regType, Integer major) {
		Assert.notNull(regType, "regType is must be not null");
		Assert.notNull(major,  "major is must be not null");
		QT_TOS tos = QT_TOS.t_TOS;
		return builder.create().select(tos.minor.max()).from(tos).where(tos.regType.eq(regType), tos.major.eq(major)).groupBy(tos.regType, tos.major).fetchOne();
	}

	@Override
	public Integer getMaxMajorVersion(ToSRegEnums regType) {
		Assert.notNull(regType, "regType is must be not null");
		QT_TOS tos = QT_TOS.t_TOS;
		return builder.create().select(tos.major.max()).from(tos).where(tos.regType.eq(regType)).groupBy(tos.regType).fetchOne();
	}

}
