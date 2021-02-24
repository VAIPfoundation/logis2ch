package com.sdc2ch.service.admin;

import java.util.List;
import java.util.Optional;

import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.web.admin.repo.domain.alloc.T_CARALC_CNFIRM_GROUP2;


public interface IAllocationConfirmGroupService {
	
	String insertQuery = "insert into T_CARALC_CNFIRM_GROUP2 (CREATE_DT, UPDATE_DT, ADJUST_TIME, CARALC_TYPE, CNFIRM_DT, CNFIRM_USER_ID, DLVY_DE, DRIVER_CD, FCTRY_CD, LDNG_EXPC_TIME, MOBILE_NO, ROUTE_HINT, TRNSMIS_DT, TRNSMIS_USER_ID, VRN) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	String insertBatchQuery = "insert into T_CARALC_CNFIRM_GROUP2 (CREATE_DT, UPDATE_DT, ADJUST_TIME, CARALC_TYPE, CNFIRM_DT, CNFIRM_USER_ID, DLVY_DE, DRIVER_CD, FCTRY_CD, LDNG_EXPC_TIME, MOBILE_NO, ROUTE_HINT, TRNSMIS_DT, TRNSMIS_USER_ID, VRN) values ";
	
	
	Optional<T_CARALC_CNFIRM_GROUP2> findById(Long id);
	Optional<T_CARALC_CNFIRM_GROUP2> findLastByUser(IUser user);
	Optional<T_CARALC_CNFIRM_GROUP2> findDlvyByUser(IUser user, String dlvyDe);
	Optional<T_CARALC_CNFIRM_GROUP2> findLastConfirmGroupByUser(IUser user);
	
	





	



	
	void syncDb();
	T_CARALC_CNFIRM_GROUP2 save(T_CARALC_CNFIRM_GROUP2 group);
	
	List<T_CARALC_CNFIRM_GROUP2> allocateAll2(List<T_CARALC_CNFIRM_GROUP2> groups);
	List<T_CARALC_CNFIRM_GROUP2> cancelAll2(List<T_CARALC_CNFIRM_GROUP2> groups);
	
	public List<T_CARALC_CNFIRM_GROUP2> search2(String dlvyDe, String fctryCd, String carAlcType, String routeNo,
			String vrn, String dcsnYn);
	

}
