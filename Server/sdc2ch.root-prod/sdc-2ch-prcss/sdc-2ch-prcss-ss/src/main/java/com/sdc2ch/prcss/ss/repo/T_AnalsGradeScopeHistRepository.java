package com.sdc2ch.prcss.ss.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdc2ch.prcss.ss.repo.domain.T_ANALS_GRADE_SCOPE_HIST;

public interface T_AnalsGradeScopeHistRepository extends JpaRepository<T_ANALS_GRADE_SCOPE_HIST, Long> {
	Optional<T_ANALS_GRADE_SCOPE_HIST> findByAclGroupIdAndRouteNoAndStatusCd(Long id, String routeNo, String stateCd);
}
