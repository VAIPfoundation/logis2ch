package com.sdc2ch.tms.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.sdc2ch.tms.config.TmsQueryBuilder;
import com.sdc2ch.tms.domain.dto.TmsLocationDto;
import com.sdc2ch.tms.domain.dto.TmsOrderStopDto;
import com.sdc2ch.tms.io.TmsLocationIO;
import com.sdc2ch.tms.io.TmsOrderStopIO;
import com.sdc2ch.tms.service.ITmsStopService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TmsStopServiceImpl implements ITmsStopService {

	@Autowired TmsQueryBuilder builder;

	@Override
	public TmsLocationIO findStopLocation(String stopCd) {
		List<?> results = builder.createSelectNativeQuery(createQuery(stopCd));
		TmsLocationDto point = null;
		if(results != null && !results.isEmpty()) {
			Object[] o = (Object[]) results.get(0);
			String lat =  o[0] + "";
			String lng =  o[1] + "";
			String name = o[2] + "";
			point = TmsLocationDto.builder()
					.lat(scale(lat).doubleValue())
					.lng(scale(lng).doubleValue())
					.name(name).build();
		}
		return point;
	}

	private String createQuery(String stopCd) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT").append(" ");
		sb.append("       CASE WHEN Latitude is null THEN Tmp_Latitude ELSE Latitude END AS LAT").append(" ");
		sb.append("     , CASE WHEN Longitude is null THEN Tmp_Longitude ELSE Longitude END AS LNG").append(" ");
		sb.append("     , Stop_Nm").append(" ");
		sb.append("  FROM [TMS].[DBO].[M_STOP]").append(" ");
		sb.append(" WHERE STOP_CD = '%s'").append(" ");
		return String.format(sb.toString(), stopCd);
	}

	private BigDecimal scale(String lat) {
		if(StringUtils.isEmpty(lat)) {
			lat = "0";
		}
		return new BigDecimal(lat).setScale(6, BigDecimal.ROUND_HALF_DOWN);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TmsOrderStopIO> findOrderStopInfoByDlvyDeAndRouteNo(String dlvyDe, String routeNo) {
		
		EntityManager em = null;
		try {
			em = builder.createEntityManager();
			Query  call =(Query) em.createNativeQuery(SP_2CH_STOP_ORDER_INFO_SEL);
			call.setParameter("dlvyDe", dlvyDe);
			call.setParameter("routeNo", routeNo);
			call.unwrap( org.hibernate.query.NativeQuery.class )
			.setResultTransformer(Transformers.aliasToBean(TmsOrderStopDto.class));
			return (List<TmsOrderStopIO>)call.getResultList();
		}catch (Exception e) {
			log.error("{}", e);
			return Collections.emptyList();
		}finally {
			if(em != null) {
				em.close();
			}
		}

	}

	@SuppressWarnings({"unchecked"})
	@Override
	public TmsOrderStopIO findStopInfoByStopCd(String stopCd) {
		
		EntityManager em = null;
		try {
			em = builder.createEntityManager();
			Query  call =(Query) em.createNativeQuery(SP_2CH_STOP_INFO_SEL);
			call.setParameter("stopCd", stopCd);
			call.unwrap( org.hibernate.query.NativeQuery.class )
			.setResultTransformer(Transformers.aliasToBean(TmsOrderStopDto.class));
			List<TmsOrderStopIO> res = call.getResultList();
			return res.isEmpty() ? null : (TmsOrderStopIO)res.get(0);
			
		}catch (Exception e) {
			log.error("{}", e);
			return null;
		}finally {
			if(em != null) {
				em.close();
			}
		}

	}

}
