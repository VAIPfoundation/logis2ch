package com.sdc2ch.nfc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.sdc2ch.nfc.domain.entity.T_usr;


public interface V_NfcUserRepository extends JpaRepository<T_usr, Integer>, QuerydslPredicateExecutor<T_usr> {

}
