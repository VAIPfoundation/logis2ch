package com.sdc2ch.tms.config;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.hibernate.Session;
import org.hibernate.procedure.ProcedureCall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sdc2ch.require.config.IApplicationDbConfig.TMS;
import com.sdc2ch.tms.service.ITmsQueryBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TmsQueryBuilder implements ITmsQueryBuilder{

	@PersistenceContext(unitName= TMS.dspu) EntityManager em;

	@PersistenceUnit(unitName = TMS.dspu)
	private EntityManagerFactory entityManagerFactory;

	private JPAQueryFactory factory;

	@Autowired
	private void onLoad() {
		factory = new JPAQueryFactory(em);
	}

	public JPAQueryFactory create(){
		return factory;
	}
	public EntityManager createEntityManager() {
		return entityManagerFactory.createEntityManager();
	}

	public void storedProcedureCall(String storedProcedureName, Object... parameter) {

		EntityManager em = entityManagerFactory.createEntityManager();
		EntityTransaction tx = null;
		ProcedureCall query = null;

		try {




			query = (ProcedureCall) em.createStoredProcedureQuery(storedProcedureName);
			int idx = 0;
			for (Object o : parameter) {
				Class<?> clazz = o.getClass();
				query = (ProcedureCall) query.registerStoredProcedureParameter(++idx, clazz, ParameterMode.IN);
			}

			idx = 0;
			for (Object o : parameter) {
				query.setParameter(++idx, o);
			}

			boolean result = query.execute();
			log.info("storedProcedureCall -> {}", result);

		} catch (Exception e) {
			log.error("storedProcedureCall:{}", e);

		} finally {
			if (query != null) {
				query.getOutputs().release();
			}


			if (em != null)
				em.close();
		}
	}

	public List<Object[]> storedProcedureCallRet(String storedProcedureName, Object ... parameter) {
		List<Object[]> resultList = new ArrayList<>();
		EntityManager em = entityManagerFactory.createEntityManager();
		Session session = null;
		EntityTransaction tx = null;
		StoredProcedureQuery query = null;
		try {
			session = em.unwrap(Session.class);
			tx = em.getTransaction();
			tx.begin();
			query = em.createStoredProcedureQuery(storedProcedureName);
			int idx = 0;
			for(Object o : parameter) {
				Class<?> clazz = o.getClass();
				query = query.registerStoredProcedureParameter(++idx, clazz, ParameterMode.IN);
			}

			idx = 0;
			for(Object o : parameter) {
				query.setParameter(++idx, o);
			}
			query.execute();
			resultList = query.getResultList();
			tx.commit();

		} catch (Exception e) {
			log.error("{}", e);
			tx.rollback();
		} finally {



			if(session != null)
				session.close();
		}
		return resultList;
	}

	public List<Object[]> storedProcedureResultCall(String storedProcedureName, Object ... parameter) {

		EntityManager em = entityManagerFactory.createEntityManager();
		Session session = null;


		List<Object[]> resultList = new ArrayList<>();

		try {

			session = em.unwrap(Session.class);



			StoredProcedureQuery query = em.createStoredProcedureQuery(storedProcedureName);
			int idx = 0;

			for(Object o : parameter) {

				if(o == null)
					continue;

				Class<?> clazz = o.getClass();
				query = query.registerStoredProcedureParameter(++idx, clazz, ParameterMode.IN);
			}

			idx = 0;
			for(Object o : parameter) {
				if(o == null)
					continue;
				query.setParameter(++idx, o);
			}
			query.execute();

			resultList = query.getResultList();

		} catch (Exception e) {
			log.error("{}", e);

		} finally {
			if(session != null)
				session.close();
		}
		return resultList;
	}

	public Object[] storedProcedureSingleResultCall(String storedProcedureName, Object ... parameter) {

		EntityManager em = entityManagerFactory.createEntityManager();
		Session session = null;

		Object[] result = null;
		try {
			session = em.unwrap(Session.class);


			StoredProcedureQuery query = em.createStoredProcedureQuery(storedProcedureName);
			int idx = 0;
			for(Object o : parameter) {
				Class<?> clazz = o.getClass();
				query = query.registerStoredProcedureParameter(++idx, clazz, ParameterMode.IN);
			}

			idx = 0;
			for(Object o : parameter) {
				query.setParameter(++idx, o);
			}
			query.execute();

			result = (Object[])query.getSingleResult();

		} catch (Exception e) {
			log.error("{}", e);

		} finally {
			if(session != null)
				session.close();
		}
		return result;
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
