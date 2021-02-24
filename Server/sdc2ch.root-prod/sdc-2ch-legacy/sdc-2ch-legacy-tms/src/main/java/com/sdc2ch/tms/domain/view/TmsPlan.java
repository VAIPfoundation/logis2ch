package com.sdc2ch.tms.domain.view;

import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_20;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Immutable;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sdc2ch.tms.io.TmsPlanIO;

import lombok.Getter;
import lombok.Setter;

 
@Entity
@Immutable
@Setter
@Getter
@Table(name = "V_2CH_M_PLAN")
public class TmsPlan implements TmsPlanIO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RowID", updatable = false, nullable = false)
	private Long id;
	@Column(name = "Center_Cd", updatable = false, nullable = false)
	private String fctryCd;
	@Column(name = "Delivery_Date", updatable = false, nullable = false)
	private String dlvyDe;
	@Column(name = "Batch_No", updatable = false, nullable = false)
	private String batchNo;
	@Column(name = "Route_No", updatable = false, nullable = false)
	private String routeNo;
	@Column(name = "CONF_RTATE_RATE", updatable = false, nullable = false)	
	private String confRtateRate;
	@Column(name = "Stop_Seq", updatable = false, nullable = false)
	private int stopSeq;
	@Column(name = "Stop_Type", updatable = false, nullable = false)
	private String stopTy;
	@Column(name = "Stop_Cd", updatable = false, nullable = false)
	private String stopCd;
	@Column(name = "Stop_Nm", updatable = false, nullable = false)
	private String dlvyLoNm;
	@Column(name = "Addr_Basic", updatable = false, nullable = false)
	private String addr;
	@Column(name = "New_Req_Time_SAT", updatable = false, nullable = false)
	private String arriveSatTime;
	@Column(name = "New_Req_Time_SUN", updatable = false, nullable = false)
	private String arriveSunTime;
	@Column(name = "New_Req_Time", updatable = false, nullable = false)
	private String arriveDayTime;
	@Column(name = "Car_Cd", updatable = false, nullable = false)
	private String vrn;
	@Column(name = "Driver_Cd", updatable = false, nullable = false)
	private String driverCd;
	@Column(name = "Driver_Nm", updatable = false, nullable = false)
	private String driverNm;
	@Column(name = "Dock_No", updatable = false, nullable = false)
	private String dockNo;
	@Column(name = "ShipSeq", updatable = false, nullable = false)
	private String shipSeq;
	@Column(name = "Car_Weight", updatable = false, nullable = false)
	private String carWegit;


	@Column(name = "latitude", updatable = false, nullable = false)
	private String lat;
	@Column(name = "Longitude", updatable = false, nullable = false)
	private String lng;
	@Column(name = "Caralc_Ty", updatable = false, nullable = false)
	private String caraclTy;
	@Column(name = "Mobile_No", updatable = false, nullable = false)
	private String mobileNo;
	@Column(name = "isEmpty", updatable = false, nullable = false)
	private Boolean empty;
	
	@Column(name = "Arrive_ETime", updatable = false, nullable = false)
	private String arrivePlanTime;
	@Column(name = "Depart_ETime", updatable = false, nullable = false)
	private String departPlanTime;
	
	@Column(name = "LDNG_ST_DE", updatable = false, nullable = false)
	private String ldngStDe;
	@Column(name = "LDNG_ST", updatable = false, nullable = false)
	private String ldngSt;
	@Column(name = "LDNG_ED", updatable = false, nullable = false)
	private String ldngEd;
	@Column(name = "LDNG_ED_DE", updatable = false, nullable = false)
	private String ldngEdDe;
	
	@Column(name = "Sche_Start_Date", updatable = false, nullable = false)
	private String scheDlvyStDe;
	@Column(name = "Sche_Start_Time", updatable = false, nullable = false)
	private String scheDlvyStTime;
	@Column(name = "Sche_End_Date", updatable = false, nullable = false)
	private String scheDlvyEdDe;
	@Column(name = "Sche_End_Time", updatable = false, nullable = false)
	private String scheDlvyEdTime;
	
	
	@Column(name = "MANAGE_CD", updatable = false, nullable = false)
	private String manageCd;
	
	
	@Column(name = "Represent_Stop_Cd", updatable = false, nullable = false)
	private String bundledDlvyLc;
	@Column(name = "CUSTOMER_MOBILE_NO", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String cMobileNo;
	@Column(name = "CUSTOMER_STAFF_MOBILE_NO", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String csMobileNo;

	@Transient
	@JsonSerialize @JsonDeserialize
	private Integer boxQty;
	
	
	@Column(name = "Conf_Distance", updatable = false, nullable = false)	
	private String confDistance;
	@Column(name = "Conf_TollCost", updatable = false, nullable = false)	
	private String confTollCost;
	@Column(name = "CarOil", updatable = false, nullable = false)	
	private String carOil;
	
	@Column(name = "CarOil_Qty", updatable = false, nullable = false)	
	private String carOilQty;
	@Column(name = "FreezingOil_Qty", updatable = false, nullable = false)	
	private String freezingOilQty;
	@Column(name = "TCom_Cd", updatable = false, nullable = false)	
	private String tComCd;
	@Column(name = "LDNG_TY", updatable = false, nullable = false)	
	private String ldngTy;
	
	@Column(name = "TimeZone_Nm", updatable = false, nullable = false)	
	private String timeZoneNm;
	
	
	public int hashCode1() {
		return (fctryCd + dlvyDe).hashCode();
	}
	
	public int hashCode2() {
		return hashCode1() + (routeNo == null ? "null".hashCode() : routeNo.hashCode());
	}
	
	public int hashCode3() {
		return hashCode2() + (vrn + replace(driverCd)).hashCode();
	}
	
	private String replace(String str) {
		return StringUtils.isEmpty(str) ? str : str.replaceAll("-", "").replaceAll(" ", "").trim();
	}
	@Override
	public String toString() {
		return "V_CARALC_PLAN [id=" + id + ", fctryCd=" + fctryCd + ", dlvyDe=" + dlvyDe + ", routeNo=" + routeNo + ", arrivePlanTime=" + arrivePlanTime + ", mobileNo="
				+ replace(mobileNo) + "]";
	}
	public String getBundledDlvyLc() {
		return StringUtils.isEmpty(bundledDlvyLc) ? hashCode() + "" : bundledDlvyLc;
	}
	@Override
	public String getOrgBundledDlvyLc() {
		return bundledDlvyLc;
	}
	
	@Override
	public int getUniqueSequence() {
		return hashCode();
	}
}
