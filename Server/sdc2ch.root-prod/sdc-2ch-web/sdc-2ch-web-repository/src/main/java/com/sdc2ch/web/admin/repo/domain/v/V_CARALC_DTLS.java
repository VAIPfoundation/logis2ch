package com.sdc2ch.web.admin.repo.domain.v;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;

import lombok.Getter;
import lombok.Setter;

 
@Entity(name="V_CARALC_DTLS")
@Getter
@Setter
@Immutable
public class V_CARALC_DTLS {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ROWID", updatable = false, nullable = false)
	private Long id;
	@Column(name = "Center_Cd", updatable = false, nullable = false)
	private String centerCd;
	@Column(name = "Delivery_Date", updatable = false, nullable = false)
	private String deliveryDate;
	@Column(name = "Route_No", updatable = false, nullable = false)
	private String routeNo;
	@Column(name = "Stop_Seq", updatable = false, nullable = false)
	private int stopSeq;
	@Column(name = "Batch_No", updatable = false, nullable = false)
	private String batchNo;
	@Column(name = "Job_ID", updatable = false, nullable = false)
	private String jobId;
	@Column(name = "Org_Route_No", updatable = false, nullable = false)
	private String orgRouteNo;
	@Column(name = "Sch_Route_No", updatable = false, nullable = false)
	private String schRouteNo;
	@Column(name = "Job_Type", updatable = false, nullable = false)
	private String jobType;
	@Column(name = "Stop_Type", updatable = false, nullable = false)
	private String stopType;
	@Column(name = "Stop_Cd", updatable = false, nullable = false)
	private String stopCd;
	@Column(name = "Addr_Basic", updatable = false, nullable = false)
	private String addrBasic;
	@Column(name = "Req_Time", updatable = false, nullable = false)
	private String reqTime;
	@Column(name = "Arrive_ETime", updatable = false, nullable = false)
	private String arriveEtime;
	@Column(name = "Alight_ETime", updatable = false, nullable = false)
	private String alightEtime;
	@Column(name = "Depart_ETime", updatable = false, nullable = false)
	private String departEtime;
	@Column(name = "Delivery_RDate", updatable = false, nullable = false)
	private String deliveryRdate;
	@Column(name = "Delivery_RTime", updatable = false, nullable = false)
	private String deliveryRtime;

	@Column(name = "longitude", updatable = false, nullable = false)
	private Double lng;
	@Column(name = "latitude", updatable = false, nullable = false)
	private Double lat;
	@Column(name = "DLVY_LC_NM", updatable = false, nullable = false)
	private String dlvyLcNm;






}
