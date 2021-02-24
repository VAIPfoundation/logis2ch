package com.sdc2ch.web.admin.repo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.sdc2ch.web.admin.repo.domain.v.V_ALARM_STTUS;

public interface V_AlarmSttusRepository extends JpaRepository<V_ALARM_STTUS, Long>, QuerydslPredicateExecutor<V_ALARM_STTUS>{

}
