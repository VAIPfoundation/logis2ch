package com.sdc2ch.tms.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.tms.domain.TmsUser;


public interface TmsUserRepository extends JpaRepository<TmsUser, Long> {

	Optional<IUser> findByUsername(String username);
}
