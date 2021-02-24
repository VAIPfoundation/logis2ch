package com.sdc2ch.web.admin.repo.domain.lgist;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "T_LGIST_PRODUCT_STD")
@Getter
@Setter
public class T_LGIST_PRODUCT_STD extends T_ID {

	@Column(name = "ITEM_CD")
	private String itemCd;
	@Column(name = "ITEM_NM")
	private String itemNm;
	@Column(name = "P1")
	private String p1;
	@Column(name = "P2")
	private String p2;
	@Column(name = "P3")
	private String p3;
	@Column(name = "P4")
	private String p4;
	@Column(name = "USE_YN")
	private boolean useYn;

	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "MODEL_ID", referencedColumnName = "ROW_ID")
	private T_LGIST_MODEL productModelFk;

}
