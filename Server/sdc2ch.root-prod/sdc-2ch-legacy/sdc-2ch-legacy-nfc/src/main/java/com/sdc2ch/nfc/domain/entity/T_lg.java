package com.sdc2ch.nfc.domain.entity;

import static com.sdc2ch.nfc.domain.dynamic.table.T_lgNameDynamicTable.TABLE_NAME;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.querydsl.core.annotations.QueryProjection;
import com.sdc2ch.nfc.io.NfcEventIO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = TABLE_NAME, catalog = TABLE_NAME)
public class T_lg implements NfcEventIO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "EVTLGUID", length = 11)
	private int id;
	@Column(name = "DEVDT", length = 11)
	private int devdt;
	@Column(name = "DEVUID", length = 11)
	private int devuid;
	@Column(name = "USRID", length = 32)
	private String usrid;
	@Column(name = "EVT", length = 11)
	private Integer evt;
	@Column(name = "SRVDT", length = 11)
	private Date srvdt;


	






















	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
