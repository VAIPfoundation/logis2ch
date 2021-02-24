package com.sdc2ch.prcss.ss.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdc2ch.prcss.ss.repo.domain.T_DRIVE_DIARY;

public interface T_DriveDiaryRepository extends JpaRepository<T_DRIVE_DIARY, Long> {

	List<T_DRIVE_DIARY> findAllByAclGroupId(Long aclGroupId);
	List<T_DRIVE_DIARY> findAllByDriverCdAndDlvyDe(String driverCd, String dlvyDe);
	List<T_DRIVE_DIARY> findAllByVrnAndDlvyDe(String vrn, String dlvyDe);
	Optional<T_DRIVE_DIARY> findByDlvyDeAndRouteNoAndDlvyLcCdAndStatusCdAndEventCd(String dlvyDe, String routeNo, String dlvyLcCd, String statusCd, String eventCd);
}
