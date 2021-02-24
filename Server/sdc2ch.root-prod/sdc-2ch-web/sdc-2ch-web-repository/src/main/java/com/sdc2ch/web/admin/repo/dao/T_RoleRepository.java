package com.sdc2ch.web.admin.repo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdc2ch.web.admin.repo.domain.T_ROLE;
import com.sdc2ch.web.admin.repo.enums.RoleEnums;

public interface T_RoleRepository extends JpaRepository<T_ROLE, Long>{
	
	T_ROLE findByRole(RoleEnums enums);

}
