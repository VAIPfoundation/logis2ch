package com.sdc2ch.web.admin.repo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdc2ch.web.admin.repo.domain.HealthCheck;

public interface HelthCheckRepository extends JpaRepository<HealthCheck, Long>{
}
