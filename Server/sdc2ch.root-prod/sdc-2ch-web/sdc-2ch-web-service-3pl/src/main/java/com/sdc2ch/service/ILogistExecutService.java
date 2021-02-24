package com.sdc2ch.service;

import com.sdc2ch.web.admin.repo.domain.lgist.T_LGIST_MODEL;
import com.sdc2ch.web.admin.repo.domain.lgist.T_LGIST_MODEL.TableType;

public interface ILogistExecutService {
	public void execute(T_LGIST_MODEL model);
	public boolean supported(TableType lgistType);
}
