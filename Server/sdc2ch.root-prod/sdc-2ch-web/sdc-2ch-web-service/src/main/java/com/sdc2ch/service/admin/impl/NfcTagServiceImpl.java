package com.sdc2ch.service.admin.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.microsoft.sqlserver.jdbc.StringUtils;
import com.sdc2ch.nfc.enums.NfcEventType;
import com.sdc2ch.nfc.io.NfcEventIO;
import com.sdc2ch.nfc.service.INfcTagHistService;
import com.sdc2ch.repo.io.TmsDriverIO;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.enums.SetupLcType;
import com.sdc2ch.require.service.I2ChUserService;
import com.sdc2ch.service.admin.INfcMappingService;
import com.sdc2ch.service.admin.INfcTagService;
import com.sdc2ch.service.mobile.model.NfcTagHistVo;
import com.sdc2ch.web.admin.repo.domain.op.T_NFC_MAPPING;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NfcTagServiceImpl implements INfcTagService{

	@Autowired(required = false) INfcTagHistService nfcTagHistSvc;
	@Autowired INfcMappingService nfcMappingSvc;
	@Autowired I2ChUserService tmsUser;

	@Override
	public List<NfcTagHistVo> searchTagHistfindAll(Integer yyyyMM, Date fromDt, Date toDt, String fctryCd, SetupLcType setupLcType, String vrn) {
		Assert.notNull(nfcTagHistSvc, "nfcTagHistSvc is must not be null");
		Assert.notNull(yyyyMM, "YearMonth is must not be null");
		Assert.notNull(fromDt, "fromDt is must not be null");
		Assert.notNull(toDt, "toDt is must not be null");

		List<T_NFC_MAPPING> nfcMappingList = nfcMappingSvc.searchNfcMappingByFctryCd(fctryCd);

		List<NfcTagHistVo> list = nfcTagHistSvc.findAll(yyyyMM, fromDt, toDt).stream().map(io -> convertToNfcTagHistVo(io, nfcMappingList)).collect(Collectors.toList());

		if(!StringUtils.isEmpty(fctryCd)) {
			list = list.stream().filter(o -> fctryCd.equals(o.getFctryCd())).collect(Collectors.toList());
		}

		if(setupLcType != null) {
			list = list.stream().filter(o -> setupLcType == o.getSetupLcTy()).collect(Collectors.toList());
		}

		if(!StringUtils.isEmpty(vrn)) {
			list = list.stream().filter(o -> o.getVrn() !=null && o.getVrn().contains(vrn)).collect(Collectors.toList());
		}

		return list;
	}


	private NfcTagHistVo convertToNfcTagHistVo(NfcEventIO io, List<T_NFC_MAPPING> nfcMappingList) {
		NfcTagHistVo vo = new NfcTagHistVo();
		BeanUtils.copyProperties(io, vo);
		vo.setEvtTy(NfcEventType.DEFAULT_EVENT_CODE.contains(vo.getEvt()) ? NfcEventType.valueOf(vo.getEvt()) : null);

		nfcMappingList.stream().filter(m -> m.getNfcId()==vo.getDevuid()).forEach(o -> {
			vo.setFctryCd(o.getFctryCd());
			vo.setSetupLcTy(o.getSetupLc());
		});

		try {
			if (!StringUtils.isEmpty(vo.getUsrid())) {
				IUser user = tmsUser.findByMobileNo("010" + vo.getUsrid()).orElseGet(() -> {
					return tmsUser.findByMobileNo("011" + vo.getUsrid()).orElse(null);
				});
				if (user != null) {
					TmsDriverIO _user = (TmsDriverIO) user.getUserDetails();
					vo.setDriverCd(user.getUsername());
					vo.setDriverNm(user.getUserDetails().name());
					vo.setVrn(_user.getCar().getVrn());
				}
			}
		} catch (Exception e) {

		}
		return vo;
	}

}
