package com.sdc2ch.web.admin.endpoint;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sdc2ch.core.error.exception.CustomBadRequestException;
import com.sdc2ch.prcss.eb.IEmptyBoxService;
import com.sdc2ch.prcss.eb.io.EmptyboxIO;
import com.sdc2ch.prcss.eb.vo.EmptyboxVo;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/adm/empty/box")
public class AdminEmptyBoxEndpoint extends AdminAbstractEndpoint {

	@Autowired IEmptyBoxService emptySvc;
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "공상자등록")
	@PostMapping(value = "/save", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<EmptyboxIO> saveEmptyBox(@Valid @RequestBody List<EmptyboxVo> ebreq, BindingResult bindingResult)
			throws CustomBadRequestException {

		
		super.verifyData(bindingResult);

		if(ebreq != null) {
			return ebreq.stream().filter(e -> !e.isErpConfirmYn() && !e.isReadOnly()).map(e -> {
				EmptyboxVo eb = new EmptyboxVo();
				eb.setId(e.getId());
				eb.setCause(StringUtils.isEmpty(e.getCause()) ? null : e.getCause());
				eb.setModifyCause(StringUtils.isEmpty(e.getModifyCause()) ? null : e.getModifyCause());
				eb.setConfirm(true);
				eb.setDlvyDe(e.getDlvyDe());
				eb.setStopCd(e.getStopCd());
				eb.setRegUser(e.getDriverCd());
				eb.setRouteNo(e.getRouteNo());
				eb.setSquareBoxQty(e.getSquareBoxQty());
				eb.setTriangleBoxQty(e.getTriangleBoxQty());
				eb.setYodelryBoxQty(e.getYodelryBoxQty());
				eb.setPalletQty(e.getPalletQty());
				return emptySvc.save(eb);
			}).collect(Collectors.toList());
		}
		return (List<EmptyboxIO> )(Object)ebreq;
	}
	
	@ApiOperation(value = "공상자등록")
	@PostMapping(value = "/list", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<EmptyboxVo> listEmptyBox(@Valid @RequestBody EmptyboxVo ebreq, BindingResult bindingResult)
			throws CustomBadRequestException {
		List<EmptyboxVo> vos = emptySvc.listEmptyboxByDlvyDeAndRouteNo(ebreq.getDlvyDe(), ebreq.getRouteNo());
		System.out.println(vos);
		return vos;
	}
}
