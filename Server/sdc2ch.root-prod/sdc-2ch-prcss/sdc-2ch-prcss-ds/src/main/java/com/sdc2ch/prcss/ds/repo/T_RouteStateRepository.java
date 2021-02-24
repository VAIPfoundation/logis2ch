package com.sdc2ch.prcss.ds.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.sdc2ch.prcss.ds.repo.domain.T_ROUTE_STATE;
import com.sdc2ch.repo.io.RouteStateID;

public interface T_RouteStateRepository extends JpaRepository<T_ROUTE_STATE, RouteStateID>, QuerydslPredicateExecutor<T_ROUTE_STATE>{
}
