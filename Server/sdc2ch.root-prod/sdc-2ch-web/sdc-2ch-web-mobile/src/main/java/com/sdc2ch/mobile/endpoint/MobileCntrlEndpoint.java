package com.sdc2ch.mobile.endpoint;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sdc2ch.api.req.SearchReq;
import com.sdc2ch.core.error.exception.CustomBadRequestException;
import com.sdc2ch.mobile.MobileController;
import com.sdc2ch.mobile.res.ArsCall;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.domain.IUserDetails;
import com.sdc2ch.require.domain.IUserRole;
import com.sdc2ch.service.admin.IAnalsVhcleService;
import com.sdc2ch.service.mobile.IMobileCntrlInfoService;
import com.sdc2ch.service.mobile.IMobileUserService;
import com.sdc2ch.service.mobile.model.MobileCtrlInfoVo;
import com.sdc2ch.web.admin.repo.domain.op.T_LOCATION_INFO_HIST;
import com.sdc2ch.web.admin.repo.domain.v.V_CARALC_PLAN;
import com.sdc2ch.web.admin.repo.domain.v.V_REALTIME_INFO;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(MobileController.REQUEST_MAPPING + "/customer")
@Slf4j
public class MobileCntrlEndpoint extends MobileController {

	@Autowired IMobileCntrlInfoService mobileCntrlSvc; 
	@Autowired IAnalsVhcleService analsVhcleSvc;
	@Autowired IMobileUserService muserSvc;
	
	@ApiOperation(value = "고객센터 관제 > 차량관제_고객센터정보조회", response = MobileCtrlInfoVo.class)

	@GetMapping(value = "/cntrl/dlvyLc", produces = SUPPORTED_TYPE)
	public @ResponseBody V_CARALC_PLAN dlvyLcCtrl(@RequestParam String dlvyDe, @RequestParam String dlvyLcCd) throws CustomBadRequestException {
		
		return mobileCntrlSvc.searchDlvyLc(super.convertDate(dlvyDe), super.convertCd(dlvyLcCd));
	}

	@ApiOperation(value = "고객센터 관제 > 차량관제_노선정보조회", response = MobileCtrlInfoVo.class)

	@GetMapping(value = "/cntrl/route", produces = SUPPORTED_TYPE)
	public @ResponseBody List<Object[]> routeCtrl(@RequestParam String dlvyDe, @RequestParam String dlvyLcCd) throws CustomBadRequestException {
		
		return mobileCntrlSvc.searchRoute(super.convertDate(dlvyDe), super.convertCd(dlvyLcCd));
	}

	@ApiOperation(value = "고객센터 관제 > 차량관제", response = MobileCtrlInfoVo.class)

	@GetMapping(value = "/cntrl/vhcle", produces = SUPPORTED_TYPE)
	public @ResponseBody MobileCtrlInfoVo vhcleCtrl(@RequestParam String dlvyDe, @RequestParam String dlvyLcCd, @RequestParam String routeNo, @RequestParam String vrn) throws CustomBadRequestException {
		
		return mobileCntrlSvc.searchVhcle(super.convertDate(dlvyDe), super.convertCd(dlvyLcCd), routeNo, vrn);
	}
	
	@ApiOperation(value = "고객센터 관제 > 분석 - 관제 - 차량관제 이력조회", response = V_REALTIME_INFO.class)
	@PostMapping(value = "/cntrl/hist", produces = SUPPORTED_TYPE)
	public @ResponseBody List<T_LOCATION_INFO_HIST> listHistVhcleCntrl(@RequestBody SearchReq param) throws CustomBadRequestException {
		
		return analsVhcleSvc.searchHistVhcleCntrl(super.convertDate(param.getDlvyDe()), param.getVrn());
	}
	
	@ApiOperation(value = "고객센터 관제 > ARS전화연결")
	@PostMapping(value = "/ars/dlvylc/call", produces = SUPPORTED_TYPE)
	public void arsDlvyLcCall(@RequestBody ArsCall call, BindingResult bindingResult) throws CustomBadRequestException {
		super.verifyData(bindingResult);
		log.info("## ARS ## /ars/dlvylc/call, dlvyDe={}, routeNo={}, dlvyLcCd={}, callType={} ", call.getDlvyDe(), call.getRouteNo(), call.getDlvyLcCd(), call.getType());
		muserSvc.arsCall(call.getType(), null, call.getDlvyDe(), call.getDlvyLcCd(), null, getAnonymousUser(), call.getRouteNo());
	}
	
	private IUser getAnonymousUser() {
		
		return new IUser() {
			@Override
			public String getUsername() {
				return "anonymous user";
			}
			@Override
			public IUserDetails getUserDetails() {
				return null;
			}
			@Override
			public List<IUserRole> getRoles() {
				return null;
			}
			@Override
			public String getPassword() {
				return null;
			}
			@Override
			public String getMobileNo() {
				return null;
			}
			@Override
			public String getFctryCd() {
				return null;
			}
		};
		
	}

}
