package com.sdc2ch.web.admin.repo.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdc2ch.web.admin.repo.domain.T_MENU;

public interface T_MenuRepository extends JpaRepository<T_MENU, Long>{
	
	public static final String ADM_MENU_ROOT_NAME = "admin_root";
	public static final String MBL_MENU_ROOT_NAME = "mobile_root";
	
	Optional<T_MENU> findByTitle(String title);

}
