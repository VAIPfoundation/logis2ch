package com.sdc2ch.web.admin.repo.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.sdc2ch.web.admin.repo.domain.v.V_CARALC_PLAN;

public interface V_CaralcPlanRepository extends JpaRepository<V_CARALC_PLAN, Long>, QuerydslPredicateExecutor<V_CARALC_PLAN>{

	Optional<List<V_CARALC_PLAN>> findAllByDlvyDeAndVrnAndIdIsNotNull(String dlvyDe, String vrn, Sort sort);


}
