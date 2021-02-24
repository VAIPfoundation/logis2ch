package com.sdc2ch.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sdc2ch.web.admin.repo.domain.lgist.T_ROUTE_PATH_MATRIX6;


public interface ILgistMatrixService {


	
	List<T_ROUTE_PATH_MATRIX6> search(Long modelId);
	List<T_ROUTE_PATH_MATRIX6> search(Long modelId, String routeNo, String vrn);
	Page<T_ROUTE_PATH_MATRIX6> search(Long modelId, String routeNo, String vrn, Pageable pageable);

	
	T_ROUTE_PATH_MATRIX6 findById(Long id);

}
