package com.sdc2ch.web.admin.repo.domain.sttus;

import static com.sdc2ch.require.repo.schema.GTConfig.ANY_NAME_LNG_20;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="T_APP_STTUS")
@Getter
@Setter
public class T_APP_STTUS {

	
	@Id
	@Column(name="APP_NAME", columnDefinition=ANY_NAME_LNG_20)
	private String appName;

	
	@Column(name="STTUS")
	private boolean sttus;

	
	@Lob
	@Column(name="MSSAGE")
	private String mssage;

	
	@Column(name="CNFIRM_DT")
	private Date cnfirmDt;

	@Column(name = "CREATE_DT", updatable = false, nullable = false)
	private Date createDt;
	@Column(name = "UPDATE_DT")
	private Date updateDt;

	@PrePersist
	protected void onPersist() {
		this.createDt = this.updateDt = Calendar.getInstance().getTime();
	}

    @PreUpdate
    protected void onUpdate() {
            this.updateDt = Calendar.getInstance().getTime();
    }
}
