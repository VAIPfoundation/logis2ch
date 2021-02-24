package com.sdc2ch.nfc.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.sdc2ch.nfc.domain.entity.T_lg;

public interface V_NfcEventRepository extends JpaRepository<T_lg, Integer>, QuerydslPredicateExecutor<T_lg>{

	@Query(value="SELECT * FROM t_lg:yyyyMM WHERE SRVDT between :fromDt AND :toDt",
			countQuery="SELECT COUNT(*) FROM t_lg:yyyyMM WHERE SRVDT between :fromDt AND :toDt",
			nativeQuery=true)
	List<T_lg> searchByFromDtAndToDt(@Param("yyyyMM") Integer yyyyMM, Date fromDt, Date toDt);

	@Query(value="SELECT * FROM t_lg:yyyyMM WHERE SRVDT between :fromDt AND :toDt AND EVT IN :eventType",
			countQuery="SELECT COUNT(*) FROM t_lg:yyyyMM WHERE SRVDT between :fromDt AND :toDt AND EVT IN :eventType",
			nativeQuery=true)
	List<T_lg> searchByFromDtAndToDt(@Param("yyyyMM") Integer yyyyMM, @Param("fromDt") Date fromDt, @Param("toDt") Date toDt, @Param("eventType") List<Integer> eventType);

}
