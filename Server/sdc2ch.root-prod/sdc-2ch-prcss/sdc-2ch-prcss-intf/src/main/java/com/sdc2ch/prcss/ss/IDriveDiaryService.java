package com.sdc2ch.prcss.ss;

import java.util.List;

import com.sdc2ch.prcss.ss.io.DriveDiaryIO2;


public interface IDriveDiaryService {
	
	List<DriveDiaryIO2> findAllByAllocateGId(Long gid);
	List<DriveDiaryIO2> findAllByUserNameAndDlvyDe(String username, String dlvyDe);
	List<DriveDiaryIO2> findAllByVrnAndDlvyDe(String vrn, String dlvyDe);

}
