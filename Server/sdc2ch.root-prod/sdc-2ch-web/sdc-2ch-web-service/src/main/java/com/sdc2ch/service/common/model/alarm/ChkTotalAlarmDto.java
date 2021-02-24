package com.sdc2ch.service.common.model.alarm;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.sdc2ch.tms.enums.FactoryType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ChkTotalAlarmDto {

	private FactoryType factoryTy; 
	private List<IChkAlarmDto> list; 


	public int getChkCnt() {
		int cnt = 0;
		if(this.list == null) {
			return cnt;
		}
		return list.stream().mapToInt(o -> o.getResultDto().size()).sum();

	}

	public String getTitle() {
		return "2CH 시스템 알람 발생 내역 : " + this.getChkCnt();
	}

	public String getContents(){
		if(this.list == null || this.list.isEmpty()) {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		String content = list
				.stream()
				.filter(o -> o != null && !o.getResultDto().isEmpty())
				.map(o -> o.getHtml()).collect(Collectors.joining(""));
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
		sb.append("</head>");
		sb.append("<body>");
		sb.append(content);
		sb.append("</body>");
		sb.append("</html>");
		return sb.toString();
	}
}
