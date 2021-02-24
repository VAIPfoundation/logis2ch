package com.sdc2ch.service.common.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.Projections;
import com.sdc2ch.core.security.auth.I2CHAuthorization;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.enums.SetupLcType;
import com.sdc2ch.require.service.I2ChUserService;
import com.sdc2ch.service.common.IComboBoxService;
import com.sdc2ch.service.common.model.ComboBoxVo;
import com.sdc2ch.tms.enums.FactoryType;
import com.sdc2ch.web.admin.repo.AdmQueryBuilder;
import com.sdc2ch.web.admin.repo.domain.v.QV_CMMN_CODE;
import com.sdc2ch.web.admin.repo.domain.v.V_CMMN_CODE;
import com.sdc2ch.web.admin.repo.enums.RoleEnums;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
class ComboBoxServiceImpl implements IComboBoxService {

	private static final String VHCLE_TY_CD = "017";
	private static final String CAR_ALC_TYPE_CD = "023";

	@Autowired
	AdmQueryBuilder builder;
	@Autowired
	protected I2CHAuthorization authorization;
	@Autowired
	protected I2ChUserService userSvc;

	@Override
	public List<ComboBoxVo> findFactoryCombo() {

		String username = authorization.userContext().getUsername();
		Optional<IUser> user = userSvc.findByUsername(username);
		boolean isAdmin = user.orElseThrow(() -> new RuntimeException("user can be null")).getRoles().stream().anyMatch(r -> RoleEnums.isSystemRole(r.getRolename()));
		List<ComboBoxVo> vo = new ArrayList<>();
		if ( isAdmin ) {
			vo.add(ComboBoxVo.of("1D1", "양주공장"));
			vo.add(ComboBoxVo.of("2D1", "용인공장"));
			vo.add(ComboBoxVo.of("3D1", "안산공장"));
			vo.add(ComboBoxVo.of("4D1", "거창공장"));
			vo.add(ComboBoxVo.of("5D1", "양주신공장"));
		} else {
			vo.add(ComboBoxVo.of(user.get().getFctryCd(), FactoryType.convert(user.get().getFctryCd()).getName() + "공장"));
		}
		return vo;
	}

	@Override
	public List<ComboBoxVo> findCarAlcTypeCombo() {
		QV_CMMN_CODE code = QV_CMMN_CODE.v_CMMN_CODE;
		return builder.create().select(Projections.fields(V_CMMN_CODE.class, code.cd, code.cdNm)).from(code)
				.where(code.groupCd.eq(CAR_ALC_TYPE_CD)).orderBy(code.cdOrderNo.asc()).fetch().stream()
				.map(c -> ComboBoxVo.of(c.getCd(), c.getCdNm())).collect(Collectors.toList());
	}

	@Override
	public List<ComboBoxVo> findVhcleTyCombo() {
		QV_CMMN_CODE code = QV_CMMN_CODE.v_CMMN_CODE;
		return builder.create().select(Projections.fields(V_CMMN_CODE.class, code.cd, code.cdNm)).from(code)
				.where(code.groupCd.eq(VHCLE_TY_CD).and(code.cdNm.ne("0톤")).and(code.cdNm.ne("5톤초장축")))
				.orderBy(code.cdOrderNo.asc()).fetch() 
				.stream()
				.map(c -> ComboBoxVo.of(c.getCd(), c.getCdNm())).collect(Collectors.toList());
	}

	@Override
	public List<ComboBoxVo> findSetupLcTyCombo(){
		return Arrays.stream(SetupLcType.values()).map(c -> ComboBoxVo.of( c.name() ,  c.lcName)).collect(Collectors.toList());
	}

	@Override
	public List<ComboBoxVo> findRouteNoCombo(String fctryCd, String fromDe, String toDe, String caralcTy, String vhcleTy) {
		Object[] param = {fctryCd, fromDe, toDe, caralcTy, vhcleTy};
		List<Object[]> resultList = builder.storedProcedureCallRet("[DBO].[SP_2CH_COMBO_ROUTE_SEL]", param);

		List<ComboBoxVo> comboList = new ArrayList<ComboBoxVo>();

		for ( Object[] res : resultList ) {
			String key = (String)res[0];
			String value = (String)res[1];
			comboList.add(ComboBoxVo.of(key, value));
		}

		return comboList;
	}

	@Override
	public List<ComboBoxVo> findDlvyLcCdCombo(String fctryCd) {
		Object[] param = {fctryCd};
		List<Object[]> resultList = builder.storedProcedureCallRet("[DBO].[SP_2CH_COMBO_DLVY_LC_SEL]", param);

		List<ComboBoxVo> comboList = new ArrayList<ComboBoxVo>();

		for ( Object[] res : resultList ) {
			String key = (String)res[0];
			String value = (String)res[1];
			comboList.add(ComboBoxVo.of(key, value));
		}

		return comboList;
	}

	@Override
	public List<ComboBoxVo> findDlvyLcTimeCombo(String fctryCd, String fromDe, String toDe, String dlvyLcCd) {
		Object[] param = {fctryCd, fromDe, toDe, dlvyLcCd};
		List<Object[]> resultList = builder.storedProcedureCallRet("[DBO].[SP_2CH_COMBO_DLVY_LC_TIME_SEL]", param);

		List<ComboBoxVo> comboList = new ArrayList<ComboBoxVo>();

		for ( Object[] res : resultList ) {
			String key = (String)res[0];
			String value = (String)res[1];
			comboList.add(ComboBoxVo.of(key, value));
		}

		return comboList;
	}

	@Override
	public List<ComboBoxVo> findTrnsprtCmpnyCombo(String fctryCd) {
		Object[] param = {fctryCd};
		List<Object[]> resultList = builder.storedProcedureCallRet("[DBO].[SP_2CH_COMBO_TRNSPRT_CMPNY_SEL]", param);

		List<ComboBoxVo> comboList = new ArrayList<ComboBoxVo>();

		for ( Object[] res : resultList ) {
			String key = (String)res[0];
			String value = (String)res[1];
			comboList.add(ComboBoxVo.of(key, value));
		}

		return comboList;
	}

}
