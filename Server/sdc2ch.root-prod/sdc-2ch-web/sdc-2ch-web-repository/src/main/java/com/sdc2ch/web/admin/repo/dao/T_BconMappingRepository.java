package com.sdc2ch.web.admin.repo.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.sdc2ch.repo.io.BeaconMappingIO;
import com.sdc2ch.web.admin.repo.domain.op.T_BCON_MAPPING;

public interface T_BconMappingRepository extends JpaRepository<T_BCON_MAPPING, Long>, QuerydslPredicateExecutor<T_BCON_MAPPING>{

	Optional<BeaconMappingIO> findByBconId(String bconId);
}
