package com.sdc2ch.web.endpoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdc2ch.core.security.auth.I2CHAuthorization;
import com.sdc2ch.core.security.auth.I2CHUserContext;


@CrossOrigin("*")
@RestController
public class DummyDataEndpoint {

	@Autowired
	I2CHAuthorization Authorization;

	
	@RequestMapping(value = "/datas/dummy/{type}", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Map<String, Object> dummy(HttpServletRequest req, @PathVariable String type,
			@RequestParam(required = false) String params)
			throws JsonParseException, JsonMappingException, IOException {


		int totalCount = 102;
		String data = params;
		ObjectMapper mapper = new ObjectMapper();

		Map<String, Object> resultMap = new LinkedHashMap<>();
		List<Map<String, Object>> results = new ArrayList<>();
		
		
		if ("read".equals(type)) {
			Map<String, Object> param = mapper.readValue(data, new TypeReference<HashMap<String, Object>>() {});
			
			System.out.println(param.toString());
			int pageNo = (int)param.get("pageNo");
			int pageSize = (int)param.get("pageSize");
			
			for (int i = pageNo; i < pageNo+pageSize; i++) {
				
				Map<String, Object> obj = new LinkedHashMap<>();
				obj.put("rowId", i + 1);
				param.forEach((k, v) -> {
					obj.put(k, random(v));
				});
				results.add(obj);
			}

		} else if ("create".equals(type) || "update".equals(type) || "destroy".equals(type)) {
			List<Map<String, Object>> paramList = mapper.readValue(data, new TypeReference<List<HashMap<String, Object>>>() {});
			for (int i = 0; i < paramList.size(); i++) {
				results.add(paramList.get(i));
			}
		}
		
		resultMap.put("data", results);
		resultMap.put("total", totalCount);

		return resultMap;
	}

	int min = 1;
	int max = 100;
	int smax = 20;

	private Object random(Object v) {

		String _v = String.valueOf(v);
		DataType t = null;
		try {
			t = DataType.valueOf(_v.toUpperCase());

		} catch (Exception e) {
			t = DataType.DEFAULT;
		}
		Random rg = new Random();

		Object data = null;
		switch (t) {
		case BOOLEAN:
			data = rg.nextBoolean();
			break;
		case FLOAT:

			data = min + rg.nextFloat() * (max - min);
			break;
		case INT:
			data = rg.nextInt(max - min) + min;
			break;
		case STRING:

			data = randomHangulName();
			break;
		default:
			data = v;
			break;

		}

		return data;
	}

	public String randomString(int num) {
		StringBuffer bName = new StringBuffer();

		for (int i = 0; i < num; i++) {
			char ch = (char) ((Math.random() * 11172) + 0xAC00);

			bName.append(ch);

		}

		return bName.toString();
	}

	public String randomHangulName() {
		List<String> 성 = Arrays.asList("김", "이", "박", "최", "정", "강", "조", "윤", "장", "임", "한", "오", "서", "신", "권", "황",
				"안", "송", "류", "전", "홍", "고", "문", "양", "손", "배", "조", "백", "허", "유", "남", "심", "노", "정", "하", "곽", "성",
				"차", "주", "우", "구", "신", "임", "나", "전", "민", "유", "진", "지", "엄", "채", "원", "천", "방", "공", "강", "현", "함",
				"변", "염", "양", "변", "여", "추", "노", "도", "소", "신", "석", "선", "설", "마", "길", "주", "연", "방", "위", "표", "명",
				"기", "반", "왕", "금", "옥", "육", "인", "맹", "제", "모", "장", "남", "탁", "국", "여", "진", "어", "은", "편", "구",
				"용");
		List<String> 이름 = Arrays.asList("가", "강", "건", "경", "고", "관", "광", "구", "규", "근", "기", "길", "나", "남", "노", "누",
				"다", "단", "달", "담", "대", "덕", "도", "동", "두", "라", "래", "로", "루", "리", "마", "만", "명", "무", "문", "미", "민",
				"바", "박", "백", "범", "별", "병", "보", "빛", "사", "산", "상", "새", "서", "석", "선", "설", "섭", "성", "세", "소", "솔",
				"수", "숙", "순", "숭", "슬", "승", "시", "신", "아", "안", "애", "엄", "여", "연", "영", "예", "오", "옥", "완", "요", "용",
				"우", "원", "월", "위", "유", "윤", "율", "으", "은", "의", "이", "익", "인", "일", "잎", "자", "잔", "장", "재", "전", "정",
				"제", "조", "종", "주", "준", "중", "지", "진", "찬", "창", "채", "천", "철", "초", "춘", "충", "치", "탐", "태", "택", "판",
				"하", "한", "해", "혁", "현", "형", "혜", "호", "홍", "화", "환", "회", "효", "훈", "휘", "희", "운", "모", "배", "부", "림",
				"봉", "혼", "황", "량", "린", "을", "비", "솜", "공", "면", "탁", "온", "디", "항", "후", "려", "균", "묵", "송", "욱", "휴",
				"언", "령", "섬", "들", "견", "추", "걸", "삼", "열", "웅", "분", "변", "양", "출", "타", "흥", "겸", "곤", "번", "식", "란",
				"더", "손", "술", "훔", "반", "빈", "실", "직", "흠", "흔", "악", "람", "뜸", "권", "복", "심", "헌", "엽", "학", "개", "롱",
				"평", "늘", "늬", "랑", "얀", "향", "울", "련");
		Collections.shuffle(성);
		Collections.shuffle(이름);
		return 성.get(0) + 이름.get(0) + 이름.get(1);
	}

	enum DataType {
		STRING, INT, BOOLEAN, FLOAT, DEFAULT
	}






















}
