package com.sdc2ch.mobile.endpoint;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.util.concurrent.UncheckedExecutionException;
import com.sdc2ch.core.error.exception.CustomBadRequestException;
import com.sdc2ch.core.security.auth.error.ErrorCode;
import com.sdc2ch.core.utils.PhoneUtils;
import com.sdc2ch.mobile.MobileController;
import com.sdc2ch.mobile.req.TosReq;
import com.sdc2ch.mobile.res.Otp;
import com.sdc2ch.mobile.res.Tos;
import com.sdc2ch.mobile.res.TosTree;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.service.I2ChUserDetailsService;
import com.sdc2ch.service.common.IOptService;
import com.sdc2ch.service.common.ITosHistService;
import com.sdc2ch.service.common.ITosService;
import com.sdc2ch.service.mobile.IMobileAppInfoService;
import com.sdc2ch.token.exceptions.JwtTokenCreatedException;
import com.sdc2ch.token.jwt.domain.JwtTokenImpl;
import com.sdc2ch.web.admin.repo.domain.T_MOBILE_APP_USE_INFO;
import com.sdc2ch.web.admin.repo.domain.T_TOS;
import com.sdc2ch.web.admin.repo.domain.T_TOS_HIST;
import com.sdc2ch.web.service.IMobileAppService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(MobileController.REQUEST_MAPPING + "/user")
public class MobileTosEndpoint extends MobileController {

	
	@Autowired ITosService tosSvc;
	@Autowired IOptService optSvc;
	@Autowired IMobileAppService mobileSvc;
	@Autowired I2ChUserDetailsService driverSvc;
	@Autowired ITosHistService tosHistSvc;
	@Autowired IMobileAppInfoService appInfoSvc;
	
	@Autowired MobileApkEndpoint apkEndpnt;

	@ApiOperation(value = "이용약관정보조회", response = TosTree.class, authorizations = {
			@Authorization(value = "2ch") })
	@GetMapping(value = TOS_CURRENT_PATH)
	public @ResponseBody TosTree tosCurrent() {
		
		Link self = linkTo(MobileTosEndpoint.class).slash(TOS_CURRENT_PATH).withSelfRel();
		Link otp = linkTo(MobileTosEndpoint.class).slash(TOS_OTP_PATH).withRel("otp");
		TosTree tostree = TosTree.builder().tos(tosSvc.findByCurrentTos().stream()
				.map(t -> new Tos(t.getId(), t.getTitle(), t.getContents(), t.getRegType()))
				.collect(Collectors.toList())).build();
		tostree.add(self, otp);
		return tostree;
	}
	@ApiOperation(value = "이용약관정보조회", response = TosTree.class, authorizations = {
			@Authorization(value = "2ch") })
	@GetMapping(value = TOS_CURRENT_PATH + "/{version}")
	public @ResponseBody TosTree tosCurrent2() {
		
		Link self = linkTo(MobileTosEndpoint.class).slash(TOS_CURRENT_PATH).withSelfRel();
		Link otp = linkTo(MobileTosEndpoint.class).slash(TOS_OTP_PATH).withRel("otp");
		TosTree tostree = TosTree.builder().tos(tosSvc.findByCurrentTos().stream()
				.map(t -> new Tos(t.getId(), t.getTitle(), t.getContents(), t.getRegType()))
				.collect(Collectors.toList())).build();
		tostree.add(self, otp);
		return tostree;
	}

	@ApiOperation(value = "약관동의OTP발송", response = Otp.class, authorizations = {
			@Authorization(value = "2ch") })
	@GetMapping(value = TOS_OTP_PATH)
	public @ResponseBody Otp tosOtp(@RequestParam String phoneNo) throws CustomBadRequestException {
		
		Otp otp = new Otp(); 
		Link self = linkTo(MobileTosEndpoint.class).slash(TOS_OTP_PATH).withSelfRel();
		otp.add(self);
		try {
			checkPhoneNo(phoneNo);
			otp.setOtp(optSvc.generateOtp(phoneNo));
		} catch (ExecutionException e) {
			log.error("{}", e.getMessage());
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "server (" + phoneNo + ")");
		}catch (Exception e) {
			log.error("{}", e.getMessage());
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "server (" + phoneNo + ")");
		}
		log.info("opt -> {}", otp);
		
		return otp;
	}
	
	@ApiOperation(value = "OTP 검증", response = Boolean.class)
	@GetMapping(value = "/tos/otp/verify")
	public @ResponseBody boolean verify(
			@RequestParam(name = "phoneNo") String phoneNo, 
			@RequestParam(name = "otp") String otp) throws CustomBadRequestException {
		
		
		checkPhoneNo(phoneNo);
		
		boolean isExpired = false;
		try {
			isExpired = optSvc.isExpired(phoneNo, otp);
		} catch (ExecutionException | UncheckedExecutionException e) {
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.VERIFY_FAIL, "Expired OTP Code -> [phoneNo:otp] -> " + join(phoneNo, otp));
		}
		return isExpired;
	}
	
	@ApiOperation(value = "이용약관동의등록", response = JwtTokenImpl.class)
    @PostMapping(TOS_SAVE_PATH)
    public @ResponseBody JwtTokenImpl add(@Valid @RequestBody TosReq tos, BindingResult bindingResult) throws CustomBadRequestException, JwtTokenCreatedException {
		
		
		String phoneNo = tos.getPhoneNo();
		
		
		super.verifyData(bindingResult);
		
		this.checkPhoneNo(phoneNo);
		
		if(!this.verify(phoneNo, tos.getOtp())) throw new CustomBadRequestException(HttpStatus.BAD_REQUEST, 
				ErrorCode.VERIFY_FAIL, "Expired OTP Code -> " + join(tos.getPhoneNo(), tos.getOtp()));
		
		String driverCd = driverSvc.findByPhoneNo(phoneNo).orElseThrow(() -> new CustomBadRequestException(HttpStatus.BAD_REQUEST, 
						ErrorCode.NOT_FOUND, "등록되지 않은 운전자 번호입니다. " + join(phoneNo))).getUserDetailsId();
		
		IUser user = userSvc.findByUsername(driverCd).orElseThrow(() -> new CustomBadRequestException(HttpStatus.BAD_REQUEST, 
						ErrorCode.NOT_FOUND, "등록되지 않은 사용자 입니다. " + join(phoneNo)));
		
		





		
		
		
		T_MOBILE_APP_USE_INFO appUseInfo = (T_MOBILE_APP_USE_INFO) mobileSvc.findAppInfoByUser(user).orElse(new T_MOBILE_APP_USE_INFO());
		appUseInfo.setApk(mobileSvc.findApkByVersion(tos.getAppMajorVer(), tos.getAppMinorVer()).get());

		appUseInfo.setAppTkn(tos.getAppTkn());
		appUseInfo.setModel(tos.getModel() + "_V" + tos.getAppMajorVer() +"."+tos.getAppMinorVer());
		appUseInfo.setOsName(tos.getOsNm());
		appUseInfo.setOsVer(tos.getOsVer());
		appUseInfo.setTelCo(tos.getTelCo());
		appUseInfo.setValidTkn(!StringUtils.isEmpty(tos.getAppTkn()));
		appUseInfo.setUserId(driverCd);
		appUseInfo.setTosses(new ArrayList<>());
		;
		
		
		
		tosSvc.findByIds(tos.getTosIds()).forEach(t -> {
			appUseInfo.getTosses().add(t);
		});
		appInfoSvc.save(appUseInfo);
		
		appUseInfo.getTosses().stream().forEach(t -> {
			T_TOS_HIST hist = new T_TOS_HIST(appUseInfo, (T_TOS)t);
			tosHistSvc.save(hist);
		});
		
		JwtTokenImpl token = jwtSvc.createToken(user);
		token.add(linkTo(MobileTosEndpoint.class).slash(TOS_SAVE_PATH).withSelfRel());
        return token;
    }
	
	private String formatNo(String phoneNo) {
		String fmt = phoneNo.startsWith("010") ? "XXX-XXXX-XXXX" : "XXX-XXX-XXXX";
		return PhoneUtils.formateToPhoneNumber(phoneNo, fmt, 13);
	}

	private void checkPhoneNo(String phoneNo) throws CustomBadRequestException {
		Optional.ofNullable(phoneNo)
				.filter(s -> PhoneUtils.validatePhoneNumber(phoneNo.substring(1)))
				.orElseThrow(() -> new CustomBadRequestException(HttpStatus.BAD_REQUEST, 
						ErrorCode.INVALID_DATA, "Invalid phone number (" + phoneNo + ")"));
	}
}
