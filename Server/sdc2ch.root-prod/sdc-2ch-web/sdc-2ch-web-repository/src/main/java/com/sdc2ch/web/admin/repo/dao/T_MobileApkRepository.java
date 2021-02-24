package com.sdc2ch.web.admin.repo.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdc2ch.web.admin.repo.domain.T_MOBILE_APK_INFO;

public interface T_MobileApkRepository extends JpaRepository<T_MOBILE_APK_INFO, Long>{

	Optional<T_MOBILE_APK_INFO> findBySha256(String crc);
}
