package com.sdc2ch.web.admin.repo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.sdc2ch.web.admin.repo.domain.event.T_EVENT_WEB_IO_HIST;

@Repository
public interface T_EventWebioHistRepository extends JpaRepository<T_EVENT_WEB_IO_HIST, Long>, QuerydslPredicateExecutor<T_EVENT_WEB_IO_HIST>{

}
