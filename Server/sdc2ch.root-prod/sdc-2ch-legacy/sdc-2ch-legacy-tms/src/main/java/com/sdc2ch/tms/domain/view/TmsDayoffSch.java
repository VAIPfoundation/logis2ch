package com.sdc2ch.tms.domain.view;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Immutable;

import com.sdc2ch.tms.io.TmsDayOffIO;

import lombok.Getter;
import lombok.Setter;

@Entity
@Immutable
@Setter
@Getter
@Table(name = "V_2CH_DAYOFF_SCH")
public class TmsDayoffSch implements TmsDayOffIO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RowID", updatable = false, nullable = false)
	private Long id;
	@Column(name = "Center_Cd", updatable = false, nullable = false)
	private String centerCd;
	@Column(name = "Dayoff_Date", updatable = false, nullable = false)
	private String dayoffDate;
	@Column(name = "End_Date", updatable = false, nullable = false)
	private String endDate;
	@Column(name = "Car_Cd", updatable = false, nullable = false)
	private String carCd;
	@Column(name = "Dayoff_Desc", updatable = false, nullable = false)
	private String dayoffDesc;
	@Column(name = "Dayoff_Type", updatable = false, nullable = false)
	private String dayoffType;
	@Column(name = "Dayoff_Cnt", updatable = false, nullable = false)
	private Integer dayoffCnt;
	@Column(name = "Reg_Flag", updatable = false, nullable = false)
	private String regFlag;
	@Column(name = "Reg_User_Id", updatable = false, nullable = false)
	private String regUserId;
	@Column(name = "Reg_Datetime", updatable = false, nullable = false)
	private Date regDateTime;
	@Column(name = "Other_Comment", updatable = false, nullable = false)
	private String otherComment;
	@Column(name = "IsConfirm", updatable = false, nullable = false)
	private Boolean isConfirm;
	@Column(name = "Dayoff_Route", updatable = false, nullable = false)
	private String dayoffRoute;
	
		
		@Transient
		private Integer schId;
		@Transient
		private String schConfirm;
		@Transient
		private String schStartDate;
		@Transient
		private String schEndDate;
		@Transient
		private String schType;
		@Transient
		private String schUnit;
		@Transient
		private String schRouteNo;
		@Transient
		private String schBigo;
		@Transient
		private String schRegUserId;
		@Transient
		private String schRegDateTime;
		@Transient
		private Integer exeId;
		@Transient
		private String exeDayoffDate;
		@Transient
		private String exeType;
		@Transient
		private String exeUnit;
		@Transient
		private String exeBigo;
		@Transient
		private String exeRegUserId;
		@Transient
		private String exeRegDateTime;
		@Transient
		private String exeDayoffDesc;
		@Transient
		private String schDayoffDesc;
		
		
		@Transient
		private String targetDate;
		@Transient
		private Double schCount;
		@Transient
		private Integer schCountMax;
		
		
		@Transient
		private String message;
	
}
