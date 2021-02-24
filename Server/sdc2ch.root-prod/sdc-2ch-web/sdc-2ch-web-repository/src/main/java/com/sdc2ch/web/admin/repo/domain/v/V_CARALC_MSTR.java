package com.sdc2ch.web.admin.repo.domain.v;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;
import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.Setter;

 
@Entity
@Getter
@Setter
@Immutable
public class V_CARALC_MSTR {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ROWID", updatable = false, nullable = false)
	private Long id;

	@Column(name = "Center_Cd", updatable = false, nullable = false)
	private String fctryCd;
	@Column(name = "Delivery_Date", updatable = false, nullable = false)
	private String dlvyDe;
	@Column(name = "Route_No", updatable = false, nullable = false)
	private String routeNo;
	@Column(name = "Batch_No", updatable = false, nullable = false)
	private String batchNo;
	@Column(name = "Caralc_Ty", updatable = false, nullable = false)
	private String caraclTy;
	@Column(name = "Time_Cd", updatable = false, nullable = false)
	private String timeCd;
	@Column(name = "Sche_Method", updatable = false, nullable = false)
	private String scheMethod;
	@Column(name = "Area_Cd", updatable = false, nullable = false)
	private String areaCd;
	@Column(name = "Zone_Cd", updatable = false, nullable = false)
	private String zoneCd;
	@Column(name = "Car_Cd", updatable = false, nullable = false)
	private String vrn;
	@Column(name = "TCom_Cd", updatable = false, nullable = false)
	private String tcomCd;
	@Column(name = "Driver_Cd", updatable = false, nullable = false)
	private String driverCd;
	@Column(name = "Driver_Nm", updatable = false, nullable = false)
	private String driverNm;
	@Column(name = "InitialStationID", updatable = false, nullable = false)
	private String initalStationID;
	@Column(name = "FinalStationID", updatable = false, nullable = false)
	private String finalStationID;
	@Column(name = "Section_Cd", updatable = false, nullable = false)
	private String sectionCd;
	@Column(name = "Dock_No", updatable = false, nullable = false)
	private String dockNo;
	@Column(name = "Sche_Start_Date", updatable = false, nullable = false)
	private String scheStartDate;
	@Column(name = "Sche_Start_Time", updatable = false, nullable = false)
	private String scheStartTime;
	@Column(name = "Sche_End_Date", updatable = false, nullable = false)
	private String scheEndDate;
	@Column(name = "Sche_End_Time", updatable = false, nullable = false)
	private String scheEndTime;
	@Column(name = "Sche_Distance", updatable = false, nullable = false)
	private Integer scheDistance;
	@Column(name = "isEmpty", updatable = false, nullable = false)
	private Boolean isempty;
	@Column(name = "Reg_Flag", updatable = false, nullable = false)
	private String regFlag;
	@Column(name = "Dock_Prior_NO", updatable = false, nullable = false)
	private Integer dockPriorNO;
	@Column(name = "Sub_Section_Cd", updatable = false, nullable = false)
	private String subSectionCd;
	@Column(name = "startTime", updatable = false, nullable = false)
	private String ldngSt;
	@Column(name = "EndTime", updatable = false, nullable = false)
	private String ldngEd;
	@Column(name = "Mobile_No", updatable = false, nullable = false)
	private String mobileNo;
	@Column(name = "Ship_Qty", updatable = false, nullable = false)
	private int shipQty;

	@Column(name = "CarOil", updatable = false, nullable = false)
	private String CarOil;

	@Column(name = "Conf_TurnRate", updatable = false, nullable = false)
	private String confTurnrate;

	@Column(name = "CONF_RTATE_RATE", updatable = false, nullable = false)
	private String confRtateRate;

	@Column(name = "Conf_Distance", updatable = false, nullable = false)
	private String confDistance;

	@Column(name = "Conf_TollCost", updatable = false, nullable = false)
	private String confTollcost;

	@Column(name = "Car_Weight", updatable = false, nullable = false)
	private String carWeight;



	
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
		return "V_CARALC_MSTR [routeNo=" + routeNo +  ", fctryCd=" + fctryCd + ", vrn=" +vrn+ "dlvyDe=" + dlvyDe + ", arrivePlanTime=" + scheStartTime + ", mobileNo="
				+ replace(mobileNo) + "]";
	}
	private String replaceRoute(String routeNo) {
		return routeNo.split("_")[0];
	}



}
