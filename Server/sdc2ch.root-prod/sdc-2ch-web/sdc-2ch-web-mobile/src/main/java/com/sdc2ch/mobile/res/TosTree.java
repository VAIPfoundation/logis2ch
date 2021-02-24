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
public class TosTree extends ResourceSupport {
	@ApiModelProperty(dataType = "array", value = "이용약관목록")
	private List<Tos> tos;
}
