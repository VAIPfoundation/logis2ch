package com.sdc2ch.prcss.ds.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdc2ch.prcss.ds.repo.domain.T_SHIPPING_INOUT_STATE;

public interface T_ShippingInoutStateRepository extends JpaRepository<T_SHIPPING_INOUT_STATE, Long>{
	
	List<T_SHIPPING_INOUT_STATE> findByAllocatedGId(Long gid);
}
