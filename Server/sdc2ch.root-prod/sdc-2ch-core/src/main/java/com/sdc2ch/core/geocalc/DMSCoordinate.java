package com.sdc2ch.core.geocalc;


public class DMSCoordinate extends Coordinate {

	private static final long serialVersionUID = -8040907448064311452L;
	double wholeDegrees, minutes, seconds;

    public DMSCoordinate(double wholeDegrees, double minutes, double seconds) {
        this.wholeDegrees = wholeDegrees;
        this.minutes = minutes;
        this.seconds = seconds;
        this.decimalDegrees = wholeDegrees + minutes / 60 + seconds / 3600;
    }

    public double getMinutes() {
        return minutes;
    }

    public double getWholeDegrees() {
        return wholeDegrees;
    }

    public double getSeconds() {
        return seconds;
    }
}
