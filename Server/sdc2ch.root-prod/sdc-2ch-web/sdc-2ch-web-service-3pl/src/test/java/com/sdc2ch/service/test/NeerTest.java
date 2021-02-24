package com.sdc2ch.service.test;

import java.math.BigDecimal;

import com.sdc2ch.service.tm.Util;

public class NeerTest {
	public static void main(String[] args) {
		
		int[] data = { 10, 15, 18, 25, 30, 35 }; 
		int near = 7; 
		int min = Integer.MAX_VALUE; 
		int nearData = 0; 
		
		for (int i = 0; i < data.length; i++) {
			int a = Math.abs(data[i] - near); 
			if (min > a) {
				min = a;
				nearData = data[i];
			}
		}
		
		System.out.println(near + "에 근접한 값 : " + nearData);
		
		double val = 999d;
		
		Util.calculate_vehicle(val, "2D1");
		
		
		
		System.out.println(new BigDecimal(val/16));
		System.out.println(new BigDecimal(val%16));
	}
}
