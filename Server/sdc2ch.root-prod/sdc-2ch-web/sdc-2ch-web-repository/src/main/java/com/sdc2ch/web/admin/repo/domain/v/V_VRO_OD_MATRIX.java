package com.sdc2ch.web.admin.repo.domain.v;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.annotations.Immutable;

import com.sdc2ch.require.repo.T_ID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Immutable
public class V_VRO_OD_MATRIX extends T_ID {

	@Column(name="PLANT")
	private String plant;
	@Column(name="TIME_ZONE")
	private String timeZone;
	@Column(name="ORI_TIME_ZONE_CD")
	private String oriTimeZoneCd;
	@Column(name="ORIENTATION")
	private String orientation;
	@Column(name="ORI_LAT")
	private String oriLat;
	@Column(name="ORI_LONG")
	private String oriLong;
	@Column(name="DESTINATION")
	private String destination;
	@Column(name="DEST_LAT")
	private String destLat;
	@Column(name="DEST_LONG")
	private String destLong;
	@Column(name="ROUTE_TYPE")
	private String routeType;
	@Column(name="ROUTE_OPT")
	private String routeOpt;
	@Column(name="DISTANCE")
	private String distance;
	@Column(name="TIME")
	private int time;
	@Column(name="PRICE")
	private int price;
	@Column(name="TYPE2_DISTANCE")
	private int type2Distance;
	@Column(name="TYPE2_TIME")
	private int type2Time;
	@Column(name="TYPE2_PRICE")
	private int type2Price;
	@Column(name="TYPE2_IN_IC")
	private String type2InIc;
	@Column(name="TYPE2_OUT_IC")
	private String type2OutIc;
	@Column(name="TYPE3_DISTANCE")
	private String type3Distance;
	@Column(name="TYPE3_TIME")
	private String type3Time;
	@Column(name="TYPE3_PRICE")
	private String type3Price;
	@Column(name="TYPE3_IN_IC")
	private String type3InIc;
	@Column(name="TYPE3_OUT_IC")
	private String type3OutIc;
	@Column(name="TYPE4_DISTANCE")
	private String type4Distance;
	@Column(name="TYPE4_TIME")
	private String type4Time;
	@Column(name="TYPE4_PRICE")
	private String type4Price;
	@Column(name="TYPE4_IN_IC")
	private String type4InIc;
	@Column(name="TYPE4_OUT_IC")
	private String type4OutIc;
	@Column(name="IN_IC")
	private String inIc;
	@Column(name="OUT_IC")
	private String outIc;
	@Column(name="IS_STOP_OVER")
	private String isStopOver;
	@Column(name="STRAIGHT_DISTANCE")
	private String straightDistance;
	@Column(name="PRIORITY")
	private String priority;
	@Column(name="IS_TARGET")
	private String isTarget;
	@Column(name="DESCR")
	private String descr;
	@Column(name="INSERT_ID")
	private String insertId;
	@Column(name="INSERT_DTTM")
	private String insertDttm;
	@Column(name="UPDATE_ID")
	private String updateId;
	@Column(name="UPDATE_DTTM")
	private String updateDttm;


}
