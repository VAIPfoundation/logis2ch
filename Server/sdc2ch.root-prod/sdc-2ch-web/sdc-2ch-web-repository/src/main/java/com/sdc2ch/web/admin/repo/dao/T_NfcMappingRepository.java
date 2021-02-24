package com.sdc2ch.web.admin.repo.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdc2ch.repo.io.NfcMappingIO;
import com.sdc2ch.web.admin.repo.domain.op.T_NFC_MAPPING;

public interface T_NfcMappingRepository extends JpaRepository<T_NFC_MAPPING, Long>{

	Optional<NfcMappingIO> findByNfcId(int nfcId);
	List<T_NFC_MAPPING> findByFctryCd(String fctryCd);
}
