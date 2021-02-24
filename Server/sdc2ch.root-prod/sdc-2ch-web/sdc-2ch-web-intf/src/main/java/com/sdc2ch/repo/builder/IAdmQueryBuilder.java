package com.sdc2ch.repo.builder;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.querydsl.jpa.impl.JPAQueryFactory;

public interface IAdmQueryBuilder {

	JPAQueryFactory create();
	EntityManager getEntityManager();
	EntityManagerFactory getEntityManagerFactory();
	void insertNative();
	
	public <T>void batchInsert(Collection<T> t);
	public <T> List<T> batchUpdate(Collection<T> t);
	
	
	boolean storedProcedureCall(String storedProcedureName, Object ... parameter);
	
	
	public List<Object[]> storedProcedureCallRet(String storedProcedureName, Object ... parameter);
	
	
	
	public List<Object[]> storedProcedureResultCall(String storedProcedureName, Object ... parameter);
}
