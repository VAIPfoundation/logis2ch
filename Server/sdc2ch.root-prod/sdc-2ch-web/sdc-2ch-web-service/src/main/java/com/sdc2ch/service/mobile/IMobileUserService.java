package com.sdc2ch.service.mobile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.sdc2ch.ars.enums.CallType;
import com.sdc2ch.ars.enums.SenderType;
import com.sdc2ch.prcss.ds.vo.ShipStateVo2;
import com.sdc2ch.prcss.eb.io.EmptyboxIO;
import com.sdc2ch.prcss.eb.vo.EmptyboxVo;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.service.common.exception.ServiceException;
import com.sdc2ch.service.mobile.model.ChkTblRegistScheduleVo;
import com.sdc2ch.service.mobile.model.MobileCaralcInfVo;
import com.sdc2ch.service.mobile.model.MobileHaveOffInfoHstVo;
import com.sdc2ch.service.mobile.model.MobileHaveOffInfoVo;
import com.sdc2ch.service.mobile.model.MobileHaveOffPossibleVo;
import com.sdc2ch.web.admin.repo.domain.alloc.T_CARALC_CNFIRM_GROUP2;
import com.sdc2ch.web.admin.repo.domain.alloc.T_RTNGUD_UNDTAKE;
import com.sdc2ch.web.admin.repo.domain.alloc.T_SNITAT_CHCK_TABLE;
import com.sdc2ch.web.admin.repo.domain.v.V_STATS_DRIVER_MONTHLY;

 
public interface IMobileUserService {
	
	MobileCaralcInfVo findAllocatedInfoByGroupId(Long groupId)throws ServiceException ;
	
	MobileCaralcInfVo findAllocatedInfoByGroupIdV2(Long groupId) throws ServiceException;
	
	MobileCaralcInfVo findAllocatedInfoByGroupIdV3(Long groupId) throws ServiceException;
	MobileCaralcInfVo findAllocatedInfoByGroupIdV4(Long allocatedGroupId) throws ServiceException;
	
	MobileCaralcInfVo findAllocatedInfoByUser(IUser user)throws ServiceException ;
	MobileCaralcInfVo findAllocatedInfoByUserV2(IUser user) throws ServiceException;
	MobileCaralcInfVo findAllocatedInfoByUserV3(IUser user) throws ServiceException;
	MobileCaralcInfVo findAllocatedInfoByUserV4(IUser user) throws ServiceException;

	Optional<T_CARALC_CNFIRM_GROUP2> findLastConfirmGroupByUser(IUser user);
	
	MobileCaralcInfVo findAllocatedInfoByUserAndDlvyDe(IUser user, String dlvyDe)throws ServiceException ;
	MobileCaralcInfVo findAllocatedInfoByUserAndDlvyDeV2(IUser user, String dlvyDe) throws ServiceException;
	MobileCaralcInfVo findAllocatedInfoByUserAndDlvyDeV3(IUser user, String dlvyDe) throws ServiceException;
	MobileCaralcInfVo findAllocatedInfoByUserAndDlvyDeV4(IUser user, String dlvyDe) throws ServiceException;
	
	List<EmptyboxVo> listEmptyBox(String routeNo, String dlvyDe);

	
	boolean isConfirm(EmptyboxIO eb);

	
	EmptyboxIO saveEmptyBox(EmptyboxIO eb);
	
	EmptyboxIO findEmptyBoxById(Long id) throws ServiceException ;

	
	Optional<T_RTNGUD_UNDTAKE> findReturningGoodsById(Long id) throws ServiceException ;

	
	T_RTNGUD_UNDTAKE saveReturningGoods(T_RTNGUD_UNDTAKE returnGoods) throws ServiceException ;

	
	boolean checkEmptyBox(IUser user)throws ServiceException;

	
	Optional<T_CARALC_CNFIRM_GROUP2> findAllocatedGroupById(Long id);

	public Optional<T_CARALC_CNFIRM_GROUP2> findLastAllocatedGroupByUser(IUser user);

	
	void saveAllocatedGroup(T_CARALC_CNFIRM_GROUP2 group);















	
	void startJob(IUser user, Long allocatedGroupId);

	
	ShipStateVo2 currentShippingState(IUser user);

	
	boolean finishJob(IUser user, Long allocatedGroupId);
	boolean checkEmptyBoxByAllocatedGroupId(Long id) throws ServiceException;

	List<EmptyboxVo> currentEmptyBoxByAllicatedGroupId(Long id);




	
	T_SNITAT_CHCK_TABLE saveSnitation(T_SNITAT_CHCK_TABLE snitationInfo);



	
	public void saveStoredProcedureCall(Object[] params, String procName);

	
	public List<Object[]> selectStoredProcedureCall(Object[] params, String procName);
	
	public List<Object[]> saveStoredProcedureCallRet(Object[] params, String procName);


	
	public List<ChkTblRegistScheduleVo> findMyCheckList(IUser user, LocalDate toDay);

	public int squareBoxQty(String dlvyDe, String vrn);
	public int squareBoxQty(String dlvyDe, String vrn, String routeNo);


	
	public V_STATS_DRIVER_MONTHLY findDriveMonthly(String dlvyDe, String vrn);

	
	void arsCall(CallType type, Long id, String dlvyDe, String dlvyLcCd, String fctryCd, IUser user);
	void arsCall(CallType type, Long id, String dlvyDe, String dlvyLcCd, String fctryCd, IUser user, String routeNo);

	
	void arsCall(SenderType senderType, CallType type, Long id, String dlvyDe, String dlvyLcCd, String fctryCd, IUser user, String routeNo);


	public List<MobileHaveOffInfoVo> upsertDayoff(Integer id, String fctryCd, String startDate, String endDate, String carCd,
			String type, String reason, String unit, String bigo, String routeNo, String username);


	
	public List<MobileHaveOffPossibleVo> selectListHaveOffPossible(String fctryCd, String searchMonth, String carCd);


	
	public List<MobileHaveOffInfoHstVo> findDayOffHstInfo(String fctryCd, String carCd, String searchMonth);

	
	public boolean isExeDayOff(IUser user, String targetDate);
	public boolean isExeDayOff(IUser user, String targetDate, double unit);

	
	public String exitFactoryByWeb(IUser user, Long allocatedGroupId, String routeNo, String dlvyDe, String fctryCd);
	
	public String enterFactoryByWeb(IUser user, Long allocatedGroupId, String routeNo, String dlvyDe, String fctryCd);

	

}
