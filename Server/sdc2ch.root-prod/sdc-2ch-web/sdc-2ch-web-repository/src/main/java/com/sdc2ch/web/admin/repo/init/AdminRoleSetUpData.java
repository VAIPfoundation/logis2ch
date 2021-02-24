package com.sdc2ch.web.admin.repo.init;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sdc2ch.core.ISetupData;
import com.sdc2ch.web.admin.repo.dao.T_RoleRepository;
import com.sdc2ch.web.admin.repo.domain.T_ROLE;
import com.sdc2ch.web.admin.repo.enums.RoleEnums;
import com.sdc2ch.web.admin.repo.init.impl.SetupDataImpl.INIT_ORDER;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AdminRoleSetUpData implements ISetupData {

	@Autowired T_RoleRepository repo;

	@Override
	public void install() {

		log.info("{}", "Start of installed AdminRoleSetUpData ->>>>>>>>");



		long cnt = repo.count();

		if(cnt == 0) {
			log.info("{}", "do something ... i'm work ...");

			for(RoleEnums r : RoleEnums.values()) {

				T_ROLE role = new T_ROLE();
				role.setRole(r);
				role.setDesc(r.getDesc());
				repo.save(role);
			}

		}
		log.info("{}", "end of Installed AdminRoleSetUpData <<<<<<<<<-");
	}

	@Override
	public int order() {
		return INIT_ORDER.ONE.ordinal();
	}

}
