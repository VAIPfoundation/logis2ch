package com.sdc2ch.prcss.ds.util;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import com.sdc2ch.core.geocalc.DegreeCoordinate;
import com.sdc2ch.core.geocalc.EarthCalc;
import com.sdc2ch.core.geocalc.Point;

public class ProcessUtils {
	
	public static boolean isNumeric(String s) {  
	    return s != null && s.matches("[-+]?\\d*\\.?\\d+");  
	}
	
	public static double distance(double lat1, double lat2, double lon1,
	        double lon2, double el1, double el2) {
		
		DegreeCoordinate currentlat = new DegreeCoordinate(lat1);
		DegreeCoordinate currentlng = new DegreeCoordinate(lon1);
		Point current = new Point(currentlat, currentlng);
		
		DegreeCoordinate lastlat = new DegreeCoordinate(lat2);
		DegreeCoordinate lastlng = new DegreeCoordinate(lon2);
		Point last = new Point(lastlat, lastlng);
		
		return EarthCalc.getDistance(current, last);

	}
	
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
	    Set<Object> seen = ConcurrentHashMap.newKeySet();
	    return t -> seen.add(keyExtractor.apply(t));
	}
}
