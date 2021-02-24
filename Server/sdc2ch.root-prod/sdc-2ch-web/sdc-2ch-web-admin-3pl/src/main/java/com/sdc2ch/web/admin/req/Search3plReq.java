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
public class Search3plReq {

	
	@ApiModelProperty(dataType = "string", value = "시작일")
	private String fromDe;
	
	@ApiModelProperty(dataType = "string", value = "종료일")
	private String toDe;

	@ApiModelProperty(dataType = "string", value = "모델명")
	private String modelNm;

	@ApiModelProperty(dataType = "string", value = "모델아이디")
	private Long modelId;

	@ApiModelProperty(dataType = "string", value = "매트릭스아이디")
	private Long matrixId;


	@ApiModelProperty(dataType = "string", value = "배차유형")
	private String caralcCd;

	@ApiModelProperty(dataType = "string", value = "공장코드")
	private String fctryCd;

	@ApiModelProperty(dataType = "string", value = "룰명")
	private String ruleNm;

	@ApiModelProperty(dataType = "string", value = "노선번호")
	private String routeNo;

	@ApiModelProperty(dataType = "string", value = "차량번호")
	private String vrn;

	@ApiModelProperty(dataType = "string", value = "수배송유형")
	private String lgistTy;

	@ApiModelProperty(dataType = "arrays", value = "노선리스트")
	private List<String> routeNos;



	@ApiModelProperty(dataType = "long", value = "페이지넘버")
	private Integer pageNo = 0;
	@ApiModelProperty(dataType = "long", value = "페이지사이즈")
	private Integer pageSize = 200;
	@ApiModelProperty(dataType = "Sortable", value = "정렬 기준 및 형식")
	private List<SortableReq> sort;



	@Getter
	@Setter
	public class SortableReq {
		private String field;
		private String dir;
	}


	
	public Sort cvtSort(List<SortableReq> list) {
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
