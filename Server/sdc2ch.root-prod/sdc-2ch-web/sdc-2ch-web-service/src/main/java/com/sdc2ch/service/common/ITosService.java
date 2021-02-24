package com.sdc2ch.service.common;

import java.util.List;
import java.util.Optional;

import com.sdc2ch.web.admin.repo.domain.T_TOS;
import com.sdc2ch.web.admin.repo.enums.ToSRegEnums;


public interface ITosService {

	List<T_TOS> findByCurrentTos();
	List<T_TOS> findByIds(Long ... ids);
	List<T_TOS> findByIds(List<Long> ids);
	Optional<T_TOS> findById(Long id);

	List<T_TOS> findByRegType(ToSRegEnums regType);

	Integer getMaxMinorVersion(ToSRegEnums regType, Integer major);

	Integer getMaxMajorVersion(ToSRegEnums regType);


	T_TOS save(Long id, String title, String contents, ToSRegEnums regType, Integer major, String regUser);

}
