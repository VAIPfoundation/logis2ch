package com.sdc2ch.web.admin.repo.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.sdc2ch.web.admin.repo.domain.v.V_CARALC_DTLS;

public interface V_CaralcDtlsRepository extends JpaRepository<V_CARALC_DTLS, Long>, QuerydslPredicateExecutor<V_CARALC_DTLS>{

	Optional<List<V_CARALC_DTLS>> findAllByDeliveryDateAndRouteNo(String deliveryDate, String routeNo);
	Optional<List<V_CARALC_DTLS>> findAllByDeliveryDateAndRouteNo(String deliveryDate, String routeNo, Sort sort);

}
