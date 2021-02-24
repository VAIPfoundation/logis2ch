package com.sdc2ch.web.admin.repo.domain.v;

import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_20;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;

import lombok.Getter;
import lombok.Setter;

 
@Entity
@Getter
@Setter
@Immutable
public class V_STATS_DRIVER_MONTHLY {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ROWID", updatable = false, nullable = false)
	private Long id;
	
	@Column(name = "FCTRY_NM", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String fctryNm;	
	@Column(name = "SETTLE_MONTH", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String settleMonth;	
	@Column(name = "TRNSPRT_CMPNY", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String trnsprtCmpny;	
	@Column(name = "VHCLE_TY", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String vhcleTy;	
	@Column(name = "VRN", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String vrn;	
	@Column(name = "DSTNC", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String dstnc;	
	@Column(name = "DSTNC_COST", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String dstncCost;	
	@Column(name = "PAY_OFFDAY", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String payOffday;	
	@Column(name = "RPAY_OFFDAY", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String rpayOffday;	
	@Column(name = "NOPAY_OFFDAY", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String nopayOffday;	
	@Column(name = "NOACT_OFFDAY", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String noactOffday;	
	@Column(name = "NOACT2_OFFDAY", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String noact2Offday;	
	@Column(name = "APPROVE_OFFDAY", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String approveOffday;	
	@Column(name = "WORK_OFFDAY", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String workOffday;	
	@Column(name = "CONF_OFFDAY", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String confOffday;	
	@Column(name = "WORKDAY", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String workday;	
	@Column(name = "RTATE_RATE", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String rtateRate;	
	@Column(name = "CONTRACT_COST", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String contractCost;	
	@Column(name = "BASIC_COST", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String basicCost;	
	@Column(name = "OIL_COST", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String oilCost;	
	@Column(name = "TOLL_COST", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String tollCost;	
	@Column(name = "BOX_COST", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String boxCost;	
	@Column(name = "WASH_COST", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String washCost;	
	@Column(name = "OTHER_COST", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String otherCost;	
	@Column(name = "TBOX_COST", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String tboxCost;	
	@Column(name = "SBOX_COST", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String sboxCost;	
	@Column(name = "YBOX_COST", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String yboxCost;	
	@Column(name = "TRANBOX_COST", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String tranboxCost;	
	@Column(name = "QUANTITY", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String quantity;	
	@Column(name = "WEIGHT", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String weight;	
	@Column(name = "VOLUME", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String volume;	
	@Column(name = "CAROIL_QTY", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String caroilQty;	
	@Column(name = "FREEZINGOIL_QTY", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String freezingoilQty;	
	@Column(name = "IS_2DRIVER", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String is2Driver;	
	@Column(name = "UPDOWN_SAM_BOX", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String updownSamBox;	
	@Column(name = "UPDOWN_SA_BOX", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String updownSaBox;	
	@Column(name = "UPDOWN_YO_BOX", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String updownYoBox;	
	@Column(name = "UPDOWN_PAPER_BOX", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String updownPaperBox;	
	@Column(name = "UPDOWN_BOX_COST", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String updownBoxCost;	
	@Column(name = "TOTAL_COST", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String totalCost;	
	
}
