package com.sdc2ch.tms.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdc2ch.tms.domain.view.TmsOilPrice;

public interface TmsOilPriveRepositiry extends JpaRepository<TmsOilPrice, Long> {
}
