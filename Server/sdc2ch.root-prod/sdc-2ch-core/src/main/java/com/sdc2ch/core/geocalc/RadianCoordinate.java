package com.sdc2ch.core.geocalc;


public class RadianCoordinate extends Coordinate {

	private static final long serialVersionUID = 7958524075207138059L;
	double radians;

    public RadianCoordinate(double radians) {
        this.decimalDegrees = Math.toDegrees(radians);
        this.radians = radians;
    }

    public double getRadians() {
        return radians;
    }
}
