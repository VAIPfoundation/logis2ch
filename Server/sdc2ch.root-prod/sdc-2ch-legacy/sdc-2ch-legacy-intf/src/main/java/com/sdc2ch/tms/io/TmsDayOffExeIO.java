package com.sdc2ch.tms.io;

import java.math.BigDecimal;
import java.util.Date;

public interface TmsDayOffExeIO {
	Long getRowId();
	String getFctryCd();
	String getDayOffDe();
	String getVrn();
	String getDayOffTy();
	String getDayOffDesc();
	BigDecimal getDayOffCnt();
	String getRegFlag();
	String getRegUserId();
	Date getRegDateTime();
	String getDayOffComment();
}
