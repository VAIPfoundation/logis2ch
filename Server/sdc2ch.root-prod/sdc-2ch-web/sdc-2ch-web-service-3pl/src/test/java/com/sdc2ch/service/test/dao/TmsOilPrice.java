package com.sdc2ch.service.test.dao;

import java.math.BigDecimal;

import com.sdc2ch.tms.io.TmsOilPriceIO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TmsOilPrice implements TmsOilPriceIO {
	private String startDate;
	private String endDate;
	private BigDecimal oilPrice;
}
