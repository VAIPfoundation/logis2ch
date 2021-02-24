package com.sdc2ch.service.common.model.alarm;

import java.util.List;

public interface IChkAlarmDto {
	List<ChkResultDto> getResultDto();
	String getHeader();
	String getBody();
	String getFooter();
	String getHtml();
}
