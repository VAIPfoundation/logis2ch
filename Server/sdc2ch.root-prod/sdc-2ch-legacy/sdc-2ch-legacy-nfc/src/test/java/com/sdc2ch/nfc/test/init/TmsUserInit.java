package com.sdc2ch.nfc.test.init;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.sdc2ch.nfc.domain.entity.T_usr;

public class TmsUserInit {

	
	
	   public void init() {
	    	
	    	InputStream ins = Thread.currentThread().getContextClassLoader().getResourceAsStream("driver_info.txt");
	    	
	    	BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
	    	
	    	String s;
	    	
	    	Date exp = expiredDate();
	    	
	    	List<T_usr> users = new ArrayList<>();
	    	
	    	
	    	try {
	    		T_usr user;
				while((s=reader.readLine()) != null) {
				
					user = new T_usr();
					
					String[] strs = s.split("\t");
					

					
					
					String phone = strs[2].replaceAll("\\p{Z}", "").replaceAll("-", "");
					phone = phone.substring(3);
					
					
			    	user.setUsrid(phone);
			    	user.setNm(strs[1]);
			    	user.setPh("");
			    	user.setEml("");
			    	user.setLgnid(phone);
			    	user.setPw("96VK8e9xmlBBM90+65EfSjd4jpicrkRTbK2meRb2Ifk=");
			    	user.setSttdt(new Date(0L));
			    	user.setExpdt(exp);
			    	user.setSeclvl("0");
			    	user.setDsbdusr("N");
			    	
			    	String cd = strs[0];
			    	int gid = 1;
			    	if("1D1".equals(cd)) {
			    		gid = 1011;
			    	} else if("2D1".equals(cd)) {
			    		gid = 1009;
			    	} else if("3D1".equals(cd)) {
			    		gid = 1008;
			    	} else if("4D1".equals(cd)) {
			    		gid = 1010;
			    	} else if("5D1".equals(cd)) {
			    		gid = 1012;	
			    	}
			    	user.setUsrgruid(gid);
			    	user.setLstmod(10);
			    	user.setPermuid(255);
			    	user.setLstmoddt(new Date());
			    	user.setUdtcnt(0);
			    	user.setPwsta(0);
			    	user.setDel("N");
			    	user.setIsusrgr("N");
			    	user.setUsridord("0" + StringUtils.leftPad(phone, 32, "("));
			    	users.add(user);
				}
			} catch (IOException e) {
				
				e.printStackTrace();
			}

	    	System.out.println(users);



	    }
	    
	    private static Date expiredDate() {
	    	
	    	SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
	    	
	    	
	    	try {
				return fmt.parse("20301231");
			} catch (ParseException e) {
				
				e.printStackTrace();
			}
	    	return null;
	    }
}
