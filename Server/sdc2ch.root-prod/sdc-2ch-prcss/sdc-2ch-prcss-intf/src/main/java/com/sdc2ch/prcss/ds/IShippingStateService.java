package com.sdc2ch.prcss.ds;

import java.util.Date;
import java.util.List;

import com.sdc2ch.prcss.ds.io.ShippingPlanIO;
import com.sdc2ch.prcss.ds.vo.ShipEventHistVo;
import com.sdc2ch.prcss.ds.vo.ShipStateVo;
import com.sdc2ch.require.domain.IUser;

public interface IShippingStateService {





	ShipStateVo getDeliveryState(IUser user);


	List<ShippingPlanIO> findShipPlanByAllocatedGroupId(Long id);
	ShipStateVo createShippingState(Long allocatedGroupId, Date trnsmisDt, String dlvyDe, IUser user);

	List<ShipEventHistVo> getShippingEventList(Long allocatedGId, String dlvyDe, IUser user);
	

}
