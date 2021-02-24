package com.sdc2ch.nfc.config;

import static com.sdc2ch.require.config.IApplicationDbConfig.cp;

import java.util.HashMap;
import java.util.List;
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

import com.sdc2ch.nfc.domain.dynamic.TableNameDynamic;
import com.sdc2ch.require.config.IApplicationDbConfig;
import com.sdc2ch.require.config.IApplicationDbConfig.NFC;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		entityManagerFactoryRef = NFC.emfr,
		transactionManagerRef   = NFC.tmr, 
		basePackages  = NFC.bp)
@PropertySource(value = cp + NFC.ps)
public class NFCDbConfig implements IApplicationDbConfig {
	
	@Autowired Environment env;
	@Autowired(required = false) List<TableNameDynamic<?>> dynamics;

	@Bean(name = NFC.ds)
	@ConfigurationProperties(prefix = NFC.cpp)
	public DataSource dataSource() {

		return IApplicationDbConfig.createDatasource(new NFC(), env);
	}

	@Bean(name = NFC.emf)
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier(NFC.ds) DataSource dataSource) {
		return builder.dataSource(dataSource).packages(NFC.dsp)
				.properties(getProp()).persistenceUnit(NFC.dspu).build();
	}

	@Bean(name = NFC.tm)
	public PlatformTransactionManager transactionManager(
			@Qualifier(NFC.emf) EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}

	private Map<String, Object> getProp() {
		Map<String, Object> p = new HashMap<>();
		p.put(NFC.ddl, NFC.ddlv);
		p.put(NFC.sddl, NFC.sddlv);
		p.put(NFC.hsfi, new HibernateInterceptor(dynamics));
		p.put("hibernate.dialect", "org.hibernate.dialect.MariaDBDialect");
		return p;
	}
}