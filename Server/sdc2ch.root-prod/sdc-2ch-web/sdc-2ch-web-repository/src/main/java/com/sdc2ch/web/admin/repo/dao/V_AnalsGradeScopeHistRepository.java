package com.sdc2ch.web.admin.repo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestBody;

import com.sdc2ch.web.admin.repo.domain.sta.V_ANALS_GRADE_SCOPE_HIST;
import com.sdc2ch.web.admin.repo.dto.IAnalsDlvyDtlsDto;
import com.sdc2ch.web.admin.repo.dto.IAnalsDlvyLcRankListDto;
import com.sdc2ch.web.admin.repo.dto.IAnalsDstrbChrtColumnDto;
import com.sdc2ch.web.admin.repo.dto.IAnalsDstrbChrtPieDto;
import com.sdc2ch.web.admin.repo.dto.IAnalsDstrbDto;
import com.sdc2ch.web.admin.repo.dto.IAnalsDstrbHistDto;
import com.sdc2ch.web.admin.repo.dto.IAnalsLdngDtlsDto;
import com.sdc2ch.web.admin.repo.dto.IAnalsRankDto;


public interface V_AnalsGradeScopeHistRepository extends JpaRepository<V_ANALS_GRADE_SCOPE_HIST, Long>, QuerydslPredicateExecutor<V_ANALS_GRADE_SCOPE_HIST>{

	
	@Query(value = "{call [dbo].[SP_2CH_ANALS_LDNG_RANK_SEL] (:fctryCd, :fromDe, :toDe, :caralcTy, :vhcleTy, :routeNo, :wkdayYn, :satYn, :sunYn)}", nativeQuery = true)
	List<IAnalsRankDto> searchAnalsLdngTimeRank(
			@Param("fctryCd") String fctryCd,
			@Param("fromDe") String fromDe,
			@Param("toDe") String toDe,
			@Param("caralcTy") String caralcTy,
			@Param("vhcleTy") String vhcleTy,
			@Param("routeNo") String routeNo,
			@Param("wkdayYn") String wkdayYn,
			@Param("satYn") String satYn,
			@Param("sunYn") String sunYn);

	
	@Query(value = "{call [dbo].[SP_2CH_ANALS_LDNG_DSTRB_SEL] (:fctryCd, :fromDe, :toDe, :caralcTy, :vhcleTy, :routeNo, :wkdayYn, :satYn, :sunYn, :stdTime)}", nativeQuery = true)
	List<IAnalsDstrbDto> searchAnalsLdngTimeDstrbList(
			@Param("fctryCd") String fctryCd,
			@Param("fromDe") String fromDe,
			@Param("toDe") String toDe,
			@Param("caralcTy") String caralcTy,
			@Param("vhcleTy") String vhcleTy,
			@Param("routeNo") String routeNo,
			@Param("wkdayYn") String wkdayYn,
			@Param("satYn") String satYn,
			@Param("sunYn") String sunYn,
			@Param("stdTime") Long stdTime
			);

	
	@Query(value = "{call [dbo].[SP_2CH_ANALS_LDNG_DSTRB_SETUP_HIST_SEL] (:fctryCd, :fromDe, :toDe, :caralcTy, :vhcleTy, :routeNo, :wkdayYn, :satYn, :sunYn, :stdTime)}", nativeQuery = true)
	List<IAnalsDstrbHistDto> searchAnalsLdngTimeDstrbSetupHist(
			@Param("fctryCd") String fctryCd,
			@Param("fromDe") String fromDe,
			@Param("toDe") String toDe,
			@Param("caralcTy") String caralcTy,
			@Param("vhcleTy") String vhcleTy,
			@Param("routeNo") String routeNo,
			@Param("wkdayYn") String wkdayYn,
			@Param("satYn") String satYn,
			@Param("sunYn") String sunYn,
			@Param("stdTime") Long stdTime
			);

	
	@Query(value = "{call [dbo].[SP_2CH_ANALS_LDNG_DSTRB_SETUP_REG] (:fctryCd, :fromDe, :toDe, :caralcTy, :vhcleTy, :routeNo, :wkdayYn, :satYn, :sunYn, :stdTime)}", nativeQuery = true)
	void saveAnalsLdngTimeDstrbSetup(
			@Param("fctryCd") String fctryCd,
			@Param("fromDe") String fromDe,
			@Param("toDe") String toDe,
			@Param("caralcTy") String caralcTy,
			@Param("vhcleTy") String vhcleTy,
			@Param("routeNo") String routeNo,
			@Param("wkdayYn") String wkdayYn,
			@Param("satYn") String satYn,
			@Param("sunYn") String sunYn,
			@Param("stdTime") Long stdTime
			);

	
	@Query(value = "{call [dbo].[SP_2CH_ANALS_LDNG_DTLS_SEL] (:fctryCd, :fromDe, :toDe, :caralcTy, :vhcleTy, :routeNo, :wkdayYn, :satYn, :sunYn)}", nativeQuery = true)
	List<IAnalsLdngDtlsDto> searchAnalsLdngTimeDetailList(
			@Param("fctryCd") String fctryCd,
			@Param("fromDe") String fromDe,
			@Param("toDe") String toDe,
			@Param("caralcTy") String caralcTy,
			@Param("vhcleTy") String vhcleTy,
			@Param("routeNo") String routeNo,
			@Param("wkdayYn") String wkdayYn,
			@Param("satYn") String satYn,
			@Param("sunYn") String sunYn
			);


	

	
	@Query(value = "{call [dbo].[SP_2CH_ANALS_DLVY_RANK_SEL] (:fctryCd, :fromDe, :toDe, :routeNo, :caralcTy, :vhcleTy, :wkdayYn, :satYn, :sunYn)}", nativeQuery = true)
	List<IAnalsRankDto> searchAnalsDlvyTimeRank(
			@Param("fctryCd") String fctryCd,
			@Param("fromDe") String fromDe,
			@Param("toDe") String toDe,
			@Param("routeNo") String routeNo,
			@Param("caralcTy") String caralcTy,
			@Param("vhcleTy") String vhcleTy,
			@Param("wkdayYn") String wkdayYn,
			@Param("satYn") String satYn,
			@Param("sunYn") String sunYn);


	
	@Query(value = "{call [dbo].[SP_2CH_ANALS_DLVY_DSTRB_SEL] (:fctryCd, :fromDe, :toDe, :routeNo, :caralcTy, :vhcleTy, :wkdayYn, :satYn, :sunYn, :stdTime)}", nativeQuery = true)
	List<IAnalsDstrbDto> searchAnalsDlvyTimeDstrbList(
			@Param("fctryCd") String fctryCd,
			@Param("fromDe") String fromDe,
			@Param("toDe") String toDe,
			@Param("routeNo") String routeNo,
			@Param("caralcTy") String caralcTy,
			@Param("vhcleTy") String vhcleTy,
			@Param("wkdayYn") String wkdayYn,
			@Param("satYn") String satYn,
			@Param("sunYn") String sunYn,
			@Param("stdTime") Long stdTime
			);

	
	@Query(value = "{call [dbo].[SP_2CH_ANALS_DLVY_DSTRB_SETUP_HIST_SEL] (:fctryCd, :fromDe, :toDe, :routeNo, :caralcTy, :vhcleTy, :wkdayYn, :satYn, :sunYn, :stdTime)}", nativeQuery = true)
	List<IAnalsDstrbHistDto> searchAnalsDlvyTimeDstrbSetupHist(
			@Param("fctryCd") String fctryCd,
			@Param("fromDe") String fromDe,
			@Param("toDe") String toDe,
			@Param("routeNo") String routeNo,
			@Param("caralcTy") String caralcTy,
			@Param("vhcleTy") String vhcleTy,
			@Param("wkdayYn") String wkdayYn,
			@Param("satYn") String satYn,
			@Param("sunYn") String sunYn,
			@Param("stdTime") Long stdTime
			);

	
	@Query(value = "{call [dbo].[SP_2CH_ANALS_DLVY_DSTRB_SETUP_REG] (:fctryCd, :fromDe, :toDe, :routeNo, :caralcTy, :vhcleTy, :wkdayYn, :satYn, :sunYn, :stdTime)}", nativeQuery = true)
	void saveAnalsDlvyTimeDstrbSetup(
			@Param("fctryCd") String fctryCd,
			@Param("fromDe") String fromDe,
			@Param("toDe") String toDe,
			@Param("routeNo") String routeNo,
			@Param("caralcTy") String caralcTy,
			@Param("vhcleTy") String vhcleTy,
			@Param("wkdayYn") String wkdayYn,
			@Param("satYn") String satYn,
			@Param("sunYn") String sunYn,
			@Param("stdTime") Long stdTime
			);

	
	@Query(value = "{call [dbo].[SP_2CH_ANALS_DLVY_DTLS_SEL] (:fctryCd, :fromDe, :toDe, :routeNo, :caralcTy, :vhcleTy, :wkdayYn, :satYn, :sunYn)}", nativeQuery = true)
	List<IAnalsDlvyDtlsDto> searchAnalsDlvyTimeDetailList(
			@Param("fctryCd") String fctryCd,
			@Param("fromDe") String fromDe,
			@Param("toDe") String toDe,
			@Param("routeNo") String routeNo,
			@Param("caralcTy") String caralcTy,
			@Param("vhcleTy") String vhcleTy,
			@Param("wkdayYn") String wkdayYn,
			@Param("satYn") String satYn,
			@Param("sunYn") String sunYn
			);

	

	
	@Query(value = "{call [dbo].[SP_2CH_ANALS_DLVYLC_RANK_SEL] (:fctryCd, :fromDe, :toDe, :wkdayYn, :satYn, :sunYn)}", nativeQuery = true)
	List<IAnalsDlvyLcRankListDto> searchAnalsDlvyLcTimeRankList(
			@Param("fctryCd") String fctryCd,
			@Param("fromDe") String fromDe,
			@Param("toDe") String toDe,
			@Param("wkdayYn") String wkdayYn,
			@Param("satYn") String satYn,
			@Param("sunYn") String sunYn);

	
	@Query(value = "{call [dbo].[SP_2CH_ANALS_DLVYLC_DSTRB_SEL] (:fctryCd, :fromDe, :toDe, :dlvyLcCd, :dlvyLcTime, :wkdayYn, :satYn, :sunYn)}", nativeQuery = true)
	List<IAnalsDstrbDto> searchAnalsDlvyLcTimeDstrbList(
			@Param("fctryCd") String fctryCd,
			@Param("fromDe") String fromDe,
			@Param("toDe") String toDe,
			@Param("dlvyLcCd") String dlvyLcCd,
			@Param("dlvyLcTime") String dlvyLcTime,
			@Param("wkdayYn") String wkdayYn,
			@Param("satYn") String satYn,
			@Param("sunYn") String sunYn);


	
	@Query(value = "{call [dbo].[SP_2CH_ANALS_DLVYLC_DTLS_SEL] (:fctryCd, :fromDe, :toDe, :dlvyLcCd, :wkdayYn, :satYn, :sunYn)}", nativeQuery = true)
	List<IAnalsDstrbDto> searchAnalsDlvyLcTimeDetailsList(
			@Param("fctryCd") String fctryCd,
			@Param("fromDe") String fromDe,
			@Param("toDe") String toDe,
			@Param("dlvyLcCd") String dlvyLcCd,
			@Param("wkdayYn") String wkdayYn,
			@Param("satYn") String satYn,
			@Param("sunYn") String sunYn);

}
