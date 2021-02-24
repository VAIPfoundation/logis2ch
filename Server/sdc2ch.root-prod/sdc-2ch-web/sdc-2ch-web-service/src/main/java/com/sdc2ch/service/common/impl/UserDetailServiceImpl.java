package com.sdc2ch.service.common.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdc2ch.repo.io.TmsCarIO;
import com.sdc2ch.require.domain.IUserDetails;
import com.sdc2ch.web.admin.repo.dao.V_CarRepository;
import com.sdc2ch.web.admin.repo.dao.V_DriverRepository;
import com.sdc2ch.web.admin.repo.domain.v.V_DRIVER;
import com.sdc2ch.web.admin.repo.domain.v.V_VHCLE;
import com.sdc2ch.web.service.ITmsDriverService;

@Service
public class UserDetailServiceImpl implements ITmsDriverService {

	
	@Autowired V_DriverRepository driverRepo;
	@Autowired V_CarRepository carRepo;
	@Override
	public Optional<IUserDetails> findByMobileNo(String mobileNo) {
		
		V_DRIVER driver = driverRepo.findByMobileNo(mobileNo).orElse(null);
		
		if(driver != null) {
			V_VHCLE car = carRepo.findByMobileNo(driver.getMobileNo()).orElse(null);
			driver.setCar(car);
		}
		
		return Optional.ofNullable(driver);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IUserDetails> findAll() {
		return (List<IUserDetails>)(Object)driverRepo.findAll();
	}

	@Override
	public IUserDetails findByDriverCd(String id) {
		return driverRepo.findByDriverCd(id).orElse(null);
	}

	@Override
	public TmsCarIO findByCar(String driverMobileNo) {
		return carRepo.findByMobileNo(driverMobileNo).orElse(null);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<TmsCarIO> findAllCar() {
		return (List<TmsCarIO>)(Object)carRepo.findAll();
	}

}
