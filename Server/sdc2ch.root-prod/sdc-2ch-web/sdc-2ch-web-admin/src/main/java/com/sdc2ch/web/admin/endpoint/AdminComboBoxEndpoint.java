package com.sdc2ch.web.admin.endpoint;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sdc2ch.service.common.IComboBoxService;
import com.sdc2ch.service.common.model.ComboBoxVo;
import com.sdc2ch.web.admin.req.SearchAnalsReq;

@RestController
@RequestMapping("/adm/combo")
public class AdminComboBoxEndpoint extends AdminAbstractEndpoint {

	@Autowired IComboBoxService comboSvc;

	@GetMapping("/factory")
	@PreAuthorize("hasAuthority('MANAGER')")
	public List<ComboBoxVo> findFactoryCombo() {
		return comboSvc.findFactoryCombo();
	}
	@GetMapping("/carAlcType")
	@PreAuthorize("hasAuthority('MANAGER')")
	public List<ComboBoxVo> findCarAlcTypeCombo() {
		return comboSvc.findCarAlcTypeCombo();
	}
	@GetMapping("/vhcleTy")
	@PreAuthorize("hasAuthority('MANAGER')")
	public List<ComboBoxVo> findVhcleTyCombo() {
		return comboSvc.findVhcleTyCombo();
	}
	@GetMapping("/setupLcTy")
	@PreAuthorize("hasAuthority('MANAGER')")
	public List<ComboBoxVo> findSetupLcTyCombo() {
		return comboSvc.findSetupLcTyCombo();
	}

	@PostMapping("/routeNo")
	@PreAuthorize("hasAuthority('MANAGER')")
	public List<ComboBoxVo> findRouteNoCombo(@RequestBody SearchAnalsReq param) {
		String fctryCd = param.getFctryCd();	
		String fromDe = param.getFromDe();	
		String toDe = param.getToDe();	
		String caralcTy = param.getCaralcTy();	
		String vhcleTy = param.getVhcleTy();	

		return comboSvc.findRouteNoCombo(fctryCd, fromDe, toDe, caralcTy, vhcleTy);
	}

	@PostMapping("/dlvyLcCd")
	@PreAuthorize("hasAuthority('MANAGER')")
	public List<ComboBoxVo> findDlvyLcCdCombo(@RequestBody SearchAnalsReq param) {
		String fctryCd = param.getFctryCd();	

		return comboSvc.findDlvyLcCdCombo(fctryCd);
	}

	@PostMapping("/dlvyLcTime")
	@PreAuthorize("hasAuthority('MANAGER')")
	public List<ComboBoxVo> findDlvyLcCdComboTime(@RequestBody SearchAnalsReq param) {
		String fctryCd = param.getFctryCd();	
		String fromDe = param.getFromDe();	
		String toDe = param.getToDe();	
		String dlvyLcCd = param.getDlvyLcCd();	

		return comboSvc.findDlvyLcTimeCombo(fctryCd, fromDe, toDe, dlvyLcCd);
	}

	@PostMapping("/trnsprtCmpny")
	@PreAuthorize("hasAuthority('MANAGER')")
	public List<ComboBoxVo> findTrnsprtCmpnyCombo(@RequestBody SearchAnalsReq param) {
		String fctryCd = param.getFctryCd();	
		return comboSvc.findTrnsprtCmpnyCombo(fctryCd);
	}



}
