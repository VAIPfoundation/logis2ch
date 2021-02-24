package com.sdc2ch.core.geocalc;



import lombok.extern.slf4j.Slf4j;


@Slf4j
public class BoundingArea {
    private Point northEast, southWest;
    private Point southEast, northWest;

    public BoundingArea(Point northEast, Point southWest) {
        this.northEast = northEast;
        this.southWest = southWest;

        southEast = new Point(new DegreeCoordinate(southWest.getLatitude()), new DegreeCoordinate(northEast.getLongitude()));
        northWest = new Point(new DegreeCoordinate(northEast.getLatitude()), new DegreeCoordinate(southWest.getLongitude()));
    }

    @Deprecated
    public Point getBottomRight() {
        log.debug("getBottomRight() is deprecated. Use getSouthWest() instead.");
        return southWest;
    }

    @Deprecated
    public Point getTopLeft() {
    	log.debug("getTopLeft() is deprecated. Use getNorthEast() instead.");
        return northEast;
    }

    public Point getNorthEast() {
        return northEast;
    }

    public Point getSouthWest() {
        return southWest;
    }

    public Point getSouthEast() {
        return southEast;
    }

    public Point getNorthWest() {
        return northWest;
    }

    @Override
    public String toString() {
        return "BoundingArea{" + "northEast=" + northEast + ", southWest=" + southWest + '}';
    }

    public boolean isContainedWithin(Point point) {
        boolean predicate1 = point.latitude >= this.southWest.latitude && point.latitude <= this.northEast.latitude;

        if(!predicate1) {
            return false;
        }

        boolean predicate2;

        if(southWest.longitude > northEast.longitude) { 
            
            boolean predicate3 = point.longitude <= northEast.longitude && point.longitude >= -180;
            boolean predicate4 = point.longitude >= southWest.longitude && point.longitude <= 180;

            predicate2 = predicate3 || predicate4;
        } else {
            predicate2 = point.longitude >= southWest.longitude && point.longitude <= northEast.longitude;
        }

        return predicate1 && predicate2;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BoundingArea other = (BoundingArea) obj;
        if (this.northEast != other.northEast && (this.northEast == null || !this.northEast.equals(other.northEast))) {
            return false;
        }
        if (this.southWest != other.southWest && (this.southWest == null || !this.southWest.equals(other.southWest))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + (this.northEast != null ? this.northEast.hashCode() : 0);
        hash = 13 * hash + (this.southWest != null ? this.southWest.hashCode() : 0);
        return hash;
    }
}
