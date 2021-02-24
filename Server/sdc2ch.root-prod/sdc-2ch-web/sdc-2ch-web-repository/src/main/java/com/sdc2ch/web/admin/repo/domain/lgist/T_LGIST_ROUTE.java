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
@Table(name = "T_LGIST_ROUTE")
@Getter
@Setter
public class T_LGIST_ROUTE extends T_ID {





	@Column(name = "ROUTE_NO")	
	private String routeNo;


	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "ID_LGIST_MODEL_FK", referencedColumnName = "ROW_ID")
	private T_LGIST_MODEL idLgistModelFk;

}
