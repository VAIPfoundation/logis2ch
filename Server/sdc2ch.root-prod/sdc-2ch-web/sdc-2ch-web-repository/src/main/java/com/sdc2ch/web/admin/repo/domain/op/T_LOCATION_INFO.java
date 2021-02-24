package com.sdc2ch.web.admin.repo.domain.op;

import static com.sdc2ch.require.repo.schema.GTConfig.ADDRESS_NNG_100;
import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_20;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sdc2ch.repo.io.ShippingID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class T_LOCATION_INFO {
	
	
	@Id
	@EmbeddedId
	@Column(name = "ROW_ID")
	private ShippingID id;
	
	@Column(name = "DATA_DT")
	private Date dataDate;	
	@Column(name = "LAT", precision=9, scale=6)
	private BigDecimal lat;
	@Column(name = "LNG", precision=9, scale=6)
	private BigDecimal lng;
	@Column(name = "ACCURACY", precision=5, scale=1)
	private BigDecimal accuracy;
	@Column(name = "DGREE")
	private int dgree;		
	@Column(name = "SPEED")
	private int speed;		
	@Column(name = "DAY_DISTANCE")
	private int distance;	
	@Column(name = "ACC_DISTANCE")
	private long accDistance; 
	@Column(name = "ALTITUDE")
    private int altitude;
	@Column(name = "EVENT", columnDefinition = ANY_ENUMS_LNG_20)
    private String event;		
	@Column(name = "ADRES", columnDefinition = ADDRESS_NNG_100)
	private String adres;
    
	@Column(name = "UPDATE_DT")
	@JsonIgnore
	protected Date updateDt;

	@PrePersist
	protected void onPersist() {
		this.updateDt = Calendar.getInstance().getTime();
	}
	
    @PreUpdate
    protected void onUpdate() {
            this.updateDt = Calendar.getInstance().getTime();
    }
}
