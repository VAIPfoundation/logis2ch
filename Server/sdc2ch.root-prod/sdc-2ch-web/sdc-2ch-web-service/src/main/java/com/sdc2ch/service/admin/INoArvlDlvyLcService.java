package com.sdc2ch.service.admin;

import java.util.List;

import com.sdc2ch.tms.io.TmsLocationIO;
import com.sdc2ch.web.admin.repo.dto.INoArvlDlvyLcDto;


public interface INoArvlDlvyLcService {

	
	List<INoArvlDlvyLcDto> listNoArvlDlvyLc(String fctryCd, String fromDe, String toDe, String caralcTy, String vhcleTy, String routeNo, String trnsprtCmpny, String vrn);


	
	TmsLocationIO findStopLocation(String stopCd);


	
	int updateNoArvlDlvyLc(String dlvyLcCd, String adres, String lat, String lng);



}
