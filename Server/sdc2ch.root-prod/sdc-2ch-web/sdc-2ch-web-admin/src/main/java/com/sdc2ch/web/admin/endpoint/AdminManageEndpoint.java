package com.sdc2ch.web.admin.endpoint;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.TransactionException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sdc2ch.core.error.exception.CustomBadRequestException;
import com.sdc2ch.core.security.auth.error.ErrorCode;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.enums.SetupLcType;
import com.sdc2ch.service.admin.IAlarmSetupService;
import com.sdc2ch.service.admin.IAnalsDlvyStdTimeSerivce;
import com.sdc2ch.service.admin.IAnalsLdngStdTimeSerivce;
import com.sdc2ch.service.admin.IBconHistService;
import com.sdc2ch.service.admin.IBconMappingService;
import com.sdc2ch.service.admin.ILocationHistService;
import com.sdc2ch.service.admin.IManageMobileAppService;
import com.sdc2ch.service.admin.IManageSensorDataService;
import com.sdc2ch.service.admin.IMobileHealthCheckService;
import com.sdc2ch.service.admin.INfcMappingService;
import com.sdc2ch.service.admin.INfcTagService;
import com.sdc2ch.service.admin.ISysActInfoService;
import com.sdc2ch.service.admin.RoutePathInfoService;
import com.sdc2ch.service.admin.model.AdminShippingStateHistVo;
import com.sdc2ch.service.common.ITosService;
import com.sdc2ch.service.mobile.model.NfcTagHistVo;
import com.sdc2ch.tms.service.ITmsPlanService;
import com.sdc2ch.web.admin.repo.dao.T_EventWebioHistRepository;
import com.sdc2ch.web.admin.repo.domain.T_MOBILE_HEALTH_CHK_HIST;
import com.sdc2ch.web.admin.repo.domain.T_TOS;
import com.sdc2ch.web.admin.repo.domain.alloc.T_ALARM_SETUP;
import com.sdc2ch.web.admin.repo.domain.alloc.type.AlarmSetupType;
import com.sdc2ch.web.admin.repo.domain.anals.T_ANALS_DLVY_STD_TIME;
import com.sdc2ch.web.admin.repo.domain.anals.T_ANALS_DLVY_STD_TIME_HIST;
import com.sdc2ch.web.admin.repo.domain.anals.T_ANALS_LDNG_STD_TIME;
import com.sdc2ch.web.admin.repo.domain.anals.T_ANALS_LDNG_STD_TIME_HIST;
import com.sdc2ch.web.admin.repo.domain.op.T_BCON_HIST;
import com.sdc2ch.web.admin.repo.domain.op.T_BCON_MAPPING;
import com.sdc2ch.web.admin.repo.domain.op.T_LOCATION_INFO_HIST;
import com.sdc2ch.web.admin.repo.domain.op.T_NFC_MAPPING;
import com.sdc2ch.web.admin.repo.domain.sys.T_SYS_ACT_INFO_HIST;
import com.sdc2ch.web.admin.repo.domain.v.V_ANALS_DLVY_STD_TIME_MSTR;
import com.sdc2ch.web.admin.repo.domain.v.V_ANALS_LDNG_STD_TIME_MSTR;
import com.sdc2ch.web.admin.repo.domain.v.V_MOBILE_APP_USE_INFO;
import com.sdc2ch.web.admin.repo.domain.v.V_REALTIME_INFO;
import com.sdc2ch.web.admin.repo.domain.v.V_TOS_HIST;
import com.sdc2ch.web.admin.repo.enums.ToSRegEnums;
import com.sdc2ch.web.admin.req.AlarmSetupReq;
import com.sdc2ch.web.admin.req.AnalsDlvyStdTimeReq;
import com.sdc2ch.web.admin.req.AnalsLdngStdTimeReq;
import com.sdc2ch.web.admin.req.BconMappingReq;
import com.sdc2ch.web.admin.req.NfcMappingReq;
import com.sdc2ch.web.admin.req.SearchManageReq;
import com.sdc2ch.web.admin.req.SortableReq;
import com.sdc2ch.web.admin.req.TosRequest;
import com.sdc2ch.web.admin.res.HealthCheckRes;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/adm/manage")
public class AdminManageEndpoint extends AdminAbstractEndpoint {

	@Autowired IManageMobileAppService manageMobileAppSvc;	
	@Autowired IManageSensorDataService manageSensorDataSvc;	
	@Autowired RoutePathInfoService routePathInfoService;	
	@Autowired ITmsPlanService tmsService;
	@Autowired IAnalsLdngStdTimeSerivce analsLdngStdTimeSvc; 
	@Autowired IAnalsDlvyStdTimeSerivce analsDlvyStdTimeSvc; 
	@Autowired ILocationHistService locationHistSvc; 
	@Autowired IMobileHealthCheckService mobileHealthChkSvc; 
	@Autowired ISysActInfoService sysActInfoSvc; 
	@Autowired IAlarmSetupService alarmSetupSvc; 
	@Autowired INfcMappingService nfcMappingSvc; 
	@Autowired INfcTagService nfcTagSvc; 

	@Autowired ITosService tosSvc; 

	@Autowired IBconMappingService bconMappingSvc; 
	@Autowired IBconHistService bconHistSvc; 

	@Autowired T_EventWebioHistRepository evtWebIoRepo; 

	@ApiOperation(value = "서버 헬스체크", response = HealthCheckRes.class)
	@PostMapping(value = "/healthCheck", produces = SUPPORTED_TYPE)
	public @ResponseBody HealthCheckRes chkServerHealth() {
		try {
			evtWebIoRepo.count();
		} catch (TransactionException e) {
				e.printStackTrace();
				log.error("{}", e);
				return new HealthCheckRes(false, e.getMessage(), sysActInfoSvc.getAppId(), sysActInfoSvc.getAppVersion());
		}
		return new HealthCheckRes(true, null,  sysActInfoSvc.getAppId(), sysActInfoSvc.getAppVersion());
	}



	@ApiOperation(value = "운영 > 단말관리 > 모바일APP관리", response = V_REALTIME_INFO.class)
	@PostMapping(value = "/mobileApp/list", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<V_MOBILE_APP_USE_INFO> listMobileApp(@RequestBody SearchManageReq param) throws CustomBadRequestException {

		String fctryCd = param.getFctryCd();	
		String vrn = param.getVrn();	
		String vhcleTy = param.getVhcleTy();	
		String mobileNo = param.getMobileNo();	
		String driverNm = param.getDriverNm();	


		return manageMobileAppSvc.searchMobileApp(fctryCd, vhcleTy, vrn, mobileNo, driverNm);
	}

	@ApiOperation(value = "운영 > 단말관리 > 모바일APP관리 : 약관동의이력", response = V_REALTIME_INFO.class)
	@PostMapping(value = "/mobileAppHist/list", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<V_TOS_HIST> listMobileAppHist(@RequestBody SearchManageReq param) throws CustomBadRequestException {
		String vrn = param.getVrn();	
		return manageMobileAppSvc.searchMobileAppHist(vrn);
	}



	@ApiOperation(value = "운영 > 이력조회 > 데이터관리 : 이력조회(일별)", response = V_REALTIME_INFO.class)
	@PostMapping(value = "/sensorDataHistDaily/list", produces = SUPPORTED_TYPE)

	public @ResponseBody List<AdminShippingStateHistVo> searchSensorDataHistDaily(@RequestBody SearchManageReq param) throws CustomBadRequestException {
		String fromDe = param.getFromDe();	
		String toDe = param.getToDe();	
		String fctryCd = param.getFctryCd();	
		String vrn = param.getVrn();	
		String driverNm = param.getDriverNm();	


		return manageSensorDataSvc.searchSensorDataHistDaily(super.convertDate(fromDe), super.convertDate(toDe), fctryCd, vrn, driverNm);
	}

	@ApiOperation(value = "운영 > 이력조회 > 데이터관리 : 이력조회(차량별)", response = V_REALTIME_INFO.class)
	@PostMapping(value = "/sensorDataHistVrn/list", produces = SUPPORTED_TYPE)

	public @ResponseBody List<AdminShippingStateHistVo> searchSensorDataHistVrn(@RequestBody SearchManageReq param) throws CustomBadRequestException {
		String fromDe = param.getFromDe();	
		String toDe = param.getToDe();	
		String fctryCd = param.getFctryCd();	
		String vrn = param.getVrn();	
		String driverNm = param.getDriverNm();	

		return manageSensorDataSvc.searchSensorDataHistVrn(super.convertDate(fromDe), super.convertDate(toDe), fctryCd, vrn, driverNm);
	}

	@ApiOperation(value = "운영 > 이력조회 > 데이터관리 : 이력조회(노선별)", response = V_REALTIME_INFO.class)
	@PostMapping(value = "/sensorDataHistRouteNo/list", produces = SUPPORTED_TYPE)

	public @ResponseBody List<AdminShippingStateHistVo> searchSensorDataHistRouteNo(@RequestBody SearchManageReq param) throws CustomBadRequestException {
		String fromDe = param.getFromDe();	
		String toDe = param.getToDe();	
		String fctryCd = param.getFctryCd();	
		String vrn = param.getVrn();	
		String driverNm = param.getDriverNm();	
		String routeNo = param.getRouteNo();	
		return manageSensorDataSvc.searchSensorDataHistRouteNo(super.convertDate(fromDe), super.convertDate(toDe), fctryCd, vrn, driverNm, routeNo);
	}

	@ApiOperation(value = "운영 > 이력조회 > 데이터관리 : 이력조회(배송지별)", response = V_REALTIME_INFO.class)
	@PostMapping(value = "/sensorDataHistDlvyLc/list", produces = SUPPORTED_TYPE)

	public @ResponseBody List<AdminShippingStateHistVo> searchSensorDataHistDlvyLc(@RequestBody SearchManageReq param) throws CustomBadRequestException {
		String fromDe = param.getFromDe();	
		String toDe = param.getToDe();	
		String fctryCd = param.getFctryCd();	
		String vrn = param.getVrn();	
		String driverNm = param.getDriverNm();	
		String routeNo = param.getRouteNo();	

		return manageSensorDataSvc.searchSensorDataHistDlvyLc(super.convertDate(fromDe), super.convertDate(toDe), fctryCd, vrn, driverNm, routeNo);
	}

	@ApiOperation(value = "운영 > 시스템관리 > 상차기준시간 관리 : 상차기준시간 마스터 정보 조회", response=T_ANALS_LDNG_STD_TIME.class,
			notes="param정보:"
					+ "fctryCd(공장코드)(requre = false),"
					+ "caralcTy(배차유형)(requre = false),"
					+ "vhcleTy(차종)(requre = false),"
					)
	@PostMapping(value = "/analsLdngStdTimeMaster/list", produces = SUPPORTED_TYPE)
	public @ResponseBody List<V_ANALS_LDNG_STD_TIME_MSTR> searchAnalsLdngStdTimeMaster(@RequestBody SearchManageReq param) throws CustomBadRequestException {
		
		
		
		return analsLdngStdTimeSvc.searchAnalsLdngStdTimeMaster(param.getFctryCd(), param.getCaralcTy(), param.getVhcleTy());
		}

	@ApiOperation(value = "운영 > 시스템관리 > 상차기준시간 관리 : 상차기준시간 디테일 정보 조회", response=T_ANALS_LDNG_STD_TIME.class,
			notes="param정보:"
					+ "fctryCd(공장코드)(requre = true),"
					+ "caralcTy(배차유형)(requre = true),"
					+ "vhcleTy(차종)(requre = true),"
			)
	@PostMapping(value = "/analsLdngStdTimeDetail/list", produces = SUPPORTED_TYPE)
	public @ResponseBody List<T_ANALS_LDNG_STD_TIME> searchAnalsLdngStdTimeDetail(@RequestBody SearchManageReq param) throws CustomBadRequestException {
		
		
		
		if(StringUtils.isEmpty(param.getFctryCd())) {
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "공장코드는 필수값입니다.");
		}
		if(StringUtils.isEmpty(param.getCaralcTy())) {
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "배차유형은 필수값입니다.");
		}
		if(StringUtils.isEmpty(param.getVhcleTy())) {
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "차종은 필수값입니다.");
		}
		return analsLdngStdTimeSvc.searchAnalsLdngStdTimeDetail(param.getFctryCd(), param.getCaralcTy(), param.getVhcleTy());
	}


	@ApiOperation(value = "운영 > 시스템관리 > 상차기준시간 관리 : 상차기준시간 등록/수정", response=T_ANALS_LDNG_STD_TIME.class	)
	@PostMapping(value = "/analsLdngStdTimeDetail/save", produces = SUPPORTED_TYPE)
	public @ResponseBody List<T_ANALS_LDNG_STD_TIME> saveAnalsLdngStdTime(@RequestBody List<AnalsLdngStdTimeReq> param, BindingResult bindingResult) throws CustomBadRequestException {
		log.info("{}",param);
		super.verifyData(bindingResult);
		try {
			List<T_ANALS_LDNG_STD_TIME> reqList = new ArrayList<T_ANALS_LDNG_STD_TIME>();
			param.stream().forEach(o -> {
				T_ANALS_LDNG_STD_TIME analsLdngStdTime = new T_ANALS_LDNG_STD_TIME();
				analsLdngStdTime.setId(o.getId());
				analsLdngStdTime.setFctryCd(o.getFctryCd());
				analsLdngStdTime.setVhcleTy(nullCheck(o.getVhcleTy()));
				analsLdngStdTime.setCaralcTy(o.getCaralcTy());
				analsLdngStdTime.setRouteNo(o.getRouteNo());
				analsLdngStdTime.setAdjustTime(o.getAdjustTime());
				analsLdngStdTime.setStdTime(o.getStdTime());
				analsLdngStdTime.setBase(o.isBase());
				if ( o.getRouteNo() == null ) {	
					analsLdngStdTime.setUseYn( true );
				} else {
					analsLdngStdTime.setUseYn(o.isUseYn());
				}
				analsLdngStdTime.setRegDt(new Date());
				analsLdngStdTime.setRegUserId(super.getConCurrentUserContext().getUsername());
				reqList.add(analsLdngStdTime);
			});
			return analsLdngStdTimeSvc.save(reqList);
		}
		catch (Exception e) {
			e.printStackTrace();
			log.info("{}", e);
			throwException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR, "저장을 실패하였습니다.");
			return null;
		}
	}


	@ApiOperation(value = "운영 > 시스템관리 > 상차기준시간 관리 : 상차기준시간 이력보기(id)", response=T_ANALS_DLVY_STD_TIME.class,
			notes="param정보 \n\r"
					+ "id(배송기준시간 ID)(requre = true),"
			)
	@PostMapping(value = "/analsLdngStdTimeHist/list", produces = SUPPORTED_TYPE)
	public @ResponseBody List<T_ANALS_LDNG_STD_TIME_HIST> searchAnalsLdngStdTimeHist(@RequestBody SearchManageReq param) throws CustomBadRequestException {
		
		if(param.getId() == null) {
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "ID는 필수값입니다.");
		}
		return analsLdngStdTimeSvc.searchHistByLdngStdTimeId(param.getId());
	}

	@ApiOperation(value = "운영 > 시스템관리 > 상차기준시간 관리 : 상차기준시간 이력보기(fctryCd,caralcTy,vhcleTy,routeNo)", response=T_ANALS_LDNG_STD_TIME.class,
			notes="param정보 \n\r"
					+ "id(상차기준시간 ID)(requre = true),"
			)
	@PostMapping(value = "/analsLdngStdTimeHistParams/list", produces = SUPPORTED_TYPE)
	public @ResponseBody List<T_ANALS_LDNG_STD_TIME_HIST> searchAnalsLdngStdTimeHistByParams(@RequestBody SearchManageReq param) throws CustomBadRequestException {
		
		String fctryCd = param.getFctryCd();
		String caralcTy = param.getCaralcTy();
		String vhcleTy = param.getVhcleTy();
		String routeNo = param.getRouteNo();
		if(StringUtils.isEmpty(fctryCd)) {
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "공장코드는 필수값입니다.");
		} else if(StringUtils.isEmpty(caralcTy)) {
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "배차유형은 필수값입니다.");
		} else if(StringUtils.isEmpty(vhcleTy)) {
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "차종은 필수값입니다.");
		} else if(StringUtils.isEmpty(routeNo)) {
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "노선번호는 필수값입니다.");
		}
		return analsLdngStdTimeSvc.searchHistByLdngStdTimeParams(fctryCd, caralcTy, vhcleTy, routeNo);
	}

	@ApiOperation(value = "운영 > 시스템관리 > 배송기준시간 관리 : 배송기준시간 마스터 정보 조회", response=T_ANALS_DLVY_STD_TIME.class,
			notes="param정보 \n\r"
					+ "fctryCd(공장코드)(requre = false), \n\r"
					+ "routeNo(노선번호)(requre = false), \n\r"
					)
	@PostMapping(value = "/analsDlvyStdTimeMaster/list", produces = SUPPORTED_TYPE)
	public @ResponseBody List<V_ANALS_DLVY_STD_TIME_MSTR> searchAnalsDlvyStdTimeMaster(@RequestBody SearchManageReq param) throws CustomBadRequestException {
		
		
		return analsDlvyStdTimeSvc.searchAnalsDlvyStdTimeMaster(param.getFctryCd(), param.getRouteNo());
	}

	@ApiOperation(value = "운영 > 시스템관리 > 배송기준시간 관리 : 배송기준시간 디테일 정보 조회", response=T_ANALS_DLVY_STD_TIME.class,
			notes="param정보:"
					+ "fctryCd(공장코드)(requre = true),"
					+ "dlvyTy(노선번호)(requre = true),"
			)
	@PostMapping(value = "/analsDlvyStdTimeDetail/list", produces = SUPPORTED_TYPE)
	public @ResponseBody List<T_ANALS_DLVY_STD_TIME> searchAnalsDlvyStdTimeDetail(@RequestBody SearchManageReq param) throws CustomBadRequestException {
		
		

		if(StringUtils.isEmpty(param.getFctryCd())) {
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "공장코드는 필수값입니다.");
		}
		if(StringUtils.isEmpty(param.getRouteNo())) {
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "노선번호는 필수값입니다.");
		}
		return analsDlvyStdTimeSvc.searchAnalsDlvyStdTimeDetail(param.getFctryCd(), param.getRouteNo());
	}

	@ApiOperation(value = "운영 > 시스템관리 > 배송기준시간 관리 : 배송기준시간 등록/수정", response=T_ANALS_DLVY_STD_TIME.class	)
	@PostMapping(value = "/analsDlvyStdTimeDetail/save", produces = SUPPORTED_TYPE)
	public @ResponseBody List<T_ANALS_DLVY_STD_TIME> saveAnalsDlvyStdTime(@RequestBody List<AnalsDlvyStdTimeReq> param, BindingResult bindingResult) throws CustomBadRequestException {
		super.verifyData(bindingResult);
		try {
			List<T_ANALS_DLVY_STD_TIME> reqList = new ArrayList<T_ANALS_DLVY_STD_TIME>();
			param.stream().forEach(o -> {
				T_ANALS_DLVY_STD_TIME analsDlvyStdTime = new T_ANALS_DLVY_STD_TIME();
				analsDlvyStdTime.setId(o.getId());
				analsDlvyStdTime.setFctryCd(o.getFctryCd());
				analsDlvyStdTime.setVhcleTy(nullCheck(o.getVhcleTy()));
				analsDlvyStdTime.setCaralcTy(o.getCaralcTy());
				analsDlvyStdTime.setRouteNo(o.getRouteNo());
				analsDlvyStdTime.setAdjustTime(o.getAdjustTime());
				analsDlvyStdTime.setStdTime(o.getStdTime());
				analsDlvyStdTime.setUseYn(o.isUseYn());
				analsDlvyStdTime.setBase(o.isBase());
				if ( o.getCaralcTy() == null && o.getVhcleTy() == null ) {	
					analsDlvyStdTime.setUseYn( true );
				} else {
					analsDlvyStdTime.setUseYn(o.isUseYn());
				}
				analsDlvyStdTime.setRegDt(new Date());
				analsDlvyStdTime.setRegUserId(super.getConCurrentUserContext().getUsername());
				reqList.add(analsDlvyStdTime);
			});
			return analsDlvyStdTimeSvc.save(reqList);
		}
		catch (Exception e) {
			e.printStackTrace();
			log.info("{}", e);
			throwException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR, "저장을 실패하였습니다.");
			return null;
		}
	}

	private Float nullCheck(String vhcleTy) {
		return StringUtils.isEmpty(vhcleTy) ? null : new BigDecimal(vhcleTy).floatValue();
	}



	@ApiOperation(value = "운영 > 시스템관리 > 배송기준시간 관리 : 배송기준시간 이력보기", response=T_ANALS_DLVY_STD_TIME.class,
			notes="param정보:"
					+ "id(배송기준시간 ID)(require = true),"
			)
	@PostMapping(value = "/analsDlvyStdTimeHist/list", produces = SUPPORTED_TYPE)
	public @ResponseBody List<T_ANALS_DLVY_STD_TIME_HIST> searchAnalsDlvyStdTimeHist(@RequestBody SearchManageReq param) throws CustomBadRequestException {
		
		if(param.getId() == null) {
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "ID는 필수값입니다.");
		}
		return analsDlvyStdTimeSvc.searchHistByDlvyStdTimeId(param.getId());
	}
	
	
	@ApiOperation(value = "운영 > 시스템관리 > 배송기준시간 관리 : 배송기준시간 이력보기(fctryCd,caralcTy,vhcleTy,routeNo)", response=T_ANALS_DLVY_STD_TIME.class,
			notes="param정보 \n\r"
					+ "id(배송기준시간 ID)(requre = true),"
			)
	@PostMapping(value = "/analsDlvyStdTimeHistParams/list", produces = SUPPORTED_TYPE)
	public @ResponseBody List<T_ANALS_DLVY_STD_TIME_HIST> searchAnalsDlvyStdTimeHistByParams(@RequestBody SearchManageReq param) throws CustomBadRequestException {
		
		String fctryCd = param.getFctryCd();
		String caralcTy = param.getCaralcTy();
		String vhcleTy = param.getVhcleTy();
		String routeNo = param.getRouteNo();
		if(StringUtils.isEmpty(fctryCd)) {
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "공장코드는 필수값입니다.");
		} else if(StringUtils.isEmpty(caralcTy)) {
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "배차유형은 필수값입니다.");
		} else if(StringUtils.isEmpty(vhcleTy)) {
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "차종은 필수값입니다.");
		} else if(StringUtils.isEmpty(routeNo)) {
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "노선번호는 필수값입니다.");
		}
		return analsDlvyStdTimeSvc.searchHistByDlvyStdTimeParams(fctryCd, caralcTy, vhcleTy, routeNo);
	}


	@ApiOperation(value = "운영 > 이력조회 > GPS 전송이력 : GPS 전송 이력 조회", response=T_LOCATION_INFO_HIST.class,
			notes="param 정보 : "
					+ "fromDt(yyyy-MM-dd HH:mm)(require = true) "
					+ "toDt(yyyy-MM-dd HH:mm)(require = true) "
					+ "fctryCd(공장코드)(require = false) "
					+ "vrn(차량번호)(require = false) "
					+ "gpsYn(GPS 사용여부)(require = false)"
					+ "PageNo(페이지 번호)(default=0)(require = false)"
					+ "PageSize(페이지 번호)(default=200)(require = false)"
					+ "sort(정렬 기준)(default=dataDate.asc)(require = false)"
					)
	@PostMapping(value = "/gpsHist/list", produces = SUPPORTED_TYPE)
	public @ResponseBody Page<T_LOCATION_INFO_HIST> searchPageLocationHist(@RequestBody SearchManageReq param) throws CustomBadRequestException {
		Date fromDt = null;
		Date toDt = null;


		if(StringUtils.isEmpty(param.getFromDt())) {
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "FromDt는 필수값입니다.");
		} else {
			try {
				fromDt = this.getDateFormat("yyyy-MM-dd HH:mm").parse(param.getFromDt());
			} catch (ParseException e) {
				throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "fromDt 날짜 형식(yyyy-MM-dd HH:mm)이 잘못되었습니다.");
			}
		}



		if(StringUtils.isEmpty(param.getToDt())) {
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "ToDt는 필수값입니다.");
		}else {
			try {
				toDt = this.getDateFormat("yyyy-MM-dd HH:mm").parse(param.getToDt());
			} catch (ParseException e) {
				throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "fromDt 날짜 형식(yyyy-MM-dd HH:mm)이 잘못되었습니다.");
			}
		}





		int offset = param.getPageNo() != null ? param.getPageNo() : 0;
		int size = param.getPageSize() != null ? param.getPageSize() : 200;

		Sort sort = this.cvtSort(param.getSort());

		PageRequest pageRequest = PageRequest.of(offset, size, sort);
		return locationHistSvc.searchPageLocationHistByDate(fromDt, toDt, param.getFctryCd(), param.getVrn(), param.getGpsYn(), (Pageable)pageRequest);

	}


	@ApiOperation(value = "운영 > 이력조회 > 모바일 APP 상태이력  : 모바일 APP 상태이력 조회", response=T_MOBILE_HEALTH_CHK_HIST.class,
			notes="param 정보 \n\r "
					+ "fromDt(yyyy-MM-dd HH:mm)(require = true) \n\r"
					+ "toDt(yyyy-MM-dd HH:mm)(require = true) \n\r"
					+ "fctryCd(공장코드)(require = false) \n\r"
					+ "vrn(차량번호)(require = false) \n\r"
					+ "mdn(전화번호)(require = false) \n\r"
					+ "PageNo(페이지 번호)(default=0)(require = false)"
					+ "PageSize(페이지 번호)(default=200)(require = false)"
					+ "sort(정렬 기준)(default=dataDate.asc)(require = false)"
			)
	@PostMapping(value = "/mobileAppSttusHist/list", produces = SUPPORTED_TYPE)
	public @ResponseBody Page<T_MOBILE_HEALTH_CHK_HIST> searchMobileHealthCheckHist(@RequestBody SearchManageReq param) throws CustomBadRequestException{
		Date fromDt = null;
		Date toDt = null;



		if(StringUtils.isEmpty(param.getFromDt())) {
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "FromDt는 필수값입니다.");
		} else {
			try {
				fromDt = this.getDateFormat("yyyy-MM-dd HH:mm").parse(param.getFromDt());
			} catch (ParseException e) {
				throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "fromDt 날짜 형식(yyyy-MM-dd HH:mm)이 잘못되었습니다.");
			}
		}



		if(StringUtils.isEmpty(param.getToDt())) {
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "ToDt는 필수값입니다.");
		}else {
			try {
				toDt = this.getDateFormat("yyyy-MM-dd HH:mm").parse(param.getToDt());
			} catch (ParseException e) {
				throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "fromDt 날짜 형식(yyyy-MM-dd HH:mm)이 잘못되었습니다.");
			}
		}





		int offset = param.getPageNo() != null ? param.getPageNo() : 0;
		int size = param.getPageSize() != null ? param.getPageSize() : 200;

		Sort sort = this.cvtSort(param.getSort());

		PageRequest pageRequest = PageRequest.of(offset, size, sort);
		return mobileHealthChkSvc.searchPageMobileHealthCheckHistByDate(fromDt, toDt, param.getFctryCd(), param.getVrn(), param.getMdn(), pageRequest);

	}

	@ApiOperation(value = "운영 > 이력조회 > 시스템 기동이력  : 시스템 기동이력", response=T_SYS_ACT_INFO_HIST.class,
			notes="param 정보 \n\r"
					+ "fromDt(yyyy-MM-dd HH:mm)(require = false), \n\r"
					+ "toDt(yyyy-MM-dd HH:mm)(require = false), \n\r"
					+ "appId(require = false), \n\r"
					+ "appName(require = false), \n\r"
					+ "appVersion(require = false), \n\r"
					+ "ip(require = false), \n\r"
					+ "host(require = false), \n\r"
					+ "sttus(require = false), \n\r"
					+ "path(require = false), \n\r"
					+ "size(require = false), \n\r"

			)
	@PostMapping(value = "/systemHist/list", produces = SUPPORTED_TYPE)
	public @ResponseBody List<T_SYS_ACT_INFO_HIST> searchSysActInfoHist(@RequestBody SearchManageReq param) throws CustomBadRequestException{
		SimpleDateFormat sdf = this.getDateFormat("yyyy-MM-dd HH:mm");
		Date fromDt = null;
		Date toDt = null;

		if (!StringUtils.isEmpty(param.getFromDt())) {
			try {
				fromDt = sdf.parse(param.getFromDt());
			} catch (ParseException e) {
				throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "fromDt 날짜 형식(yyyy-MM-dd HH:mm)이 잘못되었습니다.");
			}
		}

		if (!StringUtils.isEmpty(param.getToDt())) {
			try {
				toDt = sdf.parse(param.getToDt());
			} catch (ParseException e) {
				throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "toDt 날짜 형식(yyyy-MM-dd HH:mm)이 잘못되었습니다.");
			}
		}

		return sysActInfoSvc.searchSysActInfoHistByAll(fromDt, toDt, param.getAppId(), param.getAppName(), param.getAppVersion(), param.getIp(), param.getHost(), param.getSttus(), param.getPath(), param.getSize());
	}

	@ApiOperation(value = "운영 > 시스템관리 > 알람설정 : 알람설정 조회 ", response=T_ALARM_SETUP.class,
			notes="param 정보 \n\r"
					+ "fctryCd(require = true), \n\r"
					+ "alarmType(require = false), \n\r"
					+ "alarmValuet(require = false), \n\r"

			)
	@PostMapping(value = "/alarmSetup/list", produces = SUPPORTED_TYPE)
	public @ResponseBody List<T_ALARM_SETUP> searchalarmSetupByAll(@RequestBody SearchManageReq param) throws CustomBadRequestException{
		if(StringUtils.isEmpty(param.getFctryCd())) {
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "공장코드는 필수값입니다.");
		}

		return alarmSetupSvc.searchAlarmSetupByAll(param.getFctryCd(), param.getAlarmType(), param.getAlarmValue());

	}

	@ApiOperation(value = "운영 > 시스템관리 > 알람설정 : 알람설정 저장 ", response=T_ALARM_SETUP.class,
			notes="param 정보 \n\r"
					+ "id(require=fase) \n\r"
					+ "fctryCd(require=true) \n\r"
					+ "setupTy(require=true) \n\r"
					+ "value(require=true) \n\r"

			)
	@PostMapping(value = "/alarmSetup/save", produces = SUPPORTED_TYPE)
	public @ResponseBody List<T_ALARM_SETUP> savealarmSetup(@Valid @RequestBody List<AlarmSetupReq> req, BindingResult bindingResult) throws CustomBadRequestException{
		super.verifyData(bindingResult);
		List<T_ALARM_SETUP> entityList = new ArrayList<T_ALARM_SETUP>(req.size());
		req.stream().forEach(o -> {

			entityList.add(cvtAlarmSetup(o.getId(), o.getFctryCd(), o.getSetupTy(), o.getValue()));
		});
		try {
			return alarmSetupSvc.saveAlarmSetup(entityList);
		} catch (DataAccessException e) {
			log.error("{}", e);
			throwException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR,
					e.getRootCause().getLocalizedMessage());
		} catch (Exception e) {
			log.error("{}", e);
			throwException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
		}
		return null;
	}

	private T_ALARM_SETUP cvtAlarmSetup(Long id, String fctryCd, AlarmSetupType setupTy, String value) {
		T_ALARM_SETUP entity = new T_ALARM_SETUP();
		entity.setId(id);
		entity.setFctryCd(fctryCd);
		entity.setSetupTy(setupTy);
		entity.setValue(value);
		return entity;
	}


	@ApiOperation(value = "운영 > 단말관리 > NFC단말 맵핑 관리 : NFC단말 맵핑 관리 조회 ", response=T_NFC_MAPPING.class,
			notes="param 정보 \n\r"
					+ "fctryCd(require=false) \n\r"
			)
	@PostMapping(value = "/nfc/list", produces = SUPPORTED_TYPE)
	public @ResponseBody List<T_NFC_MAPPING> searchNfcMapping(@RequestBody SearchManageReq req){
		return nfcMappingSvc.searchNfcMappingByFctryCd(req.getFctryCd());
	}

	@ApiOperation(value = "운영 > 단말관리 > NFC단말 맵핑 관리 : NFC단말 맵핑 관리 저장 ", response=NfcMappingReq.class,
			notes="param 정보 \n\r"
					+ "fctryCd(require=false) \n\r"

			)
	@PostMapping(value = "/nfc/save", produces = SUPPORTED_TYPE)
	public @ResponseBody List<T_NFC_MAPPING> saveNfcMapping(@Valid @RequestBody List<NfcMappingReq> req, BindingResult bindingResult) throws CustomBadRequestException{
		super.verifyData(bindingResult);
		List<T_NFC_MAPPING> entityList = new ArrayList<T_NFC_MAPPING>(req.size());
		req.stream().forEach(o -> {
			entityList.add(cvtNfcMapping(o.getId(),o.getFctryCd(), o.getNfcId(), o.getNfcName(), o.getSetupLc(), o.getRm()));
		});
		try {
			return nfcMappingSvc.saveNfcMapping(entityList);
		}
		catch(DataAccessException e) {
			log.error("{}", e);
			throwException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR, e.getRootCause().getLocalizedMessage());
		}
		catch(Exception e) {
			log.error("{}", e);
			throwException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
		}
		return null;
	}

	private T_NFC_MAPPING cvtNfcMapping(Long id, String fctryCd, Integer nfcId, String nfcName, SetupLcType setupLc, String rm) {
		T_NFC_MAPPING entity = new T_NFC_MAPPING();
		entity.setId(id);
		entity.setFctryCd(fctryCd);
		entity.setNfcId(nfcId);
		entity.setNfcName(nfcName);
		entity.setSetupLc(setupLc);
		entity.setRm(rm);
		return entity;
	}

	@ApiOperation(value = "운영 > 이력조회 > NFC 태깅이력 : NFC 태깅이력 조회  ", response=NfcTagHistVo.class,
			notes="param 정보 \n\r"
					+ "fromDt(yyyy-MM-dd HH:mm)(require = true) "
					+ "toDt(yyyy-MM-dd HH:mm)(require = true) "
					+ "fctryCd(공장코드)(require = false) "
					+ "setupLcTy(창고)(require = false)"
					+ "vrn(차량번호)(require = false) "
			)
	@PostMapping(value = "/nfcTagHist/list", produces = SUPPORTED_TYPE)
	public @ResponseBody List<NfcTagHistVo> searchNfcTagHist(@RequestBody SearchManageReq req) throws CustomBadRequestException{
		
		String dtFormat = "yyyy-MM-dd HH:mm";
		Date fromDt = null;
		Date toDt = null;

		if (!StringUtils.isEmpty(req.getFromDt())) {
			try {
				fromDt = this.getDateFormat(dtFormat).parse(req.getFromDt());
			} catch (ParseException e) {
				throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "fromDt 날짜 형식(yyyy-MM-dd HH:mm)이 잘못되었습니다.");
			}
		}else {
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "fromDt는 필수값입니다.");
		}


		if (!StringUtils.isEmpty(req.getToDt())) {
			try {
				toDt = this.getDateFormat(dtFormat).parse(req.getToDt());
			} catch (ParseException e) {
				throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "toDt 날짜 형식(yyyy-MM-dd HH:mm)이 잘못되었습니다.");
			}
		}
		else {
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "toDt는 필수값입니다.");
		}

		
		LocalDateTime ldt = LocalDateTime.ofInstant(fromDt.toInstant(), ZoneId.systemDefault());
		Integer yyyyMM = (ldt.getYear() * 100)+ldt.getMonthValue();

		SetupLcType setupLcTy = null;
		if(!StringUtils.isEmpty(req.getSetupLcTy())) {
			setupLcTy = SetupLcType.valueOf(req.getSetupLcTy());
		}



		return nfcTagSvc.searchTagHistfindAll(yyyyMM, fromDt, toDt, req.getFctryCd(), setupLcTy, req.getVrn());
	}

	@ApiOperation(value = "운영 > 단말관리 > 모바일App관리 -> 약관관리 : 약관 조회", response=T_TOS.class,
			notes="param 정보 \n\r"
					+ "tosRegType(약관구분)(require = false) "
			)
	@PostMapping(value = "/tos/list", produces = SUPPORTED_TYPE)
	public @ResponseBody List<T_TOS> searchTos(@RequestBody SearchManageReq req) throws CustomBadRequestException{
		ToSRegEnums tosRegTy = null;
		if(!StringUtils.isEmpty(req.getTosRegType())) {
			tosRegTy = ToSRegEnums.valueOf(req.getTosRegType());
		}

		return tosSvc.findByRegType(tosRegTy);
	}

	@ApiOperation(value = "운영 > 단말관리 > 모바일App관리 -> 약관관리 : 약관 저장", response=T_TOS.class,
			notes="param 정보 \n\r"
					+ "id(id)(require = false)\n\r"
					+ "title(제목)(require = true)\n\r"
					+ "contents(내용)(require = true)\n\r"
					+ "regType(약관구분)(require = true)\n\r"
					+ "major(메이저 버전)(require = flase)\n\r"
					+"※ 수정은 지원하지 않습니다.."
			)
	@PostMapping(value = "/tos/save", produces = SUPPORTED_TYPE)
	public @ResponseBody List<T_TOS> saveTos(@Valid @RequestBody List<TosRequest> req, BindingResult bindingResult) throws CustomBadRequestException{
		super.verifyData(bindingResult);
		IUser user = this.getConCurrentUser().get();
		try {
			return req.stream().map(o->{return tosSvc.save(o.getId(), o.getTitle(), o.getContents(), o.getRegType(), o.getMajor(), user.getUsername());}).collect(Collectors.toList());
		}
		catch (DataAccessException e) {
			log.error("{}", e);
			throwException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR,
					e.getRootCause().getLocalizedMessage());
		} catch (Exception e) {
			log.error("{}", e);
			throwException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
		}
		return null;
	}


	@ApiOperation(value = "운영 > 단말관리 > 비콘관리 : 비콘 맵핑 조회  ", response=T_BCON_MAPPING.class,
			notes="param 정보 \n\r"
					+ "fctryCd(공장코드)(require = false) "
			)
	@PostMapping(value = "/bcon/list", produces = SUPPORTED_TYPE)
	public @ResponseBody List<T_BCON_MAPPING> searchBconMapping(@RequestBody SearchManageReq req) throws CustomBadRequestException{

		return bconMappingSvc.searchBconMapping(req.getFctryCd());
	}

	@ApiOperation(value = "운영 > 단말관리 > 비콘관리 : 비콘 맵핑 저장 ", response=T_BCON_MAPPING.class,
			notes="param 정보 \n\r"
					+ "id(공장코드)(require = false) "
					+ "setupLc(설치 위치 코드)(require = true) "
					+ "fctryCd(공장코드)(require = true) "
					+ "bconId(비콘번호)(require = true) "
					+ "bconName(비콘이름)(require = true) "
			)
	@PostMapping(value = "/bcon/save", produces = SUPPORTED_TYPE)
	public @ResponseBody List<T_BCON_MAPPING> saveBconMapping(@Valid @RequestBody List<BconMappingReq> req,
			BindingResult bindingResult) throws CustomBadRequestException {
		super.verifyData(bindingResult);
		try {
			return bconMappingSvc.saveBconMapping(req.stream().map(o -> { return this.cvtBconMapping(o.getId(), o.getFctryCd(), o.getSetupLc(), o.getBconId(), o.getBconName()); }).collect(Collectors.toList()));
		} catch (DataAccessException e) {
			log.error("{}", e);
			throwException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR,
					e.getRootCause().getLocalizedMessage());
		} catch (Exception e) {
			log.error("{}", e);
			throwException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
		}
		return null;
	}

	private T_BCON_MAPPING cvtBconMapping(Long id, String fctryCd, SetupLcType setupLc, String bconId, String bconName) {
		T_BCON_MAPPING entity = new T_BCON_MAPPING();
		entity.setId(id);
		entity.setFctryCd(fctryCd);
		entity.setSetupLc(setupLc);
		entity.setBconId(bconId);
		entity.setBconName(bconName);
		return entity;
	}

	@ApiOperation(value = "운영 > 이력관리 > 비콘 전송 이력 : 비콘 전송이력 조회  ", response=T_BCON_MAPPING.class,
			notes="param 정보 \n\r"
					+ "fromDt(yyyy-MM-dd HH:mm)(require = true) \n\r"
					+ "toDt(yyyy-MM-dd HH:mm)(require = true) \n\r"
					+ "fctryCd(공장코드)(require = false) \n\r"
					+ "setupLcTy(창고)(require = false) \n\r"
					+ "vrn(차량번호)(require = false) \n\r"
					+ "PageNo(페이지 번호)(default=0)(require = false) \n\r"
					+ "PageSize(페이지 번호)(default=200)(require = false) \n\r"
					+ "sort(정렬 기준)(default=dataDt.asc)(require = false) \n\r"

			)
	@PostMapping(value = "/bconHist/list", produces = SUPPORTED_TYPE)
	public @ResponseBody Page<T_BCON_HIST> searchBconHist(@RequestBody SearchManageReq param) throws CustomBadRequestException{
		String dtFormat = "yyyy-MM-dd HH:mm";
		Date fromDt = null;
		Date toDt = null;

		if(StringUtils.isEmpty(param.getFromDt())) { 
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "FromDt는 필수값입니다.");
		} else {
			try {
				fromDt = this.getDateFormat(dtFormat).parse(param.getFromDt());
			} catch (ParseException e) {
				throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "fromDt 날짜 형식(yyyy-MM-dd HH:mm)이 잘못되었습니다.");
			}
		}

		if(StringUtils.isEmpty(param.getToDt())) { 
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "ToDt는 필수값입니다.");
		}else {
			try {
				toDt = this.getDateFormat(dtFormat).parse(param.getToDt());
			} catch (ParseException e) {
				throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "fromDt 날짜 형식(yyyy-MM-dd HH:mm)이 잘못되었습니다.");
			}
		}


		SetupLcType setupLcTy = null;
		if(!StringUtils.isEmpty(param.getSetupLcTy())) {
			setupLcTy = SetupLcType.valueOf(param.getSetupLcTy());
		}

		int offset = param.getPageNo() != null ? param.getPageNo() : 0; 
		int size = param.getPageSize() != null ? param.getPageSize() : 200; 

		
		Sort sort = this.cvtSort(param.getSort());
		PageRequest pageable = PageRequest.of(offset, size, sort); 

		return bconHistSvc.searchPageBconHist(fromDt, toDt, param.getFctryCd(), param.getVrn(), setupLcTy, pageable);
	}

	
	private Sort cvtSort(List<SortableReq> list) {
		Sort sort = null;
		if(list != null && !list.isEmpty()) {
			List<Order> sortList = list.stream().filter(o->!StringUtils.isEmpty(o.getField())).map(o -> {
			if (!StringUtils.isEmpty(o.getDir())) {
				return "desc".equals(o.getDir().toLowerCase()) ? Sort.Order.desc(o.getField())
						: Sort.Order.asc(o.getField());
			} else {
				return Sort.Order.asc(o.getField());
			}
		}).collect(Collectors.toList());
		sort = Sort.by(sortList);
		}
		return sort;
	}

}
