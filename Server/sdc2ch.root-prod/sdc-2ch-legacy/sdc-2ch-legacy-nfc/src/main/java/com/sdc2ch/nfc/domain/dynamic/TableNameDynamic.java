package com.sdc2ch.nfc.domain.dynamic;

public interface TableNameDynamic<T> {
	
	Class<T> getEntityClass();
	boolean supported(String sql);
	String dynamicQuery(String sql);

}
