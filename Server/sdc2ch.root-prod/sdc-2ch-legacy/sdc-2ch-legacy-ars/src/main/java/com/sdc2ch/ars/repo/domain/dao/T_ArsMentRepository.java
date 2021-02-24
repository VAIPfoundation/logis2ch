package com.sdc2ch.ars.repo.domain.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdc2ch.ars.repo.domain.T_ARS_MENT;

public interface T_ArsMentRepository extends JpaRepository<T_ARS_MENT, Long>{
	Optional<T_ARS_MENT> findByMessageTy(String messageTy);
}
