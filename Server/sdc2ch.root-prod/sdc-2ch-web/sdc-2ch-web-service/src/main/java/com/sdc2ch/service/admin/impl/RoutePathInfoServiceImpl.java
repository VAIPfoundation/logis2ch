package com.sdc2ch.service.admin.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.sdc2ch.service.admin.RoutePathInfoService;
import com.sdc2ch.web.admin.repo.AdmQueryBuilder;
import com.sdc2ch.web.admin.repo.dao.T_RoutePathInfoRepository;
import com.sdc2ch.web.admin.repo.dao.T_RoutePathMatrix2Repository;
import com.sdc2ch.web.admin.repo.dao.T_RoutePathMatrix3Repository;
import com.sdc2ch.web.admin.repo.dao.T_RoutePathMatrix5Repository;
import com.sdc2ch.web.admin.repo.dao.T_RoutePathMatrixRepository;
import com.sdc2ch.web.admin.repo.domain.QT_ROUTE_PATH_MATRIX2;
import com.sdc2ch.web.admin.repo.domain.QT_ROUTE_PATH_MATRIX3;
import com.sdc2ch.web.admin.repo.domain.QT_ROUTE_PATH_MATRIX4;
import com.sdc2ch.web.admin.repo.domain.QT_ROUTE_PATH_MATRIX5;
import com.sdc2ch.web.admin.repo.domain.T_ROUTE_PATH_INFO;
import com.sdc2ch.web.admin.repo.domain.T_ROUTE_PATH_MATRIX;
import com.sdc2ch.web.admin.repo.domain.T_ROUTE_PATH_MATRIX2;
import com.sdc2ch.web.admin.repo.domain.T_ROUTE_PATH_MATRIX3;
import com.sdc2ch.web.admin.repo.domain.T_ROUTE_PATH_MATRIX4;
import com.sdc2ch.web.admin.repo.domain.T_ROUTE_PATH_MATRIX5;
import com.sdc2ch.web.admin.repo.domain.v.QV_VRO_OD_MATRIX;
import com.sdc2ch.web.admin.repo.domain.v.V_VRO_OD_MATRIX;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RoutePathInfoServiceImpl implements RoutePathInfoService {

	@Autowired T_RoutePathInfoRepository repo;
	@Autowired T_RoutePathMatrixRepository matrixRepo;
	@Autowired T_RoutePathMatrix2Repository matrix2Repo;
	@Autowired T_RoutePathMatrix3Repository matrix3Repo;
	@Autowired T_RoutePathMatrix5Repository matrix5Repo;
	@Autowired AdmQueryBuilder builder;
	
	@Override
	public List<T_ROUTE_PATH_INFO> findAll() {
		return repo.findAll();
	}
	
	
	public void save(T_ROUTE_PATH_INFO s) {
		repo.save(s);
	}
	public void save(T_ROUTE_PATH_MATRIX s) {
		matrixRepo.save(s);
	}

	@Override
	public List<T_ROUTE_PATH_MATRIX> findMatirxAll() {
		return matrixRepo.findAll();
	}


	@Override
	public List<T_ROUTE_PATH_MATRIX2> findMatirx2All() {
		QT_ROUTE_PATH_MATRIX2 qmatrix = QT_ROUTE_PATH_MATRIX2.t_ROUTE_PATH_MATRIX2;
		return builder.create().select(Projections.fields(T_ROUTE_PATH_MATRIX2.class, qmatrix.dlvyDe, qmatrix.routeNo)).from(qmatrix).fetch();
	}


	@Override
	public void save(T_ROUTE_PATH_MATRIX2 s) {
		matrix2Repo.save(s);
	}
	@Override
	public void save(T_ROUTE_PATH_MATRIX3 s) {
		matrix3Repo.save(s);
	}


	@Override
	public List<T_ROUTE_PATH_MATRIX3> findMatirx3All() {
		QT_ROUTE_PATH_MATRIX3 qmatrix = QT_ROUTE_PATH_MATRIX3.t_ROUTE_PATH_MATRIX3;
		return builder.create().select(Projections.fields(T_ROUTE_PATH_MATRIX3.class, qmatrix.dlvyDe, qmatrix.routeNo)).from(qmatrix).fetch();
	}


	@Override
	public List<T_ROUTE_PATH_MATRIX3> findMatirx3() {
		QT_ROUTE_PATH_MATRIX3 qmatrix = QT_ROUTE_PATH_MATRIX3.t_ROUTE_PATH_MATRIX3;
		return builder.create().select(Projections.fields(T_ROUTE_PATH_MATRIX3.class, qmatrix.dlvyDe, qmatrix.routeNo, qmatrix.newDistance, qmatrix.newTollCost, qmatrix.startPos, qmatrix.endPos)).from(qmatrix).fetch();
	}
	@Override
	public List<T_ROUTE_PATH_MATRIX3> listRoutePathInfo(String month) {
		return listRoutePathInfo(month, null, null, null, null);
	}
	@Override
	public List<T_ROUTE_PATH_MATRIX3> listRoutePathInfo(String month, String fctryCd, String routeNo, String vrn, String changeBase) {
		log.info("month={}, fctryCd={}, RouteNo={}, vrn={}", month, fctryCd, routeNo, vrn);

		QT_ROUTE_PATH_MATRIX3 code = QT_ROUTE_PATH_MATRIX3.t_ROUTE_PATH_MATRIX3;

		BooleanBuilder where = new BooleanBuilder();
		where.and(code.dlvyDe.like(month + '%'));

		where.and(code.routeNo.like("%" + routeNo + "%"));
		where.and(code.vrn.like("%" + vrn + "%"));

		if(!StringUtils.isEmpty(changeBase)) {
			where.and(code.startPos.eq(changeBase).or(code.endPos.eq(changeBase)));
		}


		return builder
				.create()
					.select(
						Projections.fields(
							T_ROUTE_PATH_MATRIX3.class,
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
	public T_ROUTE_PATH_MATRIX3 searchRoutePathInfo(Long id) {
		QT_ROUTE_PATH_MATRIX3 code = QT_ROUTE_PATH_MATRIX3.t_ROUTE_PATH_MATRIX3;
		return builder
				.create()
					.select(
						Projections.fields(
							T_ROUTE_PATH_MATRIX3.class,
							code.id, code.dlvyDe, code.routeNo, code.vrn, code.startPos,
							code.endPos, code.tmsDistance, code.newDistance, code.tmsTollCost, code.newTollCost,
							code.startLat, code.startLng, code.endLat, code.endLng, code.jsonData
						)
					)
					.from(code)
					.where(code.id.eq(id))
				.fetchOne();
	}


	@Override
	public V_VRO_OD_MATRIX searchTmsRoutePathInfo(String startCd, String endCd) {
		log.info("startCd={}, endCd={}", startCd, endCd);

		QV_VRO_OD_MATRIX code = QV_VRO_OD_MATRIX.v_VRO_OD_MATRIX;

		BooleanBuilder where = new BooleanBuilder();

		where.and(code.orientation.eq("%" + startCd + "%"));
		where.and(code.destination.eq("%" + endCd + "%"));

		return builder
				.create()
					.select(
						Projections.fields(
							V_VRO_OD_MATRIX.class,
							code.plant, code.timeZone, code.oriTimeZoneCd, code.orientation, code.oriLat, code.oriLong, code.destination, code.destLat, code.destLong,
							code.routeType, code.routeOpt,
							code.distance, code.time, code.price,
							code.type2Distance, code.type2Time, code.type2Price, code.type2InIc, code.type2OutIc,
							code.type3Distance, code.type3Time, code.type3Price, code.type3InIc, code.type3OutIc,
							code.type4Distance, code.type4Time, code.type4Price, code.type4InIc, code.type4OutIc,
							code.inIc, code.outIc, code.isStopOver, code.straightDistance, code.priority,
							code.isTarget, code.descr, code.insertId, code.insertDttm, code.updateId, code.updateDttm
						)
					)
					.from(code)
					.where(where)
				.fetchOne();
	}
	
	@Override
	public List<T_ROUTE_PATH_MATRIX4> listRoutePathInfo4(String month) {
		return listRoutePathInfo4(month, null, null, null, null, null);
	}
	@Override
	public List<T_ROUTE_PATH_MATRIX4> listRoutePathInfo4(String month, String fctryCd, String routeNo, String vrn, String changeBase, String routeTy2) {
		log.info("month={}, fctryCd={}, RouteNo={}, vrn={}", month, fctryCd, routeNo, vrn);

		QT_ROUTE_PATH_MATRIX4 code = QT_ROUTE_PATH_MATRIX4.t_ROUTE_PATH_MATRIX4;

		BooleanBuilder where = new BooleanBuilder();
		where.and(code.dlvyDe.like(month + '%'));

		where.and(code.routeNo.substring(0,1).eq(fctryCd.substring(0, 1)));
		if ( "drive".equals(routeTy2) ) {
			where.and(code.routeNo.substring(1,3).ne("9N"));
			where.and(code.routeNo.substring(1,3).ne("9M"));
		}
		if ( "ego".equals(routeTy2) ) {
			where.and(
				code.routeNo.substring(1,3).eq("9N")
				.or( code.routeNo.substring(1,3).eq("9M") )
			);
		}

		where.and(code.routeNo.like("%" + routeNo + "%"));
		where.and(code.vrn.like("%" + vrn + "%"));

		if(!StringUtils.isEmpty(changeBase)) {
			where.and(code.startPos.eq(changeBase).or(code.endPos.eq(changeBase)));
		}


		return builder
				.create()
					.select(
						Projections.fields(
							T_ROUTE_PATH_MATRIX4.class,
							code.id, code.dlvyDe, code.routeNo, code.vrn, code.startPos,
							code.endPos, code.tmsDistance, code.newDistance, code.tmsTollCost, code.newTollCost,
							code.routePathCount, code.routePathString
						)
					)
					.from(code)
					.where(where)
					.orderBy(code.dlvyDe.asc(), code.routeNo.asc())
				.fetch();
	}


	@Override
	public T_ROUTE_PATH_MATRIX4 searchRoutePathInfo4(Long id) {
		QT_ROUTE_PATH_MATRIX4 code = QT_ROUTE_PATH_MATRIX4.t_ROUTE_PATH_MATRIX4;
		return builder
				.create()
					.select(
						Projections.fields(
							T_ROUTE_PATH_MATRIX4.class,
							code.id, code.dlvyDe, code.routeNo, code.vrn, code.startPos,
							code.endPos, code.tmsDistance, code.newDistance, code.tmsTollCost, code.newTollCost,
							code.startLat, code.startLng, code.endLat, code.endLng, code.jsonData,
							code.routePathCount, code.routePathString
						)
					)
					.from(code)
					.where(code.id.eq(id))
				.fetchOne();
	}


	@Override
	public void save(T_ROUTE_PATH_MATRIX5 s) {
		matrix5Repo.save(s);
	}


	@Override
	public List<T_ROUTE_PATH_MATRIX5> findMatirx5All() {
		QT_ROUTE_PATH_MATRIX5 qmatrix = QT_ROUTE_PATH_MATRIX5.t_ROUTE_PATH_MATRIX5;
		return builder.create().select(Projections.fields(T_ROUTE_PATH_MATRIX5.class, qmatrix.dlvyDe, qmatrix.routeNo)).from(qmatrix).fetch();
	}
	
	@Override
	public List<T_ROUTE_PATH_MATRIX5> findMatirx5(String dest) {
		QT_ROUTE_PATH_MATRIX5 qmatrix = QT_ROUTE_PATH_MATRIX5.t_ROUTE_PATH_MATRIX5;
		return builder.create()
				.select(Projections.fields(T_ROUTE_PATH_MATRIX5.class, qmatrix.dlvyDe, qmatrix.routeNo, qmatrix.newDistance, qmatrix.newTollCost, qmatrix.startPos, qmatrix.endPos, qmatrix.routePathCnt, qmatrix.newTotalTime, qmatrix.vrn))
				.from(qmatrix)
				.where(qmatrix.endPos.eq(dest))
				.fetch();
	}
	
	@Override
	public List<T_ROUTE_PATH_MATRIX5> findMatirx5() {
		QT_ROUTE_PATH_MATRIX5 qmatrix = QT_ROUTE_PATH_MATRIX5.t_ROUTE_PATH_MATRIX5;
		return builder.create().select(Projections.fields(T_ROUTE_PATH_MATRIX5.class, qmatrix.dlvyDe, qmatrix.routeNo, qmatrix.newDistance, qmatrix.newTollCost, qmatrix.startPos, qmatrix.endPos, qmatrix.routePathCnt, qmatrix.newTotalTime)).from(qmatrix).fetch();
	}
}
