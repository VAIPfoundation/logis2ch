package com.sdc2ch.tms.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.cache.LoadingCache;
import com.sdc2ch.repo.io.TmsCarIO;
import com.sdc2ch.repo.io.TmsDriverIO;
import com.sdc2ch.require.cache.CacheMenager;
import com.sdc2ch.require.cache.CacheMenager.Future;
import com.sdc2ch.require.domain.IUserDetails;
import com.sdc2ch.web.service.ITmsDriverService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
class TmsDriverServiceImpl  {

	@Autowired ITmsDriverService driverSvc;
	private LoadingCache<String, Optional<IUserDetails>> driverCache;

	
	@Value(value = "${tms.onload.enable:true}")
	protected boolean enableTmsOnload;

	@PostConstruct
	public void onLoad() {
		
		if(enableTmsOnload==false)
			return;
		
		driverCache = CacheMenager.<String, Optional<IUserDetails>>builder().expiredTime(10).timeUnit(TimeUnit.MINUTES)
				.maxSize(2000).future(new Future<String, Optional<IUserDetails>>() {
					@Override
					public Optional<IUserDetails> get(final String key) throws Exception {
						return driverSvc.findByMobileNo(key);
					}
				}).build().reloadCache();
		
		List<IUserDetails> details = driverSvc.findAll();
		List<TmsCarIO> cars = driverSvc.findAllCar();
		cars = cars.stream().filter(distinctByKey(c -> c.getDriverCd())).collect(Collectors.toList());
		Map<String, TmsCarIO> mapped = cars.stream().collect(Collectors.toMap(TmsCarIO::getDriverCd, c -> c));
		details.stream().forEach(d -> {
			TmsCarIO car  = null;
			if(d.getMobileNo() != null) {
				TmsDriverIO driver = (TmsDriverIO) d;
				try {
					car = mapped.get(driver.getUserDetailsId());
					driver.setCar(car);
					
				}catch (Exception e) {
					log.error("{}", e);
				}
				if(car != null) {
					driverCache.put(d.getMobileNo(), Optional.of(d));
				}
				
			}else {
				System.out.println("");
			}
			
			
		});
		

	}

	public Optional<IUserDetails> findByPhoneNo(final String phoneNo) {
		
		 try {
			return driverCache.get(phoneNo);
		} catch (ExecutionException e) {
			return driverSvc.findByMobileNo(phoneNo);
		}
	}

	public Optional<IUserDetails> findByDriverCd(String id) {
		Optional<IUserDetails> details = null;
		try {
			details = driverCache.asMap().values().stream().filter(o -> o.orElse(null) != null).filter(o -> o.get().getUserDetailsId().equals(id)).findFirst().orElseGet(() -> {
				IUserDetails driver = driverSvc.findByDriverCd(id);
				if(driver != null) {
					TmsCarIO car = driverSvc.findByCar(driver.getMobileNo());
					TmsDriverIO tmsDr = (TmsDriverIO) driver;
					tmsDr.setCar(car);
				}
				return Optional.ofNullable(driver);
			});
		}catch (Exception e) {
			log.error("findByDriverCd() -> {}", e); 
		}
		IUserDetails ud = details.orElse(null);
		if(ud == null) {
			ud = driverSvc.findByDriverCd(id);
			details = Optional.ofNullable(ud);
		}
		return details;
	}
	
	public static <T> Predicate<T> distinctByKey( Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> map = new HashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

}
