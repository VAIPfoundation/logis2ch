package com.sdc2ch.service.common.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.cache.LoadingCache;
import com.sdc2ch.require.cache.CacheMenager;
import com.sdc2ch.require.cache.CacheMenager.Future;
import com.sdc2ch.service.common.IMenuService;
import com.sdc2ch.web.admin.repo.dao.T_MenuRepository;
import com.sdc2ch.web.admin.repo.domain.T_MENU;
import com.sdc2ch.web.admin.repo.domain.T_ROLE;
import com.sdc2ch.web.admin.repo.enums.RoleEnums;

@Service
class MenuServiceImpl implements IMenuService {

	private static final String ENC_KEY_SP = "#";

	private static final int EXPIRED_TIME = 1 * 365 * 10;

	@Autowired
	private T_MenuRepository menuRepo;
	private LoadingCache<String, List<T_MENU>> menuCache;

	
	@PostConstruct
	public void onLoad() {
		
		menuCache = CacheMenager.<String, List<T_MENU>>builder().expiredTime(EXPIRED_TIME).timeUnit(TimeUnit.DAYS)
				.maxSize(100).future(new Future<String, List<T_MENU>>() {
					@Override
					public List<T_MENU> get(final String key) throws Exception {
						String[] keys = decodeCacheKey(key);
						return _findByRoleAndType(RoleEnums.valueOf(keys[0]), keys[1]);
					}
				}).build().reloadCache();
	}

	@Override
	public List<T_MENU> adminMenuList(final RoleEnums role) {
		try {
			return menuCache.get(encodeCacheKey(role, ADM_MENU_ROOT_NAME));
		} catch (Exception e) {
			return _findByRoleAndType(role, ADM_MENU_ROOT_NAME);
		}
	}

	@Override
	public List<T_MENU> mobileMenuList(RoleEnums role) {

		try {
			return menuCache.get(encodeCacheKey(role, MBL_MENU_ROOT_NAME));
		} catch (Exception e) {
			return _findByRoleAndType(role, MBL_MENU_ROOT_NAME);
		}
	}

	private List<T_MENU> _findByRoleAndType(RoleEnums role, String type) {
		T_MENU root = menuRepo.findByTitle(type)
				.orElseThrow(() -> new NullPointerException("root menu cat not be null"));
		return extractMenus(root, role);
	}

	
	public List<T_MENU> extractMenus(T_MENU root, final RoleEnums role) {

		Assert.notEmpty(root.getItems(), "root menu items : [] ");

		List<T_MENU> newList = root.getItems().stream().collect(Collectors.toList());
		newList.removeIf(m -> recursive(m, role));

		return newList;
	}

	
	private boolean recursive(T_MENU m, RoleEnums role) {

		if (m.getItems() != null) {
			m.getItems().removeIf(m1 -> recursive(m1, role));
		}
		return !m.getRoles().stream().map(T_ROLE::getRole).anyMatch(r -> r == role);
	}

	private String encodeCacheKey(RoleEnums role, String key) {
		return role.name().concat(ENC_KEY_SP).concat(key);
	}

	private String[] decodeCacheKey(String key) {
		return key.split(ENC_KEY_SP);
	}

}
