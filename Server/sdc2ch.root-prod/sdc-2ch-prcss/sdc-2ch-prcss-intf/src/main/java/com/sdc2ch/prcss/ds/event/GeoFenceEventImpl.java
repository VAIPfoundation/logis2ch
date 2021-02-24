package com.sdc2ch.prcss.ds.event;

import com.sdc2ch.require.domain.IUser;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class GeoFenceEventImpl implements IGeoFenceEvent{
	
	private final IUser user;
	@Setter
	private GeoFenceEvent geoEvent;
	private final double distance;
	private final long timeStamp;
	@Setter
	@Getter
	private String stopCd;
	@Setter
	@Getter
	private String routeNo;
	@Setter
	private int uniqueSequence;
	@Setter
	private long increment;
	
	private int radius;
	
	public GeoFenceEventImpl(IUser user, double distance, int uniqueSequence, long timeStamp, int radius) {
		this.user = user;
		this.distance = distance;
		this.uniqueSequence = uniqueSequence;
		this.timeStamp = timeStamp;
		this.radius = radius;
	}
	public GeoFenceEventImpl(IUser user, double distance, int uniqueSequence, long timeStamp, GeoFenceEvent geoEvent, int radius) {
		this(user, distance, uniqueSequence, timeStamp, radius);
		this.geoEvent = geoEvent;
	}
	public GeoFenceEventImpl(IUser user, double distance, int uniqueSequence, long timeStamp, GeoFenceEvent geoEvent, int radius, String routeNo, String stopCd) {
		this(user, distance, uniqueSequence, timeStamp, radius);
		this.geoEvent = geoEvent;
		this.routeNo = routeNo;
		this.stopCd = stopCd;
	}
	
	public static GeoFenceEventImpl of(IUser user, double distance, int uniqueSequence, long timeStamp, int radius){
		return new GeoFenceEventImpl(user, distance, uniqueSequence, timeStamp, radius);
	}
	
	public IUser user() {
		return user;
	}
	
	public class GeoFenceEnterEvent extends GeoFenceEventImpl implements IGeoFenceEnterEvent {

		public GeoFenceEnterEvent(IUser user, double distance, int uniqueSequence, long timeStamp, GeoFenceEvent geoEvent,int radius) {
			this(user, distance, uniqueSequence, timeStamp, geoEvent, radius, null, null);
		}
		public GeoFenceEnterEvent(IUser user, double distance, int uniqueSequence, long timeStamp, GeoFenceEvent geoEvent,int radius, String routeNo, String stopCd) {
			super(user, distance, uniqueSequence, timeStamp, geoEvent, radius, routeNo, stopCd);
		}
	}
	public class GeoFenceArrivedEvent extends GeoFenceEventImpl implements IGeoFenceArrivedEvent {
		
		public GeoFenceArrivedEvent(IUser user, double distance, int uniqueSequence, long timeStamp, GeoFenceEvent geoEvent,int radius) {
			this(user, distance, uniqueSequence, timeStamp, geoEvent, radius, null, null);
		}
		public GeoFenceArrivedEvent(IUser user, double distance, int uniqueSequence, long timeStamp, GeoFenceEvent geoEvent,int radius, String routeNo, String stopCd) {
			super(user, distance, uniqueSequence, timeStamp, geoEvent, radius, routeNo, stopCd);
		}
	}
	public class GeoFenceExitedEvent extends GeoFenceEventImpl implements IGeoFenceExitedEvent {
		
		public GeoFenceExitedEvent(IUser user, double distance, int uniqueSequence, long timeStamp, GeoFenceEvent geoEvent,int radius) {
			this(user, distance, uniqueSequence, timeStamp, geoEvent, radius, null, null);
		}
		public GeoFenceExitedEvent(IUser user, double distance, int uniqueSequence, long timeStamp, GeoFenceEvent geoEvent,int radius, String routeNo, String stopCd) {
			super(user, distance, uniqueSequence, timeStamp, geoEvent, radius, routeNo, stopCd);
		}
	}
	
	@Override
	public ActionEventType getActionEventTy() {
		
		switch (geoEvent) {
		case GEO_ARRIVE:
			return ActionEventType.GEO_ARRIVED;
		case GEO_ENTER:
			return ActionEventType.GEO_ENTER;
		case GEO_EXIT:
			return ActionEventType.GEO_EXITED;
		default:
			break;
		}
		return ActionEventType.UNKNOWN;
	}
	
	public IGeoFenceEvent build() {
		return geoEvent == GeoFenceEvent.GEO_ARRIVE ? 
				new GeoFenceArrivedEvent(user, distance, uniqueSequence, timeStamp, geoEvent, radius)
				: geoEvent == GeoFenceEvent.GEO_ENTER ? 
						new GeoFenceEnterEvent(user, distance, uniqueSequence, timeStamp, geoEvent, radius)
						: new GeoFenceExitedEvent(user, distance, uniqueSequence, timeStamp, geoEvent, radius);
	}
	
	public IGeoFenceEvent build2() {
		return geoEvent == GeoFenceEvent.GEO_ARRIVE ? 
				new GeoFenceArrivedEvent(user, distance, uniqueSequence, timeStamp, geoEvent, radius, routeNo, stopCd)
				: geoEvent == GeoFenceEvent.GEO_ENTER ? 
						new GeoFenceEnterEvent(user, distance, uniqueSequence, timeStamp, geoEvent, radius, routeNo, stopCd)
						: new GeoFenceExitedEvent(user, distance, uniqueSequence, timeStamp, geoEvent, radius, routeNo, stopCd);
	}
}
