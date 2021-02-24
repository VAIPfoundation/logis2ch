package com.sdc2ch.web.admin.repo.domain.alloc;

import static com.sdc2ch.require.repo.schema.GTConfig.ADDRESS_NNG_100;
import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_20;
import static com.sdc2ch.require.repo.schema.GTConfig.ANY_NAME_LNG_20;
import static com.sdc2ch.require.repo.schema.GTConfig.MOBILE_NUM_LNG_13;
import static com.sdc2ch.require.repo.schema.GTConfig.USER_ID_LNG_10;
import static com.sdc2ch.require.repo.schema.GTConfig.USER_NAME_LNG_10;
import static com.sdc2ch.require.repo.schema.GTConfig.YYYYMMDD_08;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sdc2ch.require.repo.T_ID;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "T_ORDER_MST")
@Getter
@Setter
public class T_ORDER_MST extends T_ID {

	@Column(name = "DLVY_DE", columnDefinition = YYYYMMDD_08)
	private String dlvyDe;

	@Column(name = "START_LC_CD")
	private String startLcCd;

	@Column(name = "DSTN_CD")
	private String dstnCd;

	@Column(name = "DSTN_CHARGER_NM", columnDefinition = USER_NAME_LNG_10)
	private String dstnChargerNm;

	@Column(name = "DSTN_ADRES", columnDefinition = ADDRESS_NNG_100)
	private String dstnAdres;

	@Column(name = "DSTN_TELL_NO", columnDefinition = MOBILE_NUM_LNG_13)
	private String dstnTellNo;

	@Column(name = "GUDS_TY")
	private String gudsTy;

	@Column(name = "GUDS_NM", columnDefinition = ANY_NAME_LNG_20)
	private String gudsNm;

	@Column(name = "QY")
	private int qy;

	@Column(name = "WT", precision=8, scale=1)
	private BigDecimal wt;

	@Column(name = "PALLET_QY")
	private int palletQy;

	@Column(name = "ORDER_STTUS", columnDefinition = ANY_ENUMS_LNG_20)
	private String orderSttus;

	@Column(name = "ORDER_REGIST_DT")
	private Date orderRegistDt;

	@Column(name = "ORDER_REG_USER_ID", columnDefinition = USER_ID_LNG_10)
	private String orderRegUser;

	@Column(name = "ORDER_CNFIRM_DT")
	private Date orderCnfirmDt;

	@Column(name = "ORDER_CNFIRM_USER_ID", columnDefinition = USER_ID_LNG_10)
	private String orderCnfirmUser;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy="order")
	private List<T_ORDER_ALC_INFO> allocates;


}
