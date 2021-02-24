package com.sdc2ch.service.admin;

import java.util.List;

import com.sdc2ch.service.admin.model.VhcleCntrlVo;
import com.sdc2ch.web.admin.repo.domain.op.T_LOCATION_INFO_HIST;
import com.sdc2ch.web.admin.repo.domain.v.V_REALTIME_INFO;


public interface IAnalsVhcleService {

	
	V_REALTIME_INFO searchRtVhcleCntrlByVrn(String vrn);

	
	List<V_REALTIME_INFO> searchRtVhcleCntrl(String fctryCd);
	List<V_REALTIME_INFO> searchRtVhcleCntrl(String fctryCd, String vrn, String indvdlVhcleYn, String readyYn, String ldngYn, String dlvyYn,
			String rtnDriveYn, String comptYn, String hvofYn, String arvlDelayRiskYn, String arvlDelayYn, String overCtnuDriveYn);

	
	List<T_LOCATION_INFO_HIST> searchHistVhcleCntrl(String dlvyDe, String vrn);
	List<String> searchHistVhcleCntrlOnlyMongo(String vrn, String month);

	
	List<VhcleCntrlVo> searchEventHistVhcleCntrl(String dlvyDe, String vrn);


}
