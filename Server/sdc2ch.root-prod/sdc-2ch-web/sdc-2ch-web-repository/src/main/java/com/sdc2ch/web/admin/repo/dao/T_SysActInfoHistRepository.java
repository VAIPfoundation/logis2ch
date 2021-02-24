package com.sdc2ch.web.admin.repo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.sdc2ch.web.admin.repo.domain.sys.T_SYS_ACT_INFO_HIST;

public interface T_SysActInfoHistRepository extends JpaRepository<T_SYS_ACT_INFO_HIST, Long>, QuerydslPredicateExecutor<T_SYS_ACT_INFO_HIST>{

}
