package com.sdc2ch.tms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.sdc2ch.tms.domain.view.TmsDayOffExe;

public interface TmsDayOffExeRepository extends QuerydslPredicateExecutor<TmsDayOffExe>, JpaRepository<TmsDayOffExe, Long> {

}
