package com.sdc2ch.web.admin.repo.domain.lgist;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@MappedSuperclass
public abstract class T_ID {

	@Id
	@Setter
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ROW_ID")
	protected Long id;

	@Column(name = "CREATE_DT", updatable = false, nullable = false)
	@JsonIgnore
	protected Date createDt;
	@Column(name = "UPDATE_DT")
	@JsonIgnore
	protected Date updateDt;

	@PrePersist
	protected void onPersist() {
		this.createDt = this.updateDt = Calendar.getInstance().getTime();
	}
	
    @PreUpdate
    protected void onUpdate() {
            this.updateDt = Calendar.getInstance().getTime();
    }
}
