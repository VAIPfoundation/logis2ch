package com.sdc2ch.require.cache;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import lombok.Builder;

@Builder
public class CacheMenager<K, V> {

	final ListeningExecutorService executor = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(5));
	
	private int maxSize;
	private int expiredTime;
	private Future<K, V> future;
	private TimeUnit timeUnit;

	public LoadingCache<K, V> reloadCache() {
		
		Optional.of(future).orElseThrow(() -> new IllegalArgumentException("com.sdc2ch.common.impl.CacheMenager.Future<K, V> required parameter"));
		
		return CacheBuilder.newBuilder().maximumSize(maxSize).expireAfterWrite(expiredTime, timeUnit)
				.build(new CacheLoader<K, V>() {
					public V load(K key) throws Exception {
						return future.get(key);
					}
					@Override
					public ListenableFuture<V> reload(final K key, V oldValue) throws Exception {
						ListenableFutureTask<V> task = ListenableFutureTask.create(new Callable<V>() {
							public V call() throws Exception {
								return load(key);
							}
						});
						executor.execute(task);
						return task;
					}
				});
	}
	
	public LoadingCache<K, V> expiredCache() {
		return CacheBuilder.newBuilder().maximumSize(maxSize).expireAfterWrite(expiredTime, timeUnit)
				.build(new CacheLoader<K, V>() {
					public V load(K key) throws Exception {
						return future.get(key);
					}
				});
	}
	
	public interface Future<K, V> {
		V get(K k) throws Exception;
	}

}
