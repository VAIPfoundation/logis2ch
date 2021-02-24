package com.sdc2ch.service.admin.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.microsoft.sqlserver.jdbc.StringUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.sdc2ch.service.admin.ILocationHistService;
import com.sdc2ch.web.admin.repo.AdmQueryBuilder;
import com.sdc2ch.web.admin.repo.dao.T_LocationHistoryRepo;
import com.sdc2ch.web.admin.repo.domain.op.QT_LOCATION_INFO_HIST;
import com.sdc2ch.web.admin.repo.domain.op.T_LOCATION_INFO_HIST;
import com.sdc2ch.web.admin.repo.domain.v.QV_DRIVER;
import com.sdc2ch.web.admin.repo.domain.v.QV_VHCLE;

@Service
public class LocationHistServiceImpl implements ILocationHistService{

	@Autowired
	private T_LocationHistoryRepo locationHistRepo;

	@Autowired
	private AdmQueryBuilder builder;




	@Override
	public List<T_LOCATION_INFO_HIST> searchLocationHistByDate(Date fromDt, Date toDt, String fctryCd, String vrn, Boolean gpsYn) {

		QT_LOCATION_INFO_HIST from = QT_LOCATION_INFO_HIST.t_LOCATION_INFO_HIST;

		
		QV_VHCLE vhcle = QV_VHCLE.v_VHCLE;
		BooleanBuilder vhcleOn = new BooleanBuilder();
		vhcleOn.and(from.vrn.eq(vhcle.vrn));
		
		QV_DRIVER driver = QV_DRIVER.v_DRIVER;
		BooleanBuilder driverOn = new BooleanBuilder();
		driverOn.and(from.driverCd.eq(driver.driverCd));

		BooleanBuilder where = new BooleanBuilder();
		if(fromDt != null) {
			where.and(from.dataDate.goe(fromDt));
		}

		if(toDt != null) {
			where.and(from.dataDate.loe(toDt));
		}

		if (!StringUtils.isEmpty(fctryCd)) {
			where.and(vhcle.fctryCd.eq(fctryCd));
		}

		if (!StringUtils.isEmpty(vrn)) {
			where.and(from.vrn.like("%" + vrn + "%"));
		}

		if (gpsYn != null) {

		}
		return builder.create().select(Projections.fields(
				T_LOCATION_INFO_HIST.class,
				from.id,
				from.createDt,
				from.updateDt,
				from.vrn,
				from.driverCd,
				from.dataDate,
				from.lat,
				from.lng,
				from.accuracy,
				from.dgree,
				from.speed,
				from.distance,
				from.accDistance,
				from.altitude,
				from.adres,
				vhcle.fctryCd.as("fctryCd"),
				driver.mobileNo.as("mdn")
				)).from(from)
				.leftJoin(vhcle).on(vhcleOn)
				.leftJoin(driver).on(driverOn)
				.where(where)
				.orderBy(from.dataDate.desc())
				.fetch();
	}


	@Deprecated
	public List<T_LOCATION_INFO_HIST> searchLocationHistByDateOld(Date fromDt, Date toDt, String fctryCd, String vrn, Boolean gpsYn) {
		Object[] param = {fromDt, toDt, fctryCd, vrn, gpsYn};
		List<Object[]> resultList = builder.storedProcedureCallRet("[dbo].[SP_2CH_LOCATION_HIST_BY_DATE]", param);

		List<T_LOCATION_INFO_HIST> histList = new ArrayList<T_LOCATION_INFO_HIST>();

		for ( Object[] res : resultList ) {
			try {
				T_LOCATION_INFO_HIST hist = new T_LOCATION_INFO_HIST();
				int i = 0;

				Long rowId = Long.valueOf(String.valueOf(res[i++]));


				String _vrn = String.valueOf(res[i++]);
				String driverCd = String.valueOf(res[i++]);
				java.util.Date dataDt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse( String.valueOf(res[i++]) );
				BigDecimal lat = BigDecimal.valueOf(Double.valueOf(String.valueOf(res[i++])));
				BigDecimal lng = BigDecimal.valueOf(Double.valueOf(String.valueOf(res[i++])));
				BigDecimal accuracy = BigDecimal.valueOf(Double.valueOf(String.valueOf(res[i++])));
				int dgree = Integer.valueOf(String.valueOf(res[i++]));
				int speed = Integer.valueOf(String.valueOf(res[i++]));
				int distance = Integer.valueOf(String.valueOf(res[i++]));
				int accDistance = Integer.valueOf(String.valueOf(res[i++]));
				int altitude = Integer.valueOf(String.valueOf(res[i++]));
				String adres = String.valueOf(res[i++]);
				String _fctryCd = String.valueOf(res[i++]);
				String mobileNo = String.valueOf(res[i++]);

				hist.setId(rowId);
	
	
				hist.setAccDistance(accDistance);
				hist.setAccuracy(accuracy);
				hist.setLat(lat);
				hist.setLng(lng);
				hist.setAdres(adres);
				hist.setSpeed(speed);
				hist.setVrn(_vrn);
				hist.setDriverCd(driverCd);
				hist.setAltitude(altitude);
				hist.setDataDate(dataDt);
				hist.setDgree(dgree);
				hist.setDistance(distance);
				hist.setFctryCd(_fctryCd);
				hist.setMdn(mobileNo);
				histList.add(hist);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return histList;

	}


	

	@Override
	public Page<T_LOCATION_INFO_HIST> searchPageLocationHistByDate(Date fromDt, Date toDt, String fctryCd, String vrn, Boolean gpsYn, Pageable pageable) {
		QT_LOCATION_INFO_HIST from = QT_LOCATION_INFO_HIST.t_LOCATION_INFO_HIST;

		
		QV_VHCLE vhcle = QV_VHCLE.v_VHCLE;
		BooleanBuilder vhcleOn = new BooleanBuilder();
		vhcleOn.and(from.vrn.eq(vhcle.vrn));
		
		QV_DRIVER driver = QV_DRIVER.v_DRIVER;
		BooleanBuilder driverOn = new BooleanBuilder();
		driverOn.and(from.driverCd.eq(driver.driverCd));

		BooleanBuilder where = new BooleanBuilder();
		if(fromDt != null) {
			where.and(from.dataDate.goe(fromDt));
		}

		if(toDt != null) {
			where.and(from.dataDate.loe(toDt));
		}

		if (!StringUtils.isEmpty(fctryCd)) {
			where.and(vhcle.fctryCd.eq(fctryCd));
		}

		if (!StringUtils.isEmpty(vrn)) {
			where.and(from.vrn.like("%" + vrn + "%"));
		}

		if (gpsYn != null) {

		}

		JPAQuery<T_LOCATION_INFO_HIST> query = builder.create().select(Projections.fields(
				T_LOCATION_INFO_HIST.class,
				from.id.as("id"),
				from.createDt.as("createDt"),
				from.updateDt.as("updateDt"),
				from.vrn.as("vrn"),
				from.driverCd.as("driverCd"),
				from.dataDate.as("dataDate"),
				from.lat.as("lat"),
				from.lng.as("lng"),
				from.accuracy.as("accuracy"),
				from.dgree.as("dgree"),
				from.speed.as("speed"),
				from.distance.as("distance"),
				from.accDistance.as("accDistance"),
				from.altitude.as("altitude"),
				from.adres.as("adres"),
				vhcle.fctryCd.as("fctryCd"),
				driver.mobileNo.as("mdn")
				)).from(from)
				.leftJoin(vhcle).on(vhcleOn)
				.leftJoin(driver).on(driverOn)
				.where(where);


		if( pageable != null && pageable.getSort() != null) { 
			pageable.getSort().stream().forEach(o -> {
				query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, Expressions.stringPath(o.getProperty())));
			});
		}else {
			query.orderBy(from.dataDate.asc());
		}

		if(pageable != null) { 
			query.offset(pageable.getOffset());
			query.limit(pageable.getPageSize());
		}

		QueryResults<T_LOCATION_INFO_HIST> res = query.setLockMode(LockModeType.NONE).fetchResults();
		return new PageImpl<>(res.getResults(), pageable, res.getTotal());
	}


	@Deprecated
	public Page<T_LOCATION_INFO_HIST> searchPageLocationHistByDateOld2(Date fromDt, Date toDt, String fctryCd, String vrn, Boolean gpsYn, Pageable pageable) {

		Object[] param = {fromDt, toDt, fctryCd, vrn, gpsYn};
		List<Object[]> resultList = builder.storedProcedureCallRet("[dbo].[SP_2CH_LOCATION_HIST_BY_DATE]", param);

		List<T_LOCATION_INFO_HIST> histList = new ArrayList<T_LOCATION_INFO_HIST>();

		for ( Object[] res : resultList ) {
			try {
				T_LOCATION_INFO_HIST hist = new T_LOCATION_INFO_HIST();
				int i = 0;

				Long rowId = Long.valueOf(String.valueOf(res[i++]));


				String _vrn = String.valueOf(res[i++]);
				String driverCd = String.valueOf(res[i++]);
				java.util.Date dataDt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse( String.valueOf(res[i++]) );
				BigDecimal lat = BigDecimal.valueOf(Double.valueOf(String.valueOf(res[i++])));
				BigDecimal lng = BigDecimal.valueOf(Double.valueOf(String.valueOf(res[i++])));
				BigDecimal accuracy = BigDecimal.valueOf(Double.valueOf(String.valueOf(res[i++])));
				int dgree = Integer.valueOf(String.valueOf(res[i++]));
				int speed = Integer.valueOf(String.valueOf(res[i++]));
				int distance = Integer.valueOf(String.valueOf(res[i++]));
				int accDistance = Integer.valueOf(String.valueOf(res[i++]));
				int altitude = Integer.valueOf(String.valueOf(res[i++]));
				String adres = String.valueOf(res[i++]);
				String _fctryCd = String.valueOf(res[i++]);
				String mobileNo = String.valueOf(res[i++]);

				hist.setId(rowId);
	
	
				hist.setAccDistance(accDistance);
				hist.setAccuracy(accuracy);
				hist.setLat(lat);
				hist.setLng(lng);
				hist.setAdres(adres);
				hist.setSpeed(speed);
				hist.setVrn(_vrn);
				hist.setDriverCd(driverCd);
				hist.setAltitude(altitude);
				hist.setDataDate(dataDt);
				hist.setDgree(dgree);
				hist.setDistance(distance);
				hist.setFctryCd(_fctryCd);
				hist.setMdn(mobileNo);
				histList.add(hist);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}



		return new PageImpl<>(histList, pageable, 1);
	}


}
