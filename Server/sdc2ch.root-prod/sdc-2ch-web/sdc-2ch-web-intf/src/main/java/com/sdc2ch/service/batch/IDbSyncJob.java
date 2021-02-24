package com.sdc2ch.service.batch;

import java.util.List;

import com.sdc2ch.service.batch.DbSyncTask.SyncTable;

 
public interface IDbSyncJob<S, T> {
	
	
	SyncTable getType();

	
	S[] getSourceTable();
	
	
	T[] getTargetTable();
	
	
	boolean isChanged(T t, S s);
	
	
	boolean filter(T v1, S v2);
	
	
	T emptyNewObject();
	
	
	T convert(S s, T t);
	
	
	void delete(List<T> changed);
	
	
	void save(List<T> newOrUpdate);
	
	
	
	long scheduledTime();

}
