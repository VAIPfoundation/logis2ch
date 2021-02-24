package com.sdc2ch.web.admin.repo.domain.alloc;

import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_20;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.sdc2ch.require.repo.T_ID;
import com.sdc2ch.web.admin.repo.domain.alloc.type.AlramSetupType;

import lombok.Getter;
import lombok.Setter;



@Entity
@Table(name = "T_ALRAM_SETUP", uniqueConstraints=@UniqueConstraint(columnNames={"FCTRY_CD", "SETUP_TY"}))
@Getter
@Setter
public class T_ALRAM_SETUP extends T_ID{
	@Column(name = "FCTRY_CD", columnDefinition = ANY_ENUMS_LNG_20)
	private String fctryCd;

	@Column(name="SETUP_TY", nullable=false)
	@Enumerated(EnumType.STRING)
	private AlramSetupType setupTy;

	@Column(name="VALUE")
	private String value;
}
