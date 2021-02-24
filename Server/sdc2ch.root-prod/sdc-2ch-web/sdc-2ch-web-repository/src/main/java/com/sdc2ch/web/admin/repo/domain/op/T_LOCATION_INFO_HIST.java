package com.sdc2ch.web.admin.repo.domain.op;

import static com.sdc2ch.require.repo.schema.GTConfig.ADDRESS_NNG_100;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sdc2ch.require.repo.T_ID;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class T_LOCATION_INFO_HIST extends T_ID {

	@Column(name = "VRN")
	private String vrn;
	@Column(name = "DRIVER_CD")
	private String driverCd;
	@Column(name = "DATA_DT")
	private Date dataDate;	
	@Column(name = "LAT", precision=9, scale=6)
	private BigDecimal lat;
	@Column(name = "LNG", precision=9, scale=6)
	private BigDecimal lng;
	@Column(name = "ACCURACY", precision=5, scale=1)
	private BigDecimal accuracy;
	@Column(name = "DGREE")
	private int dgree;		
	@Column(name = "SPEED")
	private int speed;		
	@Column(name = "DISTANCE")
	private int distance;	
	@Column(name = "ACCDISTANCE")
	private int accDistance; 
	@Column(name = "ALTITUDE")
    private int altitude;
	@Column(name = "ADRES", columnDefinition = ADDRESS_NNG_100)
	private String adres;

	@Transient
	@JsonDeserialize
	@JsonSerialize
	private String fctryCd;

	@Transient
	@JsonDeserialize
	@JsonSerialize
	private String mdn;
}
