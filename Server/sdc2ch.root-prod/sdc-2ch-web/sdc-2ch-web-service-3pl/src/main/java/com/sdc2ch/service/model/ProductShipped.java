package com.sdc2ch.service.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProductShipped extends Product {
	private Date createdDt;
	private long production;
}
