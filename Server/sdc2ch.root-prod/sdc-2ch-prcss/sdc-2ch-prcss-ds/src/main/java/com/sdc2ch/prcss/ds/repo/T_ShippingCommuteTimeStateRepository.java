package com.sdc2ch.prcss.ds.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sdc2ch.prcss.ds.repo.domain.T_SHIPPING_COMMUTE_TIME_STATE;

public interface T_ShippingCommuteTimeStateRepository extends JpaRepository<T_SHIPPING_COMMUTE_TIME_STATE, Long>{

	
    @Query(value = "SELECT isnull(max(ROW_ID), 0) from [DBO].[T_SHIPPING_COMMUTE_TIME_STATE] where driver_cd <> ?1", nativeQuery = true)
    long getMaxIdByDriverCd(String name);
    
    List<T_SHIPPING_COMMUTE_TIME_STATE> findByAllocatedGId(Long gid);
}
