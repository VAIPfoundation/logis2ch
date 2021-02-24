package com.sdc2ch.service.mobile.impl;

import static com.sdc2ch.core.lambda.tuple.Tuple.tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.querydsl.core.types.Projections;
import com.sdc2ch.core.lambda.seq.Seq;
import com.sdc2ch.prcss.eb.IEmptyBoxService;
import com.sdc2ch.prcss.eb.vo.EmptyboxVo;
import com.sdc2ch.service.admin.IAnalsVhcleService;
import com.sdc2ch.service.admin.IUnstoringManageService;
import com.sdc2ch.service.common.ITosHistService;
import com.sdc2ch.service.mobile.IMobileCntrlInfoService;
import com.sdc2ch.service.mobile.model.MobileCtrlInfoVo;
import com.sdc2ch.web.admin.repo.AdmQueryBuilder;
import com.sdc2ch.web.admin.repo.dao.T_MobileAppUseRepository;
import com.sdc2ch.web.admin.repo.domain.v.QV_CARALC_PLAN;
import com.sdc2ch.web.admin.repo.domain.v.V_CARALC_PLAN;
import com.sdc2ch.web.admin.repo.domain.v.V_REALTIME_INFO;
import com.sdc2ch.web.admin.repo.domain.v.V_UNSTORING_MANAGE;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
class MobileCntrlInfoServiceImpl implements IMobileCntrlInfoService {

	private static final String SP_NAME = "[2CH].[DBO].[SP_2CH_DLVY_LC_CNTRL]";

	@Autowired T_MobileAppUseRepository useRepo;
	@Autowired ITosHistService tosHistSvc;


	@Autowired IAnalsVhcleService rtInfoSvc;
	@Autowired IEmptyBoxService ebInfoSvc;
	@Autowired IUnstoringManageService unstoringSvc;


	
	@Autowired AdmQueryBuilder builder;


	@Override
	public V_CARALC_PLAN searchDlvyLc(String dlvyDe, String dlvyLcCd) {
		log.info("dlvyDe={}, dlvyLcCd={}", dlvyDe, dlvyLcCd);
		QV_CARALC_PLAN view = QV_CARALC_PLAN.v_CARALC_PLAN;
		V_CARALC_PLAN result = new V_CARALC_PLAN();
		try {

			Object[] param = {dlvyDe, dlvyLcCd};
			List<Object[]> resultList = builder.storedProcedureCallRet("[dbo].[SP_2CH_STOP_ORDER_INFO_SEL_BY_STOP_CD]", param);

			System.out.println("resultList>>" + resultList.size());

			if ( resultList != null && resultList.size() > 0) {
				Object[] resultOne = resultList.get(0);

				result.setFctryCd(String.valueOf(resultOne[0]));
				result.setDlvyDe(String.valueOf(resultOne[1]));
				result.setRouteNo(String.valueOf(resultOne[2]));
				result.setDlvyLcCd(String.valueOf(resultOne[3]));
				result.setVrn(String.valueOf(resultOne[4]));
				result.setDlvySeq(String.valueOf(resultOne[5]));
				result.setDriverNm(String.valueOf(resultOne[6]));
				result.setMobileNo(String.valueOf(resultOne[7]));
				result.setDlvyLcNm(String.valueOf(resultOne[8]));
				result.setDlvyLcGroupNm(String.valueOf(resultOne[9]));
				result.setDlvyLcBranchNm(String.valueOf(resultOne[10]));


			}
		} catch (Exception e) {
			log.error("{}", e);
		}

		return result;
	}

	@Override
	public List<Object[]> searchRoute(String dlvyDe, String dlvyLcCd) {
		log.info("dlvyDe={}, dlvyLcCd={}", dlvyDe, dlvyLcCd);
		Object[] params = {"", dlvyDe, dlvyLcCd.substring(0, 5)};
		
		return builder.storedProcedureResultCall(SP_NAME, params);
	}

	@Override
	public MobileCtrlInfoVo searchVhcle(String dlvyDe, String dlvyLcCd, String routeNo, String vrn) {
		log.info("dlvyDe={}, routeNo={}, vrn={}", dlvyDe, routeNo, vrn);
		MobileCtrlInfoVo mcVo = new MobileCtrlInfoVo();

		


		V_UNSTORING_MANAGE umIo = unstoringSvc.searchOne(dlvyDe, routeNo);

		BeanUtils.copyProperties(umIo, mcVo);


		
		List<V_CARALC_PLAN> dlvyPlan = new ArrayList<V_CARALC_PLAN>();
		log.info("dlvyDe={}, dlvyLcCd={}", dlvyDe, dlvyLcCd);
		Object[] params = {dlvyDe, routeNo};
		
		List<Object[]> resultList = builder.storedProcedureCallRet("[dbo].[SP_2CH_STOP_ORDER_INFO_SEL_BY_ROUTE_NO]", params);
		if ( resultList != null && resultList.size() > 0 ) {
			for( Object[] resultObj : resultList ) {
				V_CARALC_PLAN plan = new V_CARALC_PLAN();
				plan.setFctryCd(		String.valueOf(resultObj[0]));
				plan.setDlvyDe(			String.valueOf(resultObj[1]));
				plan.setRouteNo(		String.valueOf(resultObj[2]));
				plan.setDlvyLcCd(		String.valueOf(resultObj[3]));
				plan.setVrn(			String.valueOf(resultObj[4]));
				plan.setDriverNm(		String.valueOf(resultObj[5]));
				plan.setMobileNo(		String.valueOf(resultObj[6]));
				plan.setDlvyLcNm(		String.valueOf(resultObj[7]));
				plan.setDlvyLcGroupNm(	String.valueOf(resultObj[8]));
				plan.setDlvyLcBranchNm(	String.valueOf(resultObj[9]));
				plan.setLongitude(		String.valueOf(resultObj[10]));
				plan.setLatitude(		String.valueOf(resultObj[11]));
				plan.setAddrBasic(		String.valueOf(resultObj[12]));
				plan.setStopType(		String.valueOf(resultObj[13]));
				plan.setArvlExpcTime(	String.valueOf(resultObj[14]));
				plan.setStopSeq(		String.valueOf(resultObj[15]));
				dlvyPlan.add(plan);
			}
		}




















		if ( dlvyPlan != null ) {
			
			List<EmptyboxVo> ebList = ebInfoSvc.listOnlyEmptyboxByDlvyDeAndRouteNo(dlvyDe, routeNo);
			dlvyPlan = leftjoin(dlvyPlan, ebList);
			
			mcVo.setDlvyPlan(dlvyPlan);
		}

		return mcVo;
	}


	
	private List<V_CARALC_PLAN> leftjoin(List<V_CARALC_PLAN> plan, List<EmptyboxVo> empty){
		return Seq.seq(plan)
			.flatMap(v1 -> Seq.seq(empty)
					.filter(v2 -> filter(v1, v2))
					.onEmpty(null)
					.map(v2 -> tuple(v1, v2))
					.map(t -> convert(t.v1, t.v2))
					)

			.collect(Collectors.toList());
	}

	private boolean filter(V_CARALC_PLAN v1, EmptyboxVo v2) {
		if(v1 == null) {
			return false;
		}
		if(v2 == null) {
			return false;
		}
		return StringUtils.isEmpty(v1.getStopCd()) ? false : v1.getStopCd().equals(v2.getStopCd());
	}

	private V_CARALC_PLAN convert(V_CARALC_PLAN v1, EmptyboxVo v2) {
		V_CARALC_PLAN dto = v1;
		if(v2 != null) {
			int boxQty = v2.getSquareBoxQty() + v2.getTriangleBoxQty() + v2.getYodelryBoxQty();
			dto.setBoxQty(boxQty);
		}
		return dto;
	}
}
