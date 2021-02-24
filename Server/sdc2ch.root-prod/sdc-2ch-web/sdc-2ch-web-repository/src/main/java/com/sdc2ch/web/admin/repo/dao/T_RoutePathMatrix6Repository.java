package com.sdc2ch.web.admin.repo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.sdc2ch.web.admin.repo.domain.lgist.T_ROUTE_PATH_MATRIX6;

public interface T_RoutePathMatrix6Repository extends QuerydslPredicateExecutor<T_ROUTE_PATH_MATRIX6>, JpaRepository<T_ROUTE_PATH_MATRIX6, Long> {

}
