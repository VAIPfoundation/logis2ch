package com.sdc2ch.web.admin.repo.domain.v;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Where;

import com.sdc2ch.repo.io.TmsCarIO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Immutable
@Table(name="V_VHCLE")
@Where(clause="USE_YN=1")
public class V_VHCLE implements TmsCarIO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ROW_ID")
	private Long id;
	@Column(name="VRN")
	private String vrn;
	@Column(name="VHCLE_TY")
	private BigDecimal wegith;
	@Column(name="LDNG_TY")
	private String ldngTy;


	@Column(name="FCTRY_CD")
	private String fctryCd;
	@Column(name="Driver_Cd")
	private String driverCd;
	@Column(name="MOBILENO")
	private String mobileNo;
	@Column(name="SET_ID")
	private String setId;
	@Column(name="DRIVER_NM")
	private String driverNm;
	@Column(name="TRNSPRT_CMPNY_CD")
	private String trnsprtCmpnyCd;
	@Column(name="TRNSPRT_CMPNY")
	private String trnsprtCmpny;

}
