package com.sdc2ch.agent.service.vo;

import java.util.Map;

import org.springframework.hateoas.ResourceSupport;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MeResVo {

	private String userNm;
	private String fctryCd;
	private String vrn;

}
