package com.sdc2ch.legacy.endpoint.request;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sdc2ch.aiv.event.IGpsEvent;
import com.sdc2ch.prcss.eb.vo.EmptyboxVo;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.service.io.GpsDataIO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GpsData implements IGpsEvent, GpsDataIO {

	private String osvn; 		
	private String osnm; 		
	private String model; 		
	private String mdn; 		
	private String dataDate;	
	private String gpsx;		
	private String gpsy;		
	private String dgree;		
	private String speed;		
	private String distance;	
	private String event;		
	private String gpsState;	
	private String accDistance; 
	private String address;		
	private String telco;   
    private String accuracy;
    private String altitude;
    @JsonIgnore
    private IUser user;














	@Override
	@JsonIgnore
	public IUser user() {
		return user;
	}

	@Override
	@JsonIgnore
	public double getLat() {
		return StringUtils.isEmpty(gpsy) ? 0 : Double.parseDouble(gpsy);
	}

	@Override
	@JsonIgnore
	public double getLng() {
		return StringUtils.isEmpty(gpsx) ? 0 : Double.parseDouble(gpsx);
	}

	@Override
	@JsonIgnore
	public long getTimeStamp() {
		try {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
			return fmt.parse(dataDate).getTime();
		}catch (Exception e) {
		}
		return System.currentTimeMillis();
	}

	public static void main(String[] args) {


		String dataDate = "20200108192956";
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			long t = fmt.parse(dataDate).getTime();
			System.out.println("t->"+t);
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		

		String[] routeNos = {"A", "B"};
		String stringByrouteNos = "";
		for ( String routeNo : routeNos ) {
			stringByrouteNos += ",''" + routeNo + "''";
		}


		stringByrouteNos = stringByrouteNos.substring(1);	
		System.out.println("stringByrouteNos:" + stringByrouteNos);
		System.out.println("routeNos:" + routeNos);
	}
}
