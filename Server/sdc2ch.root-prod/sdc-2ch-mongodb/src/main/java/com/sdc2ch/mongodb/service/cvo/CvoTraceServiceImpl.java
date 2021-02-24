package com.sdc2ch.mongodb.service.cvo;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.ResultHandler;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.LazyDBList;
import com.mongodb.LazyDBObject;
import com.mongodb.MongoClient;
import com.sdc2ch.cvo.io.TraceIO;
import com.sdc2ch.cvo.service.ICvoControlService;
import com.sdc2ch.mongodb.service.factory.MongoDbFactory;
import com.sdc2ch.mongodb.service.model.Trace;

@Service
public class CvoTraceServiceImpl implements ICvoControlService {


	private static final String BRN = "2168200028";
	
	private Trace convert(DBObject result) {
		LazyDBList dbo = (LazyDBList) result.get("trace");
		Trace document = null;
		for(int i = 0 ; i < dbo.size() ; i++){
			LazyDBObject ldbo = (LazyDBObject) dbo.get(i);
			DBObject coordinate = (DBObject) ldbo.get("coordinate");
			document = Trace.builder()
					.adres((String) coordinate.get("address"))
					.lat(new BigDecimal(((Double)coordinate.get("gpsY") + "")))
					.lng(new BigDecimal(((Double)coordinate.get("gpsX") + "")))
					.speed((Integer) ldbo.get("speed"))
					.dataDate(new Date((Long)ldbo.get("dataTime")))
					.build();
		}
		return document;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TraceIO> getVehicleTrace(String dlvyDe, String setId) {
		
		long s = System.currentTimeMillis();
		try {
			
			Date stDate = convertDate(dlvyDe);
			Date edDate = nextDate(stDate);
			
			DB db = findDb(stDate);
			Jongo jongo = new Jongo(db);
			MongoCollection col = jongo.getCollection(setId);

			
			
			
			
			
			Iterator<Trace> iter = col.find("{trace.dataTime : {$gte : #, $lt : #}, trace : {$exists : true}}", stDate.getTime(), edDate.getTime())
					.sort("{dataDate:1}").projection("{trace:1, _id: 0, config:1, dataDate:1, " + "drvinfos" +" :1, temp:1, lastCoordinate : 1}").map(new ResultHandler<Trace>() {
						@Override
						public Trace map(DBObject result) {
							return convert(result);
						}
					}).iterator();
			return (List<TraceIO>) (Object)Lists.newArrayList(iter);
		}catch (Exception e){
			e.printStackTrace();
			return Collections.emptyList();
		}finally {
			System.out.println(System.currentTimeMillis() -s + " ms");
		}
		
	}

	@SuppressWarnings("deprecation")
	private DB findDb(Date stDate) {
		MongoClient client = MongoDbFactory.getInstance();
		client.getReadPreference().isSlaveOk();
		String dbName = BRN + "_" + new SimpleDateFormat("yyyy").format(stDate);
		return client.getDB(dbName);
	}

	private Date nextDate(Date stDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(stDate);
		cal.add(Calendar.DAY_OF_YEAR, 1);
		return cal.getTime();
	}

	private Date convertDate(String dlvyDe) {
		try {
			return new SimpleDateFormat("yyyyMMdd").parse(dlvyDe);
		}catch (Exception e) {
		}
		return new Date();
	}
}
