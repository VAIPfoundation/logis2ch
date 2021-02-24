package com.sdc2ch.tms.service;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;

public interface ITmsQueryBuilder {

	JPAQueryFactory create();
	public List<?> createSelectNativeQuery(String query, Object... parameters);
}
