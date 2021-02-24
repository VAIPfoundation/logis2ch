package com.sdc2ch.web.admin.repo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.sdc2ch.web.admin.repo.domain.alloc.T_ALRAM_SETUP;

public interface T_AlramSetupRepository extends JpaRepository<T_ALRAM_SETUP, Long>, QuerydslPredicateExecutor<T_ALRAM_SETUP>{

}
