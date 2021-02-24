package com.sdc2ch.web.admin.repo.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.sdc2ch.web.admin.repo.domain.op.T_LOCATION_INFO_HIST;

public interface T_LocationHistoryRepo extends JpaRepository<T_LOCATION_INFO_HIST, Long>, QuerydslPredicateExecutor<T_LOCATION_INFO_HIST>{


}
