package com.sdc2ch.web.admin.repo.enums;

import java.util.Arrays;
import java.util.List;

import com.sdc2ch.require.domain.IUserRole;

public enum RoleEnums implements IUserRole{

	
	SYSTEM {
		@Override
		public List<RoleEnums> getRoles() {
			return Arrays.asList(RoleEnums.values());
		}

		@Override
		public String getDesc() {
			return "시스템관리자";
		}
	},
	SUPER {
		@Override
		public List<RoleEnums> getRoles() {
			return Arrays.asList(SUPER, FACTORY, MANAGER, PRODUCER, USER);
		}

		@Override
		public String getDesc() {
			return "본사관리자";
		}
	},
	FACTORY {
		@Override
		public List<RoleEnums> getRoles() {
			return Arrays.asList(FACTORY, MANAGER, USER);
		}

		@Override
		public String getDesc() {
			return "공장관리자";
		}
	},
	PRODUCER {
		@Override
		public List<RoleEnums> getRoles() {
			return Arrays.asList(PRODUCER, MANAGER, USER);
		}

		@Override
		public String getDesc() {
			return "생산관리자";
		}
	},
	DRIVER {
		@Override
		public List<RoleEnums> getRoles() {
			return Arrays.asList(DRIVER, USER);
		}

		@Override
		public String getDesc() {
			return "기사";
		}
	},TMP_DRIVER {
		@Override
		public List<RoleEnums> getRoles() {
			return Arrays.asList(TMP_DRIVER, USER);
		}

		@Override
		public String getDesc() {
			return "용차기사";
		}
	},
	CENTER {
		@Override
		public List<RoleEnums> getRoles() {
			return Arrays.asList(CENTER, MANAGER, USER);
		}

		@Override
		public String getDesc() {
			return "고객센터";
		}
	},
	MANAGER {
		@Override
		public List<RoleEnums> getRoles() {
			return Arrays.asList(MANAGER);
		}

		@Override
		public String getDesc() {
			return "관리자권한";
		}
	},	
	USER {
		@Override
		public List<RoleEnums> getRoles() {
			return Arrays.asList(USER);
		}

		@Override
		public String getDesc() {
			return "사용자권한";
		}
	};
	public abstract List<RoleEnums> getRoles();

	@Override
	public String getRolename() {
		return this.name();
	}
	
	public abstract String getDesc();
	public static List<RoleEnums> findRoles(String rolename) {
		try {
			switch (TMSRole.valueOf(rolename)) {
			case ADMIN:
			case POWERUSER:
				return SUPER.getRoles();
			case ACCOUNT:
				return SUPER.getRoles();
			case PLANNER:
			case PMANAGER:
				
			case PACCOUNT:
				return FACTORY.getRoles();
			case PRODUCTION:
				return PRODUCER.getRoles();
			case TDRIVER:
				return DRIVER.getRoles();
			default:
				break;
			}
			
		}catch (Exception e) {
			
		}
		return null;
		
	}
	
	enum TMSRole {
		ADMIN,
		ACCOUNT,
		
		PACCOUNT,
		PLANNER,
		PMANAGER,
		POWERUSER,
		PRODUCTION,
		TDRIVER
	}

	public static boolean isSystemRole(String rolename) {
		try {
			TMSRole role = TMSRole.valueOf(rolename);
			return TMSRole.ADMIN == role || TMSRole.POWERUSER == role;
		}catch (Exception e) {
			return false;
		}
	}
	
}
