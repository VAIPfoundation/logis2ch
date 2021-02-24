package com.sdc2ch.nfc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.sdc2ch.nfc.domain.entity.T_dev;


public interface V_NfcDeviceRepository extends JpaRepository<T_dev, Integer>, QuerydslPredicateExecutor<T_dev> {

}
