package com.sdc2ch.service.admin.impl;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sdc2ch.service.admin.IDummyService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
class DummyServiceImpl implements IDummyService {


	public List<?> getDummyList(Object vo) {

		List<Object> listVo = new ArrayList<Object>();

		Class<?> listType = java.util.List.class;

		try {
			for ( int i=0; i<3; i++ ) {
				
	            for (Field field : vo.getClass().getDeclaredFields()) {
	                field.setAccessible(true);
	                String fieldName = field.getName();


					Object value = getDummyValue(fieldName, i);
					field.set(vo, value);
					List list = new ArrayList<String>();
					if ( Collection.class.isAssignableFrom(field.getType()) ) {

					}


	            }
	            listVo.add(vo);
			}
	    } catch (Exception e) {
            e.printStackTrace();
		}


		return listVo;
	}

	private Object getDummyValue(String key, int i) {
		return getDummyMap(key, i);
	}

	private Object getDummyMap(String key, int i) {
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("id", (long)i); map.put("caralcTy", "우유,일반"); map.put("vhcleTy", "2.5"); map.put("avgLdngTime", "60"); map.put("stdTime", gdt("HH:mm", i)); map.put("gradA", "1"); map.put("gradB", "3"); map.put("gradC", "5");
		map.put("ldngReqreTime", gdt("HH,mm", i)); map.put("grad", "A"); map.put("dlvyDe", gdt("yyyy-MM-dd", i)); map.put("vrn", "서울01가0001"); map.put("driverNm", "김기사"); map.put("updtTime", gdt("HH,mm", i)); map.put("updtDt", gdt("yyyy-MM-dd HH,mm,ss", i)); map.put("updtUserId", "관리자ID1");
		map.put("arvlDelayCnt", "1"); map.put("dlvyLcNm", "고객센터1"); map.put("dlvyLcCd", "AA00001"); map.put("avgArvlDelayTime", gdt("HH:mm", i)); map.put("ty", "상차기준시간 변경"); map.put("routeNo", "11AA001");
		map.put("fctryNm", "양주공장"); map.put("trnsprtCmpny", "대성화물(주)"); map.put("ctnuDriveSctn", "서울시 관악구(12,10,00) ~ 경기도 회천군(16,30,00)"); map.put("erorTime", "4시간 20분(20분 초과)");
		map.put("ctnuRestTime", "21,00,00 ~ 07,20,00"); map.put("overCtnuDriveCnt", "3"); map.put("underRestCnt", "1"); map.put("avgCtnuDriveTime", gdt("HH:mm:ss", i)); map.put("avgRestTime", gdt("HH:mm:ss", i)); map.put("alramCnt", "0");
		map.put("ldngGradA", "10"); map.put("ldngGradB", "3"); map.put("ldngGradC", "1"); map.put("dlvyGradA", "5"); map.put("dlvyGradB", "7"); map.put("dlvyGradC", "25"); map.put("ldngTy", "일반탑차"); map.put("mobileNo", "01000000001");
		map.put("rtateRate", "2.5"); map.put("endReportTime", "04,20,00"); map.put("passageInTime", gdt("HH:mm:ss", i)); map.put("waitTime", gdt("HH:mm:ss", i)); map.put("startTime", gdt("HH:mm:ss", i)); map.put("endTime", gdt("HH:mm:ss", i)); map.put("reqreTime", gdt("HH:mm:ss", i));
		map.put("ldngGrad", "A"); map.put("passageOutTime", gdt("HH:mm:ss", i)); map.put("rtnDriveTime", gdt("HH:mm:ss", i)); map.put("entryTime", gdt("HH:mm:ss", i)); map.put("arvlTime", gdt("HH:mm:ss", i)); map.put("takeoverTime", gdt("HH:mm:ss", i)); map.put("leaveTime", gdt("HH:mm:ss", i));
		map.put("arvlExpcTime", gdt("HH:mm:ss", i)); map.put("arvlExpcTimeEror", gdt("HH:mm:ss", i)); map.put("driverDlvyGrad", "A"); map.put("arvlGrad", "A"); map.put("trmnlNo", "010-1234-5678"); map.put("modelNm", "SHV-E210S");
		map.put("telecom", "KT"); map.put("osVer", "7.0"); map.put("lcSetup", "Y"); map.put("appVer", "1.0"); map.put("stplatNo", "10"); map.put("stplatAgreDt", gdt("yyyy-MM-dd HH:mm:ss", i)); map.put("stplatGb","개인정보"); map.put("regDt", gdt("yyyy-MM-dd HH:mm:ss", i)); map.put("regUserId", "ADMIN");
		map.put("sj", "약관1제목입니다."); map.put("cn", "약관1내용 약관1내용 약관1내용 약관1내용 약관1내용 약관1내용 약관1내용 약관1내용 입니다."); map.put("gpsSttus", "Y"); map.put("lat", "37.1234");
		map.put("lng", "125.4567"); map.put("dstnc", "74"); map.put("ve", "50"); map.put("tempt", "2.3/0.0"); map.put("adres", "대한민국 서울특별시 중랑구 중화동 211-4"); map.put("wrhous", "A창고"); map.put("nfcTrmnlId", "N0001");
		map.put("taggingTy", "카드"); map.put("taggingDt", gdt("yyyy-MM-dd HH:mm:ss", i)); map.put("bconNo", "ESTIMOTE0000001"); map.put("instlLc", "정문"); map.put("btry", "80"); map.put("dozeMode", "N"); map.put("fgrndService", "Y"); map.put("networkSttus", "Y");
		map.put("appId" , "APP000001"); map.put("appNm" , "2CH-SERVER"); map.put("ip", "127.0.0.1"); map.put("host", "2ch.com"); map.put("sttus", "Y"); map.put("size", "3214"); map.put("path", "/ROOT/PATH");
		map.put("workId", "JOB001"); map.put("workNm", "배치001"); map.put("workCycle", "1D"); map.put("lastWorkDt", gdt("yyyy-MM-dd HH:mm:ss", i)); map.put("workReqreTime", gdt("HH:mm", i)); map.put("remnWorkTime", gdt("HH:mm", i)); map.put("nextWorkDt", gdt("yyyy-MM-dd HH:mm:ss", i));
		map.put("loadPlaceNm", "안산(생크림)"); map.put("dstn", "서울에프엔비"); map.put("chargerNm", "담당자"); map.put("dstnTellNo", "010-1234-5678"); map.put("gudsTy", "자재"); map.put("gudsNm", "우유"); map.put("qy", "3"); map.put("wt", "20");
		map.put("pallet", "20"); map.put("orderSttus" ,"주문등록"); map.put("cnfirmDt", gdt("yyyy-MM-dd HH:mm:ss", i)); map.put("vhcleCnt", i+""); map.put("lastReportDt", gdt("yyyy-MM-dd HH:mm:ss", i)); map.put("lastLc", "대한민국 서울특별시 중랑구 중화동 211-4"); map.put("TmsEmsSyncYn", "Y/Y");
		map.put("caralcRegDt", gdt("yyyy-MM-dd HH:mm:ss", i)); map.put("unstoringDe", gdt("yyyy-MM-dd", i)); map.put("ldngSn", i+""); map.put("workPd", 166); map.put("dlvyLcSttus", "용인공장>상봉동>상봉동(커피마마-상봉직영점)>석관동>신사역촌"); map.put("factoryExpcStart", gdt("yyyy-MM-dd HH:mm", i));
		map.put("fctryStartTime", gdt("yyyy-MM-dd HH:mm", i)); map.put("delayTime", "-0,04"); map.put("delayResn", "물량증가(상차지연)"); map.put("passageTime", gdt("yyyy-MM-dd HH:mm", i)); map.put("passageDffrnc", "-0,04"); map.put("ldngExpcTime", gdt("HH:mm", i)); map.put("parkngTime", gdt("HH:mm", i));
		map.put("noCnfrmCaralcDtlsAlramCnt", 1); map.put("noStartWorkAlramCnt", 1); map.put("noPassageAlramCnt", 0); map.put("noLdngAlramCnt", 3); map.put("fctryStartAlramCnt", 2); map.put("noEtyBoxDcsnAlramCnt", 0);
		map.put("noEndWorkAlramCnt", 1); map.put("rtngudWrhousngAlramCnt", 0); map.put("gpsOffAlramCnt", 0); map.put("ldngStartTime" , gdt("HH:mm", i)); map.put("dffrnc", gdt("HH:mm", i)); map.put("ldngStdTime", gdt("HH:mm", i)); map.put("rcverNm", "김기사"); map.put("rcverPhone", "010-1234-5678");
		map.put("alramTy", "SMS"); map.put("driveSttus", "배송중"); map.put("tempt1", "2.3"); map.put("tempt2", "0.0"); map.put("mileg", "6.3"); map.put("wtSm", "4.0"); map.put("driveDstnc", "250"); map.put("emptVhcleMoveDstnc", "80");
		map.put("ldngVhcleMoveDstnc", "170"); map.put("tonkm", "592.73"); map.put("fuelUsgqty", "20.53"); map.put("co2Dscamt", "54.78"); map.put("energyEfcIdx", "0.04"); map.put("unitTonkm", "102.87");
		map.put("month1", rand()); map.put("month2", rand()); map.put("month3", rand()); map.put("month4", rand()); map.put("month5", rand()); map.put("month6", rand()); map.put("month7", rand()); map.put("month8", rand()); map.put("month9", rand());
		map.put("month10", rand()); map.put("month11", rand()); map.put("month12", rand());

		return map.get(key);
	}

	private String rand(){  
		return rand(0, 31);
	}
	private String rand(int d, int r){  
		Double value = d + Math.floor(Math.random()*100 % r);
        return String.valueOf(value);
    }

	private String gdt(String dateformat, int idx){
		Date dt = new Date();
		SimpleDateFormat fmt = new SimpleDateFormat(dateformat);
		String dd = fmt.format(dt);

		long hhmi = Math.floorMod(dt.getTime(), (long) (24*60*60*1000.0));
		long ymd = dt.getTime() - hhmi;
        long term = hhmi/120;

        return fmt.format(new Date(ymd+term*idx+(long)(Math.random()*term/2)));
    }

}
