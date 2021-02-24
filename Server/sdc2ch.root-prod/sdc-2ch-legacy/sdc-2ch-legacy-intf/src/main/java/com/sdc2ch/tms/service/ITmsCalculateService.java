package com.sdc2ch.tms.service;

import java.util.List;
import java.util.function.Predicate;

import com.sdc2ch.tms.io.TmsCalculateIO;

public interface ITmsCalculateService {

	String storedProcedureName = "dbo.SP_REP_ROWDATA_DAY_NEW";
	
	List<TmsCalculateIO> search(String fromDe, String toDe, Predicate<TmsCalculateIO> predicate);
	List<TmsCalculateIO> search(String fromDe, String toDe, String fcrtyCd, Predicate<TmsCalculateIO> predicate);
}
