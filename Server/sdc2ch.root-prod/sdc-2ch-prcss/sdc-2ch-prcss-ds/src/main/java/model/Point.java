package model;

public class Point {
	
	private Coordinates latlng;
	private PointType pointType;

	public PointType getPointType() {
		return pointType;
	}
	public void setPointType(PointType pointType) {
		this.pointType = pointType;
	}
	public Coordinates getLatlng() {
		return latlng;
	}
	public void setLatlng(Coordinates latlng) {
		this.latlng = latlng;
	}
}
