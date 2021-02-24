package com.sdc2ch.web.admin.endpoint;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sdc2ch.core.endpoint.DefaultEndpoint;
import com.sdc2ch.core.error.exception.CustomBadRequestException;
import com.sdc2ch.service.ILgistCostService;
import com.sdc2ch.service.ILgistMatrixService;
import com.sdc2ch.service.common.model.ComboBoxVo;
import com.sdc2ch.service.model.ProductQty;
import com.sdc2ch.web.admin.repo.domain.lgist.T_LGIST_MODEL;
import com.sdc2ch.web.admin.repo.domain.lgist.T_LGIST_RULE_DTLS;
import com.sdc2ch.web.admin.repo.domain.lgist.T_LGIST_RULE_MSTR;
import com.sdc2ch.web.admin.repo.domain.lgist.T_ROUTE_PATH_MATRIX6;
import com.sdc2ch.web.admin.repo.domain.v.V_REALTIME_INFO;
import com.sdc2ch.web.admin.req.Search3plReq;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/adm/3pl")
public class Admin3plEndpoint extends DefaultEndpoint {

	@Autowired ILgistCostService costSvc;
	@Autowired ILgistMatrixService matrixSvc;


	@ApiOperation(value = "분석 > 배송 물류비분석 > 공장 콤보", response = ComboBoxVo.class)
	@PostMapping(value = "/find/factory/combo", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public List<ComboBoxVo> searchFactory() {
		return costSvc.searchFactoryCombo();
	}

	@ApiOperation(value = "분석 > 배송 물류비분석 > 배차유형 콤보", response = ComboBoxVo.class)
	@PostMapping(value = "/find/carAlcType/combo", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public List<ComboBoxVo> searchCarAlcType() {
		return costSvc.searchCarAlcTypeCombo();
	}


	@ApiOperation(value = "분석 > 배송 물류비분석 > 모델목록", response = T_LGIST_MODEL.class)
	@PostMapping(value = "/model/list", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody List<T_LGIST_MODEL> listRtVhcleCntrl(@RequestBody Search3plReq param) throws CustomBadRequestException {
		return costSvc.searchModel(convert(param.getFromDe()), convert(param.getToDe(), false), param.getModelNm(), param.getLgistTy());
	}

	@ApiOperation(value = "분석 > 배송 물류비분석 > 모델상세", response = T_LGIST_MODEL.class)
	@PostMapping(value = "/model/dtls", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public @ResponseBody T_LGIST_MODEL listRtVhcleCntrl(@RequestBody(required = true) Long id) throws CustomBadRequestException {
		T_LGIST_MODEL m =costSvc.findByModelId(id);

		
		System.out.println(m.getIdLgistRuleMstrFk());
		System.out.println(m.getModels());
		System.out.println(m.getProducts());

		return m;
	}

	@ApiOperation(value = "분석 > 배송 물류비분석 > 모델저장", response = T_LGIST_MODEL.class)
	@PostMapping(value = "/model/save", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public List<T_LGIST_MODEL> save(@RequestBody List<T_LGIST_MODEL> models) {
		return models.stream().map(m -> {

			m.setRegUserId(getConCurrentUser().get().getUsername());
			m.setRegDt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			return costSvc.saveModel(m);
					}).collect(Collectors.toList());
	}

	@ApiOperation(value = "분석 > 배송 물류비분석 > 회전율 저장", response = T_LGIST_MODEL.class)
	@PostMapping(value = "/rule/save", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public List<T_LGIST_RULE_MSTR> saveRule(@RequestBody List<T_LGIST_RULE_MSTR> rules) {
		return rules.stream().map(r -> costSvc.saveRule(r)).collect(Collectors.toList());
	}

	@ApiOperation(value = "분석 > 배송 물류비분석 > 노선 콤보", response = ComboBoxVo.class)
	@PostMapping(value = "/find/route/combo", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public List<ComboBoxVo> searchRouteNo(@RequestBody Search3plReq param) {
		return costSvc.searchRouteNoCombo(convert(param.getFromDe()), convert(param.getToDe(), false), param.getCaralcCd(),
				param.getFctryCd());
	}

	@ApiOperation(value = "분석 > 배송 물류비분석 > 아이테별생산공장 ", response = ProductQty.class)
	@PostMapping(value = "/find/product/item", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public List<ProductQty> searchItemProduct(@RequestBody Search3plReq param) {
		return costSvc.searchItemProducts(convert(param.getFromDe()), convert(param.getToDe(), false), param.getFctryCd(), param.getRouteNos().toArray(new String[param.getRouteNos().size()]));
	}

	@ApiOperation(value = "분석 > 배송 물류비분석 > 룰 목록", response = ComboBoxVo.class)
	@PostMapping(value = "/rule/list", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public List<T_LGIST_RULE_MSTR> listRule(@RequestBody Search3plReq param) {
		return costSvc.searchRule(convert(param.getFromDe()), convert(param.getToDe(), false), param.getRuleNm()).stream()
				.map(T_LGIST_RULE_MSTR::setNullDtls).collect(Collectors.toList());
	}

	@ApiOperation(value = "분석 > 배송 물류비분석 > 룰 상세", response = ComboBoxVo.class)
	@PostMapping(value = "/rule/dtls/list", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public List<T_LGIST_RULE_DTLS> listRuleDtls(@RequestBody(required = true) Long id) {
		return costSvc.findByRuleId(id).getRuleDtls();
	}


	@ApiOperation(value = "분석 > 배송 물류비분석 > 룰 콤보", response = ComboBoxVo.class)
	@PostMapping(value = "/find/rule/combo", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public List<ComboBoxVo> searchRuleCombo() {
		return costSvc.getRuleCombo();
	}

	@ApiOperation(value = "분석 > 배송 물류비분석 > 룰 콤보", response = ComboBoxVo.class)
	@PostMapping(value = "/anals/execute/{id}", produces = SUPPORTED_TYPE)
	@PreAuthorize("hasAuthority('FACTORY')")
	public String execute(@PathVariable Long id) {
		costSvc.startAnals(id);
		return "{}";
	}


	@ApiOperation(value = "분석 > 배송 물류비분석 > 노선궤적조회 - 리스트", response = V_REALTIME_INFO.class)
	@PostMapping(value = "/matrix/list", produces = SUPPORTED_TYPE)

	public @ResponseBody Page<T_ROUTE_PATH_MATRIX6> listMatrix(@RequestBody Search3plReq param) throws CustomBadRequestException {
		Long modelId = param.getModelId();	
		String routeNo = param.getRouteNo();	
		String vrn = param.getVrn();			


		int offset = param.getPageNo() != null ? param.getPageNo() : 0; 
		int size = param.getPageSize() != null ? param.getPageSize() : 200; 

		
		Sort sort = param.cvtSort(param.getSort());
		PageRequest pageable = PageRequest.of(offset, size, sort); 

		System.out.println("matrix7 dtls - pageable > " + modelId);

		return matrixSvc.search(modelId, routeNo, vrn, pageable);

	}

	@ApiOperation(value = "분석 > 배송 물류비분석 > 노선궤적조회 - 단건", response = V_REALTIME_INFO.class)
	@PostMapping(value = "/matrix/select", produces = SUPPORTED_TYPE)

	public @ResponseBody T_ROUTE_PATH_MATRIX6 searchRoutePathInfo(@RequestBody Search3plReq param) throws CustomBadRequestException {
		Long matrixId = param.getMatrixId();				

		return matrixSvc.findById(matrixId);


























	}


	private Date convert(String sDate) {
		return convert(sDate, true);
	}
	private Date convert(String sDate, boolean isStart) {
		if(StringUtils.isEmpty(sDate))
			return null;
		LocalTime time = isStart ? LocalTime.of(0, 0, 0) : LocalTime.now();
		LocalDate date = LocalDate.parse(sDate);
		LocalDateTime ldt = LocalDateTime.of(date, time);
		return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
	}

}
