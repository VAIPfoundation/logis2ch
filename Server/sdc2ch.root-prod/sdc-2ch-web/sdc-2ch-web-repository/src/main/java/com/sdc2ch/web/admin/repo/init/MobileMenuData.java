package com.sdc2ch.web.admin.repo.init;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sdc2ch.core.ISetupData;
import com.sdc2ch.web.admin.repo.dao.T_MenuRepository;
import com.sdc2ch.web.admin.repo.dao.T_RoleRepository;
import com.sdc2ch.web.admin.repo.domain.T_MENU;
import com.sdc2ch.web.admin.repo.domain.T_ROLE;
import com.sdc2ch.web.admin.repo.enums.RoleEnums;
import com.sdc2ch.web.admin.repo.init.impl.SetupDataImpl.INIT_ORDER;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MobileMenuData implements ISetupData {

	private static final String JSON_DATA_PATH = "data/MenuByMobileData.json";
	
	private T_MenuRepository repo;
	private T_RoleRepository r_repo;
	private ObjectMapper mapper;
	
	@Autowired
	public void setAutowire(ObjectMapper mapper, T_MenuRepository repo, T_RoleRepository r_repo) {
		this.mapper = mapper;
		this.repo = repo;
		this.r_repo = r_repo;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void install() {
		ClassPathResource cpr = new ClassPathResource(JSON_DATA_PATH);
		log.info("{}", "Start of installed MobileMenuData ->>>>>>>>");
		
		try {
			
			repo.findByTitle(T_MenuRepository.MBL_MENU_ROOT_NAME).orElseGet(() -> {
				
				log.info("{}", "do something ... i'm work ...");
				T_MENU root = new T_MENU();
				
				try {
					
					root.setPath("#");
					root.setPgmId(T_MenuRepository.MBL_MENU_ROOT_NAME);
					root.setTitle(T_MenuRepository.MBL_MENU_ROOT_NAME);
					repo.save(root);
					
					Gson gson = new GsonBuilder().serializeNulls().create();
					mapper.readValue(cpr.getURL(), List.class).forEach( el -> {
						T_MENU m = gson.fromJson(gson.toJsonTree(el), T_MENU.class);
						m.setParent(root);
						root.getItems().add(m);
						m.getItems().forEach(m1 -> {
							m1.setParent(m);
							if(m1.getItems() != null) {
								m1.getItems().forEach(m2 -> {
									m2.setParent(m1);
								});
							}
						});
						repo.save(m);
						
						m.getItems().forEach(m1 -> {
							repo.save(m1);
							if(m1.getItems() != null) {
								m1.getItems().forEach(m2 -> {
									repo.save(m2);
								});
							}
						});
					});
					
					
					Set<T_ROLE> roles = r_repo.findAll().stream().collect(Collectors.toSet());
					
					roles.removeIf(r -> {
						return !(r.getRole() == RoleEnums.DRIVER || r.getRole() == RoleEnums.TMP_DRIVER || r.getRole() == RoleEnums.SYSTEM);
					});
					
					roles.stream().forEach(r -> {
						r.setMenus(root.getItems().stream().collect(Collectors.toSet()));
						root.getItems().forEach(m -> {
							m.setRoles(roles);
							r.getMenus().addAll(m.getItems().stream().collect(Collectors.toSet()));
							m.getItems().forEach(m1 -> {
								m1.setRoles(roles);	
								r.getMenus().add(m1);
								if(m1.getItems() != null) {
									r.getMenus().addAll(m1.getItems().stream().collect(Collectors.toSet()));
									m1.getItems().forEach(m2 -> {
										m2.setRoles(roles);
										r.getMenus().add(m1);
									});
								}
							});
						});
					});
					r_repo.saveAll(roles);
				} catch (IOException e) {
					log.error("{}", e);
				}
				return root;
			});
			
		}catch (Exception e) {
			log.error("{}", e);
		}
		log.info("{}", "end of Installed MobileMenuData <<<<<<<<<-");
	}

	@Override
	public int order() {
		return INIT_ORDER.THREE.ordinal();
	}

}
