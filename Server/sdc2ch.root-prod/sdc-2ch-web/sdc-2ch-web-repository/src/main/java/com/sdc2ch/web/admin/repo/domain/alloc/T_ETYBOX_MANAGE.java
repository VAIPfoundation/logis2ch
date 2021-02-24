package com.sdc2ch.web.admin.repo.domain.alloc;

import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_20;
import static com.sdc2ch.require.repo.schema.GTConfig.PATH_LNG_100;
import static com.sdc2ch.require.repo.schema.GTConfig.SHOT_CONTENTS_LNG_100;
import static com.sdc2ch.require.repo.schema.GTConfig.USER_ID_LNG_10;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sdc2ch.require.repo.T_ID;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="T_ETY_BOX_MANAGE")
@Getter
@Setter
public class T_ETYBOX_MANAGE extends T_ID {

	@Column(name = "CARALC_PLAN_DTLS_ROW_ID")
	private long caralcPlanDtlsRowId;

	@Column(name = "DLVY_LC_ETY_BOX_TY01")
	private int dlvyLcEtyBoxTy01;

	@Column(name = "DLVY_LC_ETY_BOX_TY02")
	private int dlvyLcEtyBoxTy02;

	@Column(name = "DLVY_LC_ETY_BOX_TY03")
	private int dlvyLcEtyBoxTy03;

	@Column(name = "DRIVER_ETY_BOX_TY01")
	private int driverEtyBoxTy01;

	@Column(name = "DRIVER_ETY_BOX_TY02")
	private int driverEtyBoxTy02;

	@Column(name = "DRIVER_ETY_BOX_TY03")
	private int driverEtyBoxTy03;

	@Column(name = "DCSN_TY_CD", columnDefinition = ANY_ENUMS_LNG_20)
	private String dcsnTyCd;

	@Column(name = "DCSN_TY_NM", columnDefinition = SHOT_CONTENTS_LNG_100)
	private String dcsnTyNm;

	@Column(name = "IMG_FILE_PATH", columnDefinition = PATH_LNG_100)
	private String imgFilePath;

	@Column(name = "REG_DT")
	private Date regDt;

	@Column(name = "ETY_BOX_WORKER_ID", columnDefinition = USER_ID_LNG_10)
	private String etyBoxWorkerId;

	@Column(name = "ETY_BOX_WORKER_DCSN_DT")
	private Date etyBoxWorkerDcsnDt;


}
