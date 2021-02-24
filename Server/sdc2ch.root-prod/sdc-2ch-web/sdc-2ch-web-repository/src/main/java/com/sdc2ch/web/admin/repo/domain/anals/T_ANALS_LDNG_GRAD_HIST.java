package com.sdc2ch.web.admin.repo.domain.anals;

import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_20;
import static com.sdc2ch.require.repo.schema.GTConfig.FACTORY_CD_LNG_04;
import static com.sdc2ch.require.repo.schema.GTConfig.ROUTE_NO_LNG_07;
import static com.sdc2ch.require.repo.schema.GTConfig.TIME_FIELD_LNG_05;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import com.sdc2ch.require.repo.T_ID;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "T_ANALS_LDNG_GRAD_HIST")
@Getter
@Setter
public class T_ANALS_LDNG_GRAD_HIST extends T_ID {
	
	@Column(name = "FCTRY_CD", columnDefinition = FACTORY_CD_LNG_04)
	private String fctryCd;

	@Column(name = "VHCLE_TY", columnDefinition = ANY_ENUMS_LNG_20)
	private String vhcleTy;

	@Column(name = "DLVY_TY", columnDefinition = ANY_ENUMS_LNG_20)
	private String dlvyTy;

	@Column(name = "ROUTE_NO", columnDefinition = ROUTE_NO_LNG_07)
	private String routeNo;

	@Column(name = "STD_TIME", columnDefinition = TIME_FIELD_LNG_05)
	private String stdTime;

	@Column(name = "ADJUST_TIME", columnDefinition = TIME_FIELD_LNG_05)
	private String adjustTime;

	@Column(name = "USE_YN")
	@ColumnDefault(value = "0")
	private boolean useYn;

	@Column(name = "REG_USER_ID")
	private String regUserId;

	@Column(name = "REG_DATE")
	private Date regDate;

}
