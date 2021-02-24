package com.sdc2ch.web.admin.req;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.util.StringUtils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchReq {
	
	@ApiModelProperty(dataType = "string", value = "공장코드", required = true)
	private String fctryCd;
	
	@ApiModelProperty(dataType = "string", value = "검색년도")
	private String year;
	
	@ApiModelProperty(dataType = "string", value = "검색월")
	private String month;
	
	@ApiModelProperty(dataType = "string", value = "일자")
	private String dlvyDe;
	
	@ApiModelProperty(dataType = "string", value = "시작일")
	private String fromDe;
	
	@ApiModelProperty(dataType = "string", value = "종료일")
	private String toDe;
	
	@ApiModelProperty(dataType = "string", value = "배차유형")
	private String caralcTy;
	
	@ApiModelProperty(dataType = "string", value = "노선번호")
	private String routeNo;
	
	@ApiModelProperty(dataType = "string", value = "차량번호")
	private String vrn;
	
	@ApiModelProperty(dataType = "string", value = "기사명")
	private String driverNm;

	
	@ApiModelProperty(dataType = "string", value = "배차확정여부")
	private String dcsnYn;


	
	public String getYear() {
		return convertDate(this.year);
	}
	public String getMonth() {
		return convertDate(this.month);
	}
	public String getDlvyDe() {
		return convertDate(this.dlvyDe);
	}
	public String getFromDe() {
		return convertDate(this.fromDe);
	}
	public String getToDe() {
		return convertDate(this.toDe);
	}
	public String convertDate(String date) {
		if(!StringUtils.isEmpty(date)) {
			date = date.replaceAll("-", "");
		}
		return date;
	}

	@ApiModelProperty(dataType = "long", value = "페이지넘버")
	private Integer pageNo = 0;
	@ApiModelProperty(dataType = "long", value = "페이지사이즈")
	private Integer pageSize = 200;
	@ApiModelProperty(dataType = "Sortable", value = "정렬 기준 및 형식")
	private List<SortableReq> sort;




	
	private Sort cvtSort(List<SortableReq> list) {
		Sort sort = null;
		if(list != null && !list.isEmpty()) {
			List<Order> sortList = list.stream().filter(o->!StringUtils.isEmpty(o.getField())).map(o -> {
				if (!StringUtils.isEmpty(o.getDir())) {
					return "desc".equals(o.getDir().toLowerCase()) ? Sort.Order.desc(o.getField())
							: Sort.Order.asc(o.getField());
				} else {
					return Sort.Order.asc(o.getField());
				}
			}).collect(Collectors.toList());
			sort = Sort.by(sortList);
		}
		return sort;
	}

	public PageRequest getPageable() {
		int offset = this.pageNo;
		int size = this.pageSize;
		Sort sort = cvtSort(this.sort);
		return PageRequest.of(offset, size, sort); 
	}
}
