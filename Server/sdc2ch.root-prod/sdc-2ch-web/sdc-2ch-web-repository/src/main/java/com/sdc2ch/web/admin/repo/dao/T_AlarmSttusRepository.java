package com.sdc2ch.web.admin.repo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.sdc2ch.web.admin.repo.domain.alloc.T_ALARM_STTUS;
import com.sdc2ch.web.admin.repo.dto.AlarmChkDto;

public interface T_AlarmSttusRepository extends JpaRepository<T_ALARM_STTUS, Long>, QuerydslPredicateExecutor<T_ALARM_STTUS> {


	
	@Query(value = "{call [dbo].[SP_2CH_ALARM_CHK_GPS] (:dlvyDe, :fctryCd, :range, :cycle)}", nativeQuery = true)
	List<AlarmChkDto> findOffGpsByDlvyDeAndFctryCd(@Param("dlvyDe") String dlvyDe, @Param("fctryCd") String fctryCd, @Param("range") Integer range, @Param("cycle")Integer cycle);

	
	@Query(value = "{call [dbo].[SP_2CH_ALARM_CHK_CONFIRM] (:dlvyDe, :fctryCd, :range, :cycle)}", nativeQuery = true)
	List<AlarmChkDto> findAlcNoConfirmByDlvyDeAndFctryCd(@Param("dlvyDe") String dlvyDe, @Param("fctryCd") String fctryCd, @Param("range") Integer range, @Param("cycle")Integer cycle);

	
	@Query(value = "{call [dbo].[SP_2CH_ALARM_CHK_EB] (:dlvyDe, :fctryCd, :range, :cycle)}", nativeQuery = true)
	List<AlarmChkDto> findDelayEmptyBoxByDlvyDeAndFctryCd(@Param("dlvyDe") String dlvyDe, @Param("fctryCd") String fctryCd, @Param("range") Integer range, @Param("cycle")Integer cycle);

	


	@Query(value="{call [dbo].[SP_2CH_ALARM_CHK_PASSAGE] (:dlvyDe, :fctryCd, :range)}", nativeQuery=true)
	List<AlarmChkDto> findNoPassageByDlvyDeAndFctryCdAndState(@Param("dlvyDe") String dlvyDe, @Param("fctryCd") String fctryCd, @Param("range") Integer range);

	


	@Query(value="{call [dbo].[SP_2CH_ALARM_CHK_LOADING] (:dlvyDe, :fctryCd, :range)}", nativeQuery=true)
	List<AlarmChkDto> findNoLoadingByDlvyDeAndFctryCdAndState(@Param("dlvyDe") String dlvyDe, @Param("fctryCd") String fctryCd,  @Param("range") Integer range);





}
