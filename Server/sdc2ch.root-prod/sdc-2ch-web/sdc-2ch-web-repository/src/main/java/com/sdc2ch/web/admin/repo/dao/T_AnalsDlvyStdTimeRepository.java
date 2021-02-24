package com.sdc2ch.web.admin.repo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.sdc2ch.web.admin.repo.domain.anals.T_ANALS_DLVY_STD_TIME;


public interface T_AnalsDlvyStdTimeRepository extends JpaRepository<T_ANALS_DLVY_STD_TIME, Long>, QuerydslPredicateExecutor<T_ANALS_DLVY_STD_TIME>{

}
