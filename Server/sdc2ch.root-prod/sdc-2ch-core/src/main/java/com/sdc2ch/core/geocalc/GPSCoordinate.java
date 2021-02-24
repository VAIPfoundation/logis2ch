package com.sdc2ch.core.geocalc;


public class GPSCoordinate extends DMSCoordinate {

	private static final long serialVersionUID = 1150459942851015252L;

	public GPSCoordinate(double wholeDegrees, double minutes) {
        super(wholeDegrees, minutes, 0);
    }
}
