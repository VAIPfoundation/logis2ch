package com.sdc2ch.web.websocket.message;

public class LocationMessage {

    private String mdn;
    private String accuracy;
    private String altitude;
    private String latitude;
    private String longitude;
    private String speed;
    private String provider;
	public String getMdn() {
		return mdn;
	}
	public void setMdn(String mdn) {
		this.mdn = mdn;
	}
	public String getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(String accuracy) {
		this.accuracy = accuracy;
	}
	public String getAltitude() {
		return altitude;
	}
	public void setAltitude(String altitude) {
		this.altitude = altitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getSpeed() {
		return speed;
	}
	public void setSpeed(String speed) {
		this.speed = speed;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	@Override
	public String toString() {
		return "LocationMessage [mdn=" + mdn + ", accuracy=" + accuracy + ", altitude=" + altitude + ", latitude="
				+ latitude + ", longitude=" + longitude + ", speed=" + speed + ", provider=" + provider + "]";
	}
}
