package com.sdc2ch.web.admin.repo.domain.sys;

import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_20;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sdc2ch.require.repo.T_ID;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Entity
@Table(name="T_SYS_ACT_INFO_HIST")
@Getter
@Setter
@ToString
public class T_SYS_ACT_INFO_HIST extends T_ID {

	
	@Column(name="APP_ID")
	private String appId;

	
	@Column(name="APP_PID", columnDefinition = ANY_ENUMS_LNG_20)
	private String appPid;

	
	@Column(name="APP_NAME")
	private String appName;
	
	@Column(name="IP")
	private String ip;
	
	@Column(name="HOST")
	private String host;
	
	@Column(name="STATUS")
	private Boolean sttus;
	
	@Column(name="APP_VERSION")
	private String appVersion;
	
	@Column(name="SIZE")
	private Long size;
	
	@Column(name="PATH")
	private String path;
	
	@Column(name="DATA_DT")
	private Date dataDt;
}
