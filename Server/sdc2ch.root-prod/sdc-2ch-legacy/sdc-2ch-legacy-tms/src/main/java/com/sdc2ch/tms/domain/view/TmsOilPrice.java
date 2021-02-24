package com.sdc2ch.tms.domain.view;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import com.sdc2ch.tms.io.TmsOilPriceIO;

import lombok.Getter;
import lombok.Setter;

 
@Entity
@Immutable
@Setter
@Getter
@Table(name = "V_2CH_OIL_PRICE")
public class TmsOilPrice implements TmsOilPriceIO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RowID", updatable = false, nullable = false)
	private Long id;
	@Column(name = "Start_Date", updatable = false, nullable = false)
	private String startDate;
	@Column(name = "End_Date", updatable = false, nullable = false)
	private String endDate;
	@Column(name = "oil_Price", updatable = false, nullable = false)
	private BigDecimal oilPrice;
	
}
