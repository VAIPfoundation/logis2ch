package com.sdc2ch.nfc.service;

import java.util.List;
import java.util.Optional;

import com.sdc2ch.nfc.domain.entity.T_usr;

public interface INfcUserService {
	
	Optional<T_usr> findById(int userId);
	void save(T_usr user);
	void save(List<T_usr> users);

}
