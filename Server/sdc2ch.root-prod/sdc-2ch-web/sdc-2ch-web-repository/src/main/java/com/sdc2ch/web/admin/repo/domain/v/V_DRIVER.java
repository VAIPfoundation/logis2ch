package com.sdc2ch.web.admin.repo.domain.v;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Immutable;

import com.sdc2ch.repo.io.TmsCarIO;
import com.sdc2ch.repo.io.TmsDriverIO;
import com.sdc2ch.require.domain.IUser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Immutable
@Table(name="V_DRIVER")
public class V_DRIVER implements TmsDriverIO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ROW_ID")
	private Long id;
	@Column(name = "Driver_Cd")
	private String driverCd;
	@Column(name="Driver_Nm")
	private String dirverNm;
	@Column(name="MOBILE_NO")
	private String mobileNo;
	@Column(name="FCTRY_ROW_ID")
	private String fctryCd;
	@Column(name="TELL_NO")
	private String telNo;
	
	@Transient
	private IUser user;
	
	@Transient
	private TmsCarIO car;
	
	@Override
	public String getUserDetailsId() {
		return driverCd;
	}
	@Override
	public String name() {
		return dirverNm;
	}
	public String getMobileNo() {
		return mobileNo == null ? null : mobileNo.replace("-", "").replaceAll(" ", "");
	}
	@Transient
	@Override
	public String getFctryTel() {
		return FactoryTel.findTel(fctryCd);
	}

}
