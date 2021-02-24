package com.sdc2ch.web.admin.repo.domain.alloc;

import static com.sdc2ch.require.repo.schema.GTConfig.ADDRESS_NNG_100;
import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_20;
import static com.sdc2ch.require.repo.schema.GTConfig.ROUTE_NO_LNG_07;
import static com.sdc2ch.require.repo.schema.GTConfig.USER_NAME_LNG_10;
import static com.sdc2ch.require.repo.schema.GTConfig.VRN_LNG_10;
import static com.sdc2ch.require.repo.schema.GTConfig.YYYYMMDD_08;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sdc2ch.require.repo.T_ID;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "T_RT_DLVY_STTUS")
@Getter
@Setter
public class T_RT_DLVY_STTUS extends T_ID {
	
	@Column(name = "VRN", columnDefinition = VRN_LNG_10)
	private String vrn;

	@Column(name = "DLVY_DE", columnDefinition = YYYYMMDD_08)
	private String dlvyDe;

	@Column(name = "FCTRY_CD", columnDefinition = ANY_ENUMS_LNG_20)
	private String fctry;

	@Column(name = "CARALC_TY", columnDefinition = ANY_ENUMS_LNG_20)
	private String caralcTy;

	@Column(name = "ROUTE_NO", columnDefinition = ROUTE_NO_LNG_07)
	private String routeNo;

	@Column(name = "driver_nm", columnDefinition = USER_NAME_LNG_10)
	private String driverNm;

	@Column(name = "TRNSPRT_CMPNY", columnDefinition = ANY_ENUMS_LNG_20)
	private String trnsprtCmpny;

	@Column(name = "VHCLE_TY", columnDefinition = ANY_ENUMS_LNG_20)
	private String vhcleTy;

	@Column(name = "DRIVE_STTUS", columnDefinition = ANY_ENUMS_LNG_20)
	private String driveSttus;

	@Column(name = "LAT", precision=9, scale=6)
	private BigDecimal lat;

	@Column(name = "LNG", precision=9, scale=6)
	private BigDecimal lng;

	@Column(name = "ADRES", columnDefinition = ADDRESS_NNG_100)
	private String adres;

	@Column(name = "AZ")
	private int az;

	@Column(name = "CTNU_DRIVE_TIME")
	private int ctnuDriveTime;

	@Column(name = "ARVL_DELAY_RISK_YN")
	private boolean arvlDelayRiskYn;

	@Column(name = "ARVL_DELAY_YN")
	private boolean arvlDelayYn;

	@Column(name = "TEMPT1", precision=5, scale=1)
	private BigDecimal tempt1;

	@Column(name = "TEMPT2", precision=5, scale=1)
	private BigDecimal tempt2;

	@Column(name = "MILEG", precision=4, scale=1)
	private BigDecimal mileg;

	@Column(name = "WT_SM", precision=4, scale=1)
	private BigDecimal wtSm;

	@Column(name = "DLVY_DRIVE_DSTNC", precision=4, scale=1)
	private BigDecimal dlvyDriveDstnc;

	@Column(name = "EMPT_VHCL_MOVE_DSTNC", precision=4, scale=1)
	private String emptVhclMoveDstnc;

	@Column(name = "col4")
	private String col4;

	@Column(name = "TONKM", precision=4, scale=1)
	private BigDecimal tonkm;

	@Column(name = "FUEL_USGQTY", precision=4, scale=1)
	private BigDecimal fuelUsgqty;

	@Column(name = "CO2_DSCAMT", precision=4, scale=1)
	private BigDecimal co2Dscamt;

	@Column(name = "ENERGY_EFC_IDX", precision=4, scale=1)
	private BigDecimal energyEfcIdx;

	@Column(name = "UNIT_TONKM", precision=4, scale=1)
	private BigDecimal unitTonkm;

	@Column(name = "LAST_REPORT_DT")
	private Date lastReportDt;

}
