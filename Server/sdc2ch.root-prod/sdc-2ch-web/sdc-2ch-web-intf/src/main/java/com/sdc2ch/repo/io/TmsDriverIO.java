package com.sdc2ch.repo.io;

import java.util.stream.Stream;

import com.sdc2ch.require.domain.IUserDetails;

public interface TmsDriverIO extends IUserDetails {
	
	public enum FactoryTel {
		D1("031-862-4434"),
		D2("031-283-7033"),
		D3("031-491-6878"),
		D4("055-945-3760"),
		
		FF("02-");
		
		public String tel;
		FactoryTel(String tel){
			this.tel = tel;
		}
		
		public static String findTel(String fctryCd) {
			

			String _fctryCd = "D" + fctryCd.substring(0, 1);
			
			return Stream.of(FactoryTel.values()).filter(f -> f.name().equals(_fctryCd)).findFirst().orElse(FF).tel;
		}
		
	}
	TmsCarIO getCar();
	String getFctryCd();
	void setCar(TmsCarIO car);
}
