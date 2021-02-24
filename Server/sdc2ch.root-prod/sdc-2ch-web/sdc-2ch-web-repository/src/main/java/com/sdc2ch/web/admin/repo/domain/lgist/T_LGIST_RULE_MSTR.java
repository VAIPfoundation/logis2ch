package com.sdc2ch.web.admin.repo.domain.lgist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "T_LGIST_RULE_MSTR")
@Getter
@Setter
public class T_LGIST_RULE_MSTR extends T_ID {

	@Column(name = "RULE_NM", unique = true)	
	private String ruleNm;
	@Column(name = "REG_USER_ID")	
	private String regUserId;
	@Column(name = "UPDT_USER_ID")	
	private String updtUserId;
	@Column(name = "USE_YN")	
	private boolean useYn;






	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "idLgistRuleMstrFk", cascade = CascadeType.ALL)
	private List<T_LGIST_RULE_DTLS> ruleDtls = new ArrayList<>();
	
	@Transient
	public T_LGIST_RULE_MSTR setNullDtls() {
		ruleDtls = null;
		return this;
	}
	
	public Date getCreateDt() {
		return createDt;
	};

}
