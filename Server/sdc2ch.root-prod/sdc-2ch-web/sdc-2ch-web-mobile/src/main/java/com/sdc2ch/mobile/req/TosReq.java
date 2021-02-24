package com.sdc2ch.mobile.req;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TosReq {
	
	@NotNull
	@Size(min = 10, max = 13)
	private String phoneNo;
	@Size(min = 6, max = 6)
	private String otp;
	@NotNull
	private String osNm;
	@NotNull
	@Length(max = 20)
	private String osVer;
	@NotNull
	@Length(max = 200)
	private String appTkn;
	@NotNull
	private String model;
	@NotNull
	private String telCo;
	
	@NotNull
	private int appMajorVer;
	@NotNull
	private int appMinorVer;
	@NotNull
	private int appBuildVer;
	
	@NotEmpty
	@Size(min=2, max=2)
	private List<Long> tosIds;

}
