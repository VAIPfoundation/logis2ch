package com.sdc2ch.web.admin.repo.domain.lgist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "T_LGIST_MODEL")
@Getter
@Setter
public class T_LGIST_MODEL extends T_ID {

	
	public enum TableType {
		DELIVERY,
		TRANSPORT
	}


	@Column(name = "MODEL_NM")	
	private String modelNm;
	@Column(name = "LNG")	
	private String lng;
	@Column(name = "LAT")	
	private String lat;
	@Column(name = "ADRES")	
	private String adres;
	@Column(name = "POINT_NM")	
	private String pointNm;
	@Column(name = "DAY_CONFIG1")	
	private String dayConfig1;
	@Column(name = "DAY_CONFIG2")	
	private String dayConfig2;
	@Column(name = "DAY_CONFIG3")	
	private String dayConfig3;
	@Column(name = "DAY_CONFIG4")	
	private String dayConfig4;
	@Column(name = "DAY_CONFIG5")	
	private String dayConfig5;
	@Column(name = "DAY_CONFIG6")	
	private String dayConfig6;
	@Column(name = "DAY_CONFIG7")	
	private String dayConfig7;
	@Column(name = "DAY_USE1")	
	private String dayUse1;
	@Column(name = "DAY_USE2")	
	private String dayUse2;
	@Column(name = "DAY_USE3")	
	private String dayUse3;
	@Column(name = "DAY_USE4")	
	private String dayUse4;
	@Column(name = "DAY_USE5")	
	private String dayUse5;
	@Column(name = "DAY_USE6")	
	private String dayUse6;
	@Column(name = "DAY_USE7")	
	private String dayUse7;
	@Column(name = "ANALS_TY")	
	private String analsTy;
	@Column(name = "START_DE")	
	private String startDe;
	@Column(name = "END_DE")	
	private String endDe;
	@Column(name = "FCTRY_CD")	
	private String fctryCd;
	@Column(name = "STTUS")	
	private int sttus;
	@Column(name = "TOTAL_CNT")	
	private int totalCnt;
	@Column(name = "ANALS_CNT")	
	private int analsCnt;
	@Column(name = "COST_BEFORE")	
	private String costBefore;
	@Column(name = "COST_AFTER")	
	private String costAfter;
	@Column(name = "ANALS_START_DT")	
	private Date analsStartDt;
	@Column(name = "ANALS_END_DT")	
	private Date analsEndDt;
	@Column(name = "REG_DT")	
	private String regDt;
	@Column(name = "REG_USER_ID")	
	private String regUserId;
	@Column(name = "UPDT_DT")	
	private String updtDt;
	@Column(name = "UPDT_USER_ID")	
	private String updtUserId;
	@Column(name = "USE_YN")	
	private boolean useYn;

	@Column(name = "LGIST_TYPE", nullable = false)	
	@Enumerated(EnumType.STRING)
	private TableType lgistTy = TableType.DELIVERY;
	
	@Column(name = "FCTRY_TARGET")
	private String fctryTarget;

	@JsonSerialize
	@JsonDeserialize

	@ManyToOne

	@JoinColumn(name = "ID_LGIST_RULE_MSTR_FK", referencedColumnName = "ROW_ID")
	private T_LGIST_RULE_MSTR idLgistRuleMstrFk;


	@JsonSerialize
	@JsonDeserialize
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "idLgistModelFk", cascade=CascadeType.ALL )
	private List<T_LGIST_ROUTE> models = new ArrayList<>();
	
	@JsonSerialize
	@JsonDeserialize
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "productModelFk", cascade=CascadeType.ALL )
	private List<T_LGIST_PRODUCT_STD> products = new ArrayList<>();


	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "idLgistModelFk")
	private List<T_ROUTE_PATH_MATRIX6> matrixes = new ArrayList<>();

}
