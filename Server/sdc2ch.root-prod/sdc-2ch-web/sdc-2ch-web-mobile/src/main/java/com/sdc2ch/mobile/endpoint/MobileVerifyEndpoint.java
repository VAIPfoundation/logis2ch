package com.sdc2ch.mobile.endpoint;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.NumberUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sdc2ch.core.error.exception.CustomBadRequestException;
import com.sdc2ch.core.security.auth.error.ErrorCode;
import com.sdc2ch.mobile.MobileController;
import com.sdc2ch.mobile.enums.VerifyEnums;
import com.sdc2ch.mobile.res.Me;
import com.sdc2ch.mobile.res.Verify;
import com.sdc2ch.mobile.res.VerifyTree;
import com.sdc2ch.repo.io.TmsDriverIO;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.service.I2ChUserDetailsService;
import com.sdc2ch.service.mobile.IMobilePushService;
import com.sdc2ch.web.service.IMobileAppService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(MobileController.REQUEST_MAPPING)
public class MobileVerifyEndpoint extends MobileController {

	private static final String DEFAULT_TOKEN_PATH = "/default/token";

	@Autowired I2ChUserDetailsService driverSvc;
	@Autowired IMobileAppService mobileSvc;
	@Autowired IMobilePushService mpushSvc;

	@ApiOperation(value = "사용자기본인증토큰", response = String.class)
	@GetMapping(value = DEFAULT_TOKEN_PATH, produces = SUPPORTED_TYPE)
	@ResponseBody
	public Resource<String> defaultAccessToken() {
		Link link = linkTo(MobileVerifyEndpoint.class).slash(DEFAULT_TOKEN_PATH).withSelfRel();
		Resource<String> result = new Resource<String>(getDefaultAccessToken(), link);
		return result;
	}

	@ApiOperation(value = "모바일사용자검증", response = VerifyTree.class)
	@GetMapping(value = "/user/verify/{version}", produces = SUPPORTED_TYPE)
	public @ResponseBody VerifyTree verify(@PathVariable(required = false) String version, HttpServletRequest request) {
		return _verify(version, request);
	}

	@ApiOperation(value = "모바일사용자검증", response = VerifyTree.class)
	@GetMapping(value = "/user/verify", produces = SUPPORTED_TYPE)
	public @ResponseBody VerifyTree verify2(HttpServletRequest request) {
		return _verify(null, request);
	}

	private VerifyTree _verify(String version, HttpServletRequest request) {
		System.out.println(version);
		VerifyTree verifyMap = VerifyTree.builder().build();
		IUser user = super.getConCurrentUser().orElse(null);
		verifyMap.setVerifys(Arrays.asList(VerifyEnums.values()).stream().map(e -> create(e, user, verifyMap, request, version)).collect(toList()));
		return verifyMap;
	}


	@ApiOperation(value = "사용자정보", response = Me.class)
	@PreAuthorize("hasAuthority('DRIVER')")
	@GetMapping(value = "/user/me", produces = SUPPORTED_TYPE)
	public @ResponseBody Me me(HttpServletRequest request) throws CustomBadRequestException {
		IUser user  = super.getConCurrentUser().orElseThrow(() -> new CustomBadRequestException(HttpStatus.UNAUTHORIZED, ErrorCode.AUTHENTICATION, "User not Found"));
		TmsDriverIO driver = (TmsDriverIO) user.getUserDetails();
		return Me.of(user.getUserDetails().name(), driver.getFctryCd(), driver.getCar().getVrn());	

	}

	private Verify create(VerifyEnums verify, IUser user, VerifyTree verifyMap, HttpServletRequest request, String version) {

		Verify response = new Verify();
		response.setName(verify.getNm());
		response.setType(verify);

		switch (verify) {
		case ACL:
			response.setVerify(checkUser(user) && driverSvc.isInDriver(user));
			
			response.setException(true);
			response.setMessage("접근권한이 없습니다. 관리자에게 문의하세요.");
			break;
		case APP_TOKEN:
			response.setVerify(checkUser(user) && checkToken(request));
			verifyMap.add(linkTo(MobileTokenEndpoint.class).slash("appToken").withRel(verify.name()));
			break;
		case APP_VER:
			
			if(StringUtils.isEmpty(version)) {
				response.setVerify(false);
			}else {
				String appversion = mobileSvc.getCurAppVersion(user);
				if(version.equals(appversion)) {
					response.setVerify(mobileSvc.isCurrentApkVersion(version));
				}
				if("1.31".equals(version)) {
					response.setVerify(true);
				} else if("1.41".equals(version)) {
					response.setVerify(true);
				}
				
				if(version.indexOf("\\.") != -1) {
					try {
						Integer minorVersion = Integer.valueOf(version.substring(version.indexOf(".")+1));
						if ( minorVersion != null && minorVersion >= 100 ) {
							response.setVerify(true);
						}
					}
					catch(Exception e) {
						e.printStackTrace();
						log.error("{}", e);
						response.setVerify(false);
					}
				}
			}
			
			



			verifyMap.add(linkTo(MobileVerifyEndpoint.class).withSelfRel());
			break;
		case AUTH_TOKEN:
			response.setVerify(super.checkToken(request));
			verifyMap.add(linkTo(MobileTokenEndpoint.class).slash("/authToken").withRel(verify.name()));
			break;
		case PUSH_TOKEN:
			response.setVerify(checkUser(user) && mpushSvc.isPushTkn(user));

			break;
		case TOS:

			if(StringUtils.isEmpty(version)) {
				response.setVerify(checkUser(user) && mobileSvc.isTosVer(user));
			}else {
				String appversion = mobileSvc.getCurAppVersion(user);
				if(!StringUtils.isEmpty(appversion) && appversion.equals(version)) {
					response.setVerify(true);
				}else {
					response.setVerify(false);
				}
			}

			verifyMap.add(linkTo(MobileTosEndpoint.class).slash(TOS_CURRENT_PATH).withRel(verify.name()));
			break;
		case USER:
			response.setVerify(checkUser(user));
			response.setException(true);
			response.setMessage("등록되지 않은 사용자 입니다. 사용자가 맞으면 관리자에게 문의하세요.");
			break;
		default:
			break;
		}
		return response;
	}

	private boolean checkUser(IUser user) {
		return !ObjectUtils.isEmpty(user);
	}
}
