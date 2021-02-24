package com.sdc2ch.web.admin.repo.domain.sttus;

import static com.sdc2ch.require.repo.schema.GTConfig.ANY_NAME_LNG_20;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.sdc2ch.require.repo.T_ID;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="T_APP_STTUS_HIST")
@Getter
@Setter
public class T_APP_STTUS_HIST extends T_ID {

	@Column(name="APP_NAME", columnDefinition=ANY_NAME_LNG_20)
	private String appName;

	
	@Column(name="STTUS")
	private boolean sttus;

	
	@Lob
	@Column(name="MSSAGE")
	private String mssage;

	
	@Column(name="CNFIRM_DT")
	private Date cnfirmDt;
}
