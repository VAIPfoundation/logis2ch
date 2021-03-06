package com.sdc2ch.web.admin.repo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.sdc2ch.web.admin.repo.domain.T_MOBILE_HEALTH_CHK_HIST;

public interface T_MobileHealthChkHistRepository  extends JpaRepository<T_MOBILE_HEALTH_CHK_HIST, Long>, QuerydslPredicateExecutor<T_MOBILE_HEALTH_CHK_HIST>{

}
