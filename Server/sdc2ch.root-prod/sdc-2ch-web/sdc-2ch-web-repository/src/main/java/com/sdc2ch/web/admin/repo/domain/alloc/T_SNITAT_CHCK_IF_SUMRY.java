package com.sdc2ch.web.admin.repo.domain.alloc;

import static com.sdc2ch.require.repo.schema.GTConfig.ANY_ENUMS_LNG_20;
import static com.sdc2ch.require.repo.schema.GTConfig.MIDDLE_CONTENTS_LNG_300;
import static com.sdc2ch.require.repo.schema.GTConfig.TIME_LNG_04;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sdc2ch.require.repo.T_ID;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(name = "T_SNITAT_CHCK_IF_SUMRY", uniqueConstraints=@UniqueConstraint(columnNames={"FCTRY_CD", "YEAR", "MONTH", "USER_ID"})) 
@ToString
public class T_SNITAT_CHCK_IF_SUMRY extends T_ID{

	
	public enum ERPStatus {
		NEW("신규"),
		AUTO("자동상신"),
		WRITE("상신"),
		APP("결재중"),
		CANCEL("반송"),
		ENDCANCEL("승인취소"),
		END("완료")
		;
		public String statusName;
		private ERPStatus(String statusName) {
			this.statusName = statusName;
		}
	}

	@Column(name = "FCTRY_CD", columnDefinition = ANY_ENUMS_LNG_20)
	private String fctryCd;	
	@Column(name = "YEAR", columnDefinition = TIME_LNG_04)
	private String year;	
	@Column(name = "MONTH", columnDefinition = TIME_LNG_04)
	private String month;	
	@Column(name = "USER_ID")
	private String userId; 
	@Column(name = "IF_DT")
	private Date ifDate; 
	@Column(name = "IF_YN")
	@ColumnDefault(value = "0")
	private boolean ifYn; 
	@Column(name = "LEGACY_KEY")
	private String legacyKey;

	@Column(name = "ITEM01_CNT1")
	private int item01cnt1;	
	@Column(name = "ITEM01_CNT2")
	private int item01cnt2;	
	@Column(name = "ITEM01_CNT3")
	private int item01cnt3; 

	@Column(name = "ITEM02_CNT1")
	private int item02cnt1;	
	@Column(name = "ITEM02_CNT2")
	private int item02cnt2;	
	@Column(name = "ITEM02_CNT3")
	private int item02cnt3; 

	@Column(name = "ITEM03_CNT1")
	private int item03cnt1;	
	@Column(name = "ITEM03_CNT2")
	private int item03cnt2;	
	@Column(name = "ITEM03_CNT3")
	private int item03cnt3; 

	@Column(name = "ITEM04_CNT1")
	private int item04cnt1;	
	@Column(name = "ITEM04_CNT2")
	private int item04cnt2;	
	@Column(name = "ITEM04_CNT3")
	private int item04cnt3; 

	@Column(name = "ITEM05_CNT1")
	private int item05cnt1;	
	@Column(name = "ITEM05_CNT2")
	private int item05cnt2;	
	@Column(name = "ITEM05_CNT3")
	private int item05cnt3; 

	@Column(name = "ITEM06_CNT1")
	private int item06cnt1;	
	@Column(name = "ITEM06_CNT2")
	private int item06cnt2;	
	@Column(name = "ITEM06_CNT3")
	private int item06cnt3; 

	@Column(name = "ITEM07_CNT1")
	private int item07cnt1;	
	@Column(name = "ITEM07_CNT2")
	private int item07cnt2;	
	@Column(name = "ITEM07_CNT3")
	private int item07cnt3; 

	@Column(name = "ITEM08_CNT1")
	private int item08cnt1;	
	@Column(name = "ITEM08_CNT2")
	private int item08cnt2;	
	@Column(name = "ITEM08_CNT3")
	private int item08cnt3; 

	@Column(name = "ITEM09_CNT1")
	private int item09cnt1;	
	@Column(name = "ITEM09_CNT2")
	private int item09cnt2;	
	@Column(name = "ITEM09_CNT3")
	private int item09cnt3; 

	@Column(name = "ITEM10_CNT1")
	private int item10cnt1;	
	@Column(name = "ITEM10_CNT2")
	private int item10cnt2;	
	@Column(name = "ITEM10_CNT3")
	private int item10cnt3; 

	@Column(name = "CAR_CNT")
	private int totalCarCnt; 

	@Column(name = "DAYOFF_CNT")
	private int dayoffCnt; 

	@Column(name = "ERP_STATUS", columnDefinition = ANY_ENUMS_LNG_20)
	@Enumerated(EnumType.STRING)
	private ERPStatus erpStatus; 

	@Column(name = "ERP_REURL", columnDefinition = MIDDLE_CONTENTS_LNG_300)
	private String reUrl;

	@JsonSerialize
	public String getErpStatusNm() {
		return this.erpStatus != null ? this.erpStatus.statusName : null;
	}

	@Transient
	@JsonSerialize @JsonDeserialize
	private String legacyType;
	@Transient
	@JsonSerialize @JsonDeserialize
	private String appForm;
	@Transient
	@JsonSerialize @JsonDeserialize
	private String title;
	@Transient
	@JsonSerialize @JsonDeserialize
	private String makeTime;
	@Transient
	@JsonSerialize @JsonDeserialize
	private String appLine;
	@Transient
	@JsonSerialize @JsonDeserialize
	private String status;
	@Transient
	@JsonSerialize @JsonDeserialize
	private String bodyContents;
	@Transient
	@JsonSerialize @JsonDeserialize
	private String msg;
	@Transient
	@JsonSerialize @JsonDeserialize
	private String code;






}
