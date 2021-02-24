package com.sdc2ch.web.admin.repo.domain.v;

import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_20;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Immutable
@Table(name="V_DLVY_LC")
public class V_DLVY_LC {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ROWID")
	private Long id;
	@Column(name = "Center_Cd", updatable = false, nullable = false)
	private String fctryCd;
	@Column(name = "Stop_Cd", updatable = false, nullable = false)
	private String dlvyLcCd;
	@Column(name = "Stop_Nm", updatable = false, nullable = false)
	private String dlvyLcNm;
	@Column(name = "CUSTOMER_MOBILE_NO", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String cMobileNo;
	@Column(name = "CUSTOMER_STAFF_MOBILE_NO", updatable = false, nullable = false, columnDefinition = ANY_ENUMS_LNG_20)
	private String csMobileNo;
}
