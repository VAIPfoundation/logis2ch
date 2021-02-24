package com.sdc2ch.web.admin.repo.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdc2ch.web.admin.repo.domain.v.V_DRIVER;

public interface V_DriverRepository extends JpaRepository<V_DRIVER, Long>{

	Optional<V_DRIVER> findByDriverCd(String driverCd);
	Optional<V_DRIVER> findByMobileNo(String mobileNo);
}
