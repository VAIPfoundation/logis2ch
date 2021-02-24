package com.sdc2ch.service.common.model.alarm;

import java.util.List;
import java.util.stream.Collectors;

import com.sdc2ch.web.admin.repo.domain.alloc.type.AlarmType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChkNoPassageDto implements IChkAlarmDto{

	private AlarmType alarmTy;
	private List<ChkResultDto> resultDto;

	@Override
	public List<ChkResultDto> getResultDto() {
		return resultDto;
	}

	@Override
	public String getHeader() {
		StringBuffer sb = new StringBuffer();
		sb.append("<table  cellpadding=\"1\" cellspacing=\"0\"  border='1'>");
		sb.append("<caption>"+ alarmTy.alarmTyNm +"</caption>");
		sb.append("<thead>");
		sb.append("<th>이벤트 종류</th>");
		sb.append("<th>차량번호</th>");
		sb.append("<th>운전자명</th>");
		sb.append("<th>연락처</th>");
		sb.append("<th>상차 예정 시간</th>");
		sb.append("<th>시스템 확인 시간</th>");
		sb.append("<th>지연시간(분)</th>");
		sb.append("</tr>");
		return sb.toString();
	}

	@Override
	public String getBody() {
		StringBuffer sb = new StringBuffer();
		sb.append("<tbody align=\"center\">");
		String body = resultDto.stream().map(dto -> {
			return "<tr>"
					+ "<td>"+ dto.getAlarmTy().alarmTyNm+"</td>"
					+ "<td>"+ dto.getVrn()+"</td>"
					+ "<td>"+ dto.getDriverNm()+"</td>"
					+ "<td>"+ dto.getMobileNo()+"</td>"
					+ "<td>"+ dto.getChkBaseDt()+"</td>"
					+ "<td>"+ dto.getChkDt()+"</td>"
					+ "<td>"+ dto.getOverTime()+"</td>"
					+ "</tr>";
		}).collect(Collectors.joining("\r\n"));
		sb.append(body);
		sb.append("</tbody>");
		return sb.toString();
	}

	@Override
	public String getFooter() {
		StringBuffer sb = new StringBuffer();
		sb.append("</table>");
		return sb.toString();
	}
	@Override
	public String getHtml() {
		StringBuffer sb = new StringBuffer();
		sb.append(getHeader());
		sb.append(getBody());
		sb.append(getFooter());
		return sb.toString();
	}

}
