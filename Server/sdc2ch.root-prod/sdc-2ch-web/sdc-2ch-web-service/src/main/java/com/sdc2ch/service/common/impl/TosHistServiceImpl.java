package com.sdc2ch.service.common.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdc2ch.service.common.ITosHistService;
import com.sdc2ch.web.admin.repo.dao.T_ToSHistRepository;
import com.sdc2ch.web.admin.repo.domain.T_TOS_HIST;

@Service
class TosHistServiceImpl implements ITosHistService {
	
	@Autowired T_ToSHistRepository histRepo;

	@Override
	public T_TOS_HIST save(T_TOS_HIST hist) {
		return histRepo.save(hist);
	}

}
