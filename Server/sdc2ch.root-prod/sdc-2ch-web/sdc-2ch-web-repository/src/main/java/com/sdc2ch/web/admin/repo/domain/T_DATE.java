package com.sdc2ch.web.admin.repo.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(name = "T_DATE", uniqueConstraints=@UniqueConstraint(columnNames={"DT"})) 
@ToString
public class T_DATE {

	@Id
	@Column(name = "DT")
	private Date dt;	
	@Column(name = "DE")
	private String de;	
	@Column(name = "YEAR")
	private String year;	
	@Column(name = "QUARTER")
	private int quarter;	
	@Column(name = "MONTH")
	private String month;	
	@Column(name = "DAY")
	private int day;	
	@Column(name = "WEEK")
	private int week;	
	@Column(name = "MONTHLY_WEEK")
	private int monthlyWeek;	
	@Column(name = "WEEK_DAY")
	private int weekDay;	
	@Column(name = "WEEK_DAY_KR")
	private String weekDayKr;	

}
