package com.sdc2ch.tms.service.impl;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.common.cache.LoadingCache;
import com.sdc2ch.require.cache.CacheMenager;
import com.sdc2ch.require.cache.CacheMenager.Future;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.domain.IUserDetails;
import com.sdc2ch.require.service.I2ChUserDetailsService;
import com.sdc2ch.require.service.I2ChUserService;
import com.sdc2ch.tms.config.TmsQueryBuilder;
import com.sdc2ch.tms.dao.TmsUserRepository;
import com.sdc2ch.tms.domain.TmsUser;
import com.sdc2ch.tms.utils.PhoneUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TmsUserServiceImpl implements I2ChUserService, I2ChUserDetailsService {

	@Autowired
	TmsUserRepository repo;
	@Autowired
	TmsQueryBuilder builder;
	private LoadingCache<String, Optional<IUser>> userCache;

	@Autowired
	private TmsDriverServiceImpl driverSvc;

	
	@Value(value = "${tms.onload.enable:true}")
	protected boolean enableTmsOnload;

	@PostConstruct
	public void onLoad() {

		if(enableTmsOnload==false)
			return;

		userCache = CacheMenager.<String, Optional<IUser>>builder().expiredTime(10).timeUnit(TimeUnit.MINUTES)
				.maxSize(4000).future(new Future<String, Optional<IUser>>() {
					@Override
					public Optional<IUser> get(final String key) throws Exception {
						TmsUser user = (TmsUser) repo.findByUsername(key).orElse(null);
						if (user != null) {
							try {

								IUserDetails details = driverSvc.findByDriverCd(user.getUsername()).orElse(null);
								user.setUserDetails(details);







							}catch (Exception e) {
								log.error("{}", e);
							}
						}
						return Optional.of(user);
					}
				}).build().reloadCache();

		repo.findAll().stream().map(user -> {
			if (user != null) {

				try {

					if(!StringUtils.isEmpty(user.getMobileNo())) {
						IUserDetails details = driverSvc.findByPhoneNo(user.getMobileNo().replaceAll("-", "").replaceAll(" ", "")).orElse(null);
						if(details != null)
							user.setUserDetails(details);
					}








				}catch (Exception e) {
					log.error("{}", e);
				}

			}
			return user;
		}).forEach(u -> userCache.put(u.getUsername(), Optional.of(u)));
	}

	@Override
	public Optional<IUser> findByUsername(String username) {
		try {
			return userCache.get(username);
		} catch (Exception e) {
			return repo.findByUsername(username);
		}
	}

	@Override
	public boolean isInDriver(IUser user) {
		return user != null && user.getUserDetails() != null
				&& user.getUserDetails().getUserDetailsId().startsWith("D");
	}

	@Override
	public Optional<IUserDetails> findByUser(IUser user) {
		return Optional.ofNullable(user.getUserDetails());
	}

	@Override
	public Optional<IUser> findByMobileNo(String mobileNo) {
		IUserDetails userDetails = findByPhoneNo(mobileNo).orElse(null);
		return userDetails == null ? Optional.empty() : findByUsername(userDetails.getUserDetailsId());
	}

	@Override
	public Optional<IUserDetails> findByPhoneNo(String phoneNo) {
		return driverSvc.findByPhoneNo(phoneNo);
	}

	public Optional<IUserDetails> findByUserId(String id) {
		try {
			return Optional.ofNullable(userCache.get(id).get().getUserDetails());
		} catch (ExecutionException e) {
			return Optional.of(driverSvc.findByDriverCd(id).orElse(null));
		}
	}

	private String formatNo(String phoneNo) {
		if (StringUtils.isEmpty(phoneNo))
			return "";
		String fmt = phoneNo.startsWith("010") ? "XXX-XXXX-XXXX" : "XXX-XXX-XXXX";
		return PhoneUtils.formateToPhoneNumber(phoneNo, fmt, 13);
	}

	private String findCacheId(final String phoneNo) {

		return userCache.asMap().keySet().stream().filter(k -> {
			try {
				if (userCache.get(k) == null || !userCache.get(k).isPresent())
					return false;
				return phoneNo.equals(userCache.get(k).get().getMobileNo());
			} catch (ExecutionException e) {
				log.warn("{}", e);
			} catch (Exception e) {
				log.warn("{}", e);
			}
			return false;

		}).findFirst().orElse(null);
	}
}
