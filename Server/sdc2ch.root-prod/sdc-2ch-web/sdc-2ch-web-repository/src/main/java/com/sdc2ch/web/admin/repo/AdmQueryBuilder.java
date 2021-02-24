package com.sdc2ch.web.admin.repo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sdc2ch.repo.builder.IAdmQueryBuilder;
import com.sdc2ch.require.config.IApplicationDbConfig.ADM;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AdmQueryBuilder implements IAdmQueryBuilder {

	@PersistenceContext(unitName = ADM.dspu)
	EntityManager em;

	@PersistenceUnit(unitName = ADM.dspu)
	private EntityManagerFactory entityManagerFactory;

	private JPAQueryFactory factory;

	private static final int BATCH_COUNT = 100;

	private EntityManager nativeEm;

	@Autowired
	private void onLoad() {
		this.factory = new JPAQueryFactory(em);
		this.nativeEm = entityManagerFactory.createEntityManager();

	}
	public JPAQueryFactory create() {
		return factory;
	}

	public EntityManager getEntityManager() {
		return em;
	}

	public EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
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

	public void createNativeQuery(String query, Object... parameters) {

		Session session = null;
		EntityTransaction tx = null;
		EntityManager nativeEm = entityManagerFactory.createEntityManager();
		try {

			session = nativeEm.unwrap(Session.class);
			tx = nativeEm.getTransaction();
			tx.begin();
			Query _query = nativeEm.createNativeQuery(query);

			int i = 0;
			for(Object o : parameters) {
				_query = _query.setParameter(++i, o);
			}
			_query.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			if(session != null)
				session.close();
		}
	}

	@Override
	public <T>void batchInsert(Collection<T> t) {
		long stTime = System.currentTimeMillis();

		EntityManager em = entityManagerFactory.createEntityManager();
		EntityTransaction tx = null;
		Session session = null;

		try {

			session = em.unwrap(Session.class);
			tx = em.getTransaction();
			tx.begin();

			int i = 0;
			for (Object o : t) {
				session.persist(o);
				if (i != 0 && i % BATCH_COUNT == 0) {
					session.flush();
					session.clear();
				}
				i++;
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			if(session != null)
				session.close();
		}

		log.info("batch Insert Time {} ms", System.currentTimeMillis() - stTime);

	}
	@SuppressWarnings("unchecked")
	public <T> List<T> batchUpdate(Collection<T> t) {
		long stTime = System.currentTimeMillis();

		EntityManager em = entityManagerFactory.createEntityManager();
		EntityTransaction tx = null;
		Session session = null;

		List<T> results = new ArrayList<>(t.size());

		try {

			session = em.unwrap(Session.class);
			tx = em.getTransaction();
			tx.begin();

			int i = 0;
			for (Object o : t) {

				results.add((T) session.merge(o));
				if (i != 0 && i % BATCH_COUNT == 0) {
					session.flush();
					session.clear();
				}
				i++;
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			if(session != null)
				session.close();
		}

		log.info("batch Update Time {} ms", System.currentTimeMillis() - stTime);
		return results;

	}
	@Override
	public void insertNative() {
		EntityManager em = entityManagerFactory.createEntityManager();
		Session session = null;
		EntityTransaction tx = null;
		try {

			session = em.unwrap(Session.class);
			tx = em.getTransaction();
			tx.begin();
			Query _query = em.createNativeQuery(
					"INSERT INTO [2ch_test].[dbo].[T_TEST] (ADDRESS, MDN) values(?, ?)");
			_query.setParameter(1, "주소2");
			_query.setParameter(2, "param2");
			_query.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			if(session != null)
				session.close();
		}

	}







































	@Transactional
	@Override
	public boolean storedProcedureCall(String storedProcedureName, Object ... parameter) {


		EntityManager em = entityManagerFactory.createEntityManager();
		EntityTransaction tx = null;
		ProcedureCall query = null;

		try {




			query = (ProcedureCall) em.createStoredProcedureQuery(storedProcedureName);
			int idx = 0;
			for(Object o : parameter) {
				Class<?> clazz = o.getClass();
				query = (ProcedureCall) query.registerStoredProcedureParameter(++idx, clazz, ParameterMode.IN);
			}

			idx = 0;
			for(Object o : parameter) {
				query.setParameter(++idx, o);
			}

			boolean result = query.execute();

			log.info("storedProcedureCall -> {}", result);

		} catch (Exception e) {
			log.error("{}", e);

			return false;
		} finally {
			if(query != null) {
		        query.getOutputs().release();
			}


			if(em != null)
				em.close();
		}
		return true;
	}
	@Transactional
	@Override
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

			if(parameter != null) {
				List<Object> _arrays = Stream.of(parameter).filter(p -> p != null).collect(Collectors.toList());
				parameter = _arrays.toArray(new Object[_arrays.size()]);
			}

			query = em.createStoredProcedureQuery(storedProcedureName);
			int idx = 0;
			for(Object o : parameter) {
				Class<?> clazz = ( o == null ) ? String.class : o.getClass();
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
	@Transactional
	@Override
	public List<Object[]> storedProcedureResultCall(String storedProcedureName, Object ... parameter) {

		EntityManager em = entityManagerFactory.createEntityManager();
		Session session = null;


		List<Object[]> resultList = new ArrayList<>();

		try {

			session = em.unwrap(Session.class);



			if(parameter != null) {
				List<Object> _arrays = Stream.of(parameter).filter(p -> p != null).collect(Collectors.toList());
				parameter = _arrays.toArray(new Object[_arrays.size()]);
			}

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


			resultList = query.getResultList();

		} catch (Exception e) {
			log.error("{}", e);

		} finally {
			if(session != null)
				session.close();
		}

		return resultList;
	}
	public void batchInsert(String query, List<Object[]> params) {
		long stTime = System.currentTimeMillis();

		EntityManager em = entityManagerFactory.createEntityManager();
		EntityTransaction tx = null;
		Session session = null;

		try {
			session = em.unwrap(Session.class);
			tx = em.getTransaction();
			tx.begin();

			for(Object[] batch : params) {

				Query _query = em.createNativeQuery(query);

				for(int i = 0 ; i< batch.length ; i++) {
					_query.setParameter(i +1, batch[i]);
				}
				_query.executeUpdate();
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			if(session != null)
				session.close();
		}

		log.info("batch Insert Time {} ms", System.currentTimeMillis() - stTime);
	}
	public void batchInsert(String query) {
		long stTime = System.currentTimeMillis();

		EntityManager em = entityManagerFactory.createEntityManager();
		EntityTransaction tx = null;
		Session session = null;

		try {
			session = em.unwrap(Session.class);
			tx = em.getTransaction();
			tx.begin();
			Query _query = em.createNativeQuery(query);
			_query.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			if(session != null)
				session.close();
		}

		log.info("batch Insert Time {} ms", System.currentTimeMillis() - stTime);
	}
}
