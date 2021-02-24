package com.sdc2ch.prcss.ds.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdc2ch.prcss.ds.repo.domain.T_SHIPPING_RT_STATE;
import com.sdc2ch.repo.io.ShippingID;

public interface T_ShippingRtStateRepository extends JpaRepository<T_SHIPPING_RT_STATE, ShippingID>{

}
