package com.sdc2ch.web.admin.repo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.sdc2ch.web.admin.repo.domain.sta.V_ANALS_GRADE_POINT_HIST;
import com.sdc2ch.web.admin.repo.dto.IDailyDlvyErorSttusDto;
import com.sdc2ch.web.admin.repo.dto.INoArvlDlvyLcDto;


public interface V_AnalsGradePointHistRepository extends JpaRepository<V_ANALS_GRADE_POINT_HIST, Long>, QuerydslPredicateExecutor<V_ANALS_GRADE_POINT_HIST>{

	@Query(value = "{call [dbo].[SP_2CH_ANALS_LDNG_AND_DLVY_STTUS_COLUMN_SEL] (:grad, :fctryCd, :fromDe, :toDe, :caralcTy, :vhcleTy, :routeNo, :dlvyLcCd, :wkdayYn, :satYn, :sunYn)}", nativeQuery = true)
	List<IDailyDlvyErorSttusDto> listDailyDlvyErorSttus(String grad, String fctryCd, String fromDe, String toDe, String caralcTy,
			String vhcleTy, String routeNo, String dlvyLcCd, String wkdayYn, String satYn, String sunYn);

	@Query(value = "{call [dbo].[SP_2CH_ANALS_NO_ARVL_DLVY_LC_SEL] (:fctryCd, :fromDe, :toDe, :caralcTy, :vhcleTy, :routeNo, :trnsprtCmpny, :vrn)}", nativeQuery = true)
	List<INoArvlDlvyLcDto> listNoArvlDlvyLc(String fctryCd, String fromDe, String toDe, String caralcTy, String vhcleTy, String routeNo, String trnsprtCmpny, String vrn);

}
