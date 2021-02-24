package com.sdc2ch.service.admin.model;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.sdc2ch.web.admin.repo.dto.IDailyDlvyErorSttusDto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DailyDlvyErorSttusVo {


	private List<IDailyDlvyErorSttusDto> datasA;
	private List<IDailyDlvyErorSttusDto> datasB;
	private List<IDailyDlvyErorSttusDto> datasC;


	
}
