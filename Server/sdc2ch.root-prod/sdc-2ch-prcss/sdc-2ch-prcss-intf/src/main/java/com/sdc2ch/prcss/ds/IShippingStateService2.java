package com.sdc2ch.prcss.ds;

import java.util.List;

import com.sdc2ch.prcss.ds.event.ShippingStateEvent;
import com.sdc2ch.prcss.ds.io.ShippingPlanIO;
import com.sdc2ch.prcss.ds.vo.ShipStateVo2;
import com.sdc2ch.require.domain.IUser;

public interface IShippingStateService2 {

	List<ShippingStateEvent> findInoutStateByAllocatedGId(Long gid);
	List<ShippingStateEvent> findCommuteTimeStateByAllocatedGId(Long gid);
	List<ShippingStateEvent> findLdngStateByAllocatedGId(Long gid);
	ShipStateVo2 getDeliveryState2(IUser user);
	List<ShippingPlanIO> findShipPlanByAllocatedGroupId(Long id);
}
