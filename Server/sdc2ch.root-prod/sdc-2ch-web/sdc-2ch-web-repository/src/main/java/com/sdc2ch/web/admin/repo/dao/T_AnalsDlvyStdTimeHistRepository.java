package com.sdc2ch.web.admin.repo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.sdc2ch.web.admin.repo.domain.anals.T_ANALS_DLVY_STD_TIME_HIST;


public interface T_AnalsDlvyStdTimeHistRepository extends JpaRepository<T_ANALS_DLVY_STD_TIME_HIST, Long>, QuerydslPredicateExecutor<T_ANALS_DLVY_STD_TIME_HIST>{

}
