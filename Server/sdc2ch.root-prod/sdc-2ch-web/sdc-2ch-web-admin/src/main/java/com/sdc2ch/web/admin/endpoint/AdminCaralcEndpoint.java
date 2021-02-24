package com.sdc2ch.web.admin.endpoint;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sdc2ch.core.error.exception.CustomBadRequestException;
import com.sdc2ch.core.security.auth.error.ErrorCode;
import com.sdc2ch.prcss.ss.IDriveBatchService;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.service.I2ChUserService;
import com.sdc2ch.service.admin.IAlarmSttusService;
import com.sdc2ch.service.admin.IAllocationConfirmGroupService;
import com.sdc2ch.service.admin.IDlvySttusManageService;
import com.sdc2ch.service.admin.IDummyService;
import com.sdc2ch.service.admin.ISnitatChckTableManageService;
import com.sdc2ch.service.admin.IUnstoringManageService;
import com.sdc2ch.service.admin.model.IndvdVhcleVo;
import com.sdc2ch.service.common.exception.ServiceException;
import com.sdc2ch.web.admin.repo.domain.alloc.T_ALARM_STTUS;
import com.sdc2ch.web.admin.repo.domain.alloc.T_CARALC_CNFIRM2;
import com.sdc2ch.web.admin.repo.domain.alloc.T_CARALC_CNFIRM_GROUP2;
import com.sdc2ch.web.admin.repo.domain.alloc.T_SNITAT_CHCK_IF_SUMRY;
import com.sdc2ch.web.admin.repo.domain.alloc.T_SNITAT_CHCK_TABLE;
import com.sdc2ch.web.admin.repo.domain.v.V_ALARM_STTUS;
import com.sdc2ch.web.admin.repo.domain.v.V_CVO_MONITOR;
import com.sdc2ch.web.admin.repo.domain.v.V_SNITAT_CHCK_TABLE_MONTHLY;
import com.sdc2ch.web.admin.repo.domain.v.V_UNSTORING_MANAGE;
import com.sdc2ch.web.admin.req.SearchCaralcReq;
import com.sdc2ch.web.admin.req.SearchReq;
import com.sdc2ch.web.admin.vo.CloseSnitatCheckVo;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/adm/car/alc")
public class AdminCaralcEndpoint extends AdminAbstractEndpoint {


	@Autowired IDummyService dmySvc; 

	@Autowired IUnstoringManageService unstoringManageSvc; 
	@Autowired IDlvySttusManageService dlvySttusManageSvc; 
	@Autowired IAllocationConfirmGroupService confirmGroupSvc;
	@Autowired I2ChUserService userSvc;
	@Autowired ISnitatChckTableManageService snitatChckGroupSvc; 

	@Autowired IAlarmSttusService alarmSttusSvc;

	@Autowired IDriveBatchService batchSvc;
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "용차배차주문 > 조회", response = T_CARALC_CNFIRM2.class)
	@PostMapping(value = "/indvdVhcleOrderRegist/list", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<IndvdVhcleVo> listIndvdVhcleOrderRegist(@RequestBody SearchCaralcReq param) throws CustomBadRequestException {
		String fromDe = param.getFromDe();		
		String toDe = param.getToDe();			
		String fromLcNm = param.getFromLcNm();	
		String toLcNm = param.getToLcNm();		
		String caralcYn = param.getCaralcYn();	

		List<IndvdVhcleVo> result = (List<IndvdVhcleVo>) dmySvc.getDummyList(new IndvdVhcleVo());
		return result;
	}

	@ApiOperation(value = "용차배차주문 > 신규/수정", response = T_CARALC_CNFIRM2.class)
	@PostMapping(value = "/indvdVhcleOrderRegist/save", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<IndvdVhcleVo> saveIndvdVhcleOrderRegist(@RequestBody List<IndvdVhcleVo> ivVo) throws CustomBadRequestException {

		List<IndvdVhcleVo> result = (List<IndvdVhcleVo>) dmySvc.getDummyList(new IndvdVhcleVo());
		return result;
	}

	@ApiOperation(value = "용차배차주문 > 삭제", response = T_CARALC_CNFIRM2.class)
	@PostMapping(value = "/indvdVhcleOrderRegist/delete", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<IndvdVhcleVo> deleteIndvdVhcleOrderRegist(@RequestBody List<IndvdVhcleVo> ivVo) throws CustomBadRequestException {

		List<IndvdVhcleVo> result = (List<IndvdVhcleVo>) dmySvc.getDummyList(new IndvdVhcleVo());
		return result;
	}


	
	@ApiOperation(value = "용차배차 > 조회", response = T_CARALC_CNFIRM2.class)
	@PostMapping(value = "/indvdVhcle/list", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<IndvdVhcleVo> listIndvdVhcle(@RequestBody SearchCaralcReq param) throws CustomBadRequestException {
		String fromDe = param.getFromDe();		
		String toDe = param.getToDe();			
		String fromLcNm = param.getFromLcNm();	
		String toLcNm = param.getToLcNm();		
		String caralcYn = param.getCaralcYn();	
		
		
		List<IndvdVhcleVo> result = (List<IndvdVhcleVo>)dmySvc.getDummyList(new IndvdVhcleVo());
		return result;
	}

	
	@ApiOperation(value = "운행일지 > 운행일지 배치", response = String.class)
	@PostMapping(value = "/drive/diary/batch", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody String driveDiaryBatch(@RequestBody String param) throws CustomBadRequestException {
		return batchSvc.executeBatch(param);
	}

	@ApiOperation(value = "용차배차 > 주문확인", response = T_CARALC_CNFIRM2.class)
	@PostMapping(value = "/indvdVhcle/save", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<IndvdVhcleVo> saveIndvdVhcle(@RequestBody List<IndvdVhcleVo> ivVo) throws CustomBadRequestException {
		
		
		List<IndvdVhcleVo> result = (List<IndvdVhcleVo>) dmySvc.getDummyList(new IndvdVhcleVo());
		return result;
	}

	@ApiOperation(value = "용차배차 > 배차취소", response = T_CARALC_CNFIRM2.class)
	@PostMapping(value = "/indvdVhcle/unmap", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<IndvdVhcleVo> unmapIndvdVhcle(@RequestBody List<IndvdVhcleVo> ivVo) throws CustomBadRequestException {
		
		
		List<IndvdVhcleVo> result = (List<IndvdVhcleVo>) dmySvc.getDummyList(new IndvdVhcleVo());
		return result;
	}



	


	@ApiOperation(value = "배차내역조회2", response = T_CARALC_CNFIRM2.class)
	@PostMapping(value = "/list", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<T_CARALC_CNFIRM_GROUP2> listCarAlcConfirm2(@RequestBody SearchReq param) throws CustomBadRequestException {
		checkParam(param.getDlvyDe(), param.getFctryCd());
		List<T_CARALC_CNFIRM_GROUP2> result = confirmGroupSvc.search2(super.convertDate(param.getDlvyDe()), param.getFctryCd(), param.getCaralcTy(), param.getRouteNo(), param.getVrn(), param.getDcsnYn());
		
		




		return result;
	}


	private void checkParam(String date, String fctryCd) throws CustomBadRequestException {
		String message = "";
		if(!StringUtils.isEmpty(date) && !StringUtils.isEmpty(fctryCd)) {

			try {
				getDateFormat("yyyyMMdd").parse(date);
				return;
			}catch (Exception e) {
				message = e.getMessage();
			}
		}
		throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, message);
	}

	

	@ApiOperation(value = "배차확정알람", response = T_CARALC_CNFIRM_GROUP2.class)
	@PostMapping(value = "/save", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<T_CARALC_CNFIRM_GROUP2> alram2(@RequestBody List<T_CARALC_CNFIRM_GROUP2> caralcVos) throws CustomBadRequestException {
		if(caralcVos != null) {
			List<T_CARALC_CNFIRM_GROUP2> _self = caralcVos.stream()
				.filter(c -> !StringUtils.isEmpty(c.getMobileNo()))
				.map(vo -> {
					if(vo.getId() != null) {
						try {
							unmap2(Arrays.asList(vo));
						} catch (CustomBadRequestException e) {
							
							e.printStackTrace();
						}
					}
					vo.setTrnsmisDt(new Date());
					vo.setTrnsmisUser(getConCurrentUserContext().getUsername());
					return vo;
				}).collect(Collectors.toList());

			
			return confirmGroupSvc.allocateAll2(_self);
		}
		return caralcVos;
	}

	

	@ApiOperation(value = "배차확정취소", response = T_CARALC_CNFIRM_GROUP2.class)
	@PostMapping(value = "/unmap", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<T_CARALC_CNFIRM_GROUP2> unmap2(@RequestBody List<T_CARALC_CNFIRM_GROUP2> caralcVos) throws CustomBadRequestException {
		caralcVos.stream().forEach(group -> {

			group.setTrnsmisDt(null);
			group.setTrnsmisUser(null);
			group.setCnfirmDt(null);
			group.setCnfirmUser(null);
		});
		
		return confirmGroupSvc.cancelAll2(caralcVos);
	}

	@ApiOperation(value = "배차내역동기화", response = T_CARALC_CNFIRM_GROUP2.class)
	@PostMapping(value = "/sync", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public String sync() throws CustomBadRequestException {
		confirmGroupSvc.syncDb();
		return "{}";
	}

	@ApiOperation(value = "출하관리내역조회", response = V_UNSTORING_MANAGE.class)
	@PostMapping(value = "/unstoring/list", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<HashMap<String,Object>> listUnstoringManage(@RequestBody SearchCaralcReq param) throws CustomBadRequestException {

		
		String fromDe = param.getFromDe();		
		String toDe = param.getToDe();			
		String fromTime = param.getFromTime();	
		String toTime = param.getToTime();		
		String fctryCd = param.getFctryCd();	
		String vhcleTy = param.getVhcleTy();	
		String vrn = param.getVrn();			
		String caralcTy = param.getCaralcTy();



		return unstoringManageSvc.search(fctryCd, super.convertDate(fromDe), super.convertDate(toDe), fromTime, toTime, vhcleTy, vrn, caralcTy);

	}

	@ApiOperation(value = "출하관리내역 수정", response = V_UNSTORING_MANAGE.class)
	@PostMapping(value = "/unstoring/save", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody int updateUnstoringManage(@RequestBody List<V_UNSTORING_MANAGE> unstoringVos) throws CustomBadRequestException {

		return unstoringManageSvc.update(unstoringVos);
	}


	
	
	



	@ApiOperation(value = "태깅내역조회", response = V_UNSTORING_MANAGE.class)	
	@PostMapping(value = "/unstoring/tag/list", produces = SUPPORTED_TYPE)						
	@PreAuthorize("hasAuthority('FACTORY')")						
	public @ResponseBody List<HashMap<String,Object>> listTaglist(@RequestBody SearchCaralcReq param) throws CustomBadRequestException {
		String fctryCd = param.getFctryCd();	

		String month = param.getMonth();		
		
	
		return unstoringManageSvc.listTaglist(fctryCd, super.convertDate(month));					
	}						


	

	
	@ApiOperation(value = "용차배차조회 > 조회", response = T_CARALC_CNFIRM2.class)
	@PostMapping(value = "/indvdVhcleSearch/list", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<IndvdVhcleVo> listIndvdVhcleSearch(@RequestBody SearchReq param) throws CustomBadRequestException {


		return null;
	}

	@ApiOperation(value = "용차배차조회 > TMS동기화", response = T_CARALC_CNFIRM2.class)
	@PostMapping(value = "/indvdVhcleSearch/tmsSync", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<IndvdVhcleVo> saveIndvdVhcleSearch(@RequestBody SearchReq param) throws CustomBadRequestException {
		return null;
	}

	@ApiOperation(value = "용차배차조회 > EMS동기화", response = T_CARALC_CNFIRM2.class)
	@PostMapping(value = "/indvdVhcleSearch/emsSync", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<IndvdVhcleVo> unmapIndvdVhcleSearch(@RequestBody SearchReq param) throws CustomBadRequestException {
		return null;
	}



	
	@ApiOperation(value = "출하관리 > 조회", response = T_CARALC_CNFIRM2.class)
	@PostMapping(value = "/unstoringManage/list", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<IndvdVhcleVo> listUnstoringManage(@RequestBody SearchReq param) throws CustomBadRequestException {


		return null;
	}

	@ApiOperation(value = "출하관리 > 수정", response = T_CARALC_CNFIRM2.class)
	@PostMapping(value = "/unstoringManage/update", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<IndvdVhcleVo> saveUnstoringManage(@RequestBody SearchReq param) throws CustomBadRequestException {
		return null;
	}




















	
	@ApiOperation(value = "배송상태관리 > 조회", response = T_CARALC_CNFIRM2.class)
	@PostMapping(value = "/dlvySttusManage/list", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<V_CVO_MONITOR> listDlvySttusManage(@RequestBody SearchCaralcReq param) throws CustomBadRequestException {
		String fctryCd = param.getFctryCd();
		String vhcleTy = param.getVhcleTy();
		String vrn = param.getVrn();

		return dlvySttusManageSvc.search(fctryCd, vhcleTy, vrn);
	}

	
	@ApiOperation(value = "위생점검표관리 리스트", response = T_SNITAT_CHCK_TABLE.class, notes="필수param : year(yyyy), fctryCd \r\n 선택Param : VhcleTy, vrn, driverNm")
	@PostMapping(value = "/snitatChckTableManage/list", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<V_SNITAT_CHCK_TABLE_MONTHLY> listSnitatChckTableManage(@RequestBody SearchCaralcReq param) throws CustomBadRequestException {
		String year = param.getYear();
		String fctryCd = param.getFctryCd();

		
		if(StringUtils.isEmpty(fctryCd)) {
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "fctryCd는 필수값입니다.");
		}

		if(StringUtils.isEmpty(year)) {
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "year는 필수값입니다.");
		}else {
			try {
				getDateFormat("yyyy").parse(year);
			}catch (Exception e) {
				throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "year값의 형식이 맞지 않습니다.");
			}
		}

		String vhcleTy = param.getVhcleTy();
		String vrn = param.getVrn();
		String driverNm = param.getDriverNm();
		return snitatChckGroupSvc.searchMonthlyList(year, fctryCd, vhcleTy, vrn, driverNm);
	}

	@ApiOperation(value = "위생점검표관리 상세정보", response = T_SNITAT_CHCK_TABLE.class, notes="필수param : year(yyyy), month(MM), fctryCd, vrn")
	@PostMapping(value = "/snitatChckTableManage/detail", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<T_SNITAT_CHCK_TABLE> detailSnitatChckTableManage(@RequestBody SearchCaralcReq param) throws CustomBadRequestException {
		String year = param.getYear();
		String month = param.getMonth();
		String fctryCd = param.getFctryCd();
		String vrn = param.getVrn();
		return snitatChckGroupSvc.searchMonthlyDetailList(year, month, fctryCd, vrn);

	}

	@ApiOperation(value = "위생점검 월 마감", response = CloseSnitatCheckVo.class, notes="필수param : year(yyyy), month(MM), fctryCd")
	@PostMapping(value = "/snitatChckTableManage/closeMonth", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody CloseSnitatCheckVo closeMonlySnitatCheck(@RequestBody SearchCaralcReq param) throws CustomBadRequestException {
		IUser user = super.getConCurrentUser().get();
		T_SNITAT_CHCK_IF_SUMRY result;
		CloseSnitatCheckVo vo = new CloseSnitatCheckVo();
		try {
			result = snitatChckGroupSvc.interfaceSnitatCheckSumry2(user.getUsername(), param.getFctryCd(), param.getYear(), param.getMonth(), false);
			vo.setCode(StringUtils.isEmpty(result.getCode()) ? "0" : result.getCode());
			vo.setMsg(StringUtils.isEmpty(result.getMsg()) ? "정상적으로 성공하였습니다." : result.getMsg());
			vo.setUrl(result.getReUrl());
		} catch (ServiceException e) {
			vo.setCode("-1");
			vo.setMsg(e.getMessage());
		}

		return vo;
	}

	@ApiOperation(value = "위생점검 월 마감 Retry", response = CloseSnitatCheckVo.class, notes="필수param : year(yyyy), month(MM), fctryCd")
	@PostMapping(value = "/snitatChckTableManage/retryCloseMonth", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody CloseSnitatCheckVo retryCloseMonlySnitatCheck(@RequestBody SearchCaralcReq param) throws CustomBadRequestException {
		IUser user = super.getConCurrentUser().get();
		T_SNITAT_CHCK_IF_SUMRY result;
		CloseSnitatCheckVo vo = new CloseSnitatCheckVo();
		try {
			result = snitatChckGroupSvc.interfaceSnitatCheckSumry2(user.getUsername(), param.getFctryCd(), param.getYear(), param.getMonth(), true);
			vo.setCode(StringUtils.isEmpty(result.getCode()) ? "0" : result.getCode());
			vo.setMsg(StringUtils.isEmpty(result.getMsg()) ? "정상적으로 성공하였습니다." : result.getMsg());
			vo.setUrl(result.getReUrl());
		} catch (ServiceException e) {
			vo.setCode("-1");
			vo.setMsg(e.getMessage());
		}

		return vo;
	}

	
	@ApiOperation(value = "위생점검표관리 일별 리스트", response = T_SNITAT_CHCK_TABLE.class, notes="필수param : fromDe(yyyymmdd), toDe(yyyymmdd), fctryCd \r\n 선택Param : VhcleTy, vrn, driverNm, keyword")
	@PostMapping(value = "/snitatChckTableManage/daily/list", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<T_SNITAT_CHCK_TABLE> listSnitatChckTableManageDailyList(@RequestBody SearchCaralcReq param) throws CustomBadRequestException {
		String fctryCd = param.getFctryCd();
		String fromDe = param.getFromDe();
		String toDe = param.getToDe();

		
		if(StringUtils.isEmpty(fctryCd)) {
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "공장코드는 필수값입니다.");
		}

		if(StringUtils.isEmpty(fromDe)) {
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "시작일은 필수값입니다.");
		} else if(StringUtils.isEmpty(toDe)) {
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "종료일은 필수값입니다.");
		}else {
			try {
				System.out.println("fromDe" + fromDe);
				System.out.println("toDe" + toDe);
				getDateFormat("yyyymmdd").parse(fromDe);
				getDateFormat("yyyymmdd").parse(toDe);
				fromDe = convertDate(fromDe);
				toDe = convertDate(toDe);
			}catch (Exception e) {
				throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "날짜의 형식이 맞지 않습니다.");
			}
		}

		String vhcleTy = param.getVhcleTy();
		String vrn = param.getVrn();
		String driverNm = param.getDriverNm();
		String keyword = param.getKeyword();
		return snitatChckGroupSvc.searchDailyList(fromDe, toDe, fctryCd, vhcleTy, vrn, driverNm, keyword);
	}


	@ApiOperation(value = "위생점검 월 마감 리스트", response = CloseSnitatCheckVo.class, notes="param : fctryCd, year(yyyy), month(MM)")
	@PostMapping(value = "/snitatChckTableManage/closeMonth/list", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<T_SNITAT_CHCK_IF_SUMRY> closeMonlySnitatCheckList(@RequestBody SearchCaralcReq param) throws CustomBadRequestException {
		List<T_SNITAT_CHCK_IF_SUMRY> result = snitatChckGroupSvc.searchSnitatcheckSumry(param.getFctryCd(), param.getYear(), param.getMonth());

		
		

		return result;
	}


	@ApiOperation(value = "위생점검 월 마감이 안된 목록을 검색하여 해당건을 모두 저장한다.", response = Integer.class, notes="param : fctryCd, year(yyyy), month(MM)")
	@PostMapping(value = "/snitatChckTableManage/bulkInsert", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody int closeMonlySnitatCheckList(@RequestBody SearchReq param) throws CustomBadRequestException {
		return snitatChckGroupSvc.insertAllExcludeDayoffExe(param.getFctryCd(), param.getYear() + param.getMonth());
	}


	@ApiOperation(value = "배차관리 > 알람현황 > 알람현황조회", response = V_ALARM_STTUS.class,
			notes="param 정보 \n\r"
					+ "dlvyDe(배송일자)(requrie=true)\n\r"
					+ "fctryCd(공장)(requrie=false)\n\r"
					+ "caralcTy(배차유형)(requrie=false)\n\r"
					+ "routeNo(노선번호)(requrie=false)\n\r"
					+ "vrn(차량번호)(requrie=false)\n\r"
			)
	@PostMapping(value = "/alarmSttus/list", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<V_ALARM_STTUS> searchAlarmSttusCnt(@RequestBody SearchReq param) throws CustomBadRequestException {
		String _dlvyDe = "";
		if (!StringUtils.isEmpty(param.getDlvyDe())) {
			_dlvyDe = param.getDlvyDe().replace("-", "");
		}else {
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "dlvyDe는 필수 값입니다.");
			return null;
		}


		return alarmSttusSvc.searchViewAlarmSttus(_dlvyDe, param.getFctryCd(), param.getCaralcTy(), param.getVrn());
	}

	@ApiOperation(value = "배차관리 > 알람현황 > 알람현황 > 알람내역 상세조회", response = T_ALARM_STTUS.class,
			notes="param 정보 \n\r"
					+ "dlvyDe(배송일자)(requrie=false) \n\r"
					+ "fctryCd(공장)(requrie=false) \n\r"
					+ "caralcTy(배차유형)(requrie=false) \n\r"
					+ "routeNo(노선번호)(requrie=false) \n\r"
					+ "vrn(차량번호)(requrie=false) \n\r"
			)
	@PostMapping(value = "/alarmSttusHist/list", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<T_ALARM_STTUS> searchAlarmSttus(@RequestBody SearchReq param) throws CustomBadRequestException {
		return alarmSttusSvc.searchAlarmSttus(param.getDlvyDe(), param.getFctryCd(), param.getCaralcTy(), param.getRouteNo(), param.getVrn());
	}

	@ApiOperation(value = "위생점검표 일괄 완료 업데이트", response = Long.class,
	notes="필수param :"
			+ " fromDe(yyyymmdd), toDe(yyyymmdd), fctryCd \r\n"
			+ " 선택Param : VhcleTy, vrn, driverNm, keyword"
			)
	@PostMapping(value = "/snitatChckTableManage/daily/bulkUpdate", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody long updateSnitatChckTableManage(@RequestBody SearchCaralcReq param) throws CustomBadRequestException {
		String fctryCd = param.getFctryCd();
		String fromDe = param.getFromDe();
		String toDe = param.getToDe();

		

		if(StringUtils.isEmpty(fromDe)) {
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "시작일은 필수값입니다.");
		}
		if(StringUtils.isEmpty(toDe)) {
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "종료일은 필수값입니다.");
		}
		if(StringUtils.isEmpty(fctryCd)) {
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "공장코드는 필수값입니다.");
		}

		try {
			getDateFormat("yyyymmdd").parse(fromDe);
			getDateFormat("yyyymmdd").parse(toDe);
			fromDe = convertDate(fromDe);
			toDe = convertDate(toDe);
		} catch (Exception e) {
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, "날짜의 형식이 맞지 않습니다.");
		}

		String vrn = param.getVrn();
		String vhcleTy = param.getVhcleTy();
		String driverNm = param.getDriverNm();
		String keyword = param.getKeyword();
		return snitatChckGroupSvc.updateAllState(fromDe, toDe, fctryCd, vhcleTy, vrn, driverNm, keyword);
	}



}
