package com.sdc2ch.mobile.res;

import org.springframework.hateoas.ResourceSupport;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Otp extends ResourceSupport {

	@ApiModelProperty(dataType = "string", value = "otp 6숫자")
	private String otp;
}
