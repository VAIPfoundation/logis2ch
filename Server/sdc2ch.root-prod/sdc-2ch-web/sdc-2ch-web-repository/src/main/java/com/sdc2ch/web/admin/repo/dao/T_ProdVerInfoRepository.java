package com.sdc2ch.web.admin.repo.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdc2ch.web.admin.repo.domain.T_PROD_VER_INFO;

public interface T_ProdVerInfoRepository extends JpaRepository<T_PROD_VER_INFO, Long>{

	Optional<T_PROD_VER_INFO> findByProdTyAndMajorVerAndMinorVer(String prodTy, int marjor, int minor);
}
