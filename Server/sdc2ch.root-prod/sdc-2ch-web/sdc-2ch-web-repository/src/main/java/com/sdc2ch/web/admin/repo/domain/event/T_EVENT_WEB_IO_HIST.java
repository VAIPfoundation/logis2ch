package com.sdc2ch.web.admin.repo.domain.event;

import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_20;
import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ID_LNG_20;
import static com.sdc2ch.require.repo.schema.GTConfig.ROUTE_NO_LNG_07;
import static com.sdc2ch.require.repo.schema.GTConfig.USER_ID_LNG_10;
import static com.sdc2ch.require.repo.schema.GTConfig.VRN_LNG_10;
import static com.sdc2ch.require.repo.schema.GTConfig.YYYYMMDD_08;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sdc2ch.require.repo.T_ID;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "T_EVENT_WEB_IO_HIST")
@Getter
@Setter
@ToString
public class T_EVENT_WEB_IO_HIST extends T_ID {

	@Column(name = "EVENT_TY", columnDefinition = ANY_ENUMS_LNG_20)
	private String evnetType;
	@Column(name = "EVENT_DT")
	private Date eventDt;
	@Column(name = "ALC_GROUP_ID_FK")
	private Long allocatedId;
	@Column(name = "REG_ID", columnDefinition = USER_ID_LNG_10)
	private String regId;
	@Column(name = "VRN", columnDefinition = VRN_LNG_10)
	private String vrn;
	@Column(name = "DEVY_DE", columnDefinition = YYYYMMDD_08)
	private String dlvyDe;
	@Column(name = "ROUTE_NO", columnDefinition = ROUTE_NO_LNG_07)
	private String routeNo;

	@Column(name = "FCTRY_CD", columnDefinition = ANY_ID_LNG_20)
	private String fctryCd;

}
