package com.sdc2ch.web.admin.repo.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.sdc2ch.web.admin.repo.domain.v.V_VHCLE;

public interface V_CarRepository extends QuerydslPredicateExecutor<V_VHCLE>, JpaRepository<V_VHCLE, Long>{
	Optional<V_VHCLE> findByMobileNo(String mobileNo);
	Optional<V_VHCLE> findByVrn(String vrn);
}
