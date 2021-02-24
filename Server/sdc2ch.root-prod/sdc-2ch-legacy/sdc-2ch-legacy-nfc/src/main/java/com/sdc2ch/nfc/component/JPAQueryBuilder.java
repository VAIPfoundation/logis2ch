package com.sdc2ch.nfc.component;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import org.hibernate.Session;
import org.springframework.stereotype.Component;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sdc2ch.require.config.IApplicationDbConfig.NFC;
import com.sdc2ch.require.config.IApplicationDbConfig.TMS;

@Component
public class JPAQueryBuilder {

	@PersistenceContext(unitName= NFC.dspu) EntityManager em;

	@PersistenceUnit(unitName = NFC.dspu)
	private EntityManagerFactory entityManagerFactory;

	public JPAQueryFactory create(){

		return new JPAQueryFactory(em);
	}

	public EntityManager getEntityManager(){
		return this.em;
	}

	public List<?> createSelectNativeQuery(String query, Object... parameters) {

		Session session = null;
		EntityManager nativeEm = entityManagerFactory.createEntityManager();
		try {

			session = nativeEm.unwrap(Session.class);
			Query _query = nativeEm.createNativeQuery(query);

			int i = 0;
			if(parameters != null) {
				for(Object o : parameters) {
					_query = _query.setParameter(++i, o);
				}
			}
			return _query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(session != null)
				session.close();
		}
		return null;
	}











}
