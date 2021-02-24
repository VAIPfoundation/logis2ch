package com.sdc2ch.nfc.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdc2ch.nfc.domain.entity.T_usr;
import com.sdc2ch.nfc.repository.V_NfcUserRepository;
import com.sdc2ch.nfc.service.INfcUserService;

@Service
class NfcUserServiceImpl implements INfcUserService {
	
	@Autowired V_NfcUserRepository userRepo;

	@Override
	public Optional<T_usr> findById(int userId) {
		return userRepo.findById(userId);
	}

	@Override
	public void save(T_usr user) {
		userRepo.save(user);
	}

	@Override
	public void save(List<T_usr> users) {		
		userRepo.saveAll(users);
	}

}
