package com.sdc2ch.tms.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdc2ch.tms.domain.view.TmsDayoffSch;

public interface TmsDayoffSchRepository extends JpaRepository<TmsDayoffSch, Long> {

}
