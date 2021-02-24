package com.sdc2ch.prcss.ds.service;

import java.util.List;

import com.sdc2ch.prcss.ds.repo.domain.T_SHIPPING_STATE;

public interface IShippingService {
	
	List<T_SHIPPING_STATE> findAll();

}
