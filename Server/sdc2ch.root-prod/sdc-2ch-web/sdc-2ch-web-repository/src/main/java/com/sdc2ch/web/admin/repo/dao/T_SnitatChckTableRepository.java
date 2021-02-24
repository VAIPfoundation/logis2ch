package com.sdc2ch.web.admin.repo.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.sdc2ch.web.admin.repo.domain.alloc.T_SNITAT_CHCK_TABLE;

public interface T_SnitatChckTableRepository extends QuerydslPredicateExecutor<T_SNITAT_CHCK_TABLE>, JpaRepository<T_SNITAT_CHCK_TABLE, Long>{
	String insertQuery = "INSERT INTO dbo.T_SNITAT_CHCK_TABLE(CREATE_DT,UPDATE_DT,DRIVER_NM,FCTRY_CD,ITEM01,ITEM02,ITEM03,ITEM04,ITEM05,ITEM06,ITEM07,ITEM08,ITEM09,ITEM10,REG_DE,TRNSPRT_CMPNY,VHCLE_TY,VRN,DRIVER_CD) VALUES ";

	@Query(value = "UPDATE T_SNITAT_CHCK_TABLE t"
			+ " SET "
			+ " t.item01 = '○'"
			+ " ,t.item02 = '○'"
			+ " ,t.item03 = '○'"
			+ " ,t.item04 = '○'"
			+ " ,t.item05 = '○'"
			+ " ,t.item06 = '○'"
			+ " ,t.item07 = '○'"
			+ " ,t.item08 = '○'"
			+ " ,t.item09 = '○'"
			+ " ,t.item10 = '○'"
			+ " WHERE "
			+ " t.id IN (:ids) "
			)
	@Modifying
	@Transactional
	int UpdateAllState(@Param("ids") List<Long> ids);
}
