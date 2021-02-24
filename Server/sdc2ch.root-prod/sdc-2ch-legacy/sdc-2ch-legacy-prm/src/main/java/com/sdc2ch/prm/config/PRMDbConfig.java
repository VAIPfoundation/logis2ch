package com.sdc2ch.prm.config;

import static com.sdc2ch.require.config.IApplicationDbConfig.cp;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.sdc2ch.require.config.IApplicationDbConfig;
import com.sdc2ch.require.config.IApplicationDbConfig.PRM;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		entityManagerFactoryRef = PRM.emfr,
		transactionManagerRef   = PRM.tmr, 
		basePackages  = PRM.bp)
@PropertySource(value = cp + PRM.ps)
public class PRMDbConfig implements IApplicationDbConfig {
	
	@Autowired Environment env;

	@Bean(name = PRM.ds)
	@ConfigurationProperties(prefix = PRM.cpp)
	public DataSource dataSource() {
		return IApplicationDbConfig.createDatasource(new PRM(), env);
	}

	@Bean(name = PRM.emf)
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier(PRM.ds) DataSource dataSource) {
		return builder.dataSource(dataSource).packages(PRM.dsp)
				.properties(getProp()).persistenceUnit(PRM.dspu).build();
	}

	@Bean(name = PRM.tm)
	public PlatformTransactionManager transactionManager(
			@Qualifier(PRM.emf) EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}

	private Map<String, String> getProp() {
		Map<String, String> p = new HashMap<>();
		p.put(PRM.ddl, PRM.ddlv);
		return p;
	}
}