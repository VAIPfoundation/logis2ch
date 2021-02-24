package com.sdc2ch.prcss.eb.repo.domain;

import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_20;
import static com.sdc2ch.require.repo.schema.GTConfig.ROUTE_NO_LNG_07;
import static com.sdc2ch.require.repo.schema.GTConfig.YYYYMMDD_08;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sdc2ch.prcss.eb.io.EmptyboxIO;
import com.sdc2ch.require.repo.T_ID;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="T_EMPTYBOX_INFO", uniqueConstraints=@UniqueConstraint(columnNames={"DLVY_DE", "ROUTE_NO", "STOP_CD"}))
public class T_EMPTYBOX_INFO extends T_ID implements EmptyboxIO {
	
	
	@Column(name = "ROUTE_NO", columnDefinition = ROUTE_NO_LNG_07)
	private String routeNo;
	@Column(name = "DLVY_DE", nullable = false, columnDefinition = YYYYMMDD_08)
	private String dlvyDe;
	
	@Column(name = "STOP_CD", nullable = false)
	private String stopCd;
	
	@Column(name = "SQUARE_BOX_QTY")
	private int squareBoxQty;
	
	
	@Column(name = "TRIANGLE_BOX_QTY")
	private int triangleBoxQty;
	
	
	@Column(name = "YODELRY_BOX_QTY")
	private int yodelryBoxQty;

	
	@Column(name = "PALLET_QTY")
	private Integer palletQty;

	
	@Column(name = "NO_INPUT_CAUSE", columnDefinition = ANY_ENUMS_LNG_20)
	@Enumerated(EnumType.STRING)
	private Cause cause;

	
	@Column(name = "MODIFY_CAUSE", columnDefinition = ANY_ENUMS_LNG_20)
	@Enumerated(EnumType.STRING)
	private ModifyCause modifyCause;
	
	
	@Column(name = "PICTURE_BIN")
	@Lob
	private byte[] picture;
	
	
	@Column(name = "IS_CONFIRM")
	private boolean confirm;
	
	@Column(name = "REG_USER")
	private String regUser;
	
	@JsonIgnore
	@Transient
	public int hashCode() {
		return (ifNull(routeNo) + ifNull(dlvyDe) + ifNull(stopCd)).hashCode();
	}
	@JsonIgnore
	@Transient
	private String ifNull(Object o) {
		return o == null ? "" : o.toString();
	}
}
