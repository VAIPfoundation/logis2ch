package com.sdc2ch.web.admin.repo.dao.lgist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.sdc2ch.web.admin.repo.domain.lgist.T_LGIST_MODEL;

public interface T_LgistModelRepository extends QuerydslPredicateExecutor<T_LGIST_MODEL>, JpaRepository<T_LGIST_MODEL, Long>{
}
