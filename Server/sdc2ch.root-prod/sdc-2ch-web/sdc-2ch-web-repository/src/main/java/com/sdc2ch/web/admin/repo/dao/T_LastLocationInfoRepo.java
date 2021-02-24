package com.sdc2ch.web.admin.repo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdc2ch.web.admin.repo.domain.op.T_LOCATION_INFO;

public interface T_LastLocationInfoRepo extends JpaRepository<T_LOCATION_INFO, String>{
}
