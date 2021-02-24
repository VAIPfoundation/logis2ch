package com.sdc2ch.web.service;

import java.util.List;
import java.util.Optional;

import com.sdc2ch.repo.io.TmsCarIO;
import com.sdc2ch.require.domain.IUserDetails;

public interface ITmsDriverService {

	Optional<IUserDetails> findByMobileNo(String mobileNo);
	List<IUserDetails> findAll();
	List<TmsCarIO> findAllCar();
	IUserDetails findByDriverCd(String id);
	TmsCarIO findByCar(String driverMobileNo);

}
