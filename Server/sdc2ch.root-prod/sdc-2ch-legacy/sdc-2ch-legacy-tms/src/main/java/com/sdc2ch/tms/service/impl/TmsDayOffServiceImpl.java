package com.sdc2ch.tms.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdc2ch.tms.config.TmsQueryBuilder;
import com.sdc2ch.tms.dao.TmsDayoffSchRepository;
import com.sdc2ch.tms.domain.dto.TmsDayOffDto;
import com.sdc2ch.tms.domain.view.TmsDayoffSch;
import com.sdc2ch.tms.io.TmsDayOffIO;
import com.sdc2ch.tms.service.ITmsDayOffService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TmsDayOffServiceImpl implements ITmsDayOffService {

	@Autowired
	TmsQueryBuilder builder;


	@Autowired
	TmsDayoffSchRepository dayoffSchRepo;

	@Override
	public TmsDayOffIO selectOnebyId(String rowId) {
		return dayoffSchRepo.getOne(Long.valueOf(rowId));
	}


	@Override
	@Deprecated
	public int getDayoffCount(String fctryCd, String carCd, String dayoffDate) {
		EntityManager em = null;
		try {
			em = builder.createEntityManager();
			String strQuery = "SELECT COUNT(X.CAR_CD) FROM ( SELECT A.CENTER_CD, B.DDATE, A.CAR_CD, MC.CAR_WEIGHT FROM M_DAYOFF_SCH        A INNER JOIN ( SELECT  CONVERT(CHAR(8), DATEADD(D, NUMBER, ?1), 112) AS DDATE FROM    master..spt_values WHERE   TYPE = 'P' AND     CONVERT(CHAR(8), DATEADD(D, NUMBER, ?1), 112) = ?1 )           B ON      B.DDATE             BETWEEN A.Dayoff_Date AND A.End_Date AND     B.DDATE             = ?1 LEFT OUTER JOIN M_CAR AS MC  ON      A.CAR_CD          = MC.CAR_CD AND     A.CENTER_CD       = MC.CENTER_CD WHERE   B.DDATE             = ?1 AND     A.CENTER_CD         = ?2 ) X WHERE CAR_WEIGHT = (SELECT CAR_WEIGHT FROM M_CAR 	WHERE CAR_CD = ?3 	AND CENTER_CD = ?2)";
			Query query = em.createNativeQuery(strQuery);
			query.setParameter("1", dayoffDate);
			query.setParameter("2", fctryCd);
			query.setParameter("3", carCd);
			int dayoffCnt = (int)query.getSingleResult();
			return dayoffCnt;
		}catch (Exception e) {
			log.error("{}", e);
			return 0;
		}finally {
			if(em != null) {
				em.close();
			}
		}
		
	}



	@Override
	public int getDayoffLimitCnt(String fctryCd, String carCd) {
		EntityManager em = null;
		try {
			em = builder.createEntityManager();
			
			String strQuery = "SELECT ISNULL(DAYOFF_COUNT, 0) FROM M_DAYOFF_ALLOW 	WHERE CENTER_CD = ?1 	AND CAR_WEIGHT = (SELECT CAR_WEIGHT FROM M_CAR  	WHERE CAR_CD = ?2 	AND CENTER_CD = ?1)";
			Query query = em.createNativeQuery(strQuery);
			query.setParameter("1", fctryCd);
			query.setParameter("2", carCd);
			int limitCnt = (int)query.getSingleResult();
			return limitCnt;
		}catch (Exception e) {
			log.error("{}", e);
			return 0;
		}finally {
			if(em != null) {
				em.close();
			}
		}
		

	}

	@Override
	public List<TmsDayOffIO> getDayoffCountV2(String fctryCd, String carCd, String dayoffMonth) {

		
		EntityManager em = null;
		try {
			
			em = builder.createEntityManager();
			String strQuery =
			"SELECT          a.targetdate                 AS targetdate,                 Isnull(Sum(b.dayoff_cnt), 0) AS schcnt FROM            (                        SELECT CONVERT(VARCHAR(10), [날짜], 112) AS targetdate                        FROM   [dbo].[date] WITH(nolock)                        WHERE  CONVERT(VARCHAR(10), [날짜], 112) LIKE ?3) AS a LEFT OUTER JOIN                 (                           SELECT    iif(( datediff(dd, a.dayoff_date, a.end_date) > 1                           AND       a.dayoff_type = '결행' ), 1, 0) AS islongdayoff,                                     a.*                           FROM      dbo.m_dayoff_sch AS a                           LEFT JOIN dbo.m_car        AS b                           ON        a.center_cd = b.center_cd                           AND       a.car_cd = b.car_cd                           WHERE     a.center_cd = ?1                           AND       b.car_weight =                                     (                                            SELECT car_weight                                            FROM   m_car                                            WHERE  center_cd = ?1                                            AND    car_cd = ?2)) AS b ON              a.targetdate BETWEEN b.dayoff_date AND             b.end_date AND             b.islongdayoff = 0 GROUP BY        a.targetdate ORDER BY        a.targetdate ASC";
			Query query = em.createNativeQuery(strQuery);
			query.setParameter("1", fctryCd);
			query.setParameter("2", carCd);
			query.setParameter("3", dayoffMonth+"%");
			List<TmsDayOffIO> resultList = new ArrayList<TmsDayOffIO>();
			Optional.ofNullable((List<Object[]>)query.getResultList()).ifPresent(lst -> {
				resultList.addAll(lst.stream().map(obj -> cvtDtoPossibleDayOff(obj)).collect(Collectors.toList()));
			});
			return resultList;
			
		}catch (Exception e) {
			log.error("{}", e);
			return Collections.emptyList();
		}finally {
			if(em != null) {
				em.close();
			}
		}

	}


	@Override
	public List<TmsDayOffIO> getListDayOffHstInfo(String fctryCd, String carCd, String dayoffMonth) {
		
		EntityManager em = null;
		
		try {
			em = builder.createEntityManager();
			String strQuery = "SELECT          de.rowid                             AS exeid, de.center_cd                         AS centercd, de.car_cd                            AS carcd, de.dayoff_date                       AS exedayoffdate, de.dayoff_type                       AS exetype, CONVERT(VARCHAR,de.dayoff_cnt)       AS exeunit, de.reg_user_id                       AS exereguserid, CONVERT(VARCHAR,de.reg_datetime,120) AS exeregdatetime, de.dayoff_comment                    AS exebigo, ds.rowid                             AS schid, ds.isconfirm                         AS schconfirm, ds.dayoff_date                       AS schstartdate, ds.end_date                          AS schenddate, ds.dayoff_type                       AS schtype, CONVERT(VARCHAR,ds.dayoff_cnt)       AS schunit, ds.dayoff_route                      AS schrouteno, ds.other_comment                     AS schbigo, ds.reg_user_id                       AS schreguserid, CONVERT(VARCHAR,ds.reg_datetime,120) AS schregdatetime, de.dayoff_desc                       AS exedayoffdesc, ds.dayoff_desc                       AS schdayoffdesc, T.dayoff_date AS targetDate FROM ( SELECT dayoff_date, center_cd, car_cd  FROM dbo.m_dayoff_exe WHERE center_cd = ?1 AND car_cd = ?2 AND dayoff_date LIKE ?3 UNION SELECT dayoff_date, center_cd, car_cd FROM dbo.m_dayoff_sch WHERE center_cd = ?1 AND car_cd = ?2 AND dayoff_date LIKE ?3 ) t LEFT OUTER JOIN dbo.m_dayoff_exe                     AS de WITH (nolock) ON ( T.center_cd = de.center_cd AND             T.car_cd = de.car_cd AND             T.dayoff_date = de.dayoff_date )  LEFT OUTER JOIN dbo.m_dayoff_sch                     AS ds  ON              ( T.center_cd = ds.center_cd AND             T.car_cd = ds.car_cd AND             T.dayoff_date = ds.dayoff_date )   ORDER BY        T.dayoff_date ASC   ";
			Query query = em.createNativeQuery(strQuery);
			query.setParameter("1", fctryCd);
			query.setParameter("2", carCd);
			query.setParameter("3", dayoffMonth+"%");
			List<TmsDayOffIO> resultList = new ArrayList<TmsDayOffIO>();
			Optional.ofNullable((List<Object[]>)query.getResultList()).ifPresent(lst -> {
				resultList.addAll(lst.stream().map(obj -> cvtDtoOffHstInfo(obj)).collect(Collectors.toList()));
			});
			return resultList;
		}catch (Exception e) {
			log.error("{}", e);
			return Collections.emptyList();
		}finally {
			if(em != null) {
				em.close();
			}
		}
		

		
	}

	private TmsDayOffDto cvtDtoPossibleDayOff(Object[] obj) {
		TmsDayOffDto dto = new TmsDayOffDto();
		if(log.isDebugEnabled()) {
			log.info("{}", Arrays.asList(obj).toString());
		}
		dto.setTargetDate((String)obj[0]);
		dto.setSchCount((Double)obj[1]);
		return dto;
	}


	
	private TmsDayOffDto cvtDtoOffHstInfo(Object[] obj) {
		TmsDayOffDto dto = new TmsDayOffDto();
		if(log.isDebugEnabled()) {
			log.info("{}", Arrays.asList(obj).toString());
		}
		dto.setExeId((Integer) obj[0]);
		
		
		dto.setExeDayoffDate((String) obj[3]);
		dto.setExeType((String) obj[4]);
		dto.setExeUnit((String) obj[5]);
		dto.setExeRegUserId((String) obj[6]);
		dto.setExeRegDateTime((String) obj[7]);
		dto.setExeBigo((String) obj[8]);
		dto.setSchId((Integer) obj[9]);
		dto.setSchConfirm((Boolean)obj[10] == null ? null: String.valueOf((Boolean)obj[10]));
		dto.setSchStartDate((String) obj[11]);
		dto.setSchEndDate((String) obj[12]);
		dto.setSchType((String) obj[13]);
		dto.setSchUnit((String) obj[14]);
		dto.setSchRouteNo((String) obj[15]);
		dto.setSchBigo((String) obj[16]);
		dto.setSchRegUserId((String) obj[17]);
		dto.setSchRegDateTime((String) obj[18]);
		dto.setExeDayoffDesc((String) obj[19]);
		dto.setSchDayoffDesc((String) obj[20]);
		return dto;
	}




	@Override
	public int getCntExeDayOff(String fctryCd, String carCd, String targetDate) {
		
		EntityManager em = null;
		
		try {
			em = builder.createEntityManager();
			String strQuery =
			"SELECT Count(car_cd) AS cnt FROM   dbo.m_dayoff_exe WHERE  center_cd = ?1        AND car_cd = ?2        AND dayoff_date = ?3 ";
			Query query = em.createNativeQuery(strQuery);
			query.setParameter("1", fctryCd);
			query.setParameter("2", carCd);
			query.setParameter("3", targetDate);
			return (int)query.getSingleResult();
			
		}catch (Exception e) {
			log.error("{}", e);
			return 0;
		}finally {
			if(em != null) {
				em.close();
			}
		}
		

	}
	@Override
	public int getCntExeDayOff(String fctryCd, String carCd, String targetDate, double unit) {
		
		EntityManager em = null;
		try {
			em = builder.createEntityManager();
			String strQuery =
			"SELECT Count(car_cd) AS cnt FROM   dbo.m_dayoff_exe WHERE  center_cd = ?1  AND car_cd = ?2  AND dayoff_date = ?3 AND DAYoff_cnt = ?4";
			Query query = em.createNativeQuery(strQuery);
			query.setParameter("1", fctryCd);
			query.setParameter("2", carCd);
			query.setParameter("3", targetDate);
			query.setParameter("4", unit);
			return (int)query.getSingleResult();
		}catch (Exception e) {
			log.error("{}", e);
			return 0;
		}finally {
			if(em != null) {
				em.close();
			}
		}
	}
	
	@Override
	public boolean validOverPaidDayoff(String fctryCd, String carCd, String startDate, String endDate, double dayoffValue) {
		EntityManager em = null;
		try {
			em = builder.createEntityManager();
			String strQuery = "SELECT dbo.FN_GET_DAYOFF_VALLID_V3 (?1, ?2, ?3, ?4, ?5) as message";
			Query query = em.createNativeQuery(strQuery);
			query.setParameter("1", fctryCd);
			query.setParameter("2", carCd);
			query.setParameter("3", startDate);
			query.setParameter("4", endDate);
			query.setParameter("5", dayoffValue);
			String result = (String)query.getSingleResult();
			return "ACCEPT".equals(result);
		}catch (Exception e) {
			log.error("{}", e);
			return false;
		}finally {
			if(em != null) {
				em.close();
			}
		}
		

	}

	
	public boolean validDayoffSch(String fctryCd, String carCd, String targetDate) {
		EntityManager em = null;
		try {
			em = builder.createEntityManager();
			String strQuery =
			"SELECT * FROM M_DAYOFF_SCH  	 WHERE CENTER_CD = ?1 	   AND CAR_CD = ?2 	   AND DAYOFF_DATE <= ?3  AND END_DATE >= ?3";
			Query query = em.createNativeQuery(strQuery, TmsDayoffSch.class);
			query.setParameter("1", fctryCd);
			query.setParameter("2", carCd);
			query.setParameter("3", targetDate);
			return (int) query.getResultList().size() <= 0 ;
		}catch (Exception e) {
			log.error("{}", e);
			return false;
		}finally {
			if(em != null) {
				em.close();
			}
		}
		

	}



	
	@Override
	public boolean validLimitSchV2(String fctryCd, String carCd, String targetDate, String unit) {
		double currentCnt = this.getCurrentSchCntValue(fctryCd, carCd, targetDate);
		double limitCnt = this.getDayoffLimitCnt(fctryCd, carCd);
		double reqUnit = Double.parseDouble(unit);
		return ((currentCnt + reqUnit)>limitCnt) ? false: true;

	}


	
	private double getCurrentSchCntValue(String fctryCd, String carCd, String targetDate) {
		EntityManager em = null;
		
		try {
			em = builder.createEntityManager();
			String strQuery =
			"SELECT Isnull(Sum(x.dayoff_cnt), 0) AS dayoff_value FROM   (SELECT Iif(( Datediff(dd, a.dayoff_date, a.end_date) > 1 AND a.dayoff_type = '결행' ), 1, 0) AS islongdayoff, a.center_cd, a.car_cd, a.dayoff_cnt, b.car_weight FROM   dbo.m_dayoff_sch AS a LEFT JOIN dbo.m_car AS b ON a.center_cd = b.center_cd AND a.car_cd = b.car_cd WHERE  ?3 BETWEEN a.dayoff_date AND a.end_date) AS x WHERE  x.islongdayoff = 0 AND x.center_cd = ?1 AND x.car_weight = (SELECT car_weight FROM   m_car WHERE  center_cd = ?1 AND car_cd = ?2) AND x.car_cd != ?2";
			Query query = em.createNativeQuery(strQuery);
			query.setParameter("1", fctryCd);
			query.setParameter("2", carCd);
			query.setParameter("3", targetDate);
			return (double)query.getSingleResult();
		}catch (Exception e) {
			
			log.error("{}", e);
			return 0;
		}finally {
			if(em != null) {
				em.close();
			}
		}

	

	}

	@Override
	public double getSumDayoffCnt(String centerCd,  String year, String month, double dayoffCnt) {
		EntityManager em = null;
		try {
			em = builder.createEntityManager();
			String strQuery =
					"SELECT \r\n" +
					"SUM (T1.sumDayOff)\r\n" +
					"FROM(\r\n" +
					"	SELECT \r\n" +
					"	T1.Center_Cd,\r\n" +
					"	T1.Car_Cd,\r\n" +
					"	T1.Dayoff_Date,\r\n" +
					"	SUM(ISNULL(Dayoff_Cnt,0)) AS sumDayOff\r\n" +
					"	FROM dbo.M_DAYOFF_EXE AS T1	\r\n" +
					"	WHERE Center_cd = :centerCd \r\n" +
					"	AND\r\n" +
					"	Dayoff_Date like :targetYearMonth  \r\n" +
					"	GROUP BY \r\n" +
					"	T1.Center_Cd,\r\n" +
					"	T1.Car_Cd,\r\n" +
					"	T1.Dayoff_Date\r\n" +
					"	HAVING SUM(Dayoff_Cnt) >= :dayoffCnt \r\n" +
					") AS T1";

			Query query = em.createNativeQuery(strQuery);
			query.setParameter("centerCd", centerCd);
			query.setParameter("targetYearMonth", year+month+"%");
			query.setParameter("dayoffCnt", dayoffCnt);
			Number cnt =  (Number)query.getSingleResult();
			return  cnt.doubleValue();
			
		}catch (Exception e) {
			log.error("{}", e);
			return  0;
		}finally {
			if(em != null) {
				em.close();
			}
		}
		
		













	}


}
