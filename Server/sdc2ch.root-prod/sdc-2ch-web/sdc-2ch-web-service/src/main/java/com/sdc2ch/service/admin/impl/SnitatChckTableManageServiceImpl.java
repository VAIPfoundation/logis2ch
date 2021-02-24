package com.sdc2ch.service.admin.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.sdc2ch.core.security.auth.error.ErrorCode;
import com.sdc2ch.service.admin.ISnitatChckTableManageService;
import com.sdc2ch.service.common.exception.ServiceException;
import com.sdc2ch.tms.io.TmsDayOffExeIO;
import com.sdc2ch.tms.service.ITmsDayOffExeService;
import com.sdc2ch.tms.service.ITmsDayOffService;
import com.sdc2ch.web.admin.repo.AdmQueryBuilder;
import com.sdc2ch.web.admin.repo.dao.T_SnitatChckIfSumryRepository;
import com.sdc2ch.web.admin.repo.dao.T_SnitatChckTableRepository;
import com.sdc2ch.web.admin.repo.dao.V_CarRepository;
import com.sdc2ch.web.admin.repo.dao.V_CaralcMstrRepository;
import com.sdc2ch.web.admin.repo.domain.QT_DATE;
import com.sdc2ch.web.admin.repo.domain.alloc.QT_SNITAT_CHCK_IF_SUMRY;
import com.sdc2ch.web.admin.repo.domain.alloc.QT_SNITAT_CHCK_TABLE;
import com.sdc2ch.web.admin.repo.domain.alloc.T_SNITAT_CHCK_IF_SUMRY;
import com.sdc2ch.web.admin.repo.domain.alloc.T_SNITAT_CHCK_IF_SUMRY.ERPStatus;
import com.sdc2ch.web.admin.repo.domain.alloc.T_SNITAT_CHCK_TABLE;
import com.sdc2ch.web.admin.repo.domain.v.QV_VHCLE;
import com.sdc2ch.web.admin.repo.domain.v.V_SNITAT_CHCK_TABLE_MONTHLY;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
class SnitatChckTableManageServiceImpl implements ISnitatChckTableManageService {

	@Autowired
	AdmQueryBuilder builder;

	@Autowired
	ITmsDayOffExeService dayOffExeSvc;
	@Autowired
	V_CaralcMstrRepository caralcMstrRepo;
	@Autowired
	T_SnitatChckIfSumryRepository snitatChckIfSumryRepo;
	@Autowired
	T_SnitatChckTableRepository repo;
	@Autowired
	V_CarRepository vCarRepo;
	@Autowired
	ITmsDayOffService dayoffSvc;

	private static final String MESSAGE01 = "%s님이 마감처리를 진행하고 있습니다. [진행상태] -> %s";
	private static final String MESSAGE02 = "결제 시스템에서 진행 상태 확인 부탁드립니다. [진행상태] -> %s";

	@Override
	public long updateAllState(String fromDe, String toDe, String fctryCd, String vhcleTy, String vrn, String driverNm,
			String keyword) {

		List<T_SNITAT_CHCK_TABLE> whereList = this.searchDailyList(fromDe, toDe, fctryCd, vhcleTy, vrn, driverNm,
				keyword);
		if (whereList == null || whereList.isEmpty()) {
			return 0;
		}
		List<Long> ids = whereList.stream().filter(o -> o != null && o.getId() != null).map(T_SNITAT_CHCK_TABLE::getId)
				.collect(Collectors.toList());
		return repo.UpdateAllState(ids);
	}

	@Override
	public List<V_SNITAT_CHCK_TABLE_MONTHLY> searchMonthlyList(String year, String fctryCd) {
		return searchMonthlyList(year, fctryCd, null, null, null);
	}

	@Override
	public List<V_SNITAT_CHCK_TABLE_MONTHLY> searchMonthlyList(String year, String fctryCd, String vhcleTy, String vrn,
			String driverNm) {

		Object[] params = { fctryCd, year, vrn, driverNm, vhcleTy };
		String procName = "[dbo].[SP_2CH_SNITAT_CHCK_TABLE_MONTHLY]";
		List<Object[]> rows = new ArrayList<>();
		rows = builder.storedProcedureResultCall(procName, params);

		List<V_SNITAT_CHCK_TABLE_MONTHLY> result = new ArrayList<V_SNITAT_CHCK_TABLE_MONTHLY>(rows.size());
		if (rows.size() > 0) {
			for (Object[] row : rows) {
				V_SNITAT_CHCK_TABLE_MONTHLY info = new V_SNITAT_CHCK_TABLE_MONTHLY();

				info.setFctryCd((String) row[0]);
				info.setVrn((String) row[1]);
				info.setTrnsprtCmpny((String) row[2]);

				info.setVhcleTy((Double) row[3]);
				info.setDriverCd((String) row[4]);
				info.setDriverNm((String) row[5]);
				info.setYyyy((String) row[6]);

				info.setMonth1Snitat((int) row[7]);
				info.setMonth2Snitat((int) row[8]);
				info.setMonth3Snitat((int) row[9]);
				info.setMonth4Snitat((int) row[10]);
				info.setMonth5Snitat((int) row[11]);
				info.setMonth6Snitat((int) row[12]);
				info.setMonth7Snitat((int) row[13]);
				info.setMonth8Snitat((int) row[14]);
				info.setMonth9Snitat((int) row[15]);
				info.setMonth10Snitat((int) row[16]);
				info.setMonth11Snitat((int) row[17]);
				info.setMonth12Snitat((int) row[18]);

				info.setMonth1Dlvy((int) row[19]);
				info.setMonth2Dlvy((int) row[20]);
				info.setMonth3Dlvy((int) row[21]);
				info.setMonth4Dlvy((int) row[22]);
				info.setMonth5Dlvy((int) row[23]);
				info.setMonth6Dlvy((int) row[24]);
				info.setMonth7Dlvy((int) row[25]);
				info.setMonth8Dlvy((int) row[26]);
				info.setMonth9Dlvy((int) row[27]);
				info.setMonth10Dlvy((int) row[28]);
				info.setMonth11Dlvy((int) row[29]);
				info.setMonth12Dlvy((int) row[30]);

				result.add(info);
			}
		}

		return result;

		
		
	}

	@Override
	public List<T_SNITAT_CHCK_TABLE> searchMonthlyDetailList(String year, String month, String fctryCd, String vrn) {

		Object[] params = { fctryCd, year, month, vrn };
		String procName = "[dbo].[SP_2CH_SNITAT_CHCK_TABLE_DAILY]";
		List<Object[]> rows = new ArrayList<>();
		rows = builder.storedProcedureResultCall(procName, params);

		List<T_SNITAT_CHCK_TABLE> result = new ArrayList<T_SNITAT_CHCK_TABLE>(rows.size());
		if (rows.size() > 0) {
			for (Object[] row : rows) {
				T_SNITAT_CHCK_TABLE info = new T_SNITAT_CHCK_TABLE();

				info.setRegDe((String) row[0]);
				info.setFctryCd((String) row[1]);
				info.setDriverNm((String) row[2]);
				info.setTrnsprtCmpny((String) row[3]);
				info.setVhcleTy((String) row[4]);
				info.setVrn((String) row[5]);
				info.setDriverCd((String) row[6]);

				info.setItem01((String) row[7]);
				info.setItem02((String) row[8]);
				info.setItem03((String) row[9]);
				info.setItem04((String) row[10]);
				info.setItem05((String) row[11]);
				info.setItem06((String) row[12]);
				info.setItem07((String) row[13]);
				info.setItem08((String) row[14]);
				info.setItem09((String) row[15]);
				info.setItem10((String) row[16]);
				info.setRm((String) row[17]);
				info.setDlvyTy((String) row[18]);
				info.setDayoffTy((String) row[19]);
				info.setDayoffCnt((Double) row[20]);
				info.setWeekDayKr((String) row[21]);

				result.add(info);
			}
		}

		return result;

		
		
	}

	@Override
	public List<T_SNITAT_CHCK_TABLE> searchDailyList(String fromDe, String toDe, String fctryCd) {
		return searchDailyList(fromDe, toDe, fctryCd, null, null, null, null);
	}

	@Override
	public List<T_SNITAT_CHCK_TABLE> searchDailyList(String fromDe, String toDe, String fctryCd, String vhcleTy,
			String vrn, String driverNm, String keyword) {

		Object[] params = { fctryCd, fromDe, toDe, vhcleTy, vrn, driverNm, keyword };
		String procName = "[dbo].[SP_2CH_SNITAT_CHCK_TABLE_DAILY_LIST]";
		List<Object[]> rows = new ArrayList<>();
		rows = builder.storedProcedureResultCall(procName, params);

		List<T_SNITAT_CHCK_TABLE> result = new ArrayList<T_SNITAT_CHCK_TABLE>(rows.size());
		if (rows.size() > 0) {
			for (Object[] row : rows) {
				T_SNITAT_CHCK_TABLE info = new T_SNITAT_CHCK_TABLE();

				info.setRegDe((String) row[0]);
				info.setFctryCd((String) row[1]);
				info.setDriverNm((String) row[2]);
				info.setTrnsprtCmpny((String) row[3]);
				info.setVhcleTy(String.valueOf((Double) row[4]));
				info.setVrn((String) row[5]);
				info.setDriverCd((String) row[6]);

				info.setItem01((String) row[7]);
				info.setItem02((String) row[8]);
				info.setItem03((String) row[9]);
				info.setItem04((String) row[10]);
				info.setItem05((String) row[11]);
				info.setItem06((String) row[12]);
				info.setItem07((String) row[13]);
				info.setItem08((String) row[14]);
				info.setItem09((String) row[15]);
				info.setItem10((String) row[16]);
				info.setRm((String) row[17]);
				info.setDlvyTy((String) row[18]);
				info.setDayoffTy((String) row[19]);
				info.setDayoffCnt((Double) row[20]);
				info.setWeekDayKr((String) row[21]);
				info.setId(cvtObjtoLong((Number) row[22]));

				result.add(info);
			}
		}

		return result;
	}

	private Long cvtObjtoLong(Number o) {
		if (o == null)
			return null;
		return o.longValue();
	}

	@Override
	public T_SNITAT_CHCK_TABLE searchOne(String dlvyDe, String routeNo) {
		
		return null;
	}

	@Override
	public List<T_SNITAT_CHCK_TABLE> update(List<T_SNITAT_CHCK_TABLE> unstoringVos) {
		
		return null;
	}

	
	@Override
	public List<T_SNITAT_CHCK_IF_SUMRY> searchSnitatcheckSumry(String factryCd, String year, String month) {
		QT_DATE dtCode = QT_DATE.t_DATE;
		QT_SNITAT_CHCK_IF_SUMRY code = QT_SNITAT_CHCK_IF_SUMRY.t_SNITAT_CHCK_IF_SUMRY;

		BooleanBuilder on = new BooleanBuilder();
		on.and(dtCode.year.eq(code.year)).and(dtCode.month.eq(code.month));

		if (!StringUtils.isEmpty(factryCd)) {
			on.and(code.fctryCd.eq(factryCd));
		}

		BooleanBuilder where = new BooleanBuilder();
		if (!StringUtils.isEmpty(year)) {
			where.and(dtCode.year.eq(year));
		}
		if (!StringUtils.isEmpty(month)) {
			where.and(dtCode.month.eq(month));
		}
		List<T_SNITAT_CHCK_IF_SUMRY> result = builder.create()
				.selectDistinct(
						Projections.fields(T_SNITAT_CHCK_IF_SUMRY.class, dtCode.year, dtCode.month, code.fctryCd,
								code.userId, code.ifDate, code.ifYn, code.createDt, code.updateDt, code.erpStatus))
				.from(dtCode).leftJoin(code).on(on).where(where)
				.orderBy(dtCode.year.asc(), dtCode.month.asc(), code.updateDt.desc()).fetch();
		
		Collections.sort(result, new Comparator<T_SNITAT_CHCK_IF_SUMRY>() {
			@Override
			public int compare(T_SNITAT_CHCK_IF_SUMRY smry1, T_SNITAT_CHCK_IF_SUMRY smry2) {
				if (smry1.getErpStatus().ordinal() < smry2.getErpStatus().ordinal())
					return 1;
				else if (smry1.getErpStatus().ordinal() > smry2.getErpStatus().ordinal())
					return -1;
				else
					return 1;
			}
		});

		
		return result.stream().filter(distinctByKey(s -> s.getYear() + s.getMonth())).collect(Collectors.toList());























	}

	public static <T> java.util.function.Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> map = new HashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	public List<T_SNITAT_CHCK_IF_SUMRY> searchSnitatcheckSumry_org(String factryCd, String year, String month) {
		QT_DATE dtCode = QT_DATE.t_DATE;
		QT_SNITAT_CHCK_IF_SUMRY code = QT_SNITAT_CHCK_IF_SUMRY.t_SNITAT_CHCK_IF_SUMRY;

		BooleanBuilder on = new BooleanBuilder();
		on.and(dtCode.year.eq(code.year)).and(dtCode.month.eq(code.month));

		if (!StringUtils.isEmpty(factryCd)) {
			on.and(code.fctryCd.eq(factryCd));
		}

		BooleanBuilder where = new BooleanBuilder();
		if (!StringUtils.isEmpty(year)) {
			where.and(dtCode.year.eq(year));
		}
		if (!StringUtils.isEmpty(month)) {
			where.and(dtCode.month.eq(month));
		}




		return builder.create()
				.selectDistinct(Projections.fields(T_SNITAT_CHCK_IF_SUMRY.class, dtCode.year, dtCode.month,
						code.fctryCd, code.userId, code.ifDate, code.ifYn, code.createDt, code.updateDt))
				.from(dtCode).leftJoin(code).on(on).where(where).orderBy(dtCode.year.asc(), dtCode.month.asc()).fetch();
	}

	@Override
	@Transactional
	public T_SNITAT_CHCK_IF_SUMRY interfaceSnitatCheckSumry(String userId, String factryCd, String year, String month) {
		T_SNITAT_CHCK_IF_SUMRY legacyObj = snitatChckIfSumryRepo.findOneByFctryCdAndYearAndMonth(factryCd, year, month);
		try {
			if (legacyObj == null) {
				legacyObj = new T_SNITAT_CHCK_IF_SUMRY();
			}
			T_SNITAT_CHCK_TABLE sumry = this.searchSumryByfctryCdAndMonthly(factryCd, year, month);
			legacyObj.setFctryCd(factryCd);
			this.copySumryData(legacyObj, sumry);
			legacyObj.setDayoffCnt((int) dayoffSvc.getSumDayoffCnt(factryCd, year, month, 1));
			legacyObj.setMonth(month);
			legacyObj.setYear(year);
			legacyObj.setUserId(userId);
			snitatChckIfSumryRepo.save(legacyObj);
			
			
			this.makeLegacyErpHead(legacyObj);
			
			this.makeLegacyErpBody(legacyObj);
			
			legacyObj.setIfDate(new Date());
			legacyObj.setMakeTime(new SimpleDateFormat("yyyyMMddHHmmss").format(legacyObj.getIfDate()));
			legacyObj.setLegacyKey(this.getLeacyKey());
			this.saveErpSnitatCheckSumry(legacyObj);
			legacyObj.setIfYn(true);
			legacyObj.setMsg("정상적으로 성공하였습니다.");
			legacyObj.setCode("0");
			String url = "http:
					+ "&reqtime=" + legacyObj.getMakeTime() + "&legacykey=" + legacyObj.getLegacyKey() + "&ifname="
					+ legacyObj.getLegacyType();
			legacyObj.setReUrl(url);
			snitatChckIfSumryRepo.save(legacyObj);
			return legacyObj;

		} catch (Exception e) {
			e.printStackTrace();
			log.error("{}", e);
			legacyObj.setMsg("알수 없는 오류가 발생하였습니다.\r\n 잠시후 다시 시도해 주세요");
			legacyObj.setCode("-1");
			return legacyObj;
		}
	}

	public T_SNITAT_CHCK_IF_SUMRY interfaceSnitatCheckSumry3(String userId, String fctryCd, String year, String month)
			throws ServiceException {
		try {

			T_SNITAT_CHCK_IF_SUMRY legacyObj = snitatChckIfSumryRepo
					.findOneByFctryCdAndYearAndMonthAndUserId(fctryCd, year, month, userId)
					.orElse(new T_SNITAT_CHCK_IF_SUMRY());
			T_SNITAT_CHCK_TABLE sumry = this.searchSumryByfctryCdAndMonthly(fctryCd, year, month);
			legacyObj.setFctryCd(fctryCd);
			this.copySumryData(legacyObj, sumry);
			legacyObj.setDayoffCnt((int) dayoffSvc.getSumDayoffCnt(fctryCd, year, month, 1));
			legacyObj.setMonth(month);
			legacyObj.setYear(year);
			legacyObj.setUserId(userId);
			
			
			this.makeLegacyErpHead(legacyObj);
			
			this.makeLegacyErpBody(legacyObj);
			
			legacyObj.setIfDate(new Date());
			legacyObj.setMakeTime(new SimpleDateFormat("yyyyMMddHHmmss").format(legacyObj.getIfDate()));
			legacyObj.setLegacyKey(this.getLeacyKey());
			this.saveErpSnitatCheckSumry(legacyObj);
			legacyObj.setIfYn(true);
			legacyObj.setMsg("정상적으로 성공하였습니다.");
			legacyObj.setCode("0");
			String url = "http:
					+ "&reqtime=" + legacyObj.getMakeTime() + "&legacykey=" + legacyObj.getLegacyKey() + "&ifname="
					+ legacyObj.getLegacyType();
			legacyObj.setReUrl(url);
			legacyObj.setErpStatus(ERPStatus.NEW);
			snitatChckIfSumryRepo.save(legacyObj);
			return legacyObj;

		} catch (Exception e) {
			e.printStackTrace();
			log.error("{}", e);
			throw new ServiceException(HttpStatus.BAD_REQUEST, ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	public T_SNITAT_CHCK_IF_SUMRY interfaceSnitatCheckSumry2(String userId, String factryCd, String year, String month,
			Boolean retry) throws ServiceException {
		if (retry == null) {
			retry = false;
		}

		try {

			
			String yyyyMM = year + month;
			List<T_SNITAT_CHCK_TABLE> tbls = insertTargetData(factryCd, yyyyMM);

			if (tbls == null || tbls.size() != 0) {
				throw new ServiceException(HttpStatus.BAD_REQUEST, ErrorCode.INTERNAL_SERVER_ERROR,
						"위생점검표 입력이 모두 완료되지 않아 월마감 진행을 하실 수 없습니다. \r\n'일괄등록'을 실행 후 다시 시도하여 주세요. ");
			}

			if (retry) {
				this.updateCancleStatus(userId, factryCd, year, month);
			}

			List<T_SNITAT_CHCK_IF_SUMRY> sumrys = snitatChckIfSumryRepo.findAllByFctryCdAndYearAndMonth(factryCd, year,
					month);
			T_SNITAT_CHCK_IF_SUMRY last = findLastStatusByUsers(sumrys);
			
			if (last == null || last.getErpStatus() == null) {
				
				return interfaceSnitatCheckSumry3(userId, factryCd, year, month);
			}

			switch (last.getErpStatus()) {
			case NEW:
			case AUTO:
				
				
				changeUpdate(sumrys);
				
				
				return sumrys.stream().filter(s -> s.getUserId().equals(userId)).findFirst().orElseGet(() -> {
					try {
						return interfaceSnitatCheckSumry3(userId, factryCd, year, month);
					} catch (ServiceException e) {
						
						e.printStackTrace();
					}
					return null;
				});
			case CANCEL:
				return interfaceSnitatCheckSumry3(userId, factryCd, year, month);
			case WRITE:
			case APP:
			case ENDCANCEL:
			case END:
				
				throw new ServiceException(HttpStatus.BAD_REQUEST, ErrorCode.INTERNAL_SERVER_ERROR,
						String.format(MESSAGE01, last.getUserId(), last.getErpStatus().statusName));
			default:
				return null;
			}
		} catch (ServiceException e) {
			log.error("{}", e);
			throw e;
		} catch (Exception e) {
			log.error("{}", e);
			throw new ServiceException(HttpStatus.BAD_REQUEST, ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	private void updateCancleStatus(String userId, String fctryCd, String year, String month) {
		snitatChckIfSumryRepo.findOneByFctryCdAndYearAndMonthAndUserId(fctryCd, year, month, userId).ifPresent(o -> {
			o.setErpStatus(ERPStatus.CANCEL);
			snitatChckIfSumryRepo.save(o);
		});
	}

	private T_SNITAT_CHCK_IF_SUMRY findLastStatusByUsers(List<T_SNITAT_CHCK_IF_SUMRY> sumrys) {
		return sumrys != null ? sumrys.stream().max(Comparator.comparing(s -> s.getErpStatus().ordinal()))
				.orElse(new T_SNITAT_CHCK_IF_SUMRY()) : null;
	}

	private void copySumryData(T_SNITAT_CHCK_IF_SUMRY target, T_SNITAT_CHCK_TABLE source) {
		if (target != null && source != null) {
			target.setItem01cnt1(source.getItem01cnt1());
			target.setItem01cnt2(source.getItem01cnt2());
			target.setItem01cnt3(source.getItem01cnt3());

			target.setItem02cnt1(source.getItem02cnt1());
			target.setItem02cnt2(source.getItem02cnt2());
			target.setItem02cnt3(source.getItem02cnt3());

			target.setItem03cnt1(source.getItem03cnt1());
			target.setItem03cnt2(source.getItem03cnt2());
			target.setItem03cnt3(source.getItem03cnt3());

			target.setItem04cnt1(source.getItem04cnt1());
			target.setItem04cnt2(source.getItem04cnt2());
			target.setItem04cnt3(source.getItem04cnt3());

			target.setItem05cnt1(source.getItem05cnt1());
			target.setItem05cnt2(source.getItem05cnt2());
			target.setItem05cnt3(source.getItem05cnt3());

			target.setItem06cnt1(source.getItem06cnt1());
			target.setItem06cnt2(source.getItem06cnt2());
			target.setItem06cnt3(source.getItem06cnt3());

			target.setItem07cnt1(source.getItem07cnt1());
			target.setItem07cnt2(source.getItem07cnt2());
			target.setItem07cnt3(source.getItem07cnt3());

			target.setItem08cnt1(source.getItem08cnt1());
			target.setItem08cnt2(source.getItem08cnt2());
			target.setItem08cnt3(source.getItem08cnt3());

			target.setItem09cnt1(source.getItem09cnt1());
			target.setItem09cnt2(source.getItem09cnt2());
			target.setItem09cnt3(source.getItem09cnt3());

			target.setItem10cnt1(source.getItem10cnt1());
			target.setItem10cnt2(source.getItem10cnt2());
			target.setItem10cnt3(source.getItem10cnt3());
			target.setTotalCarCnt(source.getCarCnt() != null ? source.getCarCnt().intValue() : 0);
		}
	}

	
	private void makeLegacyErpHead(T_SNITAT_CHCK_IF_SUMRY legacyObj) {
		legacyObj.setLegacyType("SMILK_WHS");
		legacyObj.setAppForm("HTMLF15");
		legacyObj.setAppLine(legacyObj.getUserId());
		legacyObj.setTitle("위생점검" + " " + legacyObj.getYear() + "년 " + legacyObj.getMonth() + "월" + " " + "월 마감");
		legacyObj.setStatus("NEW");
	}

	
	private void makeLegacyErpBody(T_SNITAT_CHCK_IF_SUMRY legacyObj) {
		StringBuffer sb = new StringBuffer();
		sb.append("<center><table width=\"100%\" height=\"200\" border=\"1\" cellspacing=\"1\" cellpadding=\"0\">");
		sb.append("<tr><td bgcolor=\"#e7e7e7\" colspan=\"7\" align=\"center\">" + legacyObj.getYear() + "년 "
				+ legacyObj.getMonth() + "월</td></tr>");
		sb.append("<tr>");
		sb.append("<td width=\"10%\" bgcolor=\"#e7e7e7\"  align=\"center\">공장</td>");
		sb.append("<td width=\"10%\" bgcolor=\"#e7e7e7\" align=\"center\">총 차량</td>");
		sb.append("<td width=\"10%\" bgcolor=\"#e7e7e7\" align=\"center\">휴일일수</td>");
		sb.append("<td width=\"40%\" bgcolor=\"#e7e7e7\" align=\"center\">항목</td>");
		sb.append("<td width=\"10%\" bgcolor=\"#e7e7e7\" align=\"center\">양호</td>");
		sb.append("<td width=\"10%\" bgcolor=\"#e7e7e7\" align=\"center\">보통</td>");
		sb.append("<td width=\"10%\" bgcolor=\"#e7e7e7\" align=\"center\">미흡</td>");
		sb.append("</tr>");
		sb.append("<tr><td rowspan=\"10\" align=\"center\">" + legacyObj.getFctryCd()
				+ "</td><td rowspan=\"10\" align=\"center\">" + legacyObj.getTotalCarCnt()
				+ "</td><td rowspan=\"10\" align=\"center\">" + legacyObj.getDayoffCnt() + "</td>");
		sb.append("<td align=\"left\">두발, 수염, 손톱등 개인위생</td><td align=\"center\">" + legacyObj.getItem01cnt1()
				+ "</td><td align=\"center\">" + legacyObj.getItem01cnt2() + "</td><td align=\"center\">"
				+ legacyObj.getItem01cnt3() + "</td></tr>");
		sb.append("<tr><td align=\"left\">피부병, 심한감기 등 전염성 질병 감염</td><td align=\"center\">" + legacyObj.getItem02cnt1()
				+ "</td><td align=\"center\">" + legacyObj.getItem02cnt2() + "</td><td align=\"center\">"
				+ legacyObj.getItem02cnt3() + "</td></tr>");
		sb.append("<tr><td align=\"left\">운송 전 적재함 내부 세척</td><td align=\"center\">" + legacyObj.getItem03cnt1()
				+ "</td><td align=\"center\">" + legacyObj.getItem03cnt2() + "</td><td align=\"center\">"
				+ legacyObj.getItem03cnt3() + "</td></tr>");
		sb.append("<tr><td align=\"left\">냉동기 작동, 온도기록지,<br/>온도기록기 가동상태</td><td align=\"center\">"
				+ legacyObj.getItem04cnt1() + "</td><td align=\"center\">" + legacyObj.getItem04cnt2()
				+ "</td><td align=\"center\">" + legacyObj.getItem04cnt3() + "</td></tr>");
		sb.append("<tr><td align=\"left\">흡연, 음식물 섭취, 껌씹는행위</td><td align=\"center\">" + legacyObj.getItem05cnt1()
				+ "</td><td align=\"center\">" + legacyObj.getItem05cnt2() + "</td><td align=\"center\">"
				+ legacyObj.getItem05cnt3() + "</td></tr>");
		sb.append("<tr><td align=\"left\">영업장 및 화장실 출입시<br/>손 세척 및 신발소독</td><td align=\"center\">"
				+ legacyObj.getItem06cnt1() + "</td><td align=\"center\">" + legacyObj.getItem06cnt2()
				+ "</td><td align=\"center\">" + legacyObj.getItem06cnt3() + "</td></tr>");
		sb.append("<tr><td align=\"left\">적재함 바닥오염</td><td align=\"center\">" + legacyObj.getItem07cnt1()
				+ "</td><td align=\"center\">" + legacyObj.getItem07cnt2() + "</td><td align=\"center\">"
				+ legacyObj.getItem07cnt3() + "</td></tr>");
		sb.append("<tr><td align=\"left\">적재함 적정온도 유지</td><td align=\"center\">" + legacyObj.getItem08cnt1()
				+ "</td><td align=\"center\">" + legacyObj.getItem08cnt2() + "</td><td align=\"center\">"
				+ legacyObj.getItem08cnt3() + "</td></tr>");
		sb.append("<tr><td align=\"left\">온도기록지 기록보관</td><td align=\"center\">" + legacyObj.getItem09cnt1()
				+ "</td><td align=\"center\">" + legacyObj.getItem09cnt2() + "</td><td align=\"center\">"
				+ legacyObj.getItem09cnt3() + "</td></tr>");
		sb.append("<tr><td align=\"left\">운송 후 적재함 내부세척</td><td align=\"center\">" + legacyObj.getItem10cnt1()
				+ "</td><td align=\"center\">" + legacyObj.getItem10cnt2() + "</td><td align=\"center\">"
				+ legacyObj.getItem10cnt3() + "</td></tr>");
		sb.append("<tr><td align=\"left\" bgcolor=\"#e7e7e7\" colspan=\"7\">※총차량 기준 : 당월 차량 운행 대수</td></tr>");
		sb.append(
				"<tr><td align=\"left\" bgcolor=\"#e7e7e7\" colspan=\"7\">※휴일일수 기준 : 당월 무급,유급,비번,결행 1Day 이상</td></tr>");
		sb.append("</table></center>");
		legacyObj.setBodyContents(sb.toString());
	}

	
	private String getLeacyKey() {
		return (String) builder.getEntityManager()
				.createNativeQuery("SELECT * FROM OPENQUERY(MILK, ' SELECT XXC.T_APPLEGACY4ERP_S.NEXTVAL FROM DUAL')")
				.getSingleResult();
	}

	private void saveErpSnitatCheckSumry(T_SNITAT_CHCK_IF_SUMRY snitatChckIfSumry) throws Exception {
		
		Object[] headparams = { snitatChckIfSumry.getLegacyKey(), snitatChckIfSumry.getLegacyType(),
				snitatChckIfSumry.getAppForm(), snitatChckIfSumry.getUserId(), snitatChckIfSumry.getMakeTime(),
				snitatChckIfSumry.getTitle(), snitatChckIfSumry.getAppLine(), snitatChckIfSumry.getStatus(),
				snitatChckIfSumry.getUserId() };
		builder.storedProcedureCall("[dbo].[SP_2CH_ERP_AP_HEAD_REG]", headparams);

		
		Object[] bodyparams = { snitatChckIfSumry.getLegacyKey(), snitatChckIfSumry.getBodyContents(),
				snitatChckIfSumry.getUserId() };
		builder.storedProcedureCall("[dbo].[SP_2CH_ERP_AP_BODY_REG]", bodyparams);

	}

	public T_SNITAT_CHCK_TABLE searchSumryByfctryCdAndMonthly(String fctryCd, String year, String month) {

		String yyyyMM = year + month;
		Object[] param = { yyyyMM, fctryCd };

		List<Object[]> res = builder.storedProcedureResultCall("[dbo].[SP_2CH_SNITAT_CLOSE_MONTH_SEL]", param);
		T_SNITAT_CHCK_TABLE sumry = new T_SNITAT_CHCK_TABLE();

		for (Object[] obj : res) {
			sumry.setFctryCd((String) obj[0]);

			sumry.setItem01cnt1((int) obj[1]);
			sumry.setItem01cnt2((int) obj[2]);
			sumry.setItem01cnt3((int) obj[3]);

			sumry.setItem02cnt1((int) obj[4]);
			sumry.setItem02cnt2((int) obj[5]);
			sumry.setItem02cnt3((int) obj[6]);

			sumry.setItem03cnt1((int) obj[7]);
			sumry.setItem03cnt2((int) obj[8]);
			sumry.setItem03cnt3((int) obj[9]);

			sumry.setItem04cnt1((int) obj[10]);
			sumry.setItem04cnt2((int) obj[11]);
			sumry.setItem04cnt3((int) obj[12]);

			sumry.setItem05cnt1((int) obj[13]);
			sumry.setItem05cnt2((int) obj[14]);
			sumry.setItem05cnt3((int) obj[15]);

			sumry.setItem06cnt1((int) obj[16]);
			sumry.setItem06cnt2((int) obj[17]);
			sumry.setItem06cnt3((int) obj[18]);

			sumry.setItem07cnt1((int) obj[19]);
			sumry.setItem07cnt2((int) obj[20]);
			sumry.setItem07cnt3((int) obj[21]);

			sumry.setItem08cnt1((int) obj[22]);
			sumry.setItem08cnt2((int) obj[23]);
			sumry.setItem08cnt3((int) obj[24]);

			sumry.setItem09cnt1((int) obj[25]);
			sumry.setItem09cnt2((int) obj[26]);
			sumry.setItem09cnt3((int) obj[27]);

			sumry.setItem10cnt1((int) obj[28]);
			sumry.setItem10cnt2((int) obj[29]);
			sumry.setItem10cnt3((int) obj[30]);

			sumry.setCarCnt(Long.valueOf((int) obj[31]));
		}
		return sumry;

		

	}

	@Override
	public int insertAllExcludeDayoffExe(String fctryCd, String month) {
		
		return bulkInsert(insertTargetData(fctryCd, month));
	}

	private List<T_SNITAT_CHCK_TABLE> insertTargetData(String fctryCd, String month) {
		LocalDate ld = LocalDate.parse(month + "01", DateTimeFormatter.ofPattern("yyyyMMdd"));
		List<TmsDayOffExeIO> offs = dayOffExeSvc.findAllByFctryCdAndDlvyMon(fctryCd, month);

		offs.removeIf(t -> nullSafeConvert(t.getDayOffCnt()) < 1);

		List<T_SNITAT_CHCK_TABLE> src = Lists.newArrayList(repo.findAll(predicate(fctryCd, month)));
		Map<String, List<T_SNITAT_CHCK_TABLE>> groupBy = src.stream()
				.collect(Collectors.groupingBy(T_SNITAT_CHCK_TABLE::getVrn));

		List<SnitatChckTableWarpper> warppers = Lists.newArrayList(vCarRepo.findAll(predicate(fctryCd))).stream()
				.map(v -> SnitatChckTableWarpper.builder().driverCd(v.getDriverCd()).driverNm(v.getDriverCd())
						.vrn(v.getVrn()).fctryCd(v.getFctryCd()).trnsprtCmpny(v.getTrnsprtCmpny())
						.vhcleTy(v.getWegith().toString()).tables(groupBy.get(v.getVrn())).build())
				.collect(Collectors.toList());

		List<T_SNITAT_CHCK_TABLE> target = new ArrayList<>();
		warppers.stream().map(w -> {
			List<String> uncheck = unCheckTableDay(ld, w.getTables());
			return uncheck.stream().filter(date -> {
				boolean ismatch = offs.stream()
						.anyMatch(o -> o.getVrn().equals(w.getVrn()) && o.getDayOffDe().equals(date));
				return !ismatch;
			}).map(date2 -> {
				return cerate(w, date2);
			}).collect(Collectors.toList());
		}).forEach(a -> target.addAll(a));

		return target;
	}

	private int bulkInsert(List<T_SNITAT_CHCK_TABLE> target) {

		Lists.partition(target, 1000).forEach(ps -> {

			String params = ps.stream().map(t -> {
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				StringBuilder sb = new StringBuilder();
				sb.append("(");
				sb.append(nullConvert(fmt.format(new Date()))).append(",");
				sb.append(nullConvert(fmt.format(new Date()))).append(",");
				sb.append(nullConvert(t.getDriverNm())).append(",");
				sb.append(nullConvert(t.getFctryCd())).append(",");
				sb.append(nullConvert(t.getItem01())).append(",");
				sb.append(nullConvert(t.getItem02())).append(",");
				sb.append(nullConvert(t.getItem03())).append(",");
				sb.append(nullConvert(t.getItem04())).append(",");
				sb.append(nullConvert(t.getItem05())).append(",");
				sb.append(nullConvert(t.getItem06())).append(",");
				sb.append(nullConvert(t.getItem07())).append(",");
				sb.append(nullConvert(t.getItem08())).append(",");
				sb.append(nullConvert(t.getItem09())).append(",");
				sb.append(nullConvert(t.getItem10())).append(",");
				sb.append(nullConvert(t.getRegDe())).append(",");
				sb.append(nullConvert(t.getTrnsprtCmpny())).append(",");
				sb.append(nullConvert(t.getVhcleTy())).append(",");
				sb.append(nullConvert(t.getVrn())).append(",");
				sb.append(nullConvert(t.getDriverCd())).append(")");
				return sb.toString();
			}).collect(Collectors.joining(","));

			String query = T_SnitatChckTableRepository.insertQuery + " " + params;
			builder.batchInsert(query);
		});
		return target.size();
	}

	private String nullConvert(Object o) {
		return o == null ? null : ("'" + o + "'");
	}

	private int nullSafeConvert(BigDecimal b) {
		if (b == null)
			b = BigDecimal.ZERO;
		return b.intValue();
	}

	private T_SNITAT_CHCK_TABLE cerate(SnitatChckTableWarpper warpper, String date) {
		T_SNITAT_CHCK_TABLE tbl = new T_SNITAT_CHCK_TABLE();
		tbl.setFctryCd(warpper.getFctryCd());
		tbl.setVhcleTy(warpper.getVhcleTy());
		tbl.setVrn(warpper.getVrn());
		tbl.setDriverNm(warpper.getDriverNm());
		tbl.setDriverCd(warpper.getDriverCd());
		tbl.setTrnsprtCmpny(warpper.getTrnsprtCmpny());
		tbl.setRegDe(date);
		tbl.setItem01("○");
		tbl.setItem02("○");
		tbl.setItem03("○");
		tbl.setItem04("○");
		tbl.setItem05("○");
		tbl.setItem06("○");
		tbl.setItem07("○");
		tbl.setItem08("○");
		tbl.setItem09("○");
		tbl.setItem10("○");
		return tbl;
	}

	private List<String> unCheckTableDay(LocalDate month, List<T_SNITAT_CHCK_TABLE> tables) {

		List<T_SNITAT_CHCK_TABLE> _tables = tables == null ? Collections.emptyList() : tables;
		LocalDate start = LocalDate.of(month.getYear(), month.getMonth(), 1);
		LocalDate end = month.with(TemporalAdjusters.lastDayOfMonth());
		return IntStream.range(0, end.getDayOfMonth()).filter(i -> {
			LocalDate tomorrow = start.plusDays(i);
			boolean match = containsDate(tomorrow.toString().replaceAll("-", ""), _tables);
			return !match;
		}).mapToObj(i -> {
			return start.plusDays(i).toString().replaceAll("-", "");
		}).collect(Collectors.toList());
	}

	private boolean containsDate(String de, List<T_SNITAT_CHCK_TABLE> tables) {
		return tables.stream().anyMatch(t -> t.getRegDe().equals(de));
	}

	private Predicate predicate(String fctryCd) {
		QV_VHCLE v = QV_VHCLE.v_VHCLE;
		BooleanBuilder where = new BooleanBuilder();
		where.and(v.vrn.notLike("물류%"));
		if (!StringUtils.isEmpty(fctryCd)) {
			where.and(v.fctryCd.eq(fctryCd));
		}
		return where;
	}

	private Predicate predicate(String fctryCd, String month) {
		QT_SNITAT_CHCK_TABLE tbl = QT_SNITAT_CHCK_TABLE.t_SNITAT_CHCK_TABLE;
		BooleanBuilder where = new BooleanBuilder();
		if (!StringUtils.isEmpty(fctryCd)) {
			where.and(tbl.fctryCd.eq(fctryCd));
		}
		if (!StringUtils.isEmpty(month)) {
			where.and(tbl.regDe.like(month + "%"));
		}
		return where;
	}

	@SuppressWarnings("unchecked")
	private void changeUpdate(List<T_SNITAT_CHCK_IF_SUMRY> sumrys) throws ServiceException {

		if (sumrys != null && !sumrys.isEmpty()) {
			String query = T_SnitatChckIfSumryRepository.erpIFOpenquery;
			String params = sumrys.stream().map(T_SNITAT_CHCK_IF_SUMRY::getLegacyKey).collect(Collectors.joining(","));
			query = String.format(query, params);
			List<Object[]> results = (List<Object[]>) builder.createSelectNativeQuery(query);

			if (results != null) {
				ErpSnitatChckIfSumryWrapper end = results.stream().map(r -> {
					return ErpSnitatChckIfSumryWrapper.builder().legacyKey(nullSafeString(r[0]))
							.userId(nullSafeString(r[1])).status(ERPStatus.valueOf(nullSafeString(r[2]))).build();
				}).max(Comparator.comparing(s -> s.getStatus().ordinal())).get();

				
				
				
				if (ERPStatus.NEW != end.getStatus() && ERPStatus.AUTO != end.getStatus()) {
					
					T_SNITAT_CHCK_IF_SUMRY sumry = sumrys.stream()
							.filter(s -> s.getLegacyKey().equals(end.getLegacyKey())).findFirst().get();
					sumry.setErpStatus(end.getStatus());
					snitatChckIfSumryRepo.save(sumry);
					throw new ServiceException(HttpStatus.BAD_REQUEST, ErrorCode.INTERNAL_SERVER_ERROR,
							String.format(MESSAGE01, end.getUserId(), end.getStatus().statusName));
				}

				if (ERPStatus.NEW != end.getStatus() && ERPStatus.END != end.getStatus()
						&& ERPStatus.AUTO != end.getStatus()) {
					throw new ServiceException(HttpStatus.BAD_REQUEST, ErrorCode.INTERNAL_SERVER_ERROR,
							String.format(MESSAGE02, end.getUserId(), end.getStatus().statusName));
				}

			}
		}

	}

	private String nullSafeString(Object object) {
		return object == null ? "" : object.toString();
	}

	@Getter
	@Builder

	private static class SnitatChckTableWarpper {
		private String fctryCd;
		private String vrn;
		private String driverCd;
		private String driverNm;
		private String vhcleTy;
		private String trnsprtCmpny;
		private List<T_SNITAT_CHCK_TABLE> tables;
	}

	@Getter
	@Builder
	private static class ErpSnitatChckIfSumryWrapper {
		private String legacyKey;
		private String userId;
		private ERPStatus status;
	}
}
