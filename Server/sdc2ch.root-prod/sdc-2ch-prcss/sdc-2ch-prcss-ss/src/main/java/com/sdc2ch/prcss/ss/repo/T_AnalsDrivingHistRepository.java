package com.sdc2ch.prcss.ss.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdc2ch.prcss.ss.repo.domain.T_ANALS_DRIVING_HIST;

public interface T_AnalsDrivingHistRepository extends JpaRepository<T_ANALS_DRIVING_HIST, Long> {

	Optional<T_ANALS_DRIVING_HIST> findByAclGroupId(Long groupId);

}
