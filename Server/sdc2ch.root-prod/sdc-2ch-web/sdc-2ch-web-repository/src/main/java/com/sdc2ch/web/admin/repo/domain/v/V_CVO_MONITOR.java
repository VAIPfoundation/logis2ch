package com.sdc2ch.web.admin.repo.domain.v;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Immutable
@Table(name="V_CVO_MONITOR")
public class V_CVO_MONITOR {

	@Id

	@Column(name="ROWID")
	private Long id;

	
	@Column(name = "FCTRY_CD", updatable = false)
	private String fctryCd;

	
	@Column(name = "FCTRY_NM", updatable = false)
	private String fctryNm;

	
	@Column(name = "TRNSPRT_CMPNY_CD", updatable = false)
	private String trnsprtCmpnyCd;

	
	@Column(name = "TRNSPRT_CMPNY", updatable = false)
	private String trnsprtCmpny;

	
	@Column(name = "VHCLE_TY", updatable = false)
	private String vhcleTy;

	
	@Column(name = "LDNG_TY", updatable = false)
	private String ldngTy;

	
	@Column(name = "DRIVER_CD", updatable = false)
	private String driverCd;

	
	@Column(name = "DRIVER_NM", updatable = false)
	private String driverNm;

	
	@Column(name = "VRN", updatable = false)
	private String vrn;

	
	@Column(name = "DRIVE_STTUS", updatable = false)
	private String driveSttus;

	
	@Column(name = "TEMPT1", updatable = false, precision=11, scale=2)
	private BigDecimal tempt1;

	
	@Column(name = "TEMPT2", updatable = false, precision=11, scale=2)
	private Double tempt2;

	
	@Column(name = "LAST_REPORT_DT", updatable = false)
	private Date lastReportDt;

	
	@Column(name = "MILEG", updatable = false, precision=11, scale=2)
	private BigDecimal mileg;

	
	@Column(name = "WT_SM", updatable = false, precision=11, scale=2)
	private BigDecimal wtSm;

	
	@Column(name = "DRIVE_DSTNC", updatable = false, precision=11, scale=2)
	private BigDecimal driveDstnc;

	
	@Column(name = "EMPT_VHCLE_MOVE_DSTNC", updatable = false, precision=11, scale=2)
	private BigDecimal emptVhcleMoveDstnc;

	
	@Column(name = "LDNG_VHCLE_MOVE_DSTNC", updatable = false, precision=11, scale=2)
	private BigDecimal ldngVhcleMoveDstnc;

	
	@Column(name = "TONKM", updatable = false, precision=11, scale=2)
	private BigDecimal tonkm;

	
	@Column(name = "FUEL_USGQTY", updatable = false, precision=11, scale=2)
	private BigDecimal fuelUsgqty;

	
	@Column(name = "CO2_DSCAMT", updatable = false, precision=11, scale=2)
	private BigDecimal co2Dscamt;

	
	@Column(name = "ENERGY_EFC_IDX", updatable = false, precision=11, scale=2)
	private BigDecimal energyEfcIdx;

	
	@Column(name = "UNIT_TONKM", updatable = false, precision=11, scale=2)
	private BigDecimal unitTonkm;


}
