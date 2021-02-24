package com.sdc2ch.web.admin.repo.domain.v;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import org.hibernate.annotations.Immutable;

import com.sdc2ch.web.admin.repo.domain.v.pk.V_SNITAT_CHCK_TABLE_MONTHLY_PK;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Immutable
@IdClass(V_SNITAT_CHCK_TABLE_MONTHLY_PK.class)
@ToString
public class V_SNITAT_CHCK_TABLE_MONTHLY {

	@Id
	@Column(name = "fctry_cd", updatable = false, nullable = false)	
	private String fctryCd;
	@Id
	@Column(name = "vrn", updatable = false, nullable = false)	
	private String vrn;
	@Id
	@Column(name = "driver_cd", updatable = false, nullable = false)	
	private String driverCd;
	@Column(name = "driver_nm", updatable = false, nullable = false)	
	private String driverNm;
	@Column(name = "trnsprt_cmpny", updatable = false, nullable = false)	
	private String trnsprtCmpny;
	@Column(name = "vhcle_ty", updatable = false, nullable = false)	
	private Double vhcleTy;
	@Column(name = "yyyy", updatable = false, nullable = false)	
	private String yyyy;
	@Column(name = "month1_snitat" ,updatable = false, nullable = false)	
	private int month1Snitat;
	@Column(name = "month2_snitat",updatable = false, nullable = false)	
	private int month2Snitat;
	@Column(name = "month3_snitat",updatable = false, nullable = false)	
	private int month3Snitat;
	@Column(name = "month4_snitat",updatable = false, nullable = false)	
	private int month4Snitat;
	@Column(name = "month5_snitat",updatable = false, nullable = false)	
	private int month5Snitat;
	@Column(name = "month6_snitat",updatable = false, nullable = false)	
	private int month6Snitat;
	@Column(name = "month7_snitat",updatable = false, nullable = false)	
	private int month7Snitat;
	@Column(name = "month8_snitat",updatable = false, nullable = false)	
	private int month8Snitat;
	@Column(name = "month9_snitat",updatable = false, nullable = false)	
	private int month9Snitat;
	@Column(name = "month10_snitat",updatable = false, nullable = false)	
	private int month10Snitat;
	@Column(name = "month11_snitat",updatable = false, nullable = false)	
	private int month11Snitat;
	@Column(name = "month12_snitat",updatable = false, nullable = false)	
	private int month12Snitat;
	@Column(name = "month1_dlvy",updatable = false, nullable = false)	
	private int month1Dlvy;
	@Column(name = "month2_dlvy",updatable = false, nullable = false)	
	private int month2Dlvy;
	@Column(name = "month3_dlvy",updatable = false, nullable = false)	
	private int month3Dlvy;
	@Column(name = "month4_dlvy",updatable = false, nullable = false)	
	private int month4Dlvy;
	@Column(name = "month5_dlvy",updatable = false, nullable = false)	
	private int month5Dlvy;
	@Column(name = "month6_dlvy",updatable = false, nullable = false)	
	private int month6Dlvy;
	@Column(name = "month7_dlvy",updatable = false, nullable = false)	
	private int month7Dlvy;
	@Column(name = "month8_dlvy",updatable = false, nullable = false)	
	private int month8Dlvy;
	@Column(name = "month9_dlvy",updatable = false, nullable = false)	
	private int month9Dlvy;
	@Column(name = "month10_dlvy",updatable = false, nullable = false)	
	private int month10Dlvy;
	@Column(name = "month11_dlvy",updatable = false, nullable = false)	
	private int month11Dlvy;
	@Column(name = "month12_dlvy",updatable = false, nullable = false)	
	private int month12Dlvy;
}
