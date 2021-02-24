package com.sdc2ch.service.batch;

import static com.sdc2ch.core.lambda.tuple.Tuple.tuple;
import static java.util.stream.Collectors.toList;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sdc2ch.core.lambda.seq.Seq;
import com.sdc2ch.core.lambda.tuple.Tuple2;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DbSyncTask<T, S> {
	private T t;
	private S s;
	
	
	public DbSyncTask() {
		log.info("{}", this);
	}
	public enum SyncTable {
		M_PLAN_MSTR
	}	
	private ScheduledExecutorService executor;
	private Map<SyncTable, Task> jobMapped;
	
	
	public void update(SyncTable syncType) {
		Optional.of(jobMapped.get(syncType)).ifPresent(t -> t.run());
	}
	
	@Autowired(required = false)
	private void init(List<IDbSyncJob<S, T>> jobs) {
		this.executor = Executors.newScheduledThreadPool(jobs.size());
		this.jobMapped = new HashMap<>(jobs.size());
		this.start(jobs);
	}
	
	private void start(List<IDbSyncJob<S, T>> jobs) {
		jobs.stream().forEach(j -> {
			executor.scheduleAtFixedRate(new Task(j), 1000, j.scheduledTime(), TimeUnit.MILLISECONDS);
		});
	}
	
	private class Task implements Runnable {

		private IDbSyncJob<S, T> job;
		
		Task(IDbSyncJob<S, T> job){
			this.job = job;
			jobMapped.put(job.getType(), this);
		}
		@Override
		public void run() {
			try {
				long time = System.currentTimeMillis();
				log.info("start Schedule SyncTask {} -> {}", s, t);
				S[] s = job.getSourceTable();
				T[] t = job.getTargetTable();

				
				job.delete(ifChangedSource(s, t));
				
				t = job.getTargetTable();
				job.save(sync(s, t));
				log.info("finish Schedule SyncTask Process Time [{}] sec", (System.currentTimeMillis() - time)/1000);
			}catch (Exception e) {
				log.error("{}", e);
			}
		}
		
		
		private List<T> ifChangedSource(S[] source, T[] target){
			return Seq.seq(Arrays.asList(target)).flatMap(v1 -> Seq.seq(Arrays.asList(source))
					.filter(v2 -> job.filter(v1, v2))
					.onEmpty(null)
					.map(v2 -> tuple(v1, v2))
					.filter( t -> isCurChanged(t))
					.map(t -> t.v1)
					)
			.collect(toList());
		}
		
		private List<T> sync(S[] source, T[] target){
			return Seq.seq(Arrays.asList(source)).flatMap(v1 -> Seq.seq(Arrays.asList(target))
					.filter(v2 -> job.filter(v2, v1))
					.onEmpty(job.emptyNewObject())
					.map(v2 -> tuple(v1, v2))
					.filter(t -> isTmsChanged(t))
					.map(t -> job.convert(t.v1, t.v2))
					)
					.collect(toList());
		}
		
		private boolean isTmsChanged(Tuple2<S, T> t) {
			return job.isChanged(t.v2, t.v1);
		}
		
		private boolean isCurChanged(Tuple2<T, S> t) {
			return job.isChanged(t.v1, t.v2);
		}
	}
	
	public static Type[] getParameterizedTypes(Object object) {
	    Type superclassType = object.getClass().getGenericSuperclass();
	    if (!ParameterizedType.class.isAssignableFrom(superclassType.getClass())) {
	        return null;
	    }
	    return ((ParameterizedType)superclassType).getActualTypeArguments();
	}
}
