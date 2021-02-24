package com.sdc2ch.web.admin.endpoint;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sdc2ch.core.error.exception.CustomBadRequestException;
import com.sdc2ch.core.security.auth.error.ErrorCode;
import com.sdc2ch.service.admin.IAnalsDlvyLcTimeService;
import com.sdc2ch.service.admin.IAnalsDlvyStdTimeSerivce;
import com.sdc2ch.service.admin.IAnalsDlvyTimeService;
import com.sdc2ch.service.admin.IAnalsLdngStdTimeSerivce;
import com.sdc2ch.service.admin.IAnalsLdngTimeService;
import com.sdc2ch.service.admin.IAnalsVhcleService;
import com.sdc2ch.service.admin.IDailyDlvyErorSttusService;
import com.sdc2ch.service.admin.ILdngAndDlvySttusService;
import com.sdc2ch.service.admin.INoArvlDlvyLcService;
import com.sdc2ch.service.admin.model.DailyDlvyErorSttusVo;
import com.sdc2ch.service.admin.model.VhcleCntrlVo;
import com.sdc2ch.service.common.IComboBoxService;
import com.sdc2ch.tms.io.TmsLocationIO;
import com.sdc2ch.web.admin.repo.domain.anals.T_ANALS_DLVY_STD_TIME;
import com.sdc2ch.web.admin.repo.domain.anals.T_ANALS_LDNG_STD_TIME;
import com.sdc2ch.web.admin.repo.domain.op.T_LOCATION_INFO_HIST;
import com.sdc2ch.web.admin.repo.domain.sta.V_ANALS_GRADE_SCOPE_HIST;
import com.sdc2ch.web.admin.repo.domain.v.V_REALTIME_INFO;
import com.sdc2ch.web.admin.repo.dto.IAnalsDlvyDtlsDto;
import com.sdc2ch.web.admin.repo.dto.IAnalsDlvyLcRankListDto;
import com.sdc2ch.web.admin.repo.dto.IAnalsDstrbChrtColumnDto;
import com.sdc2ch.web.admin.repo.dto.IAnalsDstrbChrtPieDto;
import com.sdc2ch.web.admin.repo.dto.IAnalsDstrbDto;
import com.sdc2ch.web.admin.repo.dto.IAnalsDstrbHistDto;
import com.sdc2ch.web.admin.repo.dto.IAnalsLdngDtlsDto;
import com.sdc2ch.web.admin.repo.dto.IAnalsRankDto;
import com.sdc2ch.web.admin.repo.dto.INoArvlDlvyLcDto;
import com.sdc2ch.web.admin.req.SearchAnalsReq;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/adm/anals")
public class AdminAnalsEndpoint extends AdminAbstractEndpoint {

	@Autowired IComboBoxService comboSvc;

	@Autowired IAnalsVhcleService analsVhcleSvc;

	@Autowired IAnalsLdngTimeService analsLdngTimeSvc; 
	@Autowired IAnalsDlvyTimeService analsDlvyTimeSvc; 
	@Autowired IAnalsDlvyLcTimeService analsDlvyLcTimeSvc; 

	@Autowired ILdngAndDlvySttusService ldngAndDlvySttusSvc;	
	@Autowired IDailyDlvyErorSttusService dailyDlvyErorSttusSvc;	
	@Autowired INoArvlDlvyLcService noArvlDlvyLcSvc;	

	@Autowired IAnalsLdngStdTimeSerivce analsLdngStdTimeSvc; 
	@Autowired IAnalsDlvyStdTimeSerivce analsDlvyStdTimeSvc; 

	

	@ApiOperation(value = "상차시간분석 > 순위확인 > 그리드", response = IAnalsRankDto.class)
	@PostMapping(value = "/ldngTime/rank/list", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<IAnalsRankDto> searchAnalsLdngTimeRank(@RequestBody SearchAnalsReq param) throws CustomBadRequestException{

		String fctryCd =  param.getFctryCd();
		String fromDe = param.getFromDe();
		String toDe  = param.getToDe();
		String caralcTy  = param.getCaralcTy();
		String vhcleTy  = param.getVhcleTy();
		String routeNo  = param.getRouteNo();
		String wkdayYn = param.getWkdayYn();
		String satYn = param.getSatYn();
		String sunYn= param.getSunYn();
		return analsLdngTimeSvc.searchAnalsLdngTimeRank(fctryCd, fromDe, toDe, caralcTy, vhcleTy, routeNo, wkdayYn, satYn, sunYn);
	}

	@ApiOperation(value = "상차시간분석 > 분포도 분석 > 리스트 데이터(그리드,차트 겸용)", response = IAnalsDstrbDto.class)
	@PostMapping(value = "/ldngTime/dstrb/list", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<IAnalsDstrbDto> searchAnalsLdngTimeList(@RequestBody SearchAnalsReq param) throws CustomBadRequestException{
		String fctryCd = param.getFctryCd();
		String fromDe = param.getFromDe();
		String toDe = param.getToDe();
		String caralcTy = param.getCaralcTy();
		String vhcleTy = param.getVhcleTy();
		String routeNo = param.getRouteNo();
		String wkdayYn = param.getWkdayYn();
		String satYn = param.getSatYn();
		String sunYn = param.getSunYn();
		Long stdTime = param.getStdTime();
		return analsLdngTimeSvc.searchAnalsLdngTimeDstrbList(fctryCd, fromDe, toDe, caralcTy, vhcleTy, routeNo, wkdayYn, satYn, sunYn, stdTime);
	}

	@ApiOperation(value = "상차시간분석 > 분포도 분석 > 변경이력 조회", response = IAnalsDstrbHistDto.class)
	@PostMapping(value = "/ldngTime/dstrb/setupHist", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<IAnalsDstrbHistDto> searchAnalsLdngTimeSetupHist(@RequestBody SearchAnalsReq param) throws CustomBadRequestException{
		String fctryCd = param.getFctryCd();
		String fromDe = param.getFromDe();
		String toDe = param.getToDe();
		String routeNo = param.getRouteNo();
		String caralcTy = param.getCaralcTy();
		String vhcleTy = param.getVhcleTy();
		String wkdayYn = param.getWkdayYn();
		String satYn = param.getSatYn();
		String sunYn = param.getSunYn();
		Long stdTime = param.getStdTime();
		return analsLdngTimeSvc.searchAnalsLdngTimeDstrbSetupHist(fctryCd, fromDe, toDe,  routeNo, caralcTy, vhcleTy, wkdayYn, satYn, sunYn, stdTime);
	}


	@ApiOperation(value = "상차시간분석 > 분포도 분석 > 변경시간 적용")
	@PostMapping(value = "/ldngTime/dstrb/setupSave", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody String saveAnalsLdngTimeSetup(@RequestBody SearchAnalsReq param) throws CustomBadRequestException{
		String fctryCd = param.getFctryCd();
		String caralcTy = param.getCaralcTy();
		String vhcleTy = param.getVhcleTy();
		String routeNo = param.getRouteNo();
		Long stdTime = param.getStdTime();
		Long adjustTime = param.getAdjustTime();


		T_ANALS_LDNG_STD_TIME analsLdngStdTime = analsLdngStdTimeSvc.findAnalsLdngStdTimeDetail(fctryCd, caralcTy, vhcleTy, routeNo);
		try {
			List<T_ANALS_LDNG_STD_TIME> reqList = new ArrayList<T_ANALS_LDNG_STD_TIME>();
			if ( analsLdngStdTime == null ) {
				analsLdngStdTime = new T_ANALS_LDNG_STD_TIME();
				analsLdngStdTime.setId(null);
				analsLdngStdTime.setFctryCd(fctryCd);
				analsLdngStdTime.setVhcleTy(new BigDecimal(vhcleTy).floatValue());
				analsLdngStdTime.setCaralcTy(caralcTy);
				analsLdngStdTime.setRouteNo(routeNo);
				analsLdngStdTime.setAdjustTime(adjustTime);
				analsLdngStdTime.setStdTime(stdTime);
			}

			analsLdngStdTime.setBase(false);	
			analsLdngStdTime.setUseYn(true);	
			analsLdngStdTime.setRegDt(new Date());	
			analsLdngStdTime.setRegUserId(super.getConCurrentUserContext().getUsername());

			analsLdngStdTime.setStdTime(stdTime);	
			reqList.add(analsLdngStdTime);
			analsLdngStdTimeSvc.save(reqList);
		}
		catch (Exception e) {
			e.printStackTrace();
			log.info("{}", e);
			throwException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR, "저장을 실패하였습니다.");
			return "false";
		}

		return "true";
	}




	@ApiOperation(value = "상차시간분석 > 상세조회 > 그리드")
	@PostMapping(value = "/ldngTime/detail/list", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<IAnalsLdngDtlsDto> searchAnalsLdngTimeDetailList(@RequestBody SearchAnalsReq param) throws CustomBadRequestException{
		
		String fctryCd = param.getFctryCd();
		String fromDe = param.getFromDe();
		String toDe = param.getToDe();
		String caralcTy = param.getCaralcTy();
		String vhcleTy = param.getVhcleTy();
		String routeNo = param.getRouteNo();
		String wkdayYn = param.getWkdayYn();
		String satYn = param.getSatYn();
		String sunYn = param.getSunYn();
		return analsLdngTimeSvc.searchAnalsLdngTimeDetailList(fctryCd, fromDe, toDe, caralcTy, vhcleTy, routeNo, wkdayYn, satYn, sunYn);
	}


	

	@ApiOperation(value = "배송시간분석 > 순위확인 > 그리드", response = IAnalsRankDto.class)
	@PostMapping(value = "/dlvyTime/rank/list", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<IAnalsRankDto> searchAnalsDlvyTimeRank(@RequestBody SearchAnalsReq param) throws CustomBadRequestException{
		String fctryCd =  param.getFctryCd();
		String fromDe = param.getFromDe();
		String toDe  = param.getToDe();
		String routeNo  = param.getRouteNo();
		String caralcTy  = param.getCaralcTy();
		String vhcleTy  = param.getVhcleTy();
		String wkdayYn = param.getWkdayYn();
		String satYn = param.getSatYn();
		String sunYn= param.getSunYn();
		return analsDlvyTimeSvc.searchAnalsDlvyTimeRank(fctryCd, fromDe, toDe, routeNo, caralcTy, vhcleTy, wkdayYn, satYn, sunYn);
	}

	@ApiOperation(value = "배송시간분석 > 분포도 분석 > 리스트 데이터(그리드,차트 겸용)", response = IAnalsDstrbDto.class)
	@PostMapping(value = "/dlvyTime/dstrb/list", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<IAnalsDstrbDto> searchAnalsDlvyTimeList(@RequestBody SearchAnalsReq param) throws CustomBadRequestException{
		String fctryCd = param.getFctryCd();
		String fromDe = param.getFromDe();
		String toDe = param.getToDe();
		String routeNo = param.getRouteNo();
		String caralcTy = param.getCaralcTy();
		String vhcleTy = param.getVhcleTy();
		String wkdayYn = param.getWkdayYn();
		String satYn = param.getSatYn();
		String sunYn = param.getSunYn();
		Long stdTime = param.getStdTime();
		return analsDlvyTimeSvc.searchAnalsDlvyTimeDstrbList(fctryCd, fromDe, toDe,  routeNo, caralcTy, vhcleTy, wkdayYn, satYn, sunYn, stdTime);
	}

	@ApiOperation(value = "배송시간분석 > 분포도 분석 > 변경이력 조회", response = IAnalsDstrbHistDto.class)
	@PostMapping(value = "/dlvyTime/dstrb/setupHist", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<IAnalsDstrbHistDto> searchAnalsDlvyTimeSetupHist(@RequestBody SearchAnalsReq param) throws CustomBadRequestException{
		String fctryCd = param.getFctryCd();
		String fromDe = param.getFromDe();
		String toDe = param.getToDe();
		String routeNo = param.getRouteNo();
		String caralcTy = param.getCaralcTy();
		String vhcleTy = param.getVhcleTy();
		String wkdayYn = param.getWkdayYn();
		String satYn = param.getSatYn();
		String sunYn = param.getSunYn();
		Long stdTime = param.getStdTime();
		return analsDlvyTimeSvc.searchAnalsDlvyTimeDstrbSetupHist(fctryCd, fromDe, toDe,  routeNo, caralcTy, vhcleTy, wkdayYn, satYn, sunYn, stdTime);
	}




















	
	@ApiOperation(value = "배송시간분석 > 분포도 분석 > 변경시간 적용")
	@PostMapping(value = "/dlvyTime/dstrb/setupSave", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody String saveAnalsDlvyTimeSetup(@RequestBody SearchAnalsReq param) throws CustomBadRequestException{
		String fctryCd = param.getFctryCd();
		String caralcTy = param.getCaralcTy();
		String vhcleTy = param.getVhcleTy();
		String routeNo = param.getRouteNo();
		Long stdTime = param.getStdTime();
		Long adjustTime = param.getAdjustTime();


		T_ANALS_DLVY_STD_TIME analsDlvyStdTime = analsDlvyStdTimeSvc.findAnalsDlvyStdTimeDetail(fctryCd, routeNo, caralcTy, vhcleTy);
		try {
			List<T_ANALS_DLVY_STD_TIME> reqList = new ArrayList<T_ANALS_DLVY_STD_TIME>();
			if ( analsDlvyStdTime == null ) {
				analsDlvyStdTime = new T_ANALS_DLVY_STD_TIME();
				analsDlvyStdTime.setId(null);
				analsDlvyStdTime.setFctryCd(fctryCd);
				analsDlvyStdTime.setVhcleTy(new BigDecimal(vhcleTy).floatValue());
				analsDlvyStdTime.setCaralcTy(caralcTy);
				analsDlvyStdTime.setRouteNo(routeNo);
				analsDlvyStdTime.setAdjustTime(adjustTime);
				analsDlvyStdTime.setStdTime(stdTime);
			}

			analsDlvyStdTime.setBase(false);	
			analsDlvyStdTime.setUseYn(true);	
			analsDlvyStdTime.setRegDt(new Date());	
			analsDlvyStdTime.setRegUserId(super.getConCurrentUserContext().getUsername());

			analsDlvyStdTime.setStdTime(stdTime);	
			reqList.add(analsDlvyStdTime);
			analsDlvyStdTimeSvc.save(reqList);
		}
		catch (Exception e) {
			e.printStackTrace();
			log.info("{}", e);
			throwException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR, "저장을 실패하였습니다.");
			return "false";
		}

		return "true";
	}

	@ApiOperation(value = "배송시간분석 > 상세조회 > 그리드")
	@PostMapping(value = "/dlvyTime/detail/list", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<IAnalsDlvyDtlsDto> searchAnalsDlvyTimeDetailList(@RequestBody SearchAnalsReq param) throws CustomBadRequestException{
		
		String fctryCd = param.getFctryCd();
		String fromDe = param.getFromDe();
		String toDe = param.getToDe();
		String routeNo = param.getRouteNo();
		String caralcTy = param.getCaralcTy();
		String vhcleTy = param.getVhcleTy();
		String wkdayYn = param.getWkdayYn();
		String satYn = param.getSatYn();
		String sunYn = param.getSunYn();
		return analsDlvyTimeSvc.searchAnalsDlvyTimeDetailList(fctryCd, fromDe, toDe,  routeNo, caralcTy, vhcleTy, wkdayYn, satYn, sunYn);
	}

	
	@ApiOperation(value = "고객센터 분석 > 순위확인 > 리스트 (차트&그리드)")
	@PostMapping(value = "/dlvyLcTime/rank/list", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<IAnalsDlvyLcRankListDto> searchAnalsDlvyLcTimeRankList(@RequestBody SearchAnalsReq param) throws CustomBadRequestException{
		String fctryCd =  param.getFctryCd();
		String fromDe = param.getFromDe();
		String toDe  = param.getToDe();
		String wkdayYn = param.getWkdayYn();
		String satYn = param.getSatYn();
		String sunYn= param.getSunYn();
		return analsDlvyLcTimeSvc.searchAnalsDlvyLcTimeRankList(fctryCd, fromDe, toDe, wkdayYn, satYn, sunYn);
	}

	@ApiOperation(value = "고객센터 분석 > 분포도 분석 > 리스트(차트&그리드)")
	@PostMapping(value = "/dlvyLcTime/dstrb/list", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<IAnalsDstrbDto> searchAnalsDlvyLcTimeDstrbList(@RequestBody SearchAnalsReq param) throws CustomBadRequestException{
		String fctryCd = param.getFctryCd();
		String fromDe = param.getFromDe();
		String toDe = param.getToDe();

		String dlvyLcCd = param.getDlvyLcCd();
		String dlvyLcTime = param.getDlvyLcTime();

		String wkdayYn = param.getWkdayYn();
		String satYn = param.getSatYn();
		String sunYn = param.getSunYn();

		return analsDlvyLcTimeSvc.searchAnalsDlvyLcTimeDstrbList(fctryCd, fromDe, toDe,  dlvyLcCd, dlvyLcTime, wkdayYn, satYn, sunYn);

	}


	
	@ApiOperation(value = "고객센터 분석 > 상세조회 > 상세 그리드", response = IAnalsDstrbDto.class)
	@PostMapping(value = "/dlvyLcTime/detail/list", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<IAnalsDstrbDto> searchAnalsDlvyLcTimeDetailsList(@RequestBody SearchAnalsReq param) throws CustomBadRequestException{
		String fctryCd = param.getFctryCd();
		String fromDe = param.getFromDe();
		String toDe = param.getToDe();

		String dlvyLcCd = param.getDlvyLcCd();
		String dlvyLcNm = param.getDlvyLcNm();

		String wkdayYn = param.getWkdayYn();
		String satYn = param.getSatYn();
		String sunYn = param.getSunYn();

		return analsDlvyLcTimeSvc.searchAnalsDlvyLcTimeDetailsList(fctryCd, fromDe, toDe,  dlvyLcCd, wkdayYn, satYn, sunYn);
	}

	@ApiOperation(value = "분석 > 운행분석 > 상차 및 배송 현황 - 일별", response = V_ANALS_GRADE_SCOPE_HIST.class)
	@PostMapping(value = "/ldngAndDlvySttus/daily/list", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<V_ANALS_GRADE_SCOPE_HIST> listLdngAndDlvySttusDaily(@RequestBody SearchAnalsReq param) throws CustomBadRequestException {

		String dlvyDe = param.getDlvyDe();	
		String fctryCd = param.getFctryCd();
		String routeNo = param.getRouteNo();
		String vrn = param.getVrn();		

		return ldngAndDlvySttusSvc.listLdngAndDlvySttusDaily(dlvyDe, fctryCd, routeNo, vrn);
	}

	@ApiOperation(value = "분석 > 운행분석 > 상차 및 배송 현황 - 차량별", response = V_ANALS_GRADE_SCOPE_HIST.class)
	@PostMapping(value = "/ldngAndDlvySttus/vhcle/list", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<V_ANALS_GRADE_SCOPE_HIST> listLdngAndDlvySttusVhcle(@RequestBody SearchAnalsReq param) throws CustomBadRequestException {

		String dlvyDe = param.getDlvyDe();		
		String fctryCd = param.getFctryCd();	
		String vrn = param.getVrn();			
		String ldngGrad = param.getLdngGrad();	
		String dlvyGrad = param.getDlvyGrad();	

		return ldngAndDlvySttusSvc.listLdngAndDlvySttusVhcle(dlvyDe, fctryCd, vrn, ldngGrad, dlvyGrad);
	}

	@ApiOperation(value = "분석 > 운행분석 > 일별 배송오차 현황", response = DailyDlvyErorSttusVo.class)
	@PostMapping(value = "/dailyDlvyErorSttus/list", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody DailyDlvyErorSttusVo listDailyDlvyErorSttus(@RequestBody SearchAnalsReq param) throws CustomBadRequestException {

		String fctryCd = param.getFctryCd();	
		String fromDe = param.getFromDe();		
		String toDe = param.getToDe();			
		String caralcTy = param.getCaralcTy();	
		String vhcleTy = param.getVhcleTy();	
		String routeNo = param.getRouteNo();	
		String dlvyLcCd = param.getDlvyLcCd();	
		String wkdayYn = param.getWkdayYn();	
		String satYn = param.getSatYn();		
		String sunYn = param.getSunYn();		

		return dailyDlvyErorSttusSvc.listDailyDlvyErorSttus(fctryCd, fromDe, toDe, caralcTy, vhcleTy, routeNo, dlvyLcCd, wkdayYn, satYn, sunYn);
	}

	@ApiOperation(value = "분석 > 운행분석 > 미도착 배송지 분석 > 리스트", response = INoArvlDlvyLcDto.class)
	@PostMapping(value = "/noArvlDlvyLc/list", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<INoArvlDlvyLcDto> listNoArvlDlvyLc(@RequestBody SearchAnalsReq param) throws CustomBadRequestException {

		String fctryCd = param.getFctryCd();	
		String fromDe = param.getFromDe();		
		String toDe = param.getToDe();			
		String caralcTy = param.getCaralcTy();	
		String vhcleTy = param.getVhcleTy();	
		String routeNo = param.getRouteNo();	
		String trnsprtCmpny = param.getTrnsprtCmpny();	
		String vrn = param.getVrn();			

		return noArvlDlvyLcSvc.listNoArvlDlvyLc(fctryCd, fromDe, toDe, caralcTy, vhcleTy, routeNo, trnsprtCmpny, vrn);
	}


	@ApiOperation(value = "분석 > 운행분석 > 미도착 배송지 분석 > 배송지 위치 조회", response = TmsLocationIO.class)
	@PostMapping(value = "/noArvlDlvyLc/select", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody TmsLocationIO selectNoArvlDlvyLc(@RequestBody SearchAnalsReq param) throws CustomBadRequestException {
		String dlvyLcCd = param.getDlvyLcCd();	
		return noArvlDlvyLcSvc.findStopLocation(dlvyLcCd);
	}


	@ApiOperation(value = "분석 > 운행분석 > 미도착 배송지 분석 > 배송지 위치 수정", response = Integer.class)
	@PostMapping(value = "/noArvlDlvyLc/update", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody int updateNoArvlDlvyLc(@RequestBody SearchAnalsReq param) throws CustomBadRequestException {

		String dlvyLcCd = param.getDlvyLcCd();	
		String adres = param.getAdres();		
		String lat = param.getLat();			
		String lng = param.getLng();			

		return noArvlDlvyLcSvc.updateNoArvlDlvyLc(dlvyLcCd, adres, lat, lng);
	}


	@ApiOperation(value = "분석 > 관제 > 실시간 차량관제", response = V_REALTIME_INFO.class)
	@PostMapping(value = "/vhcleCntrl/rt", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<V_REALTIME_INFO> listRtVhcleCntrl(@RequestBody SearchAnalsReq param) throws CustomBadRequestException {

		String fctryCd = param.getFctryCd();	
		String vrn = param.getVrn();	
		String indvdlVhcleYn = param.getIndvdlVhcleYn();	
		String readyYn = param.getReadyYn();	
		String ldngYn = param.getLdngYn();	
		String dlvyYn = param.getDlvyYn();	
		String rtnDriveYn = param.getRtnDriveYn();	
		String comptYn = param.getComptYn();	
		String hvofYn = param.getHvofYn();	
		String arvlDelayRiskYn = param.getArvlDelayRiskYn();	
		String arvlDelayYn = param.getArvlDelayYn();	
		String overCtnuDriveYn = param.getOverCtnuDriveYn();	

		return analsVhcleSvc.searchRtVhcleCntrl(fctryCd, vrn, indvdlVhcleYn, readyYn, ldngYn, dlvyYn, rtnDriveYn, comptYn, hvofYn, arvlDelayRiskYn, arvlDelayYn, overCtnuDriveYn);
	}

	@ApiOperation(value = "분석 > 관제 > 차량관제 이력조회", response = T_LOCATION_INFO_HIST.class)
	@PostMapping(value = "/vhcleCntrl/hist", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<T_LOCATION_INFO_HIST> listHistVhcleCntrl(@RequestBody SearchAnalsReq param) throws CustomBadRequestException {
		return analsVhcleSvc.searchHistVhcleCntrl(super.convertDate(param.getDlvyDe()), param.getVrn());
	}

	@ApiOperation(value = "분석 > 관제 > 차량관제 이력조회", response = T_LOCATION_INFO_HIST.class)
	@PostMapping(value = "/vhcleCntrl/event/hist/list", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<VhcleCntrlVo> listEventHistVhcleCntrl(@RequestBody SearchAnalsReq param) throws CustomBadRequestException {
		return analsVhcleSvc.searchEventHistVhcleCntrl(super.convertDate(param.getDlvyDe()), param.getVrn());
	}

}
