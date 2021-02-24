package com.sdc2ch.service.admin;

import java.util.List;

import com.sdc2ch.service.admin.model.StatsCommonVo;
import com.sdc2ch.web.admin.repo.domain.sta.T_DRIVE_DIARY;
import com.sdc2ch.web.admin.repo.domain.sta.V_ANALS_DRIVING_HIST;
import com.sdc2ch.web.admin.repo.domain.sta.V_DRIVE_DIARY_DAILY;
import com.sdc2ch.web.admin.repo.domain.sta.V_DRIVE_DIARY_DLVY_LC;
import com.sdc2ch.web.admin.repo.domain.sta.V_DRIVE_DIARY_DTLS;
import com.sdc2ch.web.admin.repo.domain.v.V_STATS_SUMRY;


public interface IStatsVhcleService {

	
	List<StatsCommonVo> searchPeriodVhcle(String fromDe, String toDe, String fctryCd);

	
	List<StatsCommonVo> searchPeriodDlvyLc(String fromDe, String toDe, String fctryCd, String arvlGrad);

	
	
	List<StatsCommonVo> searchVhcle(String fromDe, String toDe, String fctryCd);
	List<StatsCommonVo> searchVhcle(String fromDe, String toDe, String fctryCd, String ldngGrad, String arvlGrad);
	
	List<StatsCommonVo> searchRoute(String dlvyDe, String vrn);
	
	List<StatsCommonVo> searchDlvyLc(String dlvyDe, String vrn, String routeNo);

	
	V_ANALS_DRIVING_HIST searchDriveDiarySmry(String dlvyDe, String vrn);
	
	List<T_DRIVE_DIARY> searchDriveDiaryList(String dlvyDe, String vrn);



}
