package com.sdc2ch.web.admin.repo.domain.alloc;

import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_20;
import static com.sdc2ch.require.repo.schema.GTConfig.TIME_FIELD_LNG_05;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sdc2ch.require.repo.T_ID;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="T_UNSTORING_CNFIRN")
@Getter
@Setter
public class T_UNSTORING_CNFIRN extends T_ID {
	
	
	@Column(name = "UNSTORING_DCSN_ROW_ID")
	private String unstoringDcsnRowId;

	@Column(name = "DELAY_String")
	private int delayString;

	@Column(name = "DELAY_RESN_CD", columnDefinition = ANY_ENUMS_LNG_20)
	private String delayResnCd;

	@Column(name = "DELAY_RESN")
	private String delayResn;

	@Column(name = "PASSAGE_String", columnDefinition = TIME_FIELD_LNG_05)
	private String passageString;

	@Column(name = "PASSAGE_DFFRNC")
	private int passageDffrnc;

	@Column(name = "LDNG_EXPC_String", columnDefinition = TIME_FIELD_LNG_05)
	private String ldngExpcString;

	@Column(name = "LDNG_STD_String", columnDefinition = TIME_FIELD_LNG_05)
	private String ldngStdString;

	@Column(name = "LDNG_ADJUST_String", columnDefinition = TIME_FIELD_LNG_05)
	private String ldngAdjustString;

	@Column(name = "PARKNG_String", columnDefinition = TIME_FIELD_LNG_05)
	private String parkngString;

	@Column(name = "LDNG_String", columnDefinition = TIME_FIELD_LNG_05)
	private String ldngString;

	@Column(name = "PARKNG_LDNG_DFFRNC")
	private int dffrnc;

}
