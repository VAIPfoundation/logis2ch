package com.sdc2ch.nfc.config;

import java.util.List;
import java.util.Optional;

import org.hibernate.EmptyInterceptor;

import com.sdc2ch.nfc.domain.dynamic.TableNameDynamic;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HibernateInterceptor extends EmptyInterceptor {

	
	private static final long serialVersionUID = -4680507966843423396L;

	private Optional<List<TableNameDynamic<?>>> dynamics;

	public HibernateInterceptor(List<TableNameDynamic<?>> dynamics) {
		this.dynamics = Optional.ofNullable(dynamics);
	}

	@Override
	public String onPrepareStatement(String sql) {
		
		String replaceSql = replace(sql);
		
		if(sql.equals(replaceSql))
			log.info("{}", "org -> target :> changed sql");
		return replace(sql);
	}

	private String replace(final String sql) {
		return dynamics.isPresent()
				? dynamics.get().stream().filter(t -> t.supported(sql)).map(t -> t.dynamicQuery(sql)).findAny().orElse(sql)
				: sql;
	}

	
}
