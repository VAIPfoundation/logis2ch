package com.sdc2ch.mobile.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sdc2ch.core.error.exception.CustomBadRequestException;
import com.sdc2ch.core.security.auth.error.ErrorCode;
import com.sdc2ch.mobile.MobileController;
import com.sdc2ch.web.service.IMobileAppService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(MobileController.REQUEST_MAPPING + "/user")
public class MobileApkEndpoint extends MobileController {

	@Autowired IMobileAppService mobileSvc;

	@ApiOperation(value = "APK 검증", response = Boolean.class)
	@GetMapping(value = "/apk/verify")
	public boolean verify(
			@RequestParam(name = "major") int major,
			@RequestParam(name = "minor") int minor) throws CustomBadRequestException {
		return mobileSvc.findApkByVersion(major, minor)
		.orElseThrow(() -> new CustomBadRequestException(
				HttpStatus.BAD_REQUEST,
				ErrorCode.VERIFY_FAIL,
				"Invalid apk version major.minor -> " + join(major, minor)))
		.isCurrent();
	}

	@ApiOperation(value = "WEB 검증", response = Boolean.class)
	@GetMapping(value = "/web/verify")
	public boolean verifyWebSrouces(
			@RequestParam(name = "major") int major,
			@RequestParam(name = "minor") int minor) throws CustomBadRequestException {
		return mobileSvc.findWebByVersion(major, minor)
		.orElseThrow(() -> new CustomBadRequestException(
				HttpStatus.BAD_REQUEST,
				ErrorCode.VERIFY_FAIL,
				"Invalid web version major.minor -> " + join(major, minor)))
		.isCurrent();
	}
}
