package com.sdc2ch.service.admin;

import java.util.Date;
import java.util.List;

import com.sdc2ch.require.enums.SetupLcType;
import com.sdc2ch.service.mobile.model.NfcTagHistVo;

public interface INfcTagService {

	List<NfcTagHistVo> searchTagHistfindAll(Integer yyyyMM, Date fromDt, Date toDt, String fctryCd, SetupLcType setupLcType, String vrn);
}
