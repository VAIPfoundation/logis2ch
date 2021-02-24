package com.sdc2ch.web.admin.repo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.sdc2ch.web.admin.repo.domain.sttus.T_APP_STTUS_HIST;

public interface T_AppSttusHistRepository extends JpaRepository<T_APP_STTUS_HIST, Long>, QuerydslPredicateExecutor<T_APP_STTUS_HIST> {

}
