package com.sdc2ch.web.admin.repo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.sdc2ch.web.admin.repo.domain.alloc.T_ALARM_SETUP;

public interface T_AlarmSetupRepository extends JpaRepository<T_ALARM_SETUP, Long>, QuerydslPredicateExecutor<T_ALARM_SETUP>{

}
