package com.sdc2ch.web.admin.repo.config;


import static com.sdc2ch.require.config.IApplicationDbConfig.cp;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.sdc2ch.require.config.IApplicationDbConfig;
import com.sdc2ch.require.config.IApplicationDbConfig.ADM;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		entityManagerFactoryRef = ADM.emfr,
		transactionManagerRef   = ADM.tmr,
		basePackages  = {ADM.bp_ad, ADM.bp_eb, ADM.bp_ds, ADM.bp_av, ADM.bp_as, ADM.bp_ss})
@PropertySource(value = cp + ADM.ps)
public class WebAdminDbConfig implements IApplicationDbConfig {

	@Autowired Environment env;

	@Bean(name = ADM.ds)
	@ConfigurationProperties(prefix = ADM.cpp)
	public DataSource dataSource() {

		return IApplicationDbConfig.createDatasource(new ADM(), env);
	}

	@Bean(name = ADM.emf)
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier(ADM.ds) DataSource dataSource) {
		return builder.dataSource(dataSource).packages(ADM.dsp)
				.properties(getProp()).persistenceUnit(ADM.dspu).build();
	}

	@Bean(name = ADM.tm)
	public PlatformTransactionManager transactionManager(
			@Qualifier(ADM.emf) EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}












	private Map<String, String> getProp() {
		Map<String, String> p = new HashMap<>();
		p.put(ADM.ddl, ADM.ddlv);
 
		p.put("hibernate.dialect", "org.hibernate.dialect.SQLServer2012Dialect");

		return p;
	}
}