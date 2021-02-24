package com.sdc2ch.service.impl;

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
import com.sdc2ch.repo.builder.IAdmQueryBuilder;
import com.sdc2ch.service.ILgistCostService;
import com.sdc2ch.service.ILgistMatrixService;
import com.sdc2ch.web.admin.repo.dao.T_RoutePathMatrix6Repository;

import com.sdc2ch.web.admin.repo.domain.lgist.QT_ROUTE_PATH_MATRIX6;
import com.sdc2ch.web.admin.repo.domain.lgist.T_ROUTE_PATH_MATRIX6;
import com.sdc2ch.web.admin.repo.domain.op.T_BCON_HIST;

@Service
public class LgistMatrixServiceImpl implements ILgistMatrixService {

	@Autowired ILgistCostService costSvc;

	@Autowired T_RoutePathMatrix6Repository matrixRepo;
	@Autowired IAdmQueryBuilder builder;

	@Override
	public List<T_ROUTE_PATH_MATRIX6> search(Long modelId){
		return search(modelId, null, null);
	}
	@Override
	public List<T_ROUTE_PATH_MATRIX6> search(Long modelId, String routeNo, String vrn){

		QT_ROUTE_PATH_MATRIX6 code = QT_ROUTE_PATH_MATRIX6.t_ROUTE_PATH_MATRIX6;

		BooleanBuilder where = new BooleanBuilder();
		if( modelId != null ) {
			where.and(code.idLgistModelFk.id.eq(modelId));
		} else {
			where.and(code.idLgistModelFk.id.eq((long) -1));
		}
		if(!StringUtils.isEmpty(routeNo)) {
			where.and(code.routeNo.like("%" + routeNo + "%"));
		}
		if(!StringUtils.isEmpty(vrn)) {
			where.and(code.vrn.like("%" + vrn + "%"));
		}

		return builder
				.create()
					.select(
						Projections.fields(
							T_ROUTE_PATH_MATRIX6.class,
							code.id, code.dlvyDe, code.routeNo, code.vrn, code.startPos,
							code.endPos, code.tmsDistance, code.newDistance, code.tmsTollCost, code.newTollCost

						)
					)
					.from(code)
					.where(where)
					.orderBy(code.dlvyDe.asc(), code.routeNo.asc())
				.fetch();
	}
	@Override
	public Page<T_ROUTE_PATH_MATRIX6> search(Long modelId, String routeNo, String vrn, Pageable pageable){

		QT_ROUTE_PATH_MATRIX6 code = QT_ROUTE_PATH_MATRIX6.t_ROUTE_PATH_MATRIX6;

		BooleanBuilder where = new BooleanBuilder();
		if( modelId != null ) {
			where.and(code.idLgistModelFk.id.eq(modelId));
		} else {
			where.and(code.idLgistModelFk.id.eq((long) -1));
		}
		if(!StringUtils.isEmpty(routeNo)) {
			where.and(code.routeNo.like("%" + routeNo + "%"));
		}
		if(!StringUtils.isEmpty(vrn)) {
			where.and(code.vrn.like("%" + vrn + "%"));
		}

		JPAQuery<T_ROUTE_PATH_MATRIX6> query =  builder
				.create()
					.select(
						Projections.fields(
							T_ROUTE_PATH_MATRIX6.class,
								code.id,
								code.dlvyDe, code.routeNo, code.routePathCnt,
								code.startLat, code.startLng, code.startPos,
								code.endLat, code.endLng, code.endPos,
								code.newDistance, code.newFuelCost, code.newSettleCost, code.newTollCost, code.newTotalTime, code.newTurnRate,
								code.tmsDistance, code.tmsTollCost, code.tmsTurnRate, code.vrn, code.weight,
								code.paymentCost, code.supportedCarOil, code.supportedFreezOil

						)
					)
					.from(code)

					.where(where);

		if(pageable != null && pageable.getSort() != null) { 
			pageable.getSort().stream().forEach(o -> {
				query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, Expressions.stringPath(o.getProperty())));
			});
		}else {
			query.orderBy(code.dlvyDe.asc(), code.routeNo.asc());
		}

		if(pageable != null) { 
			query.offset(pageable.getOffset());
			query.limit(pageable.getPageSize());
		}

		return new PageImpl<>(query.fetch(), pageable, query.fetchCount());
	}

	@Override
	public T_ROUTE_PATH_MATRIX6 findById(Long matrixId) {
		return matrixRepo.findById(matrixId).orElse(new T_ROUTE_PATH_MATRIX6());
	}

}
