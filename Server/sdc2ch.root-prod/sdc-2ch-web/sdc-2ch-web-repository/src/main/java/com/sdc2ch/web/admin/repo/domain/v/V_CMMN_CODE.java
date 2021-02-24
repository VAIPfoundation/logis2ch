package com.sdc2ch.web.admin.repo.domain.v;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;

import com.sdc2ch.repo.io.CommonCodeIO;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Immutable
public class V_CMMN_CODE implements CommonCodeIO {
	@Id
	@Column(name = "ROWID", updatable = false, nullable = false)
	private Long id;
	@Column(name = "GROUP_CD", updatable = false, nullable = false)
	private String groupCd;
	@Column(name = "GROUP_CD_NM", updatable = false, nullable = false)
	private String groupCdNm;
	@Column(name = "GROUP_CD_DESC", updatable = false, nullable = false)
	private String groupCdDesc;
	@Column(name = "CD", updatable = false, nullable = false)
	private String cd;
	@Column(name = "CD_NM", updatable = false, nullable = false)
	private String cdNm;
	@Column(name = "CD_ORDER_NO", updatable = false, nullable = false)
	private int cdOrderNo;
}
