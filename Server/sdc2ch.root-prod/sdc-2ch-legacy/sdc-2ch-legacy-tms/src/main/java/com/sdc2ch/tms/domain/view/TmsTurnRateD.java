package com.sdc2ch.tms.domain.view;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import com.sdc2ch.tms.io.TmsTurnRateIO;

import lombok.Getter;
import lombok.Setter;

 
@Entity
@Immutable
@Setter
@Getter
@Table(name = "V_2CH_TURN_RATE_D")
public class TmsTurnRateD implements TmsTurnRateIO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RowID", updatable = false, nullable = false)
	private Long id;
	
	@Column(name = "Center_Cd", updatable = false, nullable = false)
	private String fctryCd;
	@Column(name = "Bach_Type", updatable = false, nullable = false)
	private String caralcTy;
	@Column(name = "Min_Val", updatable = false, nullable = false)
	private int minDistance;
	@Column(name = "Max_Val", updatable = false, nullable = false)
	private int maxDistance;
	@Column(name = "Min_Landing", updatable = false, nullable = false)
	private int minLdng;
	@Column(name = "Max_Landing", updatable = false, nullable = false)
	private int maxLdng;
	@Column(name = "Min_Time", updatable = false, nullable = false)
	private int minTime;
	@Column(name = "Max_Time", updatable = false, nullable = false)
	private int maxTime;
	@Column(name = "TurnRate", updatable = false, nullable = false)
	private float turnRate;
	
}
