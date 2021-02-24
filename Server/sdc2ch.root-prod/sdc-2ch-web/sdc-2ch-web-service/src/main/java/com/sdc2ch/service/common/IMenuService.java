package com.sdc2ch.service.common;

import java.util.List;

import com.sdc2ch.web.admin.repo.dao.T_MenuRepository;
import com.sdc2ch.web.admin.repo.domain.T_MENU;
import com.sdc2ch.web.admin.repo.enums.RoleEnums;

 
public interface IMenuService {
	
	
	public static final String ADM_MENU_ROOT_NAME = T_MenuRepository.ADM_MENU_ROOT_NAME;
	
	public static final String MBL_MENU_ROOT_NAME = T_MenuRepository.MBL_MENU_ROOT_NAME;

	
	List<T_MENU> adminMenuList(RoleEnums role);
	
	
	List<T_MENU> mobileMenuList(RoleEnums role);

}
