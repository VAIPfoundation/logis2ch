package com.sdc2ch.prcss.eb.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdc2ch.prcss.eb.repo.domain.T_EMPTYBOX_INFO;

public interface T_EmptyBoxRepository extends JpaRepository<T_EMPTYBOX_INFO, Long>{

	T_EMPTYBOX_INFO findByDlvyDeAndRouteNoAndStopCd(String dlvyDe, String routeNo, String stopCd);
}
