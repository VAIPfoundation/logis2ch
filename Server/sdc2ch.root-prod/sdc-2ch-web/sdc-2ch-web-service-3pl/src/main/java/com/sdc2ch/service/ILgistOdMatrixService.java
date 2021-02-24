package com.sdc2ch.service;

import com.sdc2ch.service.factory.CarTon;
import com.sdc2ch.service.model.NaviPoint;
import com.sdc2ch.service.model.OdMatrixInfoVo;
import com.sdc2ch.service.model.TmapApiParam;

public interface ILgistOdMatrixService {
	
	

	
	OdMatrixInfoVo findNavigationInfo(TmapApiParam param);

}
