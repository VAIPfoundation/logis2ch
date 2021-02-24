package com.sdc2ch.web.service;

import java.util.List;
import java.util.Optional;

import com.sdc2ch.repo.io.AllocatedGroupIO;
import com.sdc2ch.require.domain.IUser;

public interface IAllocatedGroupService {
	Optional<AllocatedGroupIO> findAllocatedGroupById(Long allocatedGroupId);
	Optional<AllocatedGroupIO> findLastAllocatedGroupByUser(IUser user);
	AllocatedGroupIO findByDlvyDeAndVrn(String dlvyDe, String vrn);

	List<AllocatedGroupIO> findAllocatedGroupByDlvyDe(String DlvyDe);
}
