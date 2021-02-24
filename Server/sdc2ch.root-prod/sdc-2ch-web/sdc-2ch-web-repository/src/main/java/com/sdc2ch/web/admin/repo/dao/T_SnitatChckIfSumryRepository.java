package com.sdc2ch.web.admin.repo.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdc2ch.web.admin.repo.domain.alloc.T_SNITAT_CHCK_IF_SUMRY;

public interface T_SnitatChckIfSumryRepository extends JpaRepository<T_SNITAT_CHCK_IF_SUMRY, Long>{

	String erpIFOpenquery = "select legacykey, userid, status from openquery(milk, 'select * from XXC.T_APPLEGACY4ERP where legacykey in (%s)')";

	public T_SNITAT_CHCK_IF_SUMRY findOneByFctryCdAndYearAndMonth(String fctryCd, String year, String month);
	public Optional<T_SNITAT_CHCK_IF_SUMRY> findOneByFctryCdAndYearAndMonthAndUserId(String fctryCd, String year, String month, String userId);
	public List<T_SNITAT_CHCK_IF_SUMRY> findAllByFctryCdAndYearAndMonth(String fctryCd, String year, String month);

}
