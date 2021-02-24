package com.sdc2ch.mobile.res;

import java.util.List;

import org.springframework.hateoas.ResourceSupport;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VerifyTree extends ResourceSupport {
	@ApiModelProperty(dataType = "Array", value = "검증항목")
	private List<Verify> verifys;
}
