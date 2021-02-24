package com.sdc2ch.tms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sdc2ch.tms.domain.view.TmsPlan;


public interface TmsPlanRepository extends JpaRepository<TmsPlan, Long> {

	List<TmsPlan> findAllByDlvyDe(String dlvyDe);

	
	@Query(value="SELECT (CASE WHEN count(*) > 0 THEN 'true' ELSE 'false' END)\r\n" +
			"FROM TMS.dbo.M_PLAN_MST\r\n" +
			"WHERE 1=1\r\n" +
			"AND car_cd = :carCd\r\n" +
			"AND Delivery_Date = :dlvyDe\r\n" +
			"AND Route_No = :routeNo \r\n" +
			"AND PICK_USER_ID IS NOT NULL\r\n" +
			"AND PICK_DATETIME IS NOT NULL\r\n", nativeQuery=true)
	boolean isPickcomplate(@Param("carCd") String carCd, @Param("dlvyDe") String dlvyDe, @Param("routeNo") String routeNo);
}
