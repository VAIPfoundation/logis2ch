package com.sdc2ch.nfc.domain.dynamic.table;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.sdc2ch.nfc.domain.dynamic.TableNameDynamic;
import com.sdc2ch.nfc.domain.entity.T_lg;

@Component
public class T_lgNameDynamicTable implements TableNameDynamic<T_lg> {

	public static final String TABLE_NAME = "t_lg";
	
	private static final SimpleDateFormat fmt = new SimpleDateFormat("yyyyMM");
	
	private final String RULE = TABLE_NAME + "." + TABLE_NAME;
	
	@Override
	public boolean supported(String sql) {
		return sql.contains(RULE);
	}

	@Override
	public String dynamicQuery(String sql) {
		return sql.replaceAll(RULE, TABLE_NAME + fmt.format(new Date()));
	}

	@Override
	public Class<T_lg> getEntityClass() {
		return T_lg.class;
	}

}
