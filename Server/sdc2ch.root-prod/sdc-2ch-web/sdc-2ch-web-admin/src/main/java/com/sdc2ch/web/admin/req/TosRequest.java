package com.sdc2ch.web.admin.req;

import javax.validation.constraints.NotBlank;

import com.sdc2ch.web.admin.repo.enums.ToSRegEnums;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
@ToString
public class TosRequest {
	@ApiModelProperty(value = "id", required=false)
	private Long id;
	@ApiModelProperty(value = "title", example="약관3제목입니다", required=true)
	@NotBlank
	private String title;
	@ApiModelProperty(value = "contents", example="약관1내용 약관1내용 약관1내용 약관1내용 약관1내용 약관1내용 약관1내용 약관1내용 입니다.", required=true)
	@NotBlank
	private String contents;
	@ApiModelProperty(value = "regType", example="PRIVATE", required=true)
	@NotBlank
	private ToSRegEnums regType;
	@ApiModelProperty(value = "major", example="1", required=true)
	private Integer major;
}
