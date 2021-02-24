package com.sdc2ch.web.admin.repo.dao;

import java.util.List;
import java.util.Optional;

import javax.persistence.OrderBy;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.sdc2ch.web.admin.repo.domain.v.V_CARALC_MSTR;

public interface V_CaralcMstrRepository extends JpaRepository<V_CARALC_MSTR, Long>, QuerydslPredicateExecutor<V_CARALC_MSTR>{


	Optional<List<V_CARALC_MSTR>> findAllByDlvyDeAndVrn(String dlvyDe, String vrn);
	Optional<List<V_CARALC_MSTR>> findAllByDlvyDeAndVrn(String dlvyDe, String vrn, Sort sort);

}
