package com.sdc2ch.web.admin.repo.domain.v;

import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_20;
import static com.sdc2ch.require.repo.schema.GTConfig.FACTORY_CD_LNG_04;
import static com.sdc2ch.require.repo.schema.GTConfig.ROUTE_NO_LNG_07;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Immutable;

import com.sdc2ch.require.repo.T_ID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Immutable
@Table(name="V_ANALS_DLVY_STD_TIME")
public class V_ANALS_DLVY_STD_TIME extends T_ID {

	@Column(name = "FCTRY_CD", columnDefinition = FACTORY_CD_LNG_04)
	private String fctryCd;

	@Column(name = "VHCLE_TY", columnDefinition = ANY_ENUMS_LNG_20)
	private String vhcleTy;

	@Column(name = "CARALC_TY", columnDefinition = ANY_ENUMS_LNG_20)
	private String caralcTy;

	@Column(name = "ROUTE_NO", columnDefinition = ROUTE_NO_LNG_07)
	private String routeNo;

	
	@Column(name = "STD_TIME")
	private Long stdTime;

	
	@Column(name = "ADJUST_TIME")
	private Long adjustTime;

	@Column(name = "USE_YN")
	@ColumnDefault(value = "1")
	private boolean useYn;

	@Column(name = "REG_USER_ID")
	private String regUserId;

	@Column(name = "REG_DT")
	private Date regDt;

	@Column(name = "CARALC_TY_NM")
	private String caralcTyNm;

	@Column(name = "LAST_UPDATE_DT")
	private Date lastUpdateDt;

}
