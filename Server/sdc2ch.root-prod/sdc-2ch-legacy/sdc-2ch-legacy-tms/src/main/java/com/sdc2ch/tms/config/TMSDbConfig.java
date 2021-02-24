package com.sdc2ch.tms.config;

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
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.sdc2ch.require.config.IApplicationDbConfig;
import com.sdc2ch.require.config.IApplicationDbConfig.TMS;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		entityManagerFactoryRef = TMS.emfr,
		transactionManagerRef   = TMS.tmr, 
		basePackages  = TMS.bp)
@PropertySource(value = cp + TMS.ps)
public class TMSDbConfig implements IApplicationDbConfig {
	
	@Autowired Environment env;

	@Primary
	@Bean(name = TMS.ds)
	@ConfigurationProperties(prefix = TMS.cpp)
	public DataSource dataSource() {
		return IApplicationDbConfig.createDatasource(new TMS(), env);
	}

	@Primary
	@Bean(name = TMS.emf)
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier(TMS.ds) DataSource dataSource) {
		return builder.dataSource(dataSource).packages(TMS.dsp)
				.properties(getProp()).persistenceUnit(TMS.dspu).build();
	}

	@Primary
	@Bean(name = TMS.tm)
	public PlatformTransactionManager transactionManager(
			@Qualifier(TMS.emf) EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}

	private Map<String, String> getProp() {
		Map<String, String> p = new HashMap<>();
		p.put(TMS.ddl, TMS.ddlv);
		return p;
	}
}