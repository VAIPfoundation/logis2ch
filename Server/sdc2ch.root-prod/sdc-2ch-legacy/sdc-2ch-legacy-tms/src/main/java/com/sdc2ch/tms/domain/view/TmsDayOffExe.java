package com.sdc2ch.tms.domain.view;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import com.sdc2ch.tms.io.TmsDayOffExeIO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Immutable
@Table(name = "V_2CH_DAYOFF_EXE")
public class TmsDayOffExe implements TmsDayOffExeIO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RowID", updatable = false, nullable = false)
	private Long rowId;
	@Column(name = "Center_Cd", updatable = false, nullable = false)
	private String fctryCd;
	@Column(name = "Dayoff_Date", updatable = false, nullable = false)
	private String dayOffDe;
	@Column(name = "Car_Cd", updatable = false, nullable = false)
	private String vrn;
	@Column(name = "Dayoff_Type", updatable = false, nullable = false)
	private String dayOffTy;
	@Column(name = "Dayoff_Desc", updatable = false, nullable = false)
	private String dayOffDesc;
	@Column(name = "Dayoff_Cnt", updatable = false, nullable = false)
	private BigDecimal dayOffCnt;
	@Column(name = "Reg_Flag", updatable = false, nullable = false)
	private String regFlag;
	@Column(name = "Reg_User_Id", updatable = false, nullable = false)
	private String regUserId;
	@Column(name = "Reg_Datetime", updatable = false, nullable = false)
	private Date regDateTime;
	@Column(name = "Dayoff_Comment", updatable = false, nullable = false)
	private String dayOffComment;

}
