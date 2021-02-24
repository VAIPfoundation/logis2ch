package com.sdc2ch.web.admin.repo.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdc2ch.web.admin.repo.domain.T_MOBILE_APP_USE_INFO;

public interface T_MobileAppUseRepository extends JpaRepository<T_MOBILE_APP_USE_INFO, Long>{
	
	Optional<T_MOBILE_APP_USE_INFO> findByUserId(String id);
}
