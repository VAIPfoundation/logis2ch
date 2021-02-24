package com.sdc2ch.mobile.endpoint;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sdc2ch.ars.enums.SenderType;
import com.sdc2ch.core.error.exception.CustomBadRequestException;
import com.sdc2ch.core.security.auth.error.ErrorCode;
import com.sdc2ch.mobile.MobileController;
import com.sdc2ch.mobile.enums.MobilePGMEnums;
import com.sdc2ch.mobile.req.AllocatedReq;
import com.sdc2ch.mobile.req.EmptyBoxReq;
import com.sdc2ch.mobile.req.EventIOReq;
import com.sdc2ch.mobile.req.FindRouteInfo;
import com.sdc2ch.mobile.req.HaveOffRegReq;
import com.sdc2ch.mobile.req.HaveOffSearchReq;
import com.sdc2ch.mobile.req.SanitationCheckIndexReq;
import com.sdc2ch.mobile.req.SanitationReq;
import com.sdc2ch.mobile.req.SanitationSearchReq;
import com.sdc2ch.mobile.req.StatsReq;
import com.sdc2ch.mobile.req.WashCarReq;
import com.sdc2ch.mobile.res.ArsCall;
import com.sdc2ch.mobile.res.MenuTree;
import com.sdc2ch.mobile.res.MobileMenu;
import com.sdc2ch.mobile.res.SquareBoxRes;
import com.sdc2ch.mobile.res.start.Allocated;
import com.sdc2ch.mobile.res.start.DetailTree;
import com.sdc2ch.mobile.res.start.MenuDetails;
import com.sdc2ch.prcss.ds.vo.ShipStateVo2;
import com.sdc2ch.prcss.eb.io.EmptyboxIO;
import com.sdc2ch.prcss.eb.io.EmptyboxIO.Cause;
import com.sdc2ch.prcss.eb.io.EmptyboxIO.ModifyCause;
import com.sdc2ch.prcss.eb.vo.EmptyboxVo;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.service.common.IMenuService;
import com.sdc2ch.service.common.exception.ServiceException;
import com.sdc2ch.service.mobile.IMobileUserService;
import com.sdc2ch.service.mobile.model.ChkTblRegistScheduleVo;
import com.sdc2ch.service.mobile.model.MobileCaralcInfVo;
import com.sdc2ch.service.mobile.model.MobileCaralcInfVo.MobileCaralcInfVoBuilder;
import com.sdc2ch.service.mobile.model.MobileEbManRouteInfoVo;
import com.sdc2ch.service.mobile.model.MobileHaveOffInfoHstVo;
import com.sdc2ch.service.mobile.model.MobileHaveOffInfoVo;
import com.sdc2ch.service.mobile.model.MobileHaveOffPossibleVo;
import com.sdc2ch.service.mobile.model.MobileHaveOffRouteInfoVo;
import com.sdc2ch.service.mobile.model.MobileHaveOffSummaryInfoVo;
import com.sdc2ch.service.mobile.model.MobileSanitationListInfoVo;
import com.sdc2ch.service.mobile.model.MobileWashCarInfoVo;
import com.sdc2ch.web.admin.repo.dao.V_CarRepository;
import com.sdc2ch.web.admin.repo.domain.T_MENU;
import com.sdc2ch.web.admin.repo.domain.alloc.T_CARALC_CNFIRM_GROUP2;
import com.sdc2ch.web.admin.repo.domain.alloc.T_RTNGUD_UNDTAKE;
import com.sdc2ch.web.admin.repo.domain.alloc.T_SNITAT_CHCK_TABLE;
import com.sdc2ch.web.admin.repo.domain.v.V_STATS_DRIVER_MONTHLY;
import com.sdc2ch.web.admin.repo.domain.v.V_VHCLE;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(MobileController.REQUEST_MAPPING + "/user")
@Slf4j
public class MobileUserEndpoint extends MobileController {

	@Autowired
	IMenuService menuScv;
	@Autowired
	IMobileUserService muserSvc;
	@Autowired
	MobileVerifyEndpoint verifyEndpoint;
	@Autowired
	V_CarRepository carRepo;	

	@ApiOperation(value = "메뉴목록", response = MenuTree.class)
	@GetMapping(value = "/menu/list")
	public @ResponseBody MenuTree menuTree() {
		MenuTree tree = MenuTree.builder().menus(menuScv.mobileMenuList(currentRoleByUser()).stream().map(mu -> {
			MobileMenu menu = MobileMenu.builder().build();
			BeanUtils.copyProperties(mu, menu);
			return menu;
		}).collect(Collectors.toList())).build();
		return menuLink(tree);
	}

	@ApiOperation(value = "배차내역확인", response = MobileCaralcInfVo.class)
	@PreAuthorize("hasAuthority('DRIVER')")
	@PostMapping(value = "/find/car/allocated/details", produces = SUPPORTED_TYPE)
	public @ResponseBody MobileCaralcInfVo carAllocationDetails(@RequestBody(required = false) AllocatedReq req, HttpServletResponse res)
			throws ServiceException, IOException {
		MobileCaralcInfVoBuilder builder = MobileCaralcInfVo.builder();
		String dlvyDe = "";
		if (req != null) {
			dlvyDe = req.getDlvyDe();
		}

		



		MobileCaralcInfVo info = muserSvc.findAllocatedInfoByUserAndDlvyDeV4(super.getConCurrentUser().get(), dlvyDe);

		if(info == null) {
			builder.id(null);
			builder.dlvyDe(dlvyDe);
			builder.rotations(null);
			builder.resultCd(muserSvc.isExeDayOff(super.getConCurrentUser().get(), dlvyDe) ? 2 : 0);
			info = builder.build();
		}

		return info;
	}

	@ApiOperation(value = "배차내역확인", response = MobileCaralcInfVo.class)
	@PreAuthorize("hasAuthority('DRIVER')")
	@PostMapping(value = "/find/car/allocated/details/{allocatedGroupId}", produces = SUPPORTED_TYPE)
	public @ResponseBody MobileCaralcInfVo carAllocationDetails(@PathVariable Long allocatedGroupId)
			throws ServiceException {
		



		return muserSvc.findAllocatedInfoByGroupIdV4(allocatedGroupId);
	}



	@ApiOperation(value = "배차내역확인 > 공장출발", response = MobileCaralcInfVo.class)
	@PreAuthorize("hasAuthority('DRIVER')")
	@PostMapping(value = "/find/car/allocated/details/fctry/start", produces = SUPPORTED_TYPE)
	public @ResponseBody String carAllocationDetailsFctryStart(@Valid @RequestBody EventIOReq req, BindingResult bindingResult) throws ServiceException, CustomBadRequestException {
		super.verifyData(bindingResult);
		String _dlvyDe = "";
		if (!StringUtils.isEmpty(req.getDlvyDe())) {
			_dlvyDe = req.getDlvyDe().replace("-", "");
		}
		if(StringUtils.isEmpty(req.getFctryCd())) {
			IUser user = super.getConCurrentUser().orElse(null);
			
			boolean isTransport = false;
			if(!StringUtils.isEmpty(req.getRouteNo())) {
				isTransport = req.getRouteNo().contains("_");
			}
			
			if(user != null && !isTransport) {
				req.setFctryCd(user.getFctryCd());
			}
			
		}
		return muserSvc.exitFactoryByWeb(super.getConCurrentUser().get(), req.getId(), req.getRouteNo(), _dlvyDe, req.getFctryCd());
	}

	@ApiOperation(value = "배차내역확인 > 공장도착", response = MobileCaralcInfVo.class)
	@PreAuthorize("hasAuthority('DRIVER')")
	@PostMapping(value = "/find/car/allocated/details/fctry/end", produces = SUPPORTED_TYPE)
	public @ResponseBody String carAllocationDetailsFctryEnd(@Valid @RequestBody EventIOReq req, BindingResult bindingResult) throws ServiceException, CustomBadRequestException {
		super.verifyData(bindingResult);
		String _dlvyDe = "";
		if (!StringUtils.isEmpty(req.getDlvyDe())) {
			_dlvyDe = req.getDlvyDe().replace("-", "");
		}
		
		if(StringUtils.isEmpty(req.getFctryCd())) {
			IUser user = super.getConCurrentUser().orElse(null);
			
			boolean isTransport = false;
			if(!StringUtils.isEmpty(req.getRouteNo())) {
				isTransport = req.getRouteNo().contains("_");
			}
			
			if(user != null && !isTransport) {
				req.setFctryCd(user.getFctryCd());
			}
			
		}
		return muserSvc.enterFactoryByWeb(super.getConCurrentUser().get(), req.getId(), req.getRouteNo(),_dlvyDe, req.getFctryCd());
	}



	@ApiOperation(value = "배차내역확정", response = MobileCaralcInfVo.class)
	@PreAuthorize("hasAuthority('DRIVER')")
	@GetMapping(value = "/save/car/allocated/confirm/{allocatedGroupId}", produces = SUPPORTED_TYPE)
	public @ResponseBody Long saveCarAllocatedConfirm(@PathVariable Long allocatedGroupId) throws ServiceException {

		
		IUser user = super.getConCurrentUser().get();


		
		if(allocatedGroupId == -1L) {
			T_CARALC_CNFIRM_GROUP2 group = muserSvc.findLastAllocatedGroupByUser(user).orElse(null);
			if(group == null) {
				return allocatedGroupId;
			}
			return group.getId();
		}


		T_CARALC_CNFIRM_GROUP2 reqAlcGroup =muserSvc.findAllocatedGroupById(allocatedGroupId).orElse(null);
		if(reqAlcGroup == null) {
			return allocatedGroupId;
		}
		LocalDate now = LocalDate.now();
		LocalDate reqDate = LocalDate.parse(reqAlcGroup.getDlvyDe(), DateTimeFormatter.ofPattern("yyyyMMdd"));


















		return (now.isBefore(reqDate) || now.isEqual(reqDate)) ?
				saveCarAllocatedConfirm(reqAlcGroup, allocatedGroupId) :
					saveCarAllocatedConfirm(muserSvc.findLastAllocatedGroupByUser(user).orElse(null), allocatedGroupId);
	}

	private Long saveCarAllocatedConfirm(T_CARALC_CNFIRM_GROUP2 alcGroup, Long reqAllocatedGroupId) {
		if(alcGroup != null) {
			alcGroup.setCnfirmUser(super.getConCurrentUserContext().getUsername());
			alcGroup.setCnfirmDt(new Date());
			muserSvc.saveAllocatedGroup(alcGroup);
			return alcGroup.getId();
		}
		return reqAllocatedGroupId;
	}

	@ApiOperation(value = "현재상태조회", response = DetailTree.class)
	@PostMapping(value = "/current", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('DRIVER')")
	public @ResponseBody DetailTree current(HttpServletRequest request)
			throws CustomBadRequestException, ServiceException {
		IUser u = super.getConCurrentUser().get();
		Map<String, MenuDetails> mapped = makeMemuDetails(u);

		
		Allocated allocated = (com.sdc2ch.mobile.res.start.Allocated) mapped.get("PGM-100");
		DetailTree dt = DetailTree.of(allocated.getAllocatedId(), verifyEndpoint.me(request), mapped);

		
		try {
			if ( dt.getAllocatedGroupId() == null || dt.getAllocatedGroupId() == 0 ) {
				log.info( "{}", dt );
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("{}", e);
		}
		return DetailTree.of(allocated.getAllocatedId(), verifyEndpoint.me(request), mapped);
	}

	@ApiOperation(value = "운행시작보고", response = DetailTree.class)
	@PostMapping(value = "/event/start/drive/{allocatedGroupId}", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('DRIVER')")
	public @ResponseBody DetailTree startDrive(HttpServletRequest request, @PathVariable Long allocatedGroupId)
			throws CustomBadRequestException, ServiceException {

		IUser u = super.getConCurrentUser().get();



		String errormsg = "배차확정 정보가 없어 업무를 시작하실 수 없습니다. 배차확정 알림을 못받으셨다면 관리자에게 '배차내역문자 미수신'에 대한 문의하시길 부탁드립니다.";

		
		
		
		
		
		
		muserSvc.startJob(u, allocatedGroupId);
		
		
		
		
		
		
		
		
		return DetailTree.of(allocatedGroupId, verifyEndpoint.me(request), makeMemuDetails(u));
	}

	
	@ApiOperation(value = "공상자확인", response = EmptyboxIO.class)
	@GetMapping(value = "/save/empty/box/confirm/{allocatedGroupId}", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('DRIVER')")
	public @ResponseBody EmptyboxIO findEmptyBox(@PathVariable Long allocatedGroupId) throws ServiceException {
		EmptyboxIO info = muserSvc.findEmptyBoxById(allocatedGroupId);
		info.setConfirm(true);
		return muserSvc.saveEmptyBox(info);
	}

	@ApiOperation(value = "공상자목록조회")
	@GetMapping(value = "/list/empty/box/{allocatedGroupId}", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('DRIVER')")
	public @ResponseBody List<EmptyboxVo> listEmptyBox(@PathVariable Long allocatedGroupId) {
		return muserSvc.currentEmptyBoxByAllicatedGroupId(allocatedGroupId);
	}

	@ApiOperation(value = "공상자등록")
	@PostMapping(value = "/save/empty/box", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('DRIVER')")
	public @ResponseBody EmptyboxIO saveEmptyBox(@Valid @RequestBody EmptyBoxReq ebreq, BindingResult bindingResult)
			throws CustomBadRequestException {

		
		super.verifyData(bindingResult);

		int total = ebreq.getSquareBoxQty() + ebreq.getTriangleBoxQty() + ebreq.getYodelryBoxQty() + ebreq.getPalletQty();

		
		
		if (total == 0
				&& StringUtils.isEmpty(ebreq.getCause())
				&& StringUtils.isEmpty(ebreq.getModifyCause()) ) {
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.VERIFY_FAIL,
					"수량이 0일경우 반드시 사유가 있어야 합니다. -> " + join(ebreq.getCause(), ebreq.getSquareBoxQty(),
							ebreq.getTriangleBoxQty(), ebreq.getYodelryBoxQty() + ebreq.getPalletQty()));
		}

		
		if (!StringUtils.isEmpty(ebreq.getCause())
				&& com.sdc2ch.prcss.eb.io.EmptyboxIO.Cause.BAD_BOX == Cause.valueOf(ebreq.getCause())) {
			byte[] bin = ebreq.getPicture();
			if (bin == null || bin.length == 0) {
				throwException(HttpStatus.BAD_REQUEST, ErrorCode.VERIFY_FAIL, "사진 데이터가 없습니다. -> ");
			}
		}









		T_CARALC_CNFIRM_GROUP2 group = muserSvc.findAllocatedGroupById(ebreq.getAllocatedGroupId()).orElseThrow(
				() -> new CustomBadRequestException(HttpStatus.BAD_REQUEST, ErrorCode.VERIFY_FAIL, "배차 정보가 없습니다. -> "));

		EmptyboxVo eb = new EmptyboxVo();
		eb.setId(ebreq.getId());
		eb.setCause(StringUtils.isEmpty(ebreq.getCause()) ? null : Cause.valueOf(ebreq.getCause()));
		eb.setModifyCause(StringUtils.isEmpty(ebreq.getModifyCause()) ? null : ModifyCause.valueOf(ebreq.getModifyCause()));
		eb.setConfirm(true);
		eb.setDlvyDe(group.getDlvyDe());
		eb.setStopCd(ebreq.getStopCd());
		eb.setPicture(ebreq.getPicture());
		eb.setRegUser(super.getConCurrentUserContext().getUsername());
		eb.setRouteNo(ebreq.getRouteNo());
		eb.setSquareBoxQty(ebreq.getSquareBoxQty());
		eb.setTriangleBoxQty(ebreq.getTriangleBoxQty());
		eb.setYodelryBoxQty(ebreq.getYodelryBoxQty());
		eb.setPalletQty(ebreq.getPalletQty());


		
		if ( muserSvc.isConfirm(eb) ) {
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.VERIFY_FAIL, "이미 공상자 확정이 되어 수정이 불가능합니다. -> ");
		}

		return muserSvc.saveEmptyBox(eb);
	}

	@ApiOperation(value = "반품확인", response = T_RTNGUD_UNDTAKE.class)
	@PostMapping(value = "/find/returning/goods/confirm/{allocatedGroupId}", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('DRIVER')")
	public @ResponseBody T_RTNGUD_UNDTAKE findReturningGoods(@PathVariable Long allocatedGroupId)
			throws ServiceException {
		T_RTNGUD_UNDTAKE returnGoods = muserSvc.findReturningGoodsById(allocatedGroupId).orElse(null);

		if (returnGoods == null) {
			
		}
		returnGoods.setCnfirmDt(new Date());
		returnGoods.setCnfirmYn(true);
		return muserSvc.saveReturningGoods(returnGoods);
	}

	@ApiOperation(value = "반품목록조회")
	@GetMapping(value = "/list/returning/goods", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('DRIVER')")
	public String listReturningGoods() {

		return "ok";
	}

	@ApiOperation(value = "운행종료보고", response = DetailTree.class)
	@PostMapping(value = "/event/end/drive/{allocatedGroupId}", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('DRIVER')")
	public DetailTree endDrive(HttpServletRequest request, @PathVariable Long allocatedGroupId)
			throws ServiceException, CustomBadRequestException {

		IUser user = super.getConCurrentUser().get();


		log.info("request allocatedGroupId : {} ", allocatedGroupId);

		

		Long currentAlcId = allocatedGroupId;

		log.debug("currentAlcId : {} ", currentAlcId);

		if (!muserSvc.checkEmptyBoxByAllocatedGroupId(currentAlcId)) {
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_REQUEST,
					"미확인 또는 고객센터로부터 회수되지 않은 공상자내역이 있습니다." + " 회수내용이 없더라도 공상자 매뉴에서 0개라도 입력하여 주시기 바랍니다.");
		}


		if (!muserSvc.finishJob(user, currentAlcId)) {
			throwException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_REQUEST,
					"운행종료 중 예상하지 못한 시스템 예외사항이 발생되었습니다. " + "관리자에게 문의하여 주시기 바랍니다.");
		}
		return DetailTree.of(currentAlcId, verifyEndpoint.me(request), makeMemuDetails(user));
	}








	@ApiOperation(value = "휴무신청(SP)")
	@PostMapping(value = "/insert/have/off", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('DRIVER')")
	public List<MobileHaveOffInfoVo> insertHaveOff(@Valid @RequestBody HaveOffRegReq haveoffreq, BindingResult bindingResult)
			throws CustomBadRequestException {

		
		super.verifyData(bindingResult);


		return muserSvc.upsertDayoff(
				haveoffreq.getId(), haveoffreq.getFctryCd(), haveoffreq.getStartDate(), haveoffreq.getEndDate(), haveoffreq.getCarCd(),
				haveoffreq.getType(), haveoffreq.getReason(), haveoffreq.getUnit(), haveoffreq.getBigo(), haveoffreq.getRouteNo(),
				super.getConCurrentUserContext().getUsername()
				);




	}

	@ApiOperation(value = "휴무삭제(SP)")
	@PostMapping(value = "/delete/have/off", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('DRIVER')")
	public MobileHaveOffInfoVo deleteHaveOff(@Valid @RequestBody HaveOffRegReq haveoffreq, BindingResult bindingResult)
			throws CustomBadRequestException {

		
		super.verifyData(bindingResult);

		MobileHaveOffInfoVo res = new MobileHaveOffInfoVo();

		
		LocalDate now = LocalDate.now();
		LocalDate stDt = LocalDate.parse(haveoffreq.getStartDate(), DateTimeFormatter.ofPattern("yyyyMMdd"));


		if(stDt.isBefore(now)) {
			res.setRetCd(-1);
			res.setRetMessage("금일 이전 날짜의 휴무는 삭제가 불가능합니다.");
			return res;
		}

		Object[] params = { haveoffreq.getFctryCd(), haveoffreq.getStartDate(), haveoffreq.getEndDate(), haveoffreq.getCarCd() };

		String procName = "[dbo].[SP_2CH_DAYOFF_DEL]";
		muserSvc.saveStoredProcedureCall(params, procName);
		res.setRetCd(0);
		res.setRetMessage("정상 처리 되었습니다.");
		return res;
	}










	@ApiOperation(value = "휴무신청조회(SP)")
	@PostMapping(value = "/list/have/off", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('DRIVER')")
	public List<MobileHaveOffInfoVo> listHaveOff(@Valid @RequestBody HaveOffSearchReq haveoffreq, BindingResult bindingResult)
			throws CustomBadRequestException {

		
		super.verifyData(bindingResult);

		
		Object[] params = { haveoffreq.getFctryCd(), haveoffreq.getSearchMonth(), haveoffreq.getCarCd()};
		String procName = "[dbo].[SP_2CH_DAYOFF_SEL]";

		
		List<Object[]> rows = new ArrayList<>();






		rows = muserSvc.selectStoredProcedureCall(params, procName);

		
		List<MobileHaveOffInfoVo> result = new ArrayList<>(rows.size());

		if (rows.size()>0) {
			for (Object[] row : rows) {
				MobileHaveOffInfoVo info = new MobileHaveOffInfoVo();

				info.setId((Integer) row[0]);
				info.setConfirm((String) row[1]);
				info.setCarCd((String) row[2]);
				info.setStartDate((String) row[3]);
				info.setEndDate((String) row[4]);
				info.setType((String) row[5]);
				info.setReason((String) row[6]);
				info.setUnit((String) row[7]);
				info.setRouteNo((String) row[8]);
				info.setBigo((String) row[9]);
				info.setRegUserId((String) row[10]);
				info.setRegDateTime((String) row[11]);

				result.add(info);
			}
		}
		return result;
	}

	@ApiOperation(value = "휴무내역조회")
	@PostMapping(value = "/list/have/off/hist", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('DRIVER')")
	public List<MobileHaveOffInfoHstVo> listHaveOffHstInfo(@Valid @RequestBody HaveOffSearchReq haveoffreq, BindingResult bindingResult)
			throws CustomBadRequestException {
		
		super.verifyData(bindingResult);
		
		return muserSvc. findDayOffHstInfo(haveoffreq.getFctryCd(), haveoffreq.getCarCd(), haveoffreq.getSearchMonth());
	}

	@ApiOperation(value = "휴무가능일 내역조회")
	@PostMapping(value = "/list/have/off/possible", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('DRIVER')")
	public List<MobileHaveOffPossibleVo> listHaveOffPossible(@Valid @RequestBody HaveOffSearchReq haveoffreq, BindingResult bindingResult)
			throws CustomBadRequestException {

		
		super.verifyData(bindingResult);
		
		List<MobileHaveOffPossibleVo> result =muserSvc.selectListHaveOffPossible(haveoffreq.getFctryCd(), haveoffreq.getSearchMonth(), haveoffreq.getCarCd());;
		return result;
	}


	@ApiOperation(value = "휴무요약정보조회(SP)")
	@PostMapping(value = "/list/have/off/summary", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('DRIVER')")
	public List<MobileHaveOffSummaryInfoVo> listHaveOffSummary(@Valid @RequestBody HaveOffSearchReq haveoffreq, BindingResult bindingResult)
			throws CustomBadRequestException {

		
		super.verifyData(bindingResult);

		
		Object[] params = { haveoffreq.getFctryCd(), haveoffreq.getSearchMonth(), haveoffreq.getCarCd()};
		String procName = "[dbo].[SP_2CH_DAYOFFINFO_SEL]";

		
		List<Object[]> rows = new ArrayList<>();

		System.out.println("************************");
		for (Object p : params) {
			System.out.println("[" + p + "]");
		}
		System.out.println("************************");
		rows = muserSvc.selectStoredProcedureCall(params, procName);

		
		List<MobileHaveOffSummaryInfoVo> result = new ArrayList<>(rows.size());

		if (rows.size()>0) {
			for (Object[] row : rows) {
				MobileHaveOffSummaryInfoVo info = new MobileHaveOffSummaryInfoVo();

				info.setWeekday(nullSafeDouble(row[0]));
				info.setHoliday(nullSafeDouble(row[1]));
				info.setAbsent(nullSafeDouble(row[2]));
				result.add(info);
			}
		}
		return result;
	}

	private double nullSafeDouble(Object o) {
		if(o == null)
			return 0.0d;
		return (Double) o;
	}



	@ApiOperation(value = "세차등록(SP)")
	@PostMapping(value = "/insert/car/wash", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('DRIVER')")
	public String insertCarWash(@Valid @RequestBody WashCarReq washreq, BindingResult bindingResult)
			throws CustomBadRequestException {

		
		super.verifyData(bindingResult);

		Object[] params = { washreq.getWcomCd(), washreq.getWashDe(), washreq.getCarCd(), super.getConCurrentUserContext().getUsername() };
		String procName = "[dbo].[SP_2CH_WASH_COST_REG]";
		muserSvc.saveStoredProcedureCall(params, procName);
		return "ok";
	}

	@ApiOperation(value = "세차삭제(SP)")
	@PostMapping(value = "/delete/car/wash", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('DRIVER')")
	public String deleteCarWash(@Valid @RequestBody WashCarReq washreq, BindingResult bindingResult)
			throws CustomBadRequestException {

		
		super.verifyData(bindingResult);

		Object[] params = { washreq.getWcomCd(), washreq.getWashDe(), washreq.getCarCd(), super.getConCurrentUserContext().getUsername() };
		String procName = "[dbo].[SP_2CH_WASH_COST_DEL]";
		muserSvc.saveStoredProcedureCall(params, procName);
		return "ok";
	}

	@ApiOperation(value = "세차조회(SP)")
	@PostMapping(value = "/list/car/wash", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('DRIVER')")
	public List<MobileWashCarInfoVo> selectCarWash(@Valid @RequestBody WashCarReq washreq, BindingResult bindingResult)
			throws CustomBadRequestException {

		
		super.verifyData(bindingResult);

		
		Object[] params = { washreq.getWcomCd(), washreq.getWashDe(), washreq.getCarCd(), super.getConCurrentUserContext().getUsername() };
		String procName = "[dbo].[SP_2CH_WASH_COST_SEL]";

		
		List<Object[]> rows = new ArrayList<>();

		rows = muserSvc.selectStoredProcedureCall(params, procName);

		
		List<MobileWashCarInfoVo> result = new ArrayList<>(rows.size());

		if (rows.size()>0) {
			for (Object[] row : rows) {
				MobileWashCarInfoVo info = new MobileWashCarInfoVo();
				info.setWcomCd((String) row[0]);
				info.setWashDe((String) row[1]);
				info.setCarCd((String) row[2]);
				info.setProcState((String) row[3]);
				info.setRegUserId((String) row[4]);
				info.setRegDateTime((String) row[5]);

				result.add(info);
			}
		}

		return result;
	}


	@ApiOperation(value = "운행노선조회-휴무용(SP)")
	@PostMapping(value = "/list/route/have/off", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('DRIVER')")
	public List<MobileHaveOffRouteInfoVo> selectRouteInfoHaveOff(@Valid @RequestBody FindRouteInfo req, BindingResult bindingResult)
			throws CustomBadRequestException {

		
		super.verifyData(bindingResult);

		
		Object[] params = { req.getCarCd() };
		String procName = "[dbo].[SP_2CH_DAYOFF_ROUTEINFO_SEL]";

		
		List<Object[]> rows = new ArrayList<>();

		rows = muserSvc.selectStoredProcedureCall(params, procName);

		
		List<MobileHaveOffRouteInfoVo> result = new ArrayList<>(rows.size());

		if (rows.size()>0) {
			for (Object[] row : rows) {

				MobileHaveOffRouteInfoVo info = new MobileHaveOffRouteInfoVo();

				info.setCenterCd((String)row[0]);
				info.setRouteNo((String)row[1]);
				info.setStartTm((String)row[2]);

				result.add(info);
			}
		}
		return result;
	}


	@ApiOperation(value = "운행노선조회-공상자반용(SP)")
	@PostMapping(value = "/list/route/eb/man", produces = SUPPORTED_TYPE)

	public List<MobileEbManRouteInfoVo> selectRouteInfoHaveOff2(@Valid @RequestBody FindRouteInfo req, BindingResult bindingResult)
			throws CustomBadRequestException {

		
		super.verifyData(bindingResult);

		
		Object[] params = { req.getCenterCd(), req.getCarCd(), req.getDlvyDe()};
		String procName = "[dbo].[SP_2CH_EB_ROUTEINFO_SEL]";

		
		List<Object[]> rows = new ArrayList<>();

		rows = muserSvc.selectStoredProcedureCall(params, procName);

		
		List<MobileEbManRouteInfoVo> result = new ArrayList<>(rows.size());

		if (rows.size()>0) {
			for (Object[] row : rows) {

				MobileEbManRouteInfoVo info = new MobileEbManRouteInfoVo();

				info.setCenterCd((String)row[0]);
				info.setRouteNo((String)row[1]);
				info.setCarCd((String)row[2]);
				info.setStartTm((String)row[3]);

				result.add(info);
			}
		}
		return result;
	}
	@ApiOperation(value = "배송기사 사상자 조회")
	@PostMapping(value = "/list/driver/square/box", produces = SUPPORTED_TYPE)

	public @ResponseBody SquareBoxRes driverSquareBoxQty(@Valid @RequestBody FindRouteInfo req, BindingResult bindingResult)
			throws CustomBadRequestException {
		
		super.verifyData(bindingResult);
		SquareBoxRes res = new SquareBoxRes();
		int qut = muserSvc.squareBoxQty(req.getDlvyDe(), req.getCarCd(), req.getRouteNo());
		res.setSquareBoxQty(qut);
		return res;
	}










	@ApiOperation(value = "위생점검표 작성일자")
	@PostMapping(value = "/list/sanitation/check/index", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('DRIVER')")
	public @ResponseBody List<ChkTblRegistScheduleVo> listSanitationCheckIndex(@RequestBody SanitationCheckIndexReq req) {
		IUser user = super.getConCurrentUser().get();
		LocalDate fromDay = LocalDate.parse(req.getToDay());
		return muserSvc.findMyCheckList(user, fromDay);
	}

	
	
	
	
	
	
	
	

	@ApiOperation(value = "위생점검표-내역조회(SP)")
	@PostMapping(value = "/find/sanitation/check/index", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('DRIVER')")
	public List<MobileSanitationListInfoVo> findSanitationCheckIndex(@Valid @RequestBody SanitationSearchReq req, BindingResult bindingResult)
			throws CustomBadRequestException {

		
		super.verifyData(bindingResult);

		
		Object[] params = { req.getFctryCd(), req.getRegDe(), req.getCarCd()};
		String procName = "[dbo].[SP_2CH_SNITAT_CHCK_SEL]";

		
		List<Object[]> rows = new ArrayList<>();

		rows = muserSvc.selectStoredProcedureCall(params, procName);

		
		List<MobileSanitationListInfoVo> result = new ArrayList<>(rows.size());

		if (rows.size()>0) {
			for (Object[] row : rows) {

				MobileSanitationListInfoVo info = new MobileSanitationListInfoVo();

				info.setId((BigDecimal)row[0]);
				info.setRegDe((String)row[1]);
				info.setItem01((String)row[2]);
				info.setItem02((String)row[3]);
				info.setItem03((String)row[4]);
				info.setItem04((String)row[5]);
				info.setItem05((String)row[6]);
				info.setItem06((String)row[7]);
				info.setItem07((String)row[8]);
				info.setItem08((String)row[9]);
				info.setItem09((String)row[10]);
				info.setItem10((String)row[11]);
				info.setCarCd((String)row[12]);

				result.add(info);
			}
		}
		return result;
	}

	@ApiOperation(value = "위생점검표저장")
	@PostMapping(value = "/save/sanitation", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('DRIVER')")
	public @ResponseBody T_SNITAT_CHCK_TABLE saveSanitation(@Valid @RequestBody SanitationReq snitreq,
			BindingResult bindingResult) throws CustomBadRequestException, ServiceException {

		
		super.verifyData(bindingResult);


		
		T_SNITAT_CHCK_TABLE snitInfo = new T_SNITAT_CHCK_TABLE();
		snitInfo.setId(snitreq.getId());
		snitInfo.setItem01(snitreq.getItem01());
		snitInfo.setItem02(snitreq.getItem02());
		snitInfo.setItem03(snitreq.getItem03());
		snitInfo.setItem04(snitreq.getItem04());
		snitInfo.setItem05(snitreq.getItem05());
		snitInfo.setItem06(snitreq.getItem06());
		snitInfo.setItem07(snitreq.getItem07());
		snitInfo.setItem08(snitreq.getItem08());
		snitInfo.setItem09(snitreq.getItem09());
		snitInfo.setItem10(snitreq.getItem10());
		
		snitInfo.setRegDe(snitreq.getRegDe());
		snitInfo.setVrn(snitreq.getVrn());
		snitInfo.setResultCd(0);
		snitInfo.setResultMsg("정상 등록되었습니다.");

		
		String vrn = snitInfo.getVrn();
		V_VHCLE car = carRepo.findByVrn(vrn).orElse(null);
		if ( car != null ) {
			snitInfo.setFctryCd(car.getFctryCd());
			snitInfo.setTrnsprtCmpny(car.getTrnsprtCmpny());
			snitInfo.setVhcleTy(car.getWegith()+"");
			snitInfo.setDriverNm(car.getDriverNm());
			snitInfo.setDriverCd(car.getDriverCd());
		} else {
			snitInfo.setResultCd(-1);
			snitInfo.setResultMsg("차량 정보가 존재하지 않습니다. [" + vrn + "]");
			return snitInfo;

		}

		try {
			SimpleDateFormat dt = new SimpleDateFormat("yyyyymmdd");
			Date regDe = dt.parse(snitreq.getRegDe());
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		



		return muserSvc.saveSnitation(snitInfo);
	}

	@ApiOperation(value = "월간용역비통계")
	@PostMapping(value = "/stats/driveMonthly", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('DRIVER')")
	public @ResponseBody V_STATS_DRIVER_MONTHLY statsDriveMonthly(@Valid @RequestBody StatsReq statsReq,
			BindingResult bindingResult) throws CustomBadRequestException {
		return muserSvc.findDriveMonthly(statsReq.getDlvyDe(), statsReq.getVrn());
	}

	@ApiOperation(value = "운행통계")
	@GetMapping(value = "/find/drive/statistics", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('DRIVER')")
	public String findDriveStatistics() {

		return "ok";
	}


	@ApiOperation(value = "ARS전화연결")
	@PostMapping(value = "/ars/call", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('DRIVER')")
	public void arsCall(@Valid @RequestBody ArsCall call, BindingResult bindingResult) {
		IUser user = super.getConCurrentUser().get();
		String fctryTell = null;
		if(user.getUserDetails() != null)
			fctryTell = user.getUserDetails().getFctryTel();
		muserSvc.arsCall(SenderType.DRIVER, call.getType(), call.getId(), call.getDlvyDe(), call.getDlvyLcCd(), fctryTell, user, null);
	}

	private Map<String, MenuDetails> makeMemuDetails(IUser u) throws ServiceException {
		ShipStateVo2 vo2 = muserSvc.currentShippingState(u);
		return menuScv.mobileMenuList(currentRoleByUser()).stream().map(m -> find(m).getDetail(m.getPgmId(), vo2))
				.collect(Collectors.toMap(MenuDetails::getPgmId, Function.identity(), (e1, e2) -> e2,
						LinkedHashMap::new));
	}

	private MobilePGMEnums find(T_MENU m) {
		return MobilePGMEnums.valueOf(m.getPgmId().replaceAll("-", ""));
	}

	private MenuTree menuLink(final MenuTree tree) {
		tree.getMenus().forEach(m -> {
			MobilePGMEnums pgm = MobilePGMEnums.valueOf(m.getPgmId().replaceAll("-", ""));
			tree.add(linkTo(MobileUserEndpoint.class).slash(pgm.getPath()).withRel(m.getPgmId()));
		});
		tree.add(linkTo(MobileUserEndpoint.class).slash("/menu/list").withSelfRel());
		return tree;
	}
}