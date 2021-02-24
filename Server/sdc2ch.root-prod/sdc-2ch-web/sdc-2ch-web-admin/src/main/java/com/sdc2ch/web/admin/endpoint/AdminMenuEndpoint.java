package com.sdc2ch.web.admin.endpoint;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sdc2ch.service.common.IMenuService;
import com.sdc2ch.web.admin.repo.domain.T_MENU;
import com.sdc2ch.web.admin.repo.enums.RoleEnums;

@RestController
@RequestMapping("/adm/menu")
public class AdminMenuEndpoint extends AdminAbstractEndpoint {
	@Autowired IMenuService menuSvc;
	
	
	@GetMapping("/list")
	@PreAuthorize("hasAuthority('MANAGER')")
	public List<T_MENU> menus() {
		List<T_MENU> menus = menuSvc.adminMenuList(currentRoleByUser());
		
		
		menus.stream().forEach(m -> m.flattened().forEach(m1 -> {
			System.out.println( m1 + " -> " + m1.getRoles());
		}));
		
		
		menus = menuSvc.adminMenuList(RoleEnums.TMP_DRIVER);
		System.out.println(menuSvc.adminMenuList(currentRoleByUser()));
		System.out.println(menuSvc.adminMenuList(currentRoleByUser()));
		
		return menuSvc.adminMenuList(currentRoleByUser());
	}

}
