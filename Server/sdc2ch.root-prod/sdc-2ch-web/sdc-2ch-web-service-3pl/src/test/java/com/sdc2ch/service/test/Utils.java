package com.sdc2ch.service.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.sdc2ch.service.model.cost.CarTonType;
import com.sdc2ch.service.model.item.ItemTransport;
import com.sdc2ch.service.test.Vehicles.Vehicle;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Utils {
	
	
	public static CarTonType neerTon (Collection<CarTonType> hold, double totParret) {
		double min = Integer.MAX_VALUE; 
		double nearData = 0; 
		
		for (CarTonType ctt : hold) {
			double a = Math.abs(ctt.parretVolume - totParret); 
			if (min > a) {
				min = a;
				if(ctt.parretVolume > totParret)
					nearData = ctt.parretVolume;
			}
		}
		
		for(CarTonType ct : hold) {
			if(ct.parretVolume == nearData)
				return ct;
		}
		return null;
	}

}
