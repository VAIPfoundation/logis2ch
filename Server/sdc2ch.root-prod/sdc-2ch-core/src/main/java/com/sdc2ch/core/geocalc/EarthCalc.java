package com.sdc2ch.core.geocalc;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.asin;
import static java.lang.Math.atan;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;


public class EarthCalc {

    public static final double EARTH_DIAMETER = 6371.01 * 1000; 

    
    public static double getDistance(Point standPoint, Point forePoint) {

        double diffLongitudes = toRadians(forePoint.getLongitude() - standPoint.getLongitude());

        





        double slat = toRadians(standPoint.getLatitude());
        double flat = toRadians(forePoint.getLatitude());

        
        double c = sqrt(pow(cos(flat) * sin(diffLongitudes), 2d) + pow(cos(slat) * sin(flat) - sin(slat) * cos(flat) * cos(diffLongitudes), 2d));
        c = c / (sin(slat) * sin(flat) + cos(slat) * cos(flat) * cos(diffLongitudes));
        c = atan(c);

        return EARTH_DIAMETER * c;
    }

    
    public static Point pointRadialDistance(Point standPoint, double bearing, double distance) {

        double rlat1 = toRadians(standPoint.getLatitude());
        double rlon1 = toRadians(standPoint.getLongitude());
        double rbearing = toRadians(bearing);
        double rdistance = distance / EARTH_DIAMETER; 

        double rlat = asin(sin(rlat1) * cos(rdistance) + cos(rlat1) * sin(rdistance) * cos(rbearing));

        double epsilon = 0.000001;

        double rlon;

        if (cos(rlat) == 0.0 || abs(cos(rlat)) < epsilon) { 
            rlon = rlon1;

        } else {
            rlon = ((rlon1 - asin(sin(rbearing) * sin(rdistance) / cos(rlat)) + PI) % (2 * PI)) - PI;
        }

        return new Point(new RadianCoordinate(rlat), new RadianCoordinate(rlon));
    }

    
    public static double getBearing(Point standPoint, Point forePoint) {
        double latitude1 = toRadians(standPoint.getLatitude());
        double longitude1 = standPoint.getLongitude();

        double latitude2 = toRadians(forePoint.getLatitude());
        double longitude2 = forePoint.getLongitude();

        double longDiff = toRadians(longitude2 - longitude1);

        
        
        double invertedBearing = ((atan2(sin(longDiff) * cos(latitude2),
                cos(latitude1) * sin(latitude2) - sin(latitude1) * cos(latitude2) * cos(longDiff))));

        double rbearing = (-invertedBearing + 2 * PI) % (2 * PI);

        return toDegrees(rbearing);
    }


    
    public static BoundingArea getBoundingArea(Point standPoint, double distance) {

        
        Point northWest = pointRadialDistance(standPoint, 45, distance);

        
        Point southEast = pointRadialDistance(standPoint, 225, distance);

        return new BoundingArea(northWest, southEast);
    }
}
