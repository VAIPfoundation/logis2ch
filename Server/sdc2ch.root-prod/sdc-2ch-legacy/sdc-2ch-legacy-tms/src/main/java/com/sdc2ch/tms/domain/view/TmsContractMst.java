package com.sdc2ch.tms.domain.view;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import com.sdc2ch.tms.io.TmsContractIO;

import lombok.Getter;
import lombok.Setter;

 
@Entity
@Immutable
@Setter
@Getter
@Table(name = "V_2CH_CONTRACT_MST")
public class TmsContractMst implements TmsContractIO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RowID", updatable = false, nullable = false)
	private Long id;
	@Column(name = "CONTRACT_NO", updatable = false, nullable = false)
	private int contractNo;
	@Column(name = "TCom_Cd", updatable = false, nullable = false)
	private String tComCd;
	@Column(name = "Cont_Type", updatable = false, nullable = false)
	private String contType;
	@Column(name = "Start_Date", updatable = false, nullable = false)
	private String startDate;
	@Column(name = "End_Date", updatable = false, nullable = false)
	private String endDate;
	@Column(name = "is_Cancel", updatable = false, nullable = false)
	private boolean cancel;
	@Column(name = "Cancel_Date", updatable = false, nullable = false)
	private String cancelDate;
	@Column(name = "Car_Weight", updatable = false, nullable = false)
	private float carWeight;
	@Column(name = "Car_Type", updatable = false, nullable = false)
	private String carType;
	@Column(name = "offDuty_Type", updatable = false, nullable = false)
	private String offDutyType;
	@Column(name = "Month_Cost", updatable = false, nullable = false)
	private Integer monthCost;
	@Column(name = "Day_Cost", updatable = false, nullable = false)
	private Integer dayCost;
	
}
