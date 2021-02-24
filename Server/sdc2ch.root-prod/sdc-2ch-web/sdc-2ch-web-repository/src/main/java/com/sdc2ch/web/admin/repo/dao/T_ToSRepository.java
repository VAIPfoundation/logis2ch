package com.sdc2ch.web.admin.repo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.sdc2ch.web.admin.repo.domain.T_TOS;
import com.sdc2ch.web.admin.repo.enums.ToSRegEnums;

public interface T_ToSRepository extends JpaRepository<T_TOS, Long>, QuerydslPredicateExecutor<T_TOS>{


	@Modifying
	@Transactional

	@Query(value = "UPDATE T_TOS SET TOS_CURRENT = :current WHERE TOS_REG_TYPE= :regType")
	void updateCurrentByRegType(@Param("current")Boolean current, @Param("regType")String regType);


}
