package com.sdc2ch.web.admin.repo.domain.alloc;

import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_20;
import static com.sdc2ch.require.repo.schema.GTConfig.MOBILE_NUM_LNG_13;
import static com.sdc2ch.require.repo.schema.GTConfig.USER_ID_LNG_10;
import static com.sdc2ch.require.repo.schema.GTConfig.VRN_LNG_10;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sdc2ch.require.repo.T_ID;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="T_ORDER_ALC_INFO")
@Getter
@Setter
public class T_ORDER_ALC_INFO extends T_ID {

	@Column(name = "SN")
	private int sn;

	@Column(name = "VHCLE_TY", columnDefinition = ANY_ENUMS_LNG_20)
	private String vhcleTy;

	@Column(name = "VRN", columnDefinition = VRN_LNG_10)
	private String vrn;

	@Column(name = "DRIVER_MOBILE_NO", columnDefinition = MOBILE_NUM_LNG_13)
	private String driverMobileNo;

	@Column(name = "DSTNC")
	private int dstnc;

	@Column(name = "CARALC_PALLET_QY")
	private int caralcPalletQy;

	@Column(name = "CARALC_WT", precision=4, scale=1)
	private BigDecimal caralcWt;

	@Column(name = "CARALC_REG_DT")
	private Date caralcRegDate;

	@Column(name = "caralc_reg_user_id", columnDefinition = USER_ID_LNG_10)
	private String caralcRegUser;

	@Column(name = "LAST_REPORT_LAT", precision=9, scale=6)
	private BigDecimal lat;
	
	@Column(name = "LAST_REPORT_LNG", precision=9, scale=6)
	private BigDecimal lng;

	@Column(name = "LAST_REPORT_DT")
	private Date lastReportDt;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="order_id")
	private T_ORDER_MST order;

}
