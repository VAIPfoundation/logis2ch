package com.sdc2ch.api.callevent;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class CallEvent {
	@ApiModelProperty(required = true, value = "이벤트명")
	protected EventName eventName;
}
