package com.sdc2ch.web.admin.repo.init;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.sdc2ch.core.ISetupData;
import com.sdc2ch.require.enums.SetupLcType;
import com.sdc2ch.web.admin.repo.dao.T_NfcMappingRepository;
import com.sdc2ch.web.admin.repo.domain.op.T_NFC_MAPPING;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AdminNfcMappingData implements ISetupData {

	@Autowired T_NfcMappingRepository nfcRepo;
	
	@Override
	public void install() {

		try {
			ClassPathResource classPathResource = new ClassPathResource("data/nfcMappingData.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(classPathResource.getInputStream()));
			AtomicLong inc = new AtomicLong();
			String s;
			while((s = reader.readLine()) != null) {
				String[] strs = s.split(",");
				T_NFC_MAPPING mapping = new T_NFC_MAPPING();
				mapping.setId(inc.incrementAndGet());
				mapping.setFctryCd(findFctryCd(strs[3].trim()));
				mapping.setNfcId(Integer.valueOf(strs[0].trim()));
				mapping.setNfcName(strs[2].trim());
				mapping.setSetupLc(findSetupLc(strs[1].trim()));
				
				if(mapping.getSetupLc() == SetupLcType.TEST) {
					mapping.setSetupLc(SetupLcType.A1COLD);
					mapping.setFctryCd("2D1");
				}
				nfcRepo.save(mapping);
			}
		}catch (IOException e) {
			log.error("{}", e);
		}
	}

	private SetupLcType findSetupLc(String setupNm) {
		switch(setupNm) {
		case "A1냉장고" : 
			return SetupLcType.A1COLD;
		case "A2냉장고" : 
			return SetupLcType.A2COLD;
		case "B1냉장고" : 
			return SetupLcType.B1COLD;
		case "B2냉장고" : 
			return SetupLcType.B2COLD;
		case "물류사무실" : 
			return SetupLcType.OFFICE;
		case "시유창고" : 
			return SetupLcType.CU;
		case "멸균창고" : 
			return SetupLcType.STERILIZED;
		case "치즈창고" : 
			return SetupLcType.CHEESE;
		case "가공품창고" : 
			return SetupLcType.PRCSSGD;
		case "평지창고" : 
			return SetupLcType.FLAT;
		case "호상창고" : 
			return SetupLcType.HOSANG;
		}
		return SetupLcType.TEST;
	}

	@Override
	public int order() {
		return 0;
	}
	
	private String findFctryCd(String name) {
		switch(name) {
		case "양주공장" : 
			return "1D1";
		case "용인공장" : 
			return "2D1";
		case "거창공장" : 
			return "4D1";
		case "안산공장" : 
			return "3D1";
		case "양주신공장" : 
			return "5D1";
		}
		return "FFFF";
	}

}
