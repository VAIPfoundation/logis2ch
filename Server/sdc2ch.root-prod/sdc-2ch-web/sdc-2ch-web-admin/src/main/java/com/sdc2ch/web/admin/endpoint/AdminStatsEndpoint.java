package com.sdc2ch.web.admin.endpoint;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sdc2ch.core.error.exception.CustomBadRequestException;
import com.sdc2ch.service.admin.IStatsVhcleService;
import com.sdc2ch.service.admin.model.StatsCommonVo;
import com.sdc2ch.service.common.IComboBoxService;
import com.sdc2ch.web.admin.repo.domain.sta.T_DRIVE_DIARY;
import com.sdc2ch.web.admin.repo.domain.sta.V_ANALS_DRIVING_HIST;
import com.sdc2ch.web.admin.repo.domain.sta.V_DRIVE_DIARY_DAILY;
import com.sdc2ch.web.admin.repo.domain.sta.V_DRIVE_DIARY_DLVY_LC;
import com.sdc2ch.web.admin.repo.domain.sta.V_DRIVE_DIARY_DTLS;
import com.sdc2ch.web.admin.repo.domain.v.V_STATS_SUMRY;
import com.sdc2ch.web.admin.req.SearchReq;
import com.sdc2ch.web.admin.req.SearchStatsReq;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/adm/stats")
public class AdminStatsEndpoint extends AdminAbstractEndpoint {

	@Autowired IComboBoxService comboSvc;

	@Autowired IStatsVhcleService statsVhcleSvc;

	@ApiOperation(value = "기간별 차량통계 > 차량별 조회", response = V_STATS_SUMRY.class)
	@PostMapping(value = "/period/vhcle/list", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<StatsCommonVo> listPeriodVhcle(@RequestBody SearchStatsReq param) throws CustomBadRequestException {
		return statsVhcleSvc.searchPeriodVhcle(super.convertDate(param.getFromDe()), super.convertDate(param.getToDe()), param.getFctryCd());
	}


	@ApiOperation(value = "기간별 고객센터통계 > 고객센터별 조회", response = V_STATS_SUMRY.class)
	@PostMapping(value = "/period/dlvyLc/list", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<StatsCommonVo> listPeriodDlvyLc(@RequestBody SearchStatsReq param) throws CustomBadRequestException {
		return statsVhcleSvc.searchPeriodDlvyLc(super.convertDate(param.getFromDe()), super.convertDate(param.getToDe()), param.getFctryCd(), param.getArvlGrad());
	}


	@ApiOperation(value = "차량별 운행통계 > 차량별 조회", response = V_STATS_SUMRY.class)
	@PostMapping(value = "/vhcle/vhcle/list", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<StatsCommonVo> listVhcleDrive(@RequestBody SearchStatsReq param) throws CustomBadRequestException {
		String ldngGrad = param.getLdngGrad();
		String arvlGrad = param.getArvlGrad();
		ldngGrad = ldngGrad == null ? "" : ldngGrad;
		arvlGrad = arvlGrad == null ? "" : arvlGrad;
		return statsVhcleSvc.searchVhcle(super.convertDate(param.getFromDe()), super.convertDate(param.getToDe()), param.getFctryCd(), ldngGrad, arvlGrad);
	}

	@ApiOperation(value = "차량별 운행통계 > 노선별 조회", response = V_STATS_SUMRY.class)
	@PostMapping(value = "/vhcle/route/list", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<StatsCommonVo> listRouteDrive(@RequestBody SearchStatsReq param) throws CustomBadRequestException {
		return statsVhcleSvc.searchRoute(super.convertDate(param.getDlvyDe()), param.getVrn());
	}

	@ApiOperation(value = "차량별 운행통계 > 배송지별 조회", response = V_STATS_SUMRY.class)
	@PostMapping(value = "/vhcle/dlvyLc/list", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<StatsCommonVo> listDlvyLcDrive(@RequestBody SearchStatsReq param) throws CustomBadRequestException {
		return statsVhcleSvc.searchDlvyLc(super.convertDate(param.getDlvyDe()), param.getVrn(), param.getRouteNo());
	}

	@ApiOperation(value = "차량별 운행통계 > 차량운행일지 조회(종합)", response = V_STATS_SUMRY.class)
	@PostMapping(value = "/vhcle/drive/diary/smry", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody V_ANALS_DRIVING_HIST smryDriveDiary(@RequestBody SearchStatsReq param) throws CustomBadRequestException {
		return statsVhcleSvc.searchDriveDiarySmry(super.convertDate(param.getDlvyDe()), param.getVrn());
	}

	@ApiOperation(value = "차량별 운행통계 > 차량운행일지 조회", response = V_STATS_SUMRY.class)
	@PostMapping(value = "/vhcle/drive/diary/list", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<T_DRIVE_DIARY> listDriveDiary(@RequestBody SearchStatsReq param) throws CustomBadRequestException {
		return statsVhcleSvc.searchDriveDiaryList(super.convertDate(param.getDlvyDe()), param.getVrn());
	}

}
