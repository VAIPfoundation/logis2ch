package com.sdc2ch.prcss.ss.service;

import java.util.List;

import com.sdc2ch.prcss.ss.repo.domain.T_DRIVE_DIARY;

public interface IDriveDiaryService {
	
	List<T_DRIVE_DIARY> findAllByAllocateGId(Long gid);
	List<T_DRIVE_DIARY> findAllByUserNameAndDlvyDe(String username, String dlvyDe);
	List<T_DRIVE_DIARY> findAllByVrnAndDlvyDe(String vrn, String dlvyDe);

}
