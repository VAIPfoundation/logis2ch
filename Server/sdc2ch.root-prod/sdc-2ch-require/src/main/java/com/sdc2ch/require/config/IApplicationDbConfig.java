package com.sdc2ch.require.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.tomcat.jdbc.pool.DataSourceFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.env.Environment;

import lombok.extern.slf4j.Slf4j;


public interface IApplicationDbConfig {

	public static final String cp   = "classpath:";



	public abstract static class CFG {

		abstract String getPrefix();

	}

	
	public static class ADM extends CFG {
		public static final String emf    = "adm-entityManagerFactory";
		public static final String tm     = "adm-transactionManager";
		public static final String bp_ad  = "com.sdc2ch.web.admin.repo";
		public static final String bp_eb  = "com.sdc2ch.prcss.eb.repo";
		public static final String bp_ds  = "com.sdc2ch.prcss.ds.repo";
		public static final String bp_ss  = "com.sdc2ch.prcss.ss.repo";
		public static final String bp_av  = "com.sdc2ch.legacy.aiv.repo";
		public static final String bp_as  = "com.sdc2ch.ars.repo.domain.dao";
		public static final String emfr   = "adm-entityManagerFactory";
		public static final String tmr    = "adm-transactionManager";
		public static final String ps     = "adm-dev.properties";
		public static final String cpp    = "adm.datasource";
		public static final String ds     = "adm-dataSource";
		public static final String dsp[]  = {"com.sdc2ch.web.admin.repo.domain",
											 "com.sdc2ch.prcss.eb.repo.domain",
											 "com.sdc2ch.prcss.ds.repo.domain",
											 "com.sdc2ch.prcss.ss.repo.domain",
											 "com.sdc2ch.legacy.aiv.repo.domain",
											 "com.sdc2ch.ars.repo.domain"};
		public static final String dspu   = "adm-DB";
		public static final String ddl    = "hibernate.hbm2ddl.auto";
		public static final String ddlv   = "update";	

		@Override
		String getPrefix() {
			return cpp;
		}
	}

	
	public static class TMS extends CFG {
		public static final String emf  = "tms-entityManagerFactory";
		public static final String tm   = "tms-transactionManager";
		public static final String bp   = "com.sdc2ch.tms";
		public static final String emfr = "tms-entityManagerFactory";
		public static final String tmr  = "tms-transactionManager";
		public static final String ps   = "tms-dev.properties";
		public static final String cpp  = "tms.datasource";
		public static final String ds   = "tms-dataSource";
		public static final String dsp  = "com.sdc2ch.tms";
		public static final String dspu = "tms-DB";
		public static final String ddl  = "hibernate.hbm2ddl.auto";
		public static final String ddlv = "none";

		@Override
		String getPrefix() {
			return cpp;
		}
	}
	
	public static class PRM extends CFG {
		public static final String emf  = "prm-entityManagerFactory";
		public static final String tm   = "prm-transactionManager";
		public static final String bp   = "com.sdc2ch.prm";
		public static final String emfr = "prm-entityManagerFactory";
		public static final String tmr  = "prm-transactionManager";
		public static final String ps   = "prm-dev.properties";
		public static final String cpp  = "prm.datasource";
		public static final String ds   = "prm-dataSource";
		public static final String dsp  = "com.sdc2ch.prm";
		public static final String dspu = "prm-DB";
		public static final String ddl  = "hibernate.hbm2ddl.auto";
		public static final String ddlv = "none";
		@Override
		String getPrefix() {
			return cpp;
		}
	}
	
	public static class NFC extends CFG {
		public static final String emf  = "nfc-entityManagerFactory";
		public static final String tm   = "nfc-transactionManager";
		public static final String bp   = "com.sdc2ch.nfc";
		public static final String emfr = "nfc-entityManagerFactory";
		public static final String tmr  = "nfc-transactionManager";
		public static final String ps   = "nfc-dev.properties";
		public static final String cpp  = "nfc.datasource";
		public static final String ds   = "nfc-dataSource";
		public static final String dsp  = "com.sdc2ch.nfc";
		public static final String dspu = "nfc-DB";
		public static final String ddl  = "hibernate.hbm2ddl.auto";
		public static final String ddlv = "none";
		public static final String sddl = "spring.jpa.generate-ddl";
		public static final String sddlv = "false";
		public static final String hsfi = "hibernate.session_factory.interceptor";

		@Override
		String getPrefix() {
			return cpp;
		}
	}


	public static class AIV extends CFG {
		public static final String emf  = "aiv-entityManagerFactory";
		public static final String tm   = "aiv-transactionManager";
		public static final String bp   = "com.sdc2ch.aiv";
		public static final String emfr = "aiv-entityManagerFactory";
		public static final String tmr  = "aiv-transactionManager";
		public static final String ps   = "aiv-dev.properties";
		public static final String cpp  = "aiv.datasource";
		public static final String ds   = "aiv-dataSource";
		public static final String dsp  = "com.sdc2ch.aiv";
		public static final String dspu = "aiv-DB";
		public static final String ddl  = "hibernate.hbm2ddl.auto";
		public static final String ddlv = "none";
		public static final String sddl = "spring.jpa.generate-ddl";
		public static final String sddlv = "false";
		public static final String hsfi = "hibernate.session_factory.interceptor";

		@Override
		String getPrefix() {
			return cpp;
		}
	}


	public static String JDBC_URL = ".jdbc-url";
	public static String USERNAME = ".username";
	public static String PASSWORD = ".password";
	public static String MAX_ACTIVE = ".maxActive";
	public static String DRIVER_CLS_NAME = ".driverClassName";

	public static DataSource createDatasource(CFG cfg, Environment env) {














		DataSourceFactory factory = new DataSourceFactory();



























		Properties prop = new Properties();

		







		prop.setProperty("url", env.getProperty(cfg.getPrefix() + JDBC_URL));
		prop.setProperty("driverClassName", env.getProperty(cfg.getPrefix() + DRIVER_CLS_NAME));
		prop.setProperty("username", env.getProperty(cfg.getPrefix() + USERNAME));
		prop.setProperty("password", env.getProperty(cfg.getPrefix() + PASSWORD));
		

		prop.setProperty("maxActive", "400");
		prop.setProperty("maxIdle", "15");
		prop.setProperty("minIdle", "8");
		prop.setProperty("initialSize", "15");
		prop.setProperty("maxWait", "15000");



		prop.setProperty("testWhileIdle", "true");
		prop.setProperty("validationQuery", "SELECT 1");

		try {
			return factory.createDataSource(prop);

		} catch (Exception e) {
			
			e.printStackTrace();

			return DataSourceBuilder.create().build();
		}




























	}


}
