package com.sdc2ch.tms.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdc2ch.tms.domain.view.TmsTurnRateD;

public interface TmsTurnRateRepositiry extends JpaRepository<TmsTurnRateD, Long> {

}
