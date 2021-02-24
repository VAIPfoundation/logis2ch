package com.sdc2ch.prcss.ds.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdc2ch.prcss.ds.repo.domain.T_SHIPPING_LDNG_STATE;

public interface T_ShippingLdngStateRepository extends JpaRepository<T_SHIPPING_LDNG_STATE, Long>{
	List<T_SHIPPING_LDNG_STATE> findByAllocatedGId(Long gid);
}
