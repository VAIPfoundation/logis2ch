package com.sdc2ch.web.admin.repo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.sdc2ch.web.admin.repo.domain.alloc.T_CARALC_CNFIRM_GROUP2;

public interface T_CaralcCnfirmGroupRepository2 extends JpaRepository<T_CARALC_CNFIRM_GROUP2, Long>, QuerydslPredicateExecutor<T_CARALC_CNFIRM_GROUP2>{

}
