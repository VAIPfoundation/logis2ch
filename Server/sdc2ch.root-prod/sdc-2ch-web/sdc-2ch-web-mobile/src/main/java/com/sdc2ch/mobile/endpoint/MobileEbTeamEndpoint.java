package com.sdc2ch.mobile.endpoint;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sdc2ch.core.error.exception.CustomBadRequestException;
import com.sdc2ch.mobile.MobileController;
import com.sdc2ch.mobile.req.SumEbReq;
import com.sdc2ch.prcss.eb.vo.DriverSquareboxErpVo;
import com.sdc2ch.prcss.eb.vo.EmptyboxErpVo;
import com.sdc2ch.prcss.eb.vo.EmptyboxVo;
import com.sdc2ch.prcss.eb.vo.SummaryEmptyboxErpVo;
import com.sdc2ch.prcss.eb.vo.SummaryEmptyboxVo;
import com.sdc2ch.service.mobile.IMobileEbTeamService;
import com.sdc2ch.service.mobile.IMobileUserService;
import com.sdc2ch.web.admin.repo.domain.v.V_CARALC_PLAN;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(MobileController.REQUEST_MAPPING + "/ebt")
public class MobileEbTeamEndpoint extends MobileController {

	@Autowired IMobileEbTeamService ebTSvc;
	@Autowired IMobileUserService userSvc;

	@ApiOperation(value = "공상자반 > 공상자합계조회")
	@PostMapping(value = "/summary/empty/box", produces = SUPPORTED_TYPE)

	public @ResponseBody SummaryEmptyboxVo summaryEmptyBox(@Valid @RequestBody SumEbReq req, BindingResult bindingResult) throws CustomBadRequestException {
		super.verifyData(bindingResult);

		SummaryEmptyboxVo result = ebTSvc.summaryEmptyboxByRouteNoAndDlvyDe(req.getRouteNo(), req.getDlvyDe(), true);	
		if(result != null) {
			result.setDriverSquareBoxQty(userSvc.squareBoxQty(req.getDlvyDe(), req.getVrn(), req.getRouteNo()));
			List<EmptyboxVo> vos = ebTSvc.listEmptyboxByRouteNoAndDlvyDe(req.getRouteNo(), req.getDlvyDe());
			if(vos != null) {
				result.setEmptyboxList(vos);
			}
		}
		if(result == null) {
			result = new SummaryEmptyboxVo();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "공상자반 > 공상자합계조회")
	@PostMapping(value = "/summary/empty/boxerp", produces = SUPPORTED_TYPE)

	public @ResponseBody SummaryEmptyboxErpVo summaryEmptyBoxErp(@Valid @RequestBody SumEbReq req, BindingResult bindingResult) throws CustomBadRequestException {
		super.verifyData(bindingResult);
		SummaryEmptyboxErpVo result = (SummaryEmptyboxErpVo)ebTSvc.summaryEmptyboxByRouteNoAndDlvyDe(req.getRouteNo(), req.getDlvyDe(), true);
		if(result != null) {
			result.setDriverSquareBoxQty(userSvc.squareBoxQty(req.getDlvyDe(), req.getVrn(), req.getRouteNo()));
			List<EmptyboxErpVo> vos = (List<EmptyboxErpVo>)(Object)ebTSvc.listEmptyboxByRouteNoAndDlvyDe(req.getRouteNo(), req.getDlvyDe(), true);
			if(vos != null) {
				result.setEmptyboxErpList(vos);
			}
		}
		if(result == null) {
			result = new SummaryEmptyboxErpVo();
		}
		return result;
	}

	@ApiOperation(value = "기사 > 월간 기사사각상자 조회")
	@PostMapping(value = "/summary/empty/driverboxMonthly", produces = SUPPORTED_TYPE)

	public @ResponseBody List<DriverSquareboxErpVo> listEmptyBoxDriverMonthly(@Valid @RequestBody SumEbReq req, BindingResult bindingResult) throws CustomBadRequestException {

		return ebTSvc.listEmptyBoxDriverMonthly(req.getDlvyDe(), req.getVrn());
	}

	@ApiOperation(value = "기사 > 월간 파렛트 조회")
	@PostMapping(value = "/summary/empty/palletMonthly", produces = SUPPORTED_TYPE)

	public @ResponseBody List<EmptyboxErpVo> listEmptyBoxPalletMonthly(@Valid @RequestBody SumEbReq req, BindingResult bindingResult) throws CustomBadRequestException {


		List<V_CARALC_PLAN> planList = ebTSvc.listCaralcPlanMonthly(req.getDlvyDe(), req.getVrn());

		return ebTSvc.listEmptyBoxPalletMonthly(req.getDlvyDe(), planList);
	}

	@ApiOperation(value = "공상자반 > 공상자리스트조회")
	@PostMapping(value = "/list/empty/box", produces = SUPPORTED_TYPE)

	public @ResponseBody List<EmptyboxVo> listEmptyBox(@Valid @RequestBody SumEbReq req, BindingResult bindingResult) throws CustomBadRequestException {
		super.verifyData(bindingResult);
		return ebTSvc.listEmptyboxByRouteNoAndDlvyDe(req.getRouteNo(), req.getDlvyDe());
	}



}
