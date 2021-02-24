package com.sdc2ch.mobile.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MobileMenu {
	@ApiModelProperty(dataType = "string", value = "메뉴명")
	private String title;
	@ApiModelProperty(dataType = "string", value = "메뉴경로")
	private String path;
	@ApiModelProperty(dataType = "string", value = "메뉴 아이콘 css 클레스")
	private String iconClassName;
	@ApiModelProperty(dataType = "string", value = "메뉴프로그램 아이디")
	private String pgmId;

}
