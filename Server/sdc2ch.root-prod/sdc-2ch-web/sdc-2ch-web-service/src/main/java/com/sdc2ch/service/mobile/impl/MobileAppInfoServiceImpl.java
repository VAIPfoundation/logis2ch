package com.sdc2ch.service.mobile.impl;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.cache.LoadingCache;
import com.sdc2ch.require.cache.CacheMenager;
import com.sdc2ch.require.cache.CacheMenager.Future;
import com.sdc2ch.service.common.ITosHistService;
import com.sdc2ch.service.mobile.IMobileAppInfoService;
import com.sdc2ch.web.admin.repo.dao.T_MobileAppUseRepository;
import com.sdc2ch.web.admin.repo.domain.T_MOBILE_APP_USE_INFO;

@Service
class MobileAppInfoServiceImpl implements IMobileAppInfoService {

	@Autowired
	T_MobileAppUseRepository useRepo;
	@Autowired
	ITosHistService tosHistSvc;

	private LoadingCache<String, Optional<T_MOBILE_APP_USE_INFO>> appinfoCache;

	
	private static final int expiredTime = 1 * 7;
	private static final TimeUnit expiredUnit = TimeUnit.DAYS;
	private static final int maxSize = 5000;

	
	@PostConstruct
	public void onLoad() {

		appinfoCache = CacheMenager.<String, Optional<T_MOBILE_APP_USE_INFO>>builder().expiredTime(expiredTime)
				.timeUnit(expiredUnit).maxSize(maxSize).future(new Future<String, Optional<T_MOBILE_APP_USE_INFO>>() {
					@Override
					public Optional<T_MOBILE_APP_USE_INFO> get(final String key) throws Exception {
						return useRepo.findByUserId(key);
					}
				}).build().reloadCache();
		
		

	}

	@Override
	public Optional<T_MOBILE_APP_USE_INFO> findDriverAppInfoById(String id) {
		try {
			Optional<T_MOBILE_APP_USE_INFO> appInfo = appinfoCache.get(id);
			return appInfo;
		} catch (Exception e) {
			return useRepo.findByUserId(id);
		}
	}

	@Override
	@Transactional
	public T_MOBILE_APP_USE_INFO save(T_MOBILE_APP_USE_INFO mobileUseInfo) {
		useRepo.save(mobileUseInfo);
		appinfoCache.put(mobileUseInfo.getUserId(), Optional.ofNullable(mobileUseInfo));
		appinfoCache.refresh(mobileUseInfo.getUserId());
		return mobileUseInfo;
	}

	@Override
	public List<T_MOBILE_APP_USE_INFO> findAllByIds(String... ids) {
		return findAllByIds(Stream.of(ids).collect(Collectors.toList()));
	}

	@Override
	public List<T_MOBILE_APP_USE_INFO> findAllByIds(List<String> ids) {
		return appinfoCache.asMap().keySet().stream().filter(k -> ids.contains(k)).map(k -> {
			try {
				return appinfoCache.get(k);
			} catch (ExecutionException e) {
				return useRepo.findByUserId(k);
			}
		}).map(o -> o.get()).collect(Collectors.toList()); 
	}
}
