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
@Table(name = "T_LGIST_RULE_DTLS")
@Getter
@Setter
public class T_LGIST_RULE_DTLS extends T_ID {




	@Column(name = "MIN_DSTNC")	
	private int minDstnc;
	@Column(name = "MAX_DSTNC")	
	private int maxDstnc;
	@Column(name = "MIN_TIME")	
	private int minTime;
	@Column(name = "MAX_TIME")	
	private int maxTime;
	@Column(name = "MIN_DLVY_CNT")	
	private int minDlvyCnt;
	@Column(name = "MAX_DLVY_CNT")	
	private int maxDlvyCnt;
	@Column(name = "RTATE_RATE")	
	private String rtateRate;
	@Column(name = "RULE_SEQ")	
	private int seq;


	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "ID_LGIST_RULE_MSTR_FK", referencedColumnName = "ROW_ID")
	private T_LGIST_RULE_MSTR idLgistRuleMstrFk;

}
