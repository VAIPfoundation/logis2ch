package com.sdc2ch.service.common;

import java.util.List;

import com.sdc2ch.service.common.model.ComboBoxVo;

public interface IComboBoxService {

	List<ComboBoxVo> findFactoryCombo();
	List<ComboBoxVo> findCarAlcTypeCombo();
	List<ComboBoxVo> findVhcleTyCombo();
	List<ComboBoxVo> findSetupLcTyCombo();

	List<ComboBoxVo> findRouteNoCombo(String fctryCd, String fromDe, String toDe, String caralcTy, String vhcleTy);
	List<ComboBoxVo> findDlvyLcCdCombo(String fctryCd);
	List<ComboBoxVo> findDlvyLcTimeCombo(String fctryCd, String fromDe, String toDe, String dlvyLcCd);

	List<ComboBoxVo> findTrnsprtCmpnyCombo(String fctryCd);






}
