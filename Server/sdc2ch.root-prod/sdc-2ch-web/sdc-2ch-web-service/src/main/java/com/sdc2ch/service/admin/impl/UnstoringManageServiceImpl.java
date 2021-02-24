package com.sdc2ch.service.admin.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.sdc2ch.prcss.eb.vo.DriverSquareboxErpVo;
import com.sdc2ch.service.admin.IUnstoringManageService;
import com.sdc2ch.tms.service.ITmsUnstoringManageService;
import com.sdc2ch.web.admin.repo.AdmQueryBuilder;
import com.sdc2ch.web.admin.repo.domain.op.T_BCON_HIST;
import com.sdc2ch.web.admin.repo.domain.v.QV_UNSTORING_MANAGE;
import com.sdc2ch.web.admin.repo.domain.v.V_UNSTORING_MANAGE;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
class UnstoringManageServiceImpl implements IUnstoringManageService {

	
	@Autowired AdmQueryBuilder builder;

	@Autowired
	private ITmsUnstoringManageService tmsUnstoringManageSvc; 

	private QV_UNSTORING_MANAGE QGROUP = QV_UNSTORING_MANAGE.v_UNSTORING_MANAGE;

	@Override
	public List<HashMap<String,Object>> search(String fctryCd, String fromDe, String toDe) {
		return search(fctryCd, fromDe, toDe, null, null, null, null, null);
	}

	@Override
	public List<HashMap<String,Object>> search(String fctryCd, String fromDe, String toDe, String fromTime, String toTime, String vhcleTy, String vrn, String caralcTy) {
		log.info("fctryCd={}, fromDe={}, toDe={}, fromTime={}, toTime={}, vhcleTy={}, vrn={}, caralcTy={}", fctryCd, fromDe, toDe, fromTime, toTime, vhcleTy, vrn, caralcTy);


		List<HashMap<String,Object>> resultList = new ArrayList<HashMap<String,Object>>();
		List<Object[]> rows = new ArrayList<Object[]>();

		Object[] params = { fctryCd, fromDe, toDe, fromTime, toTime, vhcleTy, vrn, caralcTy };
		String procName = "[dbo].[SP_2CH_UNSTORING_SEL]";
		rows = builder.storedProcedureResultCall(procName, params);


		for ( int i=0;i<rows.size();i++) {
			HashMap<String,Object> resultRow = new HashMap<String,Object>();
			Object[] row = rows.get(i);

			int index = 0;
			resultRow.put("id",				row[index++] );	
			resultRow.put("dlvyDe",			row[index++] );	
			resultRow.put("fctryCd",		row[index++] );	
			resultRow.put("fctryNm",		row[index++] );	
			resultRow.put("routeNo",		row[index++] );	
			resultRow.put("batchNo",		row[index++] );	
			resultRow.put("caralcTy",		row[index++] );	
			resultRow.put("carWeight",		row[index++] );	
			resultRow.put("vhcleTy",		row[index++] );	
			resultRow.put("vrn",			row[index++] );	
			resultRow.put("driverNm",		row[index++] );	
			resultRow.put("fctryExpcStart",	row[index++] );	
			resultRow.put("fctryExitTime",	row[index++] );	
			resultRow.put("delayTime",		row[index++] );	
			resultRow.put("ldngExpcTime",	row[index++] );	
			resultRow.put("ldngStTime",		row[index++] );	
			resultRow.put("ldngEdTime",		row[index++] );	
			resultRow.put("ldngReqreTime",	row[index++] );	
			resultRow.put("frontEntTime",	row[index++] );	
			resultRow.put("frontExiTime",	row[index++] );	
			resultRow.put("dffrnc1",		row[index++] );	
			resultRow.put("dffrnc2",		row[index++] );	
			resultRow.put("dffrnc3",		row[index++] );	
			resultRow.put("ldngStdTime",	row[index++] );	

			resultList.add(resultRow);
		}
		return resultList;

	}

	
	
	
						
	@Override						
	public List<HashMap<String,Object>> listTaglist(String fctryCd, String month) {						
		log.info("fctryCd={}, month={}", fctryCd, month);					
				
				
		List<HashMap<String,Object>> resultList = new ArrayList<HashMap<String,Object>>();					
		List<Object[]> rows = new ArrayList<Object[]>();					
				
		Object[] params = { fctryCd, month};					
		String procName = "[dbo].[SP_2CH_TAGGING_SEL]";					
		rows = builder.storedProcedureResultCall(procName, params);					
				
		
		for ( int i=0;i<rows.size();i++) {					
		HashMap<String,Object> resultRow = new HashMap<String,Object>();				
		Object[] row = rows.get(i);				
				
		int index = 0;				
		resultRow.put("fctryCd",	row[index++] );	
		resultRow.put("carWeight",	row[index++] );	
		resultRow.put("drv_count",	row[index++] );	
		resultRow.put("success",	row[index++] );	
		resultRow.put("fail",		row[index++] );	
		resultRow.put("tagRate",	row[index++] );	
		resultList.add(resultRow);				
		}					
		return resultList;					
			
	}						


	
	@Override
	public Page<V_UNSTORING_MANAGE> search(String fctryCd, String fromDe, String toDe, Pageable pageable) {
		return search(fctryCd, fromDe, toDe, null, null, null, null, null, pageable);
	}

	@Override
	public Page<V_UNSTORING_MANAGE> search(String fctryCd, String fromDe, String toDe, String fromTime, String toTime, String vhcleTy, String vrn, String caralcTy, Pageable pageable) {


		log.info("fctryCd={}, fromDe={}, toDe={}, fromTime={}, toTime={}, vhcleTy={}, vrn={}, caralcTy={}", fctryCd, fromDe, toDe, fromTime, toTime, vhcleTy, vrn, caralcTy);
		QV_UNSTORING_MANAGE from = QV_UNSTORING_MANAGE.v_UNSTORING_MANAGE;

		BooleanBuilder where = new BooleanBuilder();
		where.and(from.dlvyDe.between(fromDe, toDe));
		where.and(from.fctryCd.eq(fctryCd));

		if ( !StringUtils.isEmpty(caralcTy) ) {
			where.and(from.batchNo.eq(caralcTy));
		}
		if ( !StringUtils.isEmpty(vhcleTy) ) {
			where.and(from.vhcleTy.eq(vhcleTy));
		}
		if ( !StringUtils.isEmpty(vrn) ) {
			where.and(from.vrn.like("%" + vrn + "%"));
		}

		JPAQuery<V_UNSTORING_MANAGE> query = builder
				.create()
					.select(
						Projections.fields(
							V_UNSTORING_MANAGE.class,
							from.id,
							from.dlvyDe.as("dlvyDe"), from.fctryCd, from.fctryNm, from.routeNo, from.batchNo,
							from.caralcTy, from.vhcleTy, from.vrn, from.dlvyLcSttus,
							from.fctryExpcStart, from.fctryExitTime, from.delayTime, from.delayResn,
							from.ldngExpcTime, from.parkngTime, from.ldngTime, from.dffrnc, from.ldngStdTime
						)
					)
					.from(from)
					.where(where);


		if(pageable != null && pageable.getSort() != null) { 
			pageable.getSort().stream().forEach(o -> {
				query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, Expressions.stringPath(o.getProperty())));
			});
		}else {
			query.orderBy(from.id.asc());
		}

		if(pageable != null) { 
			query.offset(pageable.getOffset());
			query.limit(pageable.getPageSize());
		}

		return new PageImpl<>(query.fetch(), pageable, query.fetchCount());

	}




	@Override
	public V_UNSTORING_MANAGE searchOne(String dlvyDe, String routeNo) {
		log.info("dlvyDe={}, routeNo={}", dlvyDe, routeNo);
		QV_UNSTORING_MANAGE code = QV_UNSTORING_MANAGE.v_UNSTORING_MANAGE;

		return builder
				.create()
					.select(
						Projections.fields(
							V_UNSTORING_MANAGE.class,
							code.id,
							code.dlvyDe, code.fctryCd, code.fctryNm, code.routeNo, code.batchNo,
							code.caralcTy, code.vhcleTy, code.vrn, code.driverNm, code.dlvyLcSttus,
							code.fctryExpcStart, code.fctryExitTime, code.delayTime, code.delayResn,
							code.ldngExpcTime, code.parkngTime, code.ldngTime, code.dffrnc, code.ldngStdTime
						)
					)
					.from(code)
					.where(code.dlvyDe.eq(dlvyDe).and(code.routeNo.eq(routeNo)))
					.orderBy(code.ldngTime.asc())
				.fetchFirst();
	}

	@Override
	public int update(List<V_UNSTORING_MANAGE> unstoringVos) {

		int result = 0;
		try {
			for( V_UNSTORING_MANAGE v : unstoringVos ) {
				result += tmsUnstoringManageSvc.UpdateDelayResnById(v.getId(), v.getDelayResn());
			}
		} catch (Exception e) {
			
			e.printStackTrace();
			return 0;
		}

		return result;
	}

}